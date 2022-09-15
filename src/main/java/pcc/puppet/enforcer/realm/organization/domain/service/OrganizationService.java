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
package pcc.puppet.enforcer.realm.organization.domain.service;

import pcc.puppet.enforcer.realm.organization.adapters.presenter.OrganizationPresenter;
import pcc.puppet.enforcer.realm.organization.ports.command.OrganizationCreateCommand;
import pcc.puppet.enforcer.realm.organization.ports.event.OrganizationCreateEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface OrganizationService {
  Mono<OrganizationCreateEvent> create(String requester, OrganizationCreateCommand createCommand);

  Mono<OrganizationPresenter> findById(String requester, String organizationId);

  Flux<OrganizationPresenter> findByParentId(String requester, String organizationId);
}
