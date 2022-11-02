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
package pcc.puppet.enforcer.realm.organization.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static pcc.puppet.enforcer.realm.TestDomainGenerator.REQUESTER_ID;

import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pcc.puppet.enforcer.realm.TestDomainGenerator;
import pcc.puppet.enforcer.realm.common.format.DateFormat;
import pcc.puppet.enforcer.realm.common.generator.DomainFactory;
import pcc.puppet.enforcer.realm.organization.adapters.http.OrganizationClient;
import pcc.puppet.enforcer.realm.organization.adapters.presenter.OrganizationPresenter;
import pcc.puppet.enforcer.realm.organization.ports.command.OrganizationCreateCommand;
import pcc.puppet.enforcer.realm.organization.ports.event.OrganizationCreateEvent;
import pcc.puppet.enforcer.security.password.SecurePasswordGenerator;

@MicronautTest(transactional = false)
class OrganizationControllerTest {

  @Inject private OrganizationClient client;
  @Inject private TestDomainGenerator generator;
  @Inject private SecurePasswordGenerator passwordGenerator;

  @Test
  void organizationCreate_ShouldSucceedWithCleanData_ReturnHttpOkWithDetails() {
    OrganizationCreateCommand createCommand = DomainFactory.organizationCreateCommand();
    OrganizationCreateEvent createEventResponse =
        client.organizationCreate(REQUESTER_ID, createCommand).block();
    assertNotNull(createEventResponse);
    assertNotNull(createEventResponse.getId());
    assertNotNull(createEventResponse.getCreatedAt());
    assertTrue(DateFormat.isValid(createEventResponse.getCreatedAt()));
    assertTrue(DateFormat.isValid(createEventResponse.getContactId().getCreatedAt()));
    assertEquals(createCommand.getParentId(), createEventResponse.getParentId());
    assertEquals(createCommand.getName(), createEventResponse.getName());
    assertEquals(createCommand.getLocation(), createEventResponse.getLocation());
    assertEquals(createCommand.getCountry(), createEventResponse.getCountry());
    assertEquals(createCommand.getCity(), createEventResponse.getCity());
    assertEquals(createCommand.getTaxId(), createEventResponse.getTaxId());
    assertEquals(
        createCommand.getContactId().getFirstName(),
        createEventResponse.getContactId().getFirstName());
    assertEquals(
        createCommand.getContactId().getLastName(),
        createEventResponse.getContactId().getLastName());
    assertEquals(
        createCommand.getContactId().getPhoneNumber(),
        createEventResponse.getContactId().getPhoneNumber());
    assertEquals(
        createCommand.getContactId().getEmail(), createEventResponse.getContactId().getEmail());
    assertEquals(
        createCommand.getContactId().getPosition(),
        createEventResponse.getContactId().getPosition());
    assertEquals(
        createCommand.getContactId().getZoneId(), createEventResponse.getContactId().getZoneId());
    assertEquals(
        createCommand.getContactId().getLocale(), createEventResponse.getContactId().getLocale());
    assertEquals(
        createCommand.getContactId().getCountry(), createEventResponse.getContactId().getCountry());
    assertEquals(
        createCommand.getContactId().getCity(), createEventResponse.getContactId().getCity());
    assertEquals(
        createCommand.getContactId().getCurrency(),
        createEventResponse.getContactId().getCurrency());
  }

  @Test
  void findOrganization_OnFoundOrganization_ShouldReturnOk() {
    OrganizationCreateEvent createEventResponse = generator.organization();
    assertNotNull(createEventResponse);
    OrganizationPresenter organizationPresenter =
        client.findOrganization(REQUESTER_ID, createEventResponse.getId()).block();
    assertNotNull(organizationPresenter);
    assertEquals(createEventResponse.getId(), organizationPresenter.getId());
    assertEquals(createEventResponse.getCreatedAt(), organizationPresenter.getCreatedAt());
    assertEquals(createEventResponse.getParentId(), organizationPresenter.getParentId());
    assertEquals(createEventResponse.getName(), organizationPresenter.getName());
    assertEquals(createEventResponse.getLocation(), organizationPresenter.getLocation());
    assertEquals(createEventResponse.getCountry(), organizationPresenter.getCountry());
    assertEquals(createEventResponse.getCity(), organizationPresenter.getCity());
    assertEquals(createEventResponse.getTaxId(), organizationPresenter.getTaxId());
    assertEquals(
        createEventResponse.getContactId().getFirstName(),
        organizationPresenter.getContactId().getFirstName());
    assertEquals(
        createEventResponse.getContactId().getLastName(),
        organizationPresenter.getContactId().getLastName());
    assertEquals(
        createEventResponse.getContactId().getPhoneNumber(),
        organizationPresenter.getContactId().getPhoneNumber());
    assertEquals(
        createEventResponse.getContactId().getEmail(),
        organizationPresenter.getContactId().getEmail());
    assertEquals(
        createEventResponse.getContactId().getPosition(),
        organizationPresenter.getContactId().getPosition());
    assertEquals(
        createEventResponse.getContactId().getZoneId(),
        organizationPresenter.getContactId().getZoneId());
    assertEquals(
        createEventResponse.getContactId().getLocale(),
        organizationPresenter.getContactId().getLocale());
    assertEquals(
        createEventResponse.getContactId().getCountry(),
        organizationPresenter.getContactId().getCountry());
    assertEquals(
        createEventResponse.getContactId().getCity(),
        organizationPresenter.getContactId().getCity());
    assertEquals(
        createEventResponse.getContactId().getCurrency(),
        organizationPresenter.getContactId().getCurrency());
  }

  @Test
  void findChildOrganizations_OnFoundChildOrganizations_ShouldReturnArrayOk() {
    String organizationId = DomainFactory.id();

    OrganizationCreateEvent createEventResponseFirst = generator.organization(organizationId);
    assertNotNull(createEventResponseFirst);

    OrganizationCreateEvent createEventResponseSecond = generator.organization(organizationId);
    assertNotNull(createEventResponseSecond);

    List<OrganizationPresenter> childOrganizations =
        client.findChildOrganizations(REQUESTER_ID, organizationId).collectList().block();

    assertNotNull(childOrganizations);
    assertFalse(childOrganizations.isEmpty());
    assertEquals(2, childOrganizations.size());
    childOrganizations.forEach(
        organization -> assertEquals(organizationId, organization.getParentId()));
  }

  @Test
  void organizationLogin_whenOrganizationExistsAndCredentialsAreValid_shouldReturnAccessToken() {
    String organizationId = DomainFactory.id();
    UsernamePasswordCredentials credentials =
        new UsernamePasswordCredentials(organizationId, passwordGenerator.password(16));
    Assertions.assertAll(
        () -> {
          AccessRefreshToken accessRefreshToken =
              client.organizationLogin(organizationId, credentials).block();
          assertNotNull(accessRefreshToken);
        });
  }
}
