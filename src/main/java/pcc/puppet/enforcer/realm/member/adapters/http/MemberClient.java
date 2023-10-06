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

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.DEPARTMENT;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.ORGANIZATION;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.REQUESTER;

import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.annotation.SpanTag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
import pcc.puppet.enforcer.realm.member.adapters.presenter.MemberPresenter;
import pcc.puppet.enforcer.realm.member.domain.MemberOperations;
import pcc.puppet.enforcer.realm.member.ports.command.MemberCreateCommand;
import pcc.puppet.enforcer.realm.member.ports.event.MemberCreateEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@HttpExchange
public interface MemberClient extends MemberOperations {
  String USER_AGENT = "MemberClient/" + Project.VERSION + " (" + Project.NAME + ")";

  @PostExchange(
      accept = MediaType.APPLICATION_JSON_VALUE,
      contentType = MediaType.APPLICATION_JSON_VALUE)
  Mono<MemberCreateEvent> memberCreate(
      @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @RequestHeader(AUTHORIZATION) String authorization,
      @SpanTag(REQUESTER) @NotNull @RequestHeader(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NotNull @RequestHeader(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NotNull @RequestHeader(DEPARTMENT) String departmentId,
      @NotNull @Valid @RequestBody MemberCreateCommand createCommand);

  @Override
  @Observed(name = "member-client::member-create")
  default Mono<MemberCreateEvent> memberCreate(
      @NotNull String requester,
      @NotNull String organizationId,
      @NotNull String departmentId,
      @NotNull @Valid MemberCreateCommand createCommand) {
    return JwtTool.authentication()
        .flatMap(
            token ->
                memberCreate(
                    USER_AGENT,
                    JwtTool.toBearer(token),
                    requester,
                    organizationId,
                    departmentId,
                    createCommand));
  }

  @GetExchange(value = "/{memberId}", accept = MediaType.APPLICATION_JSON_VALUE)
  Mono<MemberPresenter> findMember(
      @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @RequestHeader(AUTHORIZATION) String authorization,
      @SpanTag(REQUESTER) @NotNull @RequestHeader(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NotNull @RequestHeader(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NotNull @RequestHeader(DEPARTMENT) String departmentId,
      @SpanTag @NotNull @PathVariable String memberId);

  @Override
  @Observed(name = "member-client::find-member")
  default Mono<MemberPresenter> findMember(
      @NotNull String requester,
      @NotNull String organizationId,
      @NotNull String departmentId,
      @NotNull String memberId) {
    return JwtTool.authentication()
        .flatMap(
            token ->
                findMember(
                    USER_AGENT,
                    JwtTool.toBearer(token),
                    requester,
                    organizationId,
                    departmentId,
                    memberId));
  }

  @GetExchange(
      value = "/organization/{parentOrganizationId}",
      accept = MediaType.APPLICATION_JSON_VALUE)
  Flux<MemberPresenter> findOrganizationMembers(
      @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @RequestHeader(AUTHORIZATION) String authorization,
      @SpanTag(REQUESTER) @NotNull @RequestHeader(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NotNull @RequestHeader(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NotNull @RequestHeader(DEPARTMENT) String departmentId,
      @SpanTag @NotNull @PathVariable String parentOrganizationId);

  @Override
  @Observed(name = "member-client::find-organization-members")
  default Flux<MemberPresenter> findOrganizationMembers(
      @SpanTag(REQUESTER) @NotNull @RequestHeader(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NotNull @RequestHeader(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NotNull @RequestHeader(DEPARTMENT) String departmentId,
      @SpanTag @NotNull @PathVariable String parentOrganizationId) {
    return JwtTool.authentication()
        .flatMapMany(
            token ->
                findOrganizationMembers(
                    USER_AGENT,
                    JwtTool.toBearer(token),
                    requester,
                    organizationId,
                    departmentId,
                    parentOrganizationId));
  }

  @GetExchange(
      value = "/department/{parentDepartmentId}",
      accept = MediaType.APPLICATION_JSON_VALUE)
  Flux<MemberPresenter> findDepartmentMembers(
      @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @RequestHeader(AUTHORIZATION) String authorization,
      @SpanTag(REQUESTER) @NotNull @RequestHeader(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NotNull @RequestHeader(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NotNull @RequestHeader(DEPARTMENT) String departmentId,
      @SpanTag @NotNull @PathVariable String parentDepartmentId);

  @Override
  @Observed(name = "member-client::find-department-members")
  default Flux<MemberPresenter> findDepartmentMembers(
      @NotNull String requester,
      @NotNull String organizationId,
      @NotNull String departmentId,
      @NotNull String parentDepartmentId) {
    return JwtTool.authentication()
        .flatMapMany(
            token ->
                findDepartmentMembers(
                    USER_AGENT,
                    JwtTool.toBearer(token),
                    requester,
                    organizationId,
                    departmentId,
                    parentDepartmentId));
  }
}
