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

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientRepresentation;
import pcc.puppet.enforcer.realm.organization.adapters.repository.OrganizationCredentialsRepository;
import pcc.puppet.enforcer.realm.organization.domain.OrganizationCredentials;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class VaultDefaultSecretService implements KVSecretService {

  private final OrganizationCredentialsRepository credentialsRepository;

  @Override
  @Observed(name = "vault-default-secret-service::create-client-secret")
  public Mono<OrganizationCredentials> createClientSecret(
      String organizationId, KeycloakClientRepresentation clientRepresentation) {
    OrganizationCredentials request = getRequest(organizationId, clientRepresentation);
    return credentialsRepository.save(request);
  }

  @Override
  @Observed(name = "vault-default-secret-service::get-credentials")
  public Mono<OrganizationCredentials> getCredentials(String organizationId) {
    return credentialsRepository.findById(organizationId);
  }

  private static OrganizationCredentials getRequest(
      String organizationId, KeycloakClientRepresentation clientRepresentation) {
    return OrganizationCredentials.builder()
        .id(organizationId)
        .clientName(clientRepresentation.getName())
        .clientId(clientRepresentation.getClientId())
        .clientSecret(clientRepresentation.getSecret())
        .build();
  }
}
