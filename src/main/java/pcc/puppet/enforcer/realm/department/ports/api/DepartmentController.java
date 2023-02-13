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

import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.ORGANIZATION;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.REQUESTER;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pcc.puppet.enforcer.realm.department.adapters.presenter.DepartmentPresenter;
import pcc.puppet.enforcer.realm.department.domain.DepartmentOperations;
import pcc.puppet.enforcer.realm.department.domain.service.DepartmentService;
import pcc.puppet.enforcer.realm.department.ports.command.DepartmentCreateCommand;
import pcc.puppet.enforcer.realm.department.ports.event.DepartmentCreateEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@ExecuteOn(TaskExecutors.IO)
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("${micronaut.http.services.pcc-realm-department.path}")
@RequiredArgsConstructor
public class DepartmentController implements DepartmentOperations {
  private final DepartmentService departmentService;

  @Timed
  @Counted
  @Override
  @NewSpan
  @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  public Mono<DepartmentCreateEvent> departmentCreate(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NonNull @Header(ORGANIZATION) String organizationId,
      @NonNull @Valid @Body DepartmentCreateCommand createCommand) {
    return departmentService.create(requester, createCommand);
  }

  @Timed
  @Counted
  @Override
  @NewSpan
  @Get(uri = "/{departmentId}", produces = MediaType.APPLICATION_JSON)
  public Mono<DepartmentPresenter> findDepartment(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NonNull @Header(ORGANIZATION) String organizationId,
      @SpanTag @NonNull String departmentId) {
    return departmentService.findById(requester, departmentId);
  }

  @Timed
  @Counted
  @Override
  @NewSpan
  @Get(uri = "/{departmentId}/child", produces = MediaType.APPLICATION_JSON)
  public Flux<DepartmentPresenter> findChildDepartments(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NonNull @Header(ORGANIZATION) String organizationId,
      @SpanTag @NonNull String departmentId) {
    return departmentService.findByParentId(requester, departmentId);
  }
}
