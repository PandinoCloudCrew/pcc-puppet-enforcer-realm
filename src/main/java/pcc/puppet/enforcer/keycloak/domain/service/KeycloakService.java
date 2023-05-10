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
import pcc.puppet.enforcer.keycloak.domain.KeycloakGroupRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakTokenDetails;
import pcc.puppet.enforcer.realm.organization.domain.Organization;
import pcc.puppet.enforcer.realm.passport.ports.event.ConsumerPassportCreateEvent;
import reactor.core.publisher.Mono;

public interface KeycloakService {
  Mono<BearerTokenResponse> adminLogin();

  Mono<BearerTokenResponse> clientLogin(String clientId, String clientSecret);

  Mono<BearerTokenResponse> userLogin(String username, String password);

  Mono<KeycloakTokenDetails> introspect(String token);

  Mono<Optional<String>> createClient(
      String name, String description, String clientId, String clientSecret);

  Mono<Optional<String>> createGroup(KeycloakGroupRepresentation group);

  Mono<Optional<String>> createUser(
      String clientId,
      String clientSecret,
      ConsumerPassportCreateEvent createEvent,
      String username,
      String password);

  default KeycloakGroupRepresentation groupFromOrganization(Organization organization) {
    return KeycloakGroupRepresentation.builder()
        .name(organization.getId())
        .path(organization.getId())
        .attribute("name", new String[] {organization.getName()})
        .attribute("parentId", new String[] {organization.getParentId()})
        .attribute("country", new String[] {organization.getCountry()})
        .attribute("city", new String[] {organization.getCity()})
        .attribute("createdBy", new String[] {organization.getCreatedBy()})
        .attribute("createdAt", new String[] {organization.getCreatedAt().toString()})
        .build();
  }
}
