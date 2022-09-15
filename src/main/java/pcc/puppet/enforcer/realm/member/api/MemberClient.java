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
package pcc.puppet.enforcer.realm.member.api;

import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.DEPARTMENT;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.ORGANIZATION;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.REQUESTER;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;
import javax.validation.Valid;
import pcc.puppet.enforcer.realm.member.api.command.MemberCreateCommand;
import pcc.puppet.enforcer.realm.member.api.event.MemberCreateEvent;
import pcc.puppet.enforcer.realm.member.api.presenter.MemberPresenter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Client("${micronaut.http.services.member.path}")
public interface MemberClient extends MemberOperations {

  @Override
  @NewSpan
  @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  Mono<MemberCreateEvent> memberCreate(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NonNull @Header(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NonNull @Header(DEPARTMENT) String departmentId,
      @NonNull @Valid @Body MemberCreateCommand createCommand);

  @Override
  @NewSpan
  @Get(uri = "/{memberId}", consumes = MediaType.APPLICATION_JSON)
  Mono<MemberPresenter> findMember(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NonNull @Header(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NonNull @Header(DEPARTMENT) String departmentId,
      @SpanTag @NonNull @QueryValue("memberId") String memberId);

  @Override
  @NewSpan
  @Get(uri = "/organization/{parentOrganizationId}", consumes = MediaType.APPLICATION_JSON)
  Flux<MemberPresenter> findOrganizationMembers(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NonNull @Header(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NonNull @Header(DEPARTMENT) String departmentId,
      @SpanTag @NonNull String parentOrganizationId);

  @Override
  @NewSpan
  @Get(uri = "/department/{parentDepartmentId}", consumes = MediaType.APPLICATION_JSON)
  Flux<MemberPresenter> findDepartmentMembers(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NonNull @Header(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NonNull @Header(DEPARTMENT) String departmentId,
      @SpanTag @NonNull String parentDepartmentId);
}