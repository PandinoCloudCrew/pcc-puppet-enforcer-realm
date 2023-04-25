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
package pcc.puppet.enforcer.realm.member.adapters.http;

import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.DEPARTMENT;
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
import pcc.puppet.enforcer.realm.member.adapters.presenter.MemberPresenter;
import pcc.puppet.enforcer.realm.member.domain.MemberOperations;
import pcc.puppet.enforcer.realm.member.ports.command.MemberCreateCommand;
import pcc.puppet.enforcer.realm.member.ports.event.MemberCreateEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@HttpExchange("${spring.http.services.pcc-realm-member.url}")
public interface MemberClient extends MemberOperations {
String USER_AGENT = "MemberClient/" + Project.VERSION + " (" + Project.NAME + ")";
  @PostExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
  Mono<MemberCreateEvent> memberCreate(
      @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @SpanTag(REQUESTER) @NotNull @RequestHeader(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NotNull @RequestHeader(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NotNull @RequestHeader(DEPARTMENT) String departmentId,
      @NotNull @Valid @RequestBody MemberCreateCommand createCommand);
  @Override
default  Mono<MemberCreateEvent> memberCreate(
      @NotNull String requester,
      @NotNull String organizationId,
      @NotNull String departmentId,
      @NotNull @Valid MemberCreateCommand createCommand) {
    return memberCreate(USER_AGENT, requester, organizationId, departmentId, createCommand);
  }
  @GetExchange(value = "/{memberId}", accept = MediaType.APPLICATION_JSON_VALUE)
  Mono<MemberPresenter> findMember(
      @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @SpanTag(REQUESTER) @NotNull @RequestHeader(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NotNull @RequestHeader(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NotNull @RequestHeader(DEPARTMENT) String departmentId,
      @SpanTag @NotNull @PathVariable String memberId);
  @Override
 default Mono<MemberPresenter> findMember(
      @NotNull String requester,
      @NotNull String organizationId,
      @NotNull String departmentId,
      @NotNull String memberId) {
    return findMember(USER_AGENT, requester, organizationId, departmentId, memberId);
  }
  @GetExchange(value = "/organization/{parentOrganizationId}", accept = MediaType.APPLICATION_JSON_VALUE)
  Flux<MemberPresenter> findOrganizationMembers(
      @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @SpanTag(REQUESTER) @NotNull @RequestHeader(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NotNull @RequestHeader(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NotNull @RequestHeader(DEPARTMENT) String departmentId,
      @SpanTag @NotNull @PathVariable String parentOrganizationId);
  @Override
  default Flux<MemberPresenter> findOrganizationMembers(
      @SpanTag(REQUESTER) @NotNull @RequestHeader(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NotNull @RequestHeader(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NotNull @RequestHeader(DEPARTMENT) String departmentId,
      @SpanTag @NotNull @PathVariable String parentOrganizationId) {
    return findOrganizationMembers(USER_AGENT, requester, organizationId, departmentId, parentOrganizationId);
  }
  @GetExchange(value = "/department/{parentDepartmentId}", accept = MediaType.APPLICATION_JSON_VALUE)
  Flux<MemberPresenter> findDepartmentMembers(
      @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @SpanTag(REQUESTER) @NotNull @RequestHeader(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NotNull @RequestHeader(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NotNull @RequestHeader(DEPARTMENT) String departmentId,
      @SpanTag @NotNull @PathVariable String parentDepartmentId);

  @Override
 default Flux<MemberPresenter> findDepartmentMembers(
      @NotNull String requester,
      @NotNull String organizationId,
      @NotNull String departmentId,
      @NotNull String parentDepartmentId) {
    return findDepartmentMembers(USER_AGENT, requester, organizationId, departmentId,parentDepartmentId);
  }
}
