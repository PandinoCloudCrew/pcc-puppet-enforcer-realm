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
package pcc.puppet.enforcer.realm.organization.service;

import io.micronaut.tracing.annotation.ContinueSpan;
import jakarta.inject.Singleton;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import pcc.puppet.enforcer.realm.organization.Organization;
import pcc.puppet.enforcer.realm.organization.api.OrganizationPresenter;
import pcc.puppet.enforcer.realm.organization.api.command.OrganizationCreateCommand;
import pcc.puppet.enforcer.realm.organization.api.event.OrganizationCreateEvent;
import pcc.puppet.enforcer.realm.organization.mapper.OrganizationMapper;
import pcc.puppet.enforcer.realm.organization.repository.OrganizationRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
  private final OrganizationMapper mapper;
  private final OrganizationRepository repository;

  @ContinueSpan
  @Override
  public Mono<OrganizationCreateEvent> create(
      String requester, OrganizationCreateCommand createCommand) {
    Organization organization = mapper.commandToDomain(createCommand);
    organization.setCreatedBy(requester);
    organization.setCreatedAt(Instant.now());
    return repository.save(organization).map(mapper::domainToEvent);
  }

  @ContinueSpan
  @Override
  public Mono<OrganizationPresenter> findById(String requester, String organizationId) {
    return repository.findById(new ObjectId(organizationId)).map(mapper::domainToPresenter);
  }

  @ContinueSpan
  @Override
  public Flux<OrganizationPresenter> findByParentId(String requester, String organizationId) {
    return repository.findByParentId(new ObjectId(organizationId)).map(mapper::domainToPresenter);
  }
}
