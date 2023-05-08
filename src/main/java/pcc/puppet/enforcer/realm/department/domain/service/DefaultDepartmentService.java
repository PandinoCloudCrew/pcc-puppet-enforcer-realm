/*
 * Copyright 2022 Pandino Cloud Crew (C)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pcc.puppet.enforcer.realm.department.domain.service;

import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.annotation.SpanTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.realm.common.contact.domain.service.ContactInformationService;
import pcc.puppet.enforcer.realm.common.generator.DomainFactory;
import pcc.puppet.enforcer.realm.department.adapters.mapper.DepartmentOutputMapper;
import pcc.puppet.enforcer.realm.department.adapters.presenter.DepartmentPresenter;
import pcc.puppet.enforcer.realm.department.adapters.repository.DepartmentRepository;
import pcc.puppet.enforcer.realm.department.domain.Department;
import pcc.puppet.enforcer.realm.department.ports.command.DepartmentCreateCommand;
import pcc.puppet.enforcer.realm.department.ports.event.DepartmentCreateEvent;
import pcc.puppet.enforcer.realm.department.ports.mapper.DepartmentInputMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultDepartmentService implements DepartmentService {
  private final DepartmentOutputMapper outputMapper;
  private final DepartmentInputMapper inputMapper;
  private final DepartmentRepository repository;
  private final ContactInformationService contactInformationService;

  @Override
  @Observed(name = "default-department-service::create")
  public Mono<DepartmentCreateEvent> create(
      @SpanTag String requester, @SpanTag DepartmentCreateCommand createCommand) {
    Department department = inputMapper.commandToDomain(createCommand);
    department.setId(DomainFactory.id());
    return contactInformationService
        .save(requester, department.getId(), createCommand.getContactId())
        .flatMap(
            contactInformation -> {
              department.setContactId(contactInformation);
              return repository.save(department);
            })
        .map(outputMapper::domainToEvent);
  }

  @Override
  @Observed(name = "default-department-service::find-by-id")
  public Mono<DepartmentPresenter> findById(
      @SpanTag String requester, @SpanTag String departmentId) {
    return contactInformationService
        .findByOwnerId(departmentId)
        .flatMap(
            contactInformation ->
                repository
                    .findById(departmentId)
                    .map(
                        department -> {
                          department.setContactId(contactInformation);
                          return department;
                        })
                    .map(outputMapper::domainToPresenter));
  }

  @Override
  @Observed(name = "default-department-service::find-by-parent-id")
  public Flux<DepartmentPresenter> findByParentId(
      @SpanTag String requester, @SpanTag String departmentId) {
    return repository
        .findByParentId(departmentId)
        .flatMap(
            department ->
                contactInformationService
                    .findByOwnerId(department.getId())
                    .map(department::setContact))
        .map(outputMapper::domainToPresenter);
  }
}
