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

import io.micronaut.core.annotation.NonNull;
import pcc.puppet.enforcer.realm.member.adapters.presenter.MemberPresenter;
import pcc.puppet.enforcer.realm.member.ports.command.MemberCreateCommand;
import pcc.puppet.enforcer.realm.member.ports.event.MemberCreateEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MemberOperations {

  Mono<MemberCreateEvent> memberCreate(
      @NonNull String requester,
      @NonNull String organizationId,
      @NonNull String departmentId,
      @NonNull MemberCreateCommand createCommand);

  Mono<MemberPresenter> findMember(
      @NonNull String requester,
      @NonNull String organizationId,
      @NonNull String departmentId,
      @NonNull String memberId);

  Flux<MemberPresenter> findOrganizationMembers(
      @NonNull String requester,
      @NonNull String organizationId,
      @NonNull String departmentId,
      @NonNull String id);

  Flux<MemberPresenter> findDepartmentMembers(
      @NonNull String requester,
      @NonNull String organizationId,
      @NonNull String departmentId,
      @NonNull String id);
}
