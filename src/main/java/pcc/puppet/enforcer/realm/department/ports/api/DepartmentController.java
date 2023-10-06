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

package pcc.puppet.enforcer.realm.department.ports.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.ORGANIZATION;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.REQUESTER;

import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.annotation.SpanTag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pcc.puppet.enforcer.realm.department.adapters.presenter.DepartmentPresenter;
import pcc.puppet.enforcer.realm.department.domain.DepartmentOperations;
import pcc.puppet.enforcer.realm.department.domain.service.DepartmentService;
import pcc.puppet.enforcer.realm.department.ports.command.DepartmentCreateCommand;
import pcc.puppet.enforcer.realm.department.ports.event.DepartmentCreateEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(value = "${spring.http.services.pcc-realm-department.path}")
@RequiredArgsConstructor
public class DepartmentController implements DepartmentOperations {
  private final DepartmentService departmentService;

  @Override
  @PostMapping(
      produces = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE},
      consumes = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE})
  @Observed(name = "department-controller::department-create")
  public Mono<DepartmentCreateEvent> departmentCreate(
      @SpanTag(REQUESTER) @NotNull @RequestHeader(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NotNull @RequestHeader(ORGANIZATION) String organizationId,
      @NotNull @Valid @RequestBody DepartmentCreateCommand createCommand) {
    return departmentService.create(requester, createCommand);
  }

  @Override
  @GetMapping(value = "/{departmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Observed(name = "department-controller::find-department")
  public Mono<DepartmentPresenter> findDepartment(
      @SpanTag(REQUESTER) @NotNull @RequestHeader(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NotNull @RequestHeader(ORGANIZATION) String organizationId,
      @SpanTag @NotNull @PathVariable String departmentId) {
    return departmentService.findById(requester, departmentId);
  }

  @Override
  @GetMapping(value = "/{departmentId}/child", produces = MediaType.APPLICATION_JSON_VALUE)
  @Observed(name = "department-controller::find-child-departments")
  public Flux<DepartmentPresenter> findChildDepartments(
      @SpanTag(REQUESTER) @NotNull @RequestHeader(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NotNull @RequestHeader(ORGANIZATION) String organizationId,
      @SpanTag @NotNull @PathVariable String departmentId) {
    return departmentService.findByParentId(requester, departmentId);
  }
}
