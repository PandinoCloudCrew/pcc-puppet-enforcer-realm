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

import io.micronaut.cache.annotation.CacheConfig;
import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import pcc.puppet.enforcer.keycloak.adapters.http.KeycloakAdminClient;
import pcc.puppet.enforcer.keycloak.domain.KeycloakAddressClaimSetRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientCredentials;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakCredentialRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakIntrospection;
import pcc.puppet.enforcer.keycloak.domain.KeycloakTokenDetails;
import pcc.puppet.enforcer.keycloak.domain.KeycloakUserInfoRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakUserRepresentation;
import pcc.puppet.enforcer.keycloak.ports.configuration.KeycloakProperties;
import pcc.puppet.enforcer.realm.passport.ports.event.ConsumerPassportCreateEvent;
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
  public Mono<KeycloakTokenDetails> introspect(String token) {
    KeycloakIntrospection introspection =
        KeycloakIntrospection.builder()
            .token(token)
            .client_id(keycloakProperties.getClientId())
            .client_secret(keycloakProperties.getClientSecret())
            .build();
    return adminClient.introspect(keycloakProperties.getRealm(), introspection);
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
              return adminClient.createClient(
                  String.format("Bearer %s", bearerAccessRefreshToken.getAccessToken()),
                  keycloakProperties.getRealm(),
                  clientRepresentation);
            });
  }

  @Override
  public Mono<Optional<String>> createUser(
      ConsumerPassportCreateEvent createEvent, String username, String password) {
    return token()
        .flatMap(
            bearerAccessRefreshToken -> {
              KeycloakUserRepresentation userRepresentation =
                  KeycloakUserRepresentation.builder()
                      .firstName(createEvent.getMember().getContactId().getFirstName())
                      .lastName(createEvent.getMember().getContactId().getLastName())
                      .email(createEvent.getMember().getContactId().getEmail())
                      .username(username)
                      .credentials(
                          List.of(
                              KeycloakCredentialRepresentation.builder()
                                  .type("password")
                                  .temporary(false)
                                  .value(password)
                                  .build()))
                      .emailVerified(true)
                      .enabled(true)
                      .attributes(
                          KeycloakUserInfoRepresentation.builder()
                              .website(createEvent.getMember().getId())
                              .phoneNumber(createEvent.getMember().getContactId().getPhoneNumber())
                              .phoneNumberVerified(true)
                              .email(createEvent.getMember().getContactId().getEmail())
                              .emailVerified(true)
                              .locale(createEvent.getMember().getContactId().getLocale())
                              .zoneinfo(createEvent.getMember().getContactId().getZoneId())
                              .address(
                                  KeycloakAddressClaimSetRepresentation.builder()
                                      .streetAddress(createEvent.getOrganization().getLocation())
                                      .locality(createEvent.getOrganization().getCity())
                                      .country(createEvent.getOrganization().getCountry())
                                      .build())
                              .build()
                              .asAttributes())
                      .attribute("memberId", createEvent.getMember().getId())
                      .attribute("organizationId", createEvent.getOrganization().getId())
                      .attribute("organizationName", createEvent.getOrganization().getName())
                      .attribute("departmentId", createEvent.getDepartment().getId())
                      .attribute("departmentName", createEvent.getDepartment().getName())
                      .attribute(
                          "currency", createEvent.getOrganization().getContactId().getCurrency())
                      .build();
              return adminClient.createUser(
                  String.format("Bearer %s", bearerAccessRefreshToken.getAccessToken()),
                  keycloakProperties.getRealm(),
                  userRepresentation);
            });
  }
}
