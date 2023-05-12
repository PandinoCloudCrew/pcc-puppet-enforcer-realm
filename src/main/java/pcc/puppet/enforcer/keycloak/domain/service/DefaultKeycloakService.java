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

import io.micrometer.observation.annotation.Observed;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.keycloak.adapters.http.KeycloakAdminClient;
import pcc.puppet.enforcer.keycloak.domain.BearerTokenResponse;
import pcc.puppet.enforcer.keycloak.domain.KeycloakAddressClaimSetRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientCredentials;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakCredentialRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakGroupRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakIntrospection;
import pcc.puppet.enforcer.keycloak.domain.KeycloakTokenDetails;
import pcc.puppet.enforcer.keycloak.domain.KeycloakUserCredentials;
import pcc.puppet.enforcer.keycloak.domain.KeycloakUserInfoRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakUserRepresentation;
import pcc.puppet.enforcer.keycloak.ports.configuration.KeycloakProperties;
import pcc.puppet.enforcer.realm.common.util.JwtTool;
import pcc.puppet.enforcer.realm.passport.ports.event.ConsumerPassportCreateEvent;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@CacheConfig
public class DefaultKeycloakService implements KeycloakService {

  private final KeycloakAdminClient adminClient;
  private final KeycloakProperties keycloakProperties;
  private final String rootClientId;
  private final String rootClientSecret;
  private final String rootRealm;

  public DefaultKeycloakService(
      KeycloakAdminClient adminClient, KeycloakProperties keycloakProperties) {
    this.adminClient = adminClient;
    this.keycloakProperties = keycloakProperties;
    this.rootClientId = keycloakProperties.getClientId();
    this.rootClientSecret = keycloakProperties.getClientSecret();
    this.rootRealm = keycloakProperties.getRealm();
  }

  @Override
  @Observed(name = "default-keycloak-service::token")
  public Mono<BearerTokenResponse> adminLogin() {
    KeycloakClientCredentials credentials =
        KeycloakClientCredentials.builder()
            .clientId(rootClientId)
            .clientSecret(rootClientSecret)
            .build();
    return adminClient.clientLogin(rootRealm, credentials);
  }

  @Override
  @Observed(name = "default-keycloak-service::introspect")
  public Mono<KeycloakTokenDetails> introspect(String token) {
    KeycloakIntrospection introspection =
        KeycloakIntrospection.builder()
            .token(token)
            .clientId(rootClientId)
            .clientSecret(rootClientSecret)
            .build();
    return adminClient.introspect(rootRealm, introspection);
  }

  @Override
  @Observed(name = "default-keycloak-service::token")
  public Mono<BearerTokenResponse> clientLogin(String clientId, String clientSecret) {
    KeycloakClientCredentials credentials =
        KeycloakClientCredentials.builder().clientId(clientId).clientSecret(clientSecret).build();
    return adminClient.clientLogin(rootRealm, credentials);
  }

  @Override
  public Mono<BearerTokenResponse> userLogin(String username, String password) {
    KeycloakUserCredentials credentials =
        KeycloakUserCredentials.builder()
            .username(username)
            .password(password)
            .clientId(keycloakProperties.getAdminClientId())
            .build();
    return adminClient.userLogin(rootRealm, credentials);
  }

  @Override
  @Observed(name = "default-keycloak-service::create-client")
  public Mono<Optional<String>> createClient(KeycloakClientRepresentation client) {
    return adminLogin()
        .map(JwtTool::toBearer)
        .flatMap(auth -> adminClient.createClient(auth, rootRealm, client))
        .map(response -> handleKeycloakResponse(client.getClientId(), response));
  }

  @Override
  public Mono<Optional<String>> createGroup(KeycloakGroupRepresentation group) {
    return adminLogin()
        .map(JwtTool::toBearer)
        .flatMap(auth -> adminClient.createGroup(auth, rootRealm, group))
        .map(response -> handleKeycloakResponse(group.getName(), response));
  }

  @Override
  public Mono<Optional<String>> createChildGroup(
      String parentGroupId, KeycloakGroupRepresentation group) {
    return adminLogin()
        .map(JwtTool::toBearer)
        .flatMap(auth -> adminClient.createChildGroup(auth, rootRealm, parentGroupId, group))
        .map(response -> handleKeycloakResponse(group.getName(), response));
  }

  @Override
  @Observed(name = "default-keycloak-service::create-user")
  public Mono<Optional<String>> createUser(
      String clientId,
      String clientSecret,
      ConsumerPassportCreateEvent createEvent,
      String username,
      String password) {
    return clientLogin(clientId, clientSecret)
        .map(JwtTool::toBearer)
        .flatMap(
            auth ->
                adminClient.createUser(
                    auth, rootRealm, getUserRepresentation(createEvent, username, password)))
        .map(response -> handleKeycloakResponse(username, response));
  }

  @Override
  public Mono<KeycloakUserRepresentation> findUserByUsername(String username) {
    return adminLogin()
        .map(JwtTool::toBearer)
        .flatMapMany(auth -> adminClient.findUserByUsername(auth, rootRealm, username))
        .single();
  }

  @Override
  public Mono<KeycloakGroupRepresentation> findGroupByPath(String path) {
    return adminLogin()
        .map(JwtTool::toBearer)
        .flatMap(auth -> adminClient.findGroupByPath(auth, rootRealm, path));
  }

  @Override
  public Mono<Optional<String>> attachUserToGroup(String userId, String groupId) {
    return adminLogin()
        .map(JwtTool::toBearer)
        .flatMap(auth -> adminClient.addUserToGroup(auth, rootRealm, userId, groupId))
        .map(response -> handleKeycloakResponse(userId, response));
  }

  private static Optional<String> handleKeycloakResponse(
      String entityId, ResponseEntity<Void> response) {
    if (response.getStatusCode().is2xxSuccessful()) {
      return Optional.of("created-" + entityId);
    }
    log.warn("keycloak creation issues {}", response);
    return Optional.empty();
  }

  private static KeycloakUserRepresentation getUserRepresentation(
      ConsumerPassportCreateEvent createEvent, String username, String password) {
    return KeycloakUserRepresentation.builder()
        .firstName(createEvent.getMember().getContactId().getFirstName())
        .lastName(createEvent.getMember().getContactId().getLastName())
        .email(createEvent.getMember().getContactId().getEmail())
        .username(username)
        .credentials(
            List.of(
                KeycloakCredentialRepresentation.builder()
                    .type("password")
                    .temporary(false)
                    .value(password)
                    .build()))
        .emailVerified(true)
        .enabled(true)
        .attributes(
            KeycloakUserInfoRepresentation.builder()
                .website(createEvent.getMember().getId())
                .phoneNumber(createEvent.getMember().getContactId().getPhoneNumber())
                .phoneNumberVerified(true)
                .email(createEvent.getMember().getContactId().getEmail())
                .emailVerified(true)
                .locale(createEvent.getMember().getContactId().getLocale())
                .zoneinfo(createEvent.getMember().getContactId().getZoneId())
                .address(
                    KeycloakAddressClaimSetRepresentation.builder()
                        .streetAddress(createEvent.getOrganization().getLocation())
                        .locality(createEvent.getOrganization().getCity())
                        .country(createEvent.getOrganization().getCountry())
                        .build())
                .build()
                .asAttributes())
        .attribute("memberId", createEvent.getMember().getId())
        .attribute("organizationId", createEvent.getOrganization().getId())
        .attribute("organizationName", createEvent.getOrganization().getName())
        .attribute("departmentId", createEvent.getDepartment().getId())
        .attribute("departmentName", createEvent.getDepartment().getName())
        .attribute("currency", createEvent.getOrganization().getContactId().getCurrency())
        .build();
  }
}
