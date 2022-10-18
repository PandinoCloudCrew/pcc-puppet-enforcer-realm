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
package pcc.puppet.enforcer.realm.keycloak.adapters.http;

import static io.micronaut.http.HttpHeaders.AUTHORIZATION;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import java.util.Optional;
import javax.validation.Valid;
import pcc.puppet.enforcer.realm.keycloak.domain.KeycloakClientCredentials;
import pcc.puppet.enforcer.realm.keycloak.domain.KeycloakClientRepresentation;
import reactor.core.publisher.Mono;

@Client("provider-keycloak")
public interface KeycloakAdminClient {

  @Post(
      uri = "/auth/realms/{realm}/protocol/openid-connect/token",
      produces = MediaType.APPLICATION_FORM_URLENCODED,
      consumes = MediaType.APPLICATION_JSON)
  Mono<AccessRefreshToken> token(
      @NonNull String realm, @Valid @Body KeycloakClientCredentials credentials);

  @Post(
      uri = "/auth/admin/realms/{realm}/clients",
      produces = MediaType.APPLICATION_JSON,
      consumes = MediaType.APPLICATION_JSON)
  Mono<Optional<String>> createUser(
      @Header(AUTHORIZATION) String authorization,
      @NonNull String realm,
      @Valid @Body KeycloakClientRepresentation request);
}