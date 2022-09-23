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
package pcc.puppet.enforcer.realm.keycloak.domain.service;

import io.micronaut.cache.annotation.CacheConfig;
import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import jakarta.inject.Singleton;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import pcc.puppet.enforcer.realm.keycloak.adapters.http.KeycloakAdminClient;
import pcc.puppet.enforcer.realm.keycloak.domain.KeycloakClientCredentials;
import pcc.puppet.enforcer.realm.keycloak.domain.KeycloakClientRepresentation;
import pcc.puppet.enforcer.realm.keycloak.ports.configuration.KeycloakProperties;
import reactor.core.publisher.Mono;

@Singleton
@CacheConfig("keycloak")
@RequiredArgsConstructor
public class DefaultKeycloakService implements KeycloakService {

  private final KeycloakAdminClient adminClient;
  private final KeycloakProperties keycloakProperties;

  @Override
  @Cacheable
  public Mono<AccessRefreshToken> token() {
    KeycloakClientCredentials credentials =
        KeycloakClientCredentials.builder()
            .client_id(keycloakProperties.getClientId())
            .client_secret(keycloakProperties.getClientSecret())
            .build();
    return adminClient.token(keycloakProperties.getRealm(), credentials);
  }

  @Override
  public Mono<AccessRefreshToken> token(String clientId, String clientSecret) {
    KeycloakClientCredentials credentials =
        KeycloakClientCredentials.builder().client_id(clientId).client_secret(clientSecret).build();
    return adminClient.token(keycloakProperties.getRealm(), credentials);
  }

  @Override
  public Mono<Optional<String>> createClient(
      String name, String description, String clientId, String clientSecret) {
    return token()
        .flatMap(
            bearerAccessRefreshToken -> {
              KeycloakClientRepresentation clientRepresentation =
                  KeycloakClientRepresentation.builder()
                      .clientId(clientId)
                      .secret(clientSecret)
                      .name(name)
                      .description(description)
                      .serviceAccountsEnabled(Boolean.TRUE)
                      .build();
              return adminClient.createUser(
                  String.format("Bearer %s", bearerAccessRefreshToken.getAccessToken()),
                  keycloakProperties.getRealm(),
                  clientRepresentation);
            });
  }
}
