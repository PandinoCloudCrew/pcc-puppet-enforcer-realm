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
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.PathVariable;
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
import pcc.puppet.enforcer.realm.member.api.command.MemberCreateCommand;
import pcc.puppet.enforcer.realm.member.api.event.MemberCreateEvent;
import pcc.puppet.enforcer.realm.member.api.presenter.MemberPresenter;
import pcc.puppet.enforcer.realm.member.service.MemberService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@ExecuteOn(TaskExecutors.IO)
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("${micronaut.http.services.member.path}")
@RequiredArgsConstructor
public class MemberController implements MemberOperations {
  private final MemberService memberService;

  @Override
  @NewSpan
  @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  public Mono<MemberCreateEvent> memberCreate(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NonNull @Header(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NonNull @Header(DEPARTMENT) String departmentId,
      @NonNull @Valid @Body MemberCreateCommand createCommand) {
    return memberService.create(requester, createCommand);
  }

  @Override
  @NewSpan
  @Get(uri = "/{memberId}", produces = MediaType.APPLICATION_JSON)
  public Mono<MemberPresenter> findMember(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NonNull @Header(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NonNull @Header(DEPARTMENT) String departmentId,
      @SpanTag @NonNull @PathVariable String memberId) {
    return memberService.findById(requester, memberId);
  }

  @Override
  @NewSpan
  @Get(uri = "/organization/{parentOrganizationId}", produces = MediaType.APPLICATION_JSON)
  public Flux<MemberPresenter> findOrganizationMembers(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NonNull @Header(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NonNull @Header(DEPARTMENT) String departmentId,
      @SpanTag @NonNull @PathVariable String parentOrganizationId) {
    return memberService.findByOrganizationId(requester, parentOrganizationId);
  }

  @Override
  @NewSpan
  @Get(uri = "/department/{parentDepartmentId}", produces = MediaType.APPLICATION_JSON)
  public Flux<MemberPresenter> findDepartmentMembers(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NonNull @Header(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NonNull @Header(DEPARTMENT) String departmentId,
      @SpanTag @NonNull @PathVariable String parentDepartmentId) {
    return memberService.findByDepartmentId(requester, parentDepartmentId);
  }
}
