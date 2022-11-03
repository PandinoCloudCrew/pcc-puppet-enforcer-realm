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
package pcc.puppet.enforcer.realm.organization.domain.service;

import io.micronaut.cache.annotation.CacheConfig;
import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;
import jakarta.inject.Singleton;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientRepresentation;
import pcc.puppet.enforcer.keycloak.domain.service.KeycloakService;
import pcc.puppet.enforcer.realm.common.contact.domain.service.ContactInformationService;
import pcc.puppet.enforcer.realm.common.generator.DomainFactory;
import pcc.puppet.enforcer.realm.organization.adapters.mapper.OrganizationOutputMapper;
import pcc.puppet.enforcer.realm.organization.adapters.presenter.OrganizationPresenter;
import pcc.puppet.enforcer.realm.organization.adapters.repository.OrganizationRepository;
import pcc.puppet.enforcer.realm.organization.domain.Organization;
import pcc.puppet.enforcer.realm.organization.ports.command.OrganizationCreateCommand;
import pcc.puppet.enforcer.realm.organization.ports.event.OrganizationCreateEvent;
import pcc.puppet.enforcer.realm.organization.ports.mapper.OrganizationInputMapper;
import pcc.puppet.enforcer.security.password.SecurePasswordGenerator;
import pcc.puppet.enforcer.vault.domain.KVSecretService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Singleton
@CacheConfig("organization")
@RequiredArgsConstructor
public class DefaultOrganizationService implements OrganizationService {

  private final OrganizationInputMapper inputMapper;
  private final OrganizationOutputMapper outputMapper;
  private final OrganizationRepository repository;
  private final ContactInformationService contactInformationService;
  private final KeycloakService keycloakService;
  private final KVSecretService secretService;
  private final SecurePasswordGenerator passwordGenerator;
  public static final int DEFAULT_PASSWORD_LENGTH = 16;

  @NewSpan
  @Override
  public Mono<OrganizationCreateEvent> create(
      @SpanTag String requester, OrganizationCreateCommand createCommand) {
    Organization organization = inputMapper.commandToDomain(createCommand);
    organization.setId(DomainFactory.id());
    organization.setCreatedBy(requester);
    organization.setCreatedAt(Instant.now());
    KeycloakClientRepresentation clientRepresentation =
        KeycloakClientRepresentation.builder()
            .clientId(organization.getId())
            .name(organization.getName())
            .description(
                String.format(
                    """
                Country: %s
                City: %s
                TaxId: %s
                ParentId: %s
                """,
                    organization.getCountry(),
                    organization.getCity(),
                    organization.getTaxId(),
                    organization.getParentId()))
            .serviceAccountsEnabled(true)
            .secret(passwordGenerator.password(DEFAULT_PASSWORD_LENGTH))
            .build();
    return keycloakService
        .createClient(
            clientRepresentation.getName(),
            clientRepresentation.getDescription(),
            clientRepresentation.getClientId(),
            clientRepresentation.getSecret())
        .flatMap(
            created ->
                secretService
                    .createClientSecret(clientRepresentation)
                    .flatMap(
                        vaultResponseV2 ->
                            contactInformationService
                                .save(requester, organization.getId(), createCommand.getContactId())
                                .flatMap(
                                    contactInformation -> {
                                      organization.setContactId(contactInformation);
                                      return repository.save(organization);
                                    })
                                .map(inputMapper::domainToEvent)));
  }

  @NewSpan
  @Override
  @Cacheable
  public Mono<OrganizationPresenter> findById(
      @SpanTag String requester, @SpanTag String organizationId) {
    return contactInformationService
        .findByOwnerId(organizationId)
        .flatMap(
            contactInformation ->
                repository
                    .findById(organizationId)
                    .map(
                        organization -> {
                          organization.setContactId(contactInformation);
                          return organization;
                        })
                    .map(outputMapper::domainToPresenter));
  }

  @NewSpan
  @Override
  public Flux<OrganizationPresenter> findByParentId(
      @SpanTag String requester, @SpanTag String organizationId) {
    return repository
        .findByParentId(organizationId)
        .flatMap(
            organization ->
                contactInformationService
                    .findById(organization.getContactId().getId())
                    .map(organization::setContact))
        .map(outputMapper::domainToPresenter);
  }
}
