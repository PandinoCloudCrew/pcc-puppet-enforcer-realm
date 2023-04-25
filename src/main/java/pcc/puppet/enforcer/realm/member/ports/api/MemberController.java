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
package pcc.puppet.enforcer.realm.member.ports.api;

import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.DEPARTMENT;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.ORGANIZATION;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.REQUESTER;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micrometer.tracing.annotation.NewSpan;
import io.micrometer.tracing.annotation.SpanTag;
import jakarta.validation.constraints.NotNull;
import javax.validation.Valid;
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
import pcc.puppet.enforcer.realm.member.adapters.presenter.MemberPresenter;
import pcc.puppet.enforcer.realm.member.domain.MemberOperations;
import pcc.puppet.enforcer.realm.member.domain.service.MemberService;
import pcc.puppet.enforcer.realm.member.ports.command.MemberCreateCommand;
import pcc.puppet.enforcer.realm.member.ports.event.MemberCreateEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
    @RequestMapping("${spring.http.services.pcc-realm-member.path}")
@RequiredArgsConstructor
public class MemberController implements MemberOperations {
  private final MemberService memberService;

  @Timed
  @Counted
  @Override
  @NewSpan
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<MemberCreateEvent> memberCreate(
      @SpanTag(REQUESTER) @NotNull @RequestHeader(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NotNull @RequestHeader(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NotNull @RequestHeader(DEPARTMENT) String departmentId,
      @NotNull @Valid @RequestBody MemberCreateCommand createCommand) {
    return memberService.create(requester, createCommand);
  }

  @Timed
  @Counted
  @Override
  @NewSpan
  @GetMapping(value = "/{memberId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<MemberPresenter> findMember(
      @SpanTag(REQUESTER) @NotNull @RequestHeader(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NotNull @RequestHeader(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NotNull @RequestHeader(DEPARTMENT) String departmentId,
      @SpanTag @NotNull @PathVariable String memberId) {
    return memberService.findById(requester, memberId);
  }

  @Timed
  @Counted
  @Override
  @NewSpan
  @GetMapping(value = "/organization/{parentOrganizationId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<MemberPresenter> findOrganizationMembers(
      @SpanTag(REQUESTER) @NotNull @RequestHeader(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NotNull @RequestHeader(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NotNull @RequestHeader(DEPARTMENT) String departmentId,
      @SpanTag @NotNull @PathVariable String parentOrganizationId) {
    return memberService.findByOrganizationId(requester, parentOrganizationId);
  }

  @Timed
  @Counted
  @Override
  @NewSpan
  @GetMapping(value = "/department/{parentDepartmentId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<MemberPresenter> findDepartmentMembers(
      @SpanTag(REQUESTER) @NotNull @RequestHeader(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NotNull @RequestHeader(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NotNull @RequestHeader(DEPARTMENT) String departmentId,
      @SpanTag @NotNull @PathVariable String parentDepartmentId) {
    return memberService.findByDepartmentId(requester, parentDepartmentId);
  }
}
