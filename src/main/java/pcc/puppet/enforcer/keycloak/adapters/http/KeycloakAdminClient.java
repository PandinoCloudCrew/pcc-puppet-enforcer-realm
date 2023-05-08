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
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import pcc.puppet.enforcer.app.Project;
import pcc.puppet.enforcer.keycloak.domain.BearerTokenResponse;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientCredentials;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakIntrospection;
import pcc.puppet.enforcer.keycloak.domain.KeycloakTokenDetails;
import pcc.puppet.enforcer.keycloak.domain.KeycloakUserCredentials;
import pcc.puppet.enforcer.keycloak.domain.KeycloakUserRepresentation;
import reactor.core.publisher.Mono;

@HttpExchange
public interface KeycloakAdminClient {
  String USER_AGENT = "KeycloakAdminClient/" + Project.VERSION + " (" + Project.NAME + ")";

  @PostExchange(
      value = "/realms/{realm}/protocol/openid-connect/token",
      contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      accept = MediaType.APPLICATION_JSON_VALUE)
  Mono<BearerTokenResponse> token(
      @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @PathVariable String realm,
      @Valid @RequestBody MultiValueMap<String, String> credentials);

  @Observed(name = "keycloak-admin-client::token")
  default Mono<BearerTokenResponse> clientLogin(
      @NotNull String realm, @Valid KeycloakClientCredentials credentials) {
    return token(USER_AGENT, realm, credentials.toFormData());
  }

  @Observed(name = "keycloak-admin-client::token")
  default Mono<BearerTokenResponse> userLogin(
      @NotNull String realm, @Valid KeycloakUserCredentials credentials) {
    return token(USER_AGENT, realm, credentials.toFormData());
  }

  @PostExchange(
      value = "/realms/{realm}/protocol/openid-connect/token/introspect",
      contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      accept = MediaType.APPLICATION_JSON_VALUE)
  Mono<KeycloakTokenDetails> introspect(
      @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @PathVariable String realm,
      @Valid @RequestBody MultiValueMap<String, String> introspection);

  @Observed(name = "keycloak-admin-client::introspect")
  default Mono<KeycloakTokenDetails> introspect(
      @NotNull String realm, @Valid KeycloakIntrospection introspection) {
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
      @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @PathVariable String realm,
      @Valid @RequestBody KeycloakClientRepresentation request) {
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
      @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @PathVariable String realm,
      @Valid @RequestBody KeycloakUserRepresentation request) {
    return createUser(USER_AGENT, authorization, realm, request);
  }
}
