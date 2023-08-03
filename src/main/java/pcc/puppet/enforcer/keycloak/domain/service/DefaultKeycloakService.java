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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.keycloak.adapters.http.KeycloakAdminClient;
import pcc.puppet.enforcer.keycloak.domain.BearerTokenResponse;
import pcc.puppet.enforcer.keycloak.domain.BearerTokenStatus;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientCredentials;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakGroupRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakUserCredentials;
import pcc.puppet.enforcer.keycloak.domain.KeycloakUserRepresentation;
import pcc.puppet.enforcer.keycloak.ports.configuration.KeycloakProperties;
import pcc.puppet.enforcer.realm.common.util.JwtTool;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class DefaultKeycloakService implements KeycloakService {
  private final Map<KeycloakClientCredentials, BearerTokenStatus> availableAdminToken;

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
    this.availableAdminToken = new ConcurrentHashMap<>();
  }

  private KeycloakClientCredentials adminCredentials() {
    return KeycloakClientCredentials.builder()
        .clientId(rootClientId)
        .clientSecret(rootClientSecret)
        .build();
  }

  private BearerTokenResponse adminTokenCache(KeycloakClientCredentials credentials) {
    BearerTokenStatus token = this.availableAdminToken.getOrDefault(credentials, null);
    if (Objects.isNull(token) || token.isExpired()) {
      log.debug("Token has expired");
      return null;
    }
    return token.getBearerTokenResponse();
  }

  @Override
  @Observed(name = "default-keycloak-service::token")
  public Mono<BearerTokenResponse> adminLogin() {
    KeycloakClientCredentials credentials = adminCredentials();
    return Optional.ofNullable(adminTokenCache(credentials))
        .map(Mono::just)
        .orElseGet(
            () ->
                adminClient
                    .clientLogin(rootRealm, credentials)
                    .map(
                        token -> {
                          log.info("Token has been requested.");
                          this.availableAdminToken.put(credentials, new BearerTokenStatus(token));
                          return token;
                        }));
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
      String clientId, String clientSecret, KeycloakUserRepresentation userRepresentation) {
    return clientLogin(clientId, clientSecret)
        .map(JwtTool::toBearer)
        .flatMap(auth -> adminClient.createUser(auth, rootRealm, userRepresentation))
        .map(response -> handleKeycloakResponse(userRepresentation.getUsername(), response));
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
  public Mono<KeycloakGroupRepresentation> findChildGroupByPath(
      String organizationId, String departmentId) {
    return adminLogin()
        .map(JwtTool::toBearer)
        .flatMap(
            auth ->
                adminClient.findChildGroupByPath(auth, rootRealm, organizationId, departmentId));
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
}
