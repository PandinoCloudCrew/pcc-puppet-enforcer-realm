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
package pcc.puppet.enforcer.realm.member.domain;

import jakarta.validation.constraints.NotNull;
import pcc.puppet.enforcer.realm.member.adapters.presenter.MemberPresenter;
import pcc.puppet.enforcer.realm.member.ports.command.MemberCreateCommand;
import pcc.puppet.enforcer.realm.member.ports.event.MemberCreateEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MemberOperations {

  Mono<MemberCreateEvent> memberCreate(
      @NotNull String requester,
      @NotNull String organizationId,
      @NotNull String departmentId,
      @NotNull MemberCreateCommand createCommand);

  Mono<MemberPresenter> findMember(
      @NotNull String requester,
      @NotNull String organizationId,
      @NotNull String departmentId,
      @NotNull String memberId);

  Flux<MemberPresenter> findOrganizationMembers(
      @NotNull String requester,
      @NotNull String organizationId,
      @NotNull String departmentId,
      @NotNull String id);

  Flux<MemberPresenter> findDepartmentMembers(
      @NotNull String requester,
      @NotNull String organizationId,
      @NotNull String departmentId,
      @NotNull String id);
}
