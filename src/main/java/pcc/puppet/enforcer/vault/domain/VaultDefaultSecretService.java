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

package pcc.puppet.enforcer.vault.domain;

import static pcc.puppet.enforcer.keycloak.domain.KeycloakClientRepresentation.CLIENT_ID;
import static pcc.puppet.enforcer.keycloak.domain.KeycloakClientRepresentation.CLIENT_NAME;
import static pcc.puppet.enforcer.keycloak.domain.KeycloakClientRepresentation.CLIENT_SECRET;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientRepresentation;
import pcc.puppet.enforcer.vault.adapters.http.VaultSecretsClient;
import pcc.puppet.enforcer.vault.adapters.http.request.VaultSecretCreateRequest;
import pcc.puppet.enforcer.vault.ports.configuration.VaultProperties;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class VaultDefaultSecretService implements KVSecretService {

  public static final String KEYCLOAK_CLIENT_SUFFIX = "client-credentials";
  private final VaultSecretsClient secretsClient;
  private final ApplicationContext applicationConfiguration;
  private final VaultProperties vaultClientConfiguration;

  @Observed(name = "vault-default-secret-service::create-client-secret")
  public Mono<String> createClientSecret(KeycloakClientRepresentation clientRepresentation) {
    String applicationName = applicationConfiguration.getApplicationName();
    String uri = vaultClientConfiguration.getUri();
    String token = vaultClientConfiguration.getToken();
    String engine = vaultClientConfiguration.getSecretEngineName();
    VaultSecretCreateRequest request =
        VaultSecretCreateRequest.builder()
            .property(CLIENT_NAME, clientRepresentation.getName())
            .property(CLIENT_ID, clientRepresentation.getClientId())
            .property(CLIENT_SECRET, clientRepresentation.getSecret())
            .build();
    String secretKey =
        String.format(
            "%s/%s/%s",
            applicationName,
            KEYCLOAK_CLIENT_SUFFIX,
            clientRepresentation.getClientId()); // TODO use organization id to get this information
    log.info("creating vault key {}", secretKey);
    return secretsClient
        .createSecret(uri, token, engine, secretKey, request)
        .mapNotNull(
            response -> {
              if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
              }
              log.warn("error creating vault secret {}", response);
              return response.getStatusCode().toString();
            });
  }
}
