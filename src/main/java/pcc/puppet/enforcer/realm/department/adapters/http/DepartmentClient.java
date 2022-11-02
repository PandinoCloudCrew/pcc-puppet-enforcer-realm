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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.tracing.annotation.SpanTag;
import javax.validation.Valid;
import pcc.puppet.enforcer.app.Project;
import pcc.puppet.enforcer.realm.department.adapters.presenter.DepartmentPresenter;
import pcc.puppet.enforcer.realm.department.domain.DepartmentOperations;
import pcc.puppet.enforcer.realm.department.ports.command.DepartmentCreateCommand;
import pcc.puppet.enforcer.realm.department.ports.event.DepartmentCreateEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Client("pcc-realm-department")
@Header(name = HttpHeaders.ACCEPT_ENCODING, value = "gzip, deflate")
@Header(
    name = HttpHeaders.USER_AGENT,
    value = "DepartmentClient/" + Project.VERSION + " (" + Project.NAME + ")")
public interface DepartmentClient extends DepartmentOperations {

  @Override
  @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  Mono<DepartmentCreateEvent> departmentCreate(
      @NonNull @SpanTag(REQUESTER) @Header(REQUESTER) String requester,
      @NonNull @SpanTag(ORGANIZATION) @Header(ORGANIZATION) String organizationId,
      @NonNull @Body @Valid DepartmentCreateCommand createCommand);

  @Override
  @Get(uri = "/{departmentId}", consumes = MediaType.APPLICATION_JSON)
  Mono<DepartmentPresenter> findDepartment(
      @NonNull @SpanTag(REQUESTER) @Header(REQUESTER) String requester,
      @NonNull @SpanTag(ORGANIZATION) @Header(ORGANIZATION) String organizationId,
      @NonNull String departmentId);

  @Override
  @Get(uri = "/{departmentId}/child", consumes = MediaType.APPLICATION_JSON)
  Flux<DepartmentPresenter> findChildDepartments(
      @NonNull @SpanTag(REQUESTER) @Header(REQUESTER) String requester,
      @NonNull @SpanTag(ORGANIZATION) @Header(ORGANIZATION) String organizationId,
      @SpanTag @NonNull String departmentId);
}
