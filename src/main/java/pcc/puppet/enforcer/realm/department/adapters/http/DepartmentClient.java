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

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.CONSUMER;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.DEPARTMENT;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.ORGANIZATION;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.REQUESTER;

import io.micrometer.observation.annotation.Observed;
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
import pcc.puppet.enforcer.realm.common.util.JwtTool;
import pcc.puppet.enforcer.realm.department.adapters.presenter.DepartmentPresenter;
import pcc.puppet.enforcer.realm.department.domain.DepartmentOperations;
import pcc.puppet.enforcer.realm.department.ports.command.DepartmentCreateCommand;
import pcc.puppet.enforcer.realm.department.ports.event.DepartmentCreateEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@HttpExchange
public interface DepartmentClient extends DepartmentOperations {
  String USER_AGENT = "DepartmentClient/" + Project.VERSION + " (" + Project.NAME + ")";

  @PostExchange(
      accept = MediaType.APPLICATION_JSON_VALUE,
      contentType = MediaType.APPLICATION_JSON_VALUE)
  Mono<DepartmentCreateEvent> departmentCreate(
      @NotNull @SpanTag(CONSUMER) @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @SpanTag(REQUESTER) @RequestHeader(REQUESTER) String requester,
      @NotNull @SpanTag(ORGANIZATION) @RequestHeader(ORGANIZATION) String organizationId,
      @NotNull @Valid @RequestBody DepartmentCreateCommand createCommand);

  @Override
  @Observed(name = "department-client::department-create")
  default Mono<DepartmentCreateEvent> departmentCreate(
      @NotNull String requester,
      @NotNull String organizationId,
      @NotNull @Valid DepartmentCreateCommand createCommand) {
    return JwtTool.authentication()
        .flatMap(
            token ->
                departmentCreate(
                    USER_AGENT, JwtTool.toBearer(token), requester, organizationId, createCommand));
  }

  @GetExchange(value = "/{departmentId}", accept = MediaType.APPLICATION_JSON_VALUE)
  Mono<DepartmentPresenter> findDepartment(
      @NotNull @SpanTag(CONSUMER) @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @SpanTag(REQUESTER) @RequestHeader(REQUESTER) String requester,
      @NotNull @SpanTag(ORGANIZATION) @RequestHeader(ORGANIZATION) String organizationId,
      @NotNull @PathVariable String departmentId);

  @Override
  @Observed(name = "department-client::find-department")
  default Mono<DepartmentPresenter> findDepartment(
      @NotNull String requester, @NotNull String organizationId, @NotNull String departmentId) {
    return JwtTool.authentication()
        .flatMap(
            token ->
                findDepartment(
                    USER_AGENT, JwtTool.toBearer(token), requester, organizationId, departmentId));
  }

  @GetExchange(value = "/{departmentId}/child", accept = MediaType.APPLICATION_JSON_VALUE)
  Flux<DepartmentPresenter> findChildDepartments(
      @NotNull @SpanTag(CONSUMER) @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @SpanTag(REQUESTER) @RequestHeader(REQUESTER) String requester,
      @NotNull @SpanTag(ORGANIZATION) @RequestHeader(ORGANIZATION) String organizationId,
      @NotNull @SpanTag(DEPARTMENT) @PathVariable String departmentId);

  @Override
  @Observed(name = "department-client::find-child-departments")
  default Flux<DepartmentPresenter> findChildDepartments(
      @NotNull String requester, @NotNull String organizationId, @NotNull String departmentId) {
    return JwtTool.authentication()
        .flatMapMany(
            token ->
                findChildDepartments(
                    USER_AGENT, JwtTool.toBearer(token), requester, organizationId, departmentId));
  }
}
