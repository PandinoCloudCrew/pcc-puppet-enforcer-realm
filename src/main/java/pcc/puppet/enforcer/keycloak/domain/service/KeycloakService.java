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

package pcc.puppet.enforcer.keycloak.domain.service;

import java.util.Optional;
import pcc.puppet.enforcer.keycloak.domain.BearerTokenResponse;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakGroupRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakUserRepresentation;
import reactor.core.publisher.Mono;

public interface KeycloakService {
  Mono<BearerTokenResponse> adminLogin();

  Mono<BearerTokenResponse> clientLogin(String clientId, String clientSecret);

  Mono<BearerTokenResponse> userLogin(String username, String password);

  Mono<Optional<String>> createClient(KeycloakClientRepresentation client);

  Mono<Optional<String>> createGroup(KeycloakGroupRepresentation group);

  Mono<Optional<String>> createChildGroup(String parentGroupId, KeycloakGroupRepresentation group);

  Mono<Optional<String>> createUser(
      String clientId, String clientSecret, KeycloakUserRepresentation userRepresentation);

  Mono<KeycloakUserRepresentation> findUserByUsername(String username);

  Mono<KeycloakGroupRepresentation> findGroupByPath(String path);

  Mono<KeycloakGroupRepresentation> findChildGroupByPath(
      String organizationId, String departmentId);

  Mono<Optional<String>> attachUserToGroup(String userId, String groupId);
}
