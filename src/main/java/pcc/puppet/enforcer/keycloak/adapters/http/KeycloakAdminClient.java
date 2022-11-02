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

import static io.micronaut.http.HttpHeaders.AUTHORIZATION;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import java.util.Optional;
import javax.validation.Valid;
import pcc.puppet.enforcer.app.Project;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientCredentials;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakIntrospection;
import pcc.puppet.enforcer.keycloak.domain.KeycloakTokenDetails;
import pcc.puppet.enforcer.keycloak.domain.KeycloakUserRepresentation;
import reactor.core.publisher.Mono;

@Client("provider-keycloak")
@Header(
    name = HttpHeaders.USER_AGENT,
    value = "KeycloakAdminClient/" + Project.VERSION + " (" + Project.NAME + ")")
public interface KeycloakAdminClient {

  @Post(
      uri = "/realms/{realm}/protocol/openid-connect/token",
      produces = MediaType.APPLICATION_FORM_URLENCODED,
      consumes = MediaType.APPLICATION_JSON)
  Mono<AccessRefreshToken> token(
      @NonNull String realm, @Valid @Body KeycloakClientCredentials credentials);

  @Post(
      uri = "/realms/{realm}/protocol/openid-connect/token/introspect",
      produces = MediaType.APPLICATION_FORM_URLENCODED,
      consumes = MediaType.APPLICATION_JSON)
  Mono<KeycloakTokenDetails> introspect(
      @NonNull String realm, @Valid @Body KeycloakIntrospection introspection);

  @Post(
      uri = "/admin/realms/{realm}/clients",
      produces = MediaType.APPLICATION_JSON,
      consumes = MediaType.APPLICATION_JSON)
  Mono<Optional<String>> createClient(
      @Header(AUTHORIZATION) String authorization,
      @NonNull String realm,
      @Valid @Body KeycloakClientRepresentation request);

  @Post(
      uri = "/admin/realms/{realm}/users",
      produces = MediaType.APPLICATION_JSON,
      consumes = MediaType.APPLICATION_JSON)
  Mono<Optional<String>> createUser(
      @Header(AUTHORIZATION) String authorization,
      @NonNull String realm,
      @Valid @Body KeycloakUserRepresentation request);
}
