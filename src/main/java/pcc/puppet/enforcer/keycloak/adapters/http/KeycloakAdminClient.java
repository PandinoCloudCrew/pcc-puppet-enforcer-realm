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

package pcc.puppet.enforcer.keycloak.adapters.http;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import io.micrometer.observation.annotation.Observed;
import jakarta.validation.constraints.NotNull;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;
import pcc.puppet.enforcer.app.Project;
import pcc.puppet.enforcer.keycloak.domain.BearerTokenResponse;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientCredentials;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakGroupRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakIntrospection;
import pcc.puppet.enforcer.keycloak.domain.KeycloakTokenDetails;
import pcc.puppet.enforcer.keycloak.domain.KeycloakUserCredentials;
import pcc.puppet.enforcer.keycloak.domain.KeycloakUserRepresentation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@HttpExchange
public interface KeycloakAdminClient {
  String USER_AGENT = "KeycloakAdminClient/" + Project.VERSION + " (" + Project.NAME + ")";

  @PostExchange(
      value = "/realms/{realm}/protocol/openid-connect/token",
      contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      accept = MediaType.APPLICATION_JSON_VALUE)
  Mono<BearerTokenResponse> token(
      @NotNull @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @PathVariable String realm,
      @NotNull @Valid @RequestBody MultiValueMap<String, String> credentials);

  @Observed(name = "keycloak-admin-client::token")
  default Mono<BearerTokenResponse> clientLogin(
      @NotNull String realm, @NotNull @Valid KeycloakClientCredentials credentials) {
    return token(USER_AGENT, realm, credentials.toFormData());
  }

  @Observed(name = "keycloak-admin-client::token")
  default Mono<BearerTokenResponse> userLogin(
      @NotNull String realm, @NotNull @Valid KeycloakUserCredentials credentials) {
    return token(USER_AGENT, realm, credentials.toFormData());
  }

  @PostExchange(
      value = "/realms/{realm}/protocol/openid-connect/token/introspect",
      contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      accept = MediaType.APPLICATION_JSON_VALUE)
  Mono<KeycloakTokenDetails> introspect(
      @NotNull @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @PathVariable String realm,
      @NotNull @Valid @RequestBody MultiValueMap<String, String> introspection);

  @Observed(name = "keycloak-admin-client::introspect")
  default Mono<KeycloakTokenDetails> introspect(
      @NotNull String realm, @NotNull @Valid KeycloakIntrospection introspection) {
    return introspect(USER_AGENT, realm, introspection.toFormData());
  }

  @PostExchange(
      value = "/admin/realms/{realm}/clients",
      contentType = MediaType.APPLICATION_JSON_VALUE,
      accept = MediaType.APPLICATION_JSON_VALUE)
  Mono<ResponseEntity<Void>> createClient(
      @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @PathVariable String realm,
      @Valid @RequestBody KeycloakClientRepresentation request);

  @Observed(name = "keycloak-admin-client::create-client")
  default Mono<ResponseEntity<Void>> createClient(
      @NotNull String authorization,
      @NotNull String realm,
      @NotNull @Valid KeycloakClientRepresentation request) {
    return createClient(USER_AGENT, authorization, realm, request);
  }

  @PostExchange(
      value = "/admin/realms/{realm}/users",
      contentType = MediaType.APPLICATION_JSON_VALUE,
      accept = MediaType.APPLICATION_JSON_VALUE)
  Mono<ResponseEntity<Void>> createUser(
      @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @PathVariable String realm,
      @Valid @RequestBody KeycloakUserRepresentation request);

  @Observed(name = "keycloak-admin-client::create-user")
  default Mono<ResponseEntity<Void>> createUser(
      @NotNull String authorization,
      @NotNull String realm,
      @NotNull @Valid KeycloakUserRepresentation request) {
    return createUser(USER_AGENT, authorization, realm, request);
  }

  @PostExchange(
      value = "/admin/realms/{realm}/groups",
      contentType = MediaType.APPLICATION_JSON_VALUE,
      accept = MediaType.APPLICATION_JSON_VALUE)
  Mono<ResponseEntity<Void>> createGroup(
      @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @PathVariable String realm,
      @Valid @RequestBody KeycloakGroupRepresentation request);

  @Observed(name = "keycloak-admin-client::create-group")
  default Mono<ResponseEntity<Void>> createGroup(
      @NotNull String authorization,
      @NotNull String realm,
      @NotNull @Valid KeycloakGroupRepresentation request) {
    return createGroup(USER_AGENT, authorization, realm, request);
  }

  @PostExchange(
      value = "/admin/realms/{realm}/groups/{parentGroupId}/children",
      contentType = MediaType.APPLICATION_JSON_VALUE,
      accept = MediaType.APPLICATION_JSON_VALUE)
  Mono<ResponseEntity<Void>> createChildGroup(
      @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @PathVariable String realm,
      @NotNull @PathVariable String parentGroupId,
      @Valid @RequestBody KeycloakGroupRepresentation request);

  @Observed(name = "keycloak-admin-client::create-child-group")
  default Mono<ResponseEntity<Void>> createChildGroup(
      @NotNull String authorization,
      @NotNull String realm,
      @NotNull String parentGroupId,
      @NotNull @Valid KeycloakGroupRepresentation request) {
    return createChildGroup(USER_AGENT, authorization, realm, parentGroupId, request);
  }

  @GetExchange(value = "/admin/realms/{realm}/users", accept = MediaType.APPLICATION_JSON_VALUE)
  Flux<KeycloakUserRepresentation> findUserByUsername(
      @NotNull @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @PathVariable String realm,
      @NotNull @RequestParam String username);

  @Observed(name = "keycloak-admin-client::find-user-by-username")
  default Flux<KeycloakUserRepresentation> findUserByUsername(
      @NotNull String authorization, @NotNull String realm, @NotNull String username) {
    return findUserByUsername(USER_AGENT, authorization, realm, username);
  }

  @GetExchange(
      value = "/admin/realms/{realm}/group-by-path/{groupPath}",
      accept = MediaType.APPLICATION_JSON_VALUE)
  Mono<KeycloakGroupRepresentation> findGroupByPath(
      @NotNull @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @PathVariable String realm,
      @NotNull @PathVariable String groupPath);

  @Observed(name = "keycloak-admin-client::find-group-by-path")
  default Mono<KeycloakGroupRepresentation> findGroupByPath(
      @NotNull String authorization, @NotNull String realm, @NotNull String groupPath) {
    return findGroupByPath(USER_AGENT, authorization, realm, groupPath);
  }

  @GetExchange(
      value = "/admin/realms/{realm}/group-by-path/{organizationId}/{departmentId}",
      accept = MediaType.APPLICATION_JSON_VALUE)
  Mono<KeycloakGroupRepresentation> findChildGroupByPath(
      @NotNull @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @PathVariable String realm,
      @NotNull @PathVariable String organizationId,
      @NotNull @PathVariable String departmentId);

  @Observed(name = "keycloak-admin-client::find-child-group-by-path")
  default Mono<KeycloakGroupRepresentation> findChildGroupByPath(
      @NotNull String authorization,
      @NotNull String realm,
      @NotNull String organizationId,
      @NotNull String departmentId) {
    return findChildGroupByPath(USER_AGENT, authorization, realm, organizationId, departmentId);
  }

  @PutExchange(
      value = "/admin/realms/{realm}/users/{userId}/groups/{groupId}",
      accept = MediaType.APPLICATION_JSON_VALUE)
  Mono<ResponseEntity<Void>> addUserToGroup(
      @NotNull @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @PathVariable String realm,
      @NotNull @PathVariable String userId,
      @NotNull @PathVariable String groupId);

  @Observed(name = "keycloak-admin-client::add-user-to-group")
  default Mono<ResponseEntity<Void>> addUserToGroup(
      @NotNull String authorization,
      @NotNull String realm,
      @NotNull String userId,
      @NotNull String groupId) {
    return addUserToGroup(USER_AGENT, authorization, realm, userId, groupId);
  }
}
