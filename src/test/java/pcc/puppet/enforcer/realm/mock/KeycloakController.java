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
package pcc.puppet.enforcer.realm.mock;

import static io.micronaut.http.HttpHeaders.AUTHORIZATION;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import java.util.Optional;
import javax.validation.Valid;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientCredentials;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakIntrospection;
import pcc.puppet.enforcer.keycloak.domain.KeycloakTokenDetails;
import pcc.puppet.enforcer.keycloak.domain.KeycloakUserRepresentation;
import pcc.puppet.enforcer.realm.common.generator.DomainFactory;
import reactor.core.publisher.Mono;

@Controller("/keycloak")
public class KeycloakController {

  @Post(
      uri = "/realms/{realm}/protocol/openid-connect/token",
      consumes = MediaType.APPLICATION_FORM_URLENCODED,
      produces = MediaType.APPLICATION_JSON)
  public Mono<AccessRefreshToken> getToken(
      String realm, @Body KeycloakClientCredentials credentials) {
    return Mono.just(new AccessRefreshToken());
  }

  @Post(
      uri = "/realms/{realm}/protocol/openid-connect/token/introspect",
      consumes = MediaType.APPLICATION_FORM_URLENCODED,
      produces = MediaType.APPLICATION_JSON)
  public Mono<KeycloakTokenDetails> tokenIntrospect(
      String realm, @Body KeycloakIntrospection introspection) {
    return Mono.just(DomainFactory.keycloakTokenDetails());
  }

  @Post(
      uri = "/admin/realms/{realm}/clients",
      produces = MediaType.APPLICATION_JSON,
      consumes = MediaType.APPLICATION_JSON)
  public Mono<Optional<String>> createClient(
      @NonNull @Header(AUTHORIZATION) String authorization,
      @NonNull String realm,
      @Valid @Body KeycloakClientRepresentation request) {
    return Mono.just(Optional.of(request.getDescription()));
  }

  @Post(
      uri = "/admin/realms/{realm}/users",
      produces = MediaType.APPLICATION_JSON,
      consumes = MediaType.APPLICATION_JSON)
  public Mono<Optional<String>> createUser(
      @NonNull @Header(AUTHORIZATION) String authorization,
      @NonNull String realm,
      @Valid @Body KeycloakUserRepresentation request) {
    return Mono.just(Optional.of(request.getEmail()));
  }
}
