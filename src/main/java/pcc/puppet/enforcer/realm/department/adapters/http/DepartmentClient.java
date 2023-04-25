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
package pcc.puppet.enforcer.realm.department.adapters.http;

import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.ORGANIZATION;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.REQUESTER;

import io.micrometer.tracing.annotation.SpanTag;
import jakarta.validation.constraints.NotNull;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import pcc.puppet.enforcer.app.Project;
import pcc.puppet.enforcer.realm.department.adapters.presenter.DepartmentPresenter;
import pcc.puppet.enforcer.realm.department.domain.DepartmentOperations;
import pcc.puppet.enforcer.realm.department.ports.command.DepartmentCreateCommand;
import pcc.puppet.enforcer.realm.department.ports.event.DepartmentCreateEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@HttpExchange("${spring.http.services.pcc-realm-department.url}")
public interface DepartmentClient extends DepartmentOperations {
String USER_AGENT = "DepartmentClient/" + Project.VERSION + " (" + Project.NAME + ")";
  @PostExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
  Mono<DepartmentCreateEvent> departmentCreate(
      @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @SpanTag(REQUESTER) @RequestHeader(REQUESTER) String requester,
      @NotNull @SpanTag(ORGANIZATION) @RequestHeader(ORGANIZATION) String organizationId,
      @NotNull @RequestBody @Valid DepartmentCreateCommand createCommand);
  @Override
  default Mono<DepartmentCreateEvent> departmentCreate(
      @NotNull String requester,
      @NotNull String organizationId,
      @NotNull @Valid DepartmentCreateCommand createCommand) {
    return departmentCreate(USER_AGENT, requester, organizationId, createCommand);
  }
  @GetExchange(value = "/{departmentId}", accept = MediaType.APPLICATION_JSON_VALUE)
  Mono<DepartmentPresenter> findDepartment(
      @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @SpanTag(REQUESTER) @RequestHeader(REQUESTER) String requester,
      @NotNull @SpanTag(ORGANIZATION) @RequestHeader(ORGANIZATION) String organizationId,
      @NotNull @PathVariable String departmentId);
  @Override
  default Mono<DepartmentPresenter> findDepartment(
      @NotNull String requester,
      @NotNull String organizationId,
      @NotNull String departmentId) {
    return findDepartment(USER_AGENT, requester, organizationId, departmentId);
  }

  @GetExchange(value = "/{departmentId}/child", accept = MediaType.APPLICATION_JSON_VALUE)
  Flux<DepartmentPresenter> findChildDepartments(
      @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @SpanTag(REQUESTER) @RequestHeader(REQUESTER) String requester,
      @NotNull @SpanTag(ORGANIZATION) @RequestHeader(ORGANIZATION) String organizationId,
      @SpanTag @NotNull @PathVariable String departmentId);

  @Override
  default Flux<DepartmentPresenter> findChildDepartments(
      @NotNull String requester,
      @NotNull String organizationId,
      @SpanTag String departmentId) {
    return findChildDepartments(USER_AGENT, requester, organizationId, departmentId);
  }
}
