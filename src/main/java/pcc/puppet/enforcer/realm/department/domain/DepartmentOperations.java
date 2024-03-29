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

package pcc.puppet.enforcer.realm.department.domain;

import jakarta.validation.constraints.NotNull;
import pcc.puppet.enforcer.realm.department.adapters.presenter.DepartmentPresenter;
import pcc.puppet.enforcer.realm.department.ports.command.DepartmentCreateCommand;
import pcc.puppet.enforcer.realm.department.ports.event.DepartmentCreateEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DepartmentOperations {

  Mono<DepartmentCreateEvent> departmentCreate(
      @NotNull String requester,
      @NotNull String organizationId,
      @NotNull DepartmentCreateCommand createCommand);

  Mono<DepartmentPresenter> findDepartment(
      @NotNull String requester, @NotNull String organizationId, @NotNull String departmentId);

  Flux<DepartmentPresenter> findChildDepartments(
      @NotNull String requester, @NotNull String organizationId, @NotNull String departmentId);
}
