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
package pcc.puppet.enforcer.realm.department.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.List;
import org.junit.jupiter.api.Test;
import pcc.puppet.enforcer.realm.common.DomainFactory;
import pcc.puppet.enforcer.realm.common.format.DateFormat;
import pcc.puppet.enforcer.realm.department.DepartmentClient;
import pcc.puppet.enforcer.realm.department.api.command.DepartmentCreateCommand;
import pcc.puppet.enforcer.realm.department.api.event.DepartmentCreateEvent;
import pcc.puppet.enforcer.realm.department.api.presenter.DepartmentPresenter;
import pcc.puppet.enforcer.realm.organization.api.OrganizationClient;
import pcc.puppet.enforcer.realm.organization.api.command.OrganizationCreateCommand;
import pcc.puppet.enforcer.realm.organization.api.event.OrganizationCreateEvent;

@MicronautTest
class DepartmentControllerTest {
  @Inject private DepartmentClient client;

  @Inject private OrganizationClient organizationClient;

  private final String REQUESTER_ID = "yesid.bocanegra@pandino.co";

  @Test
  void departmentCreate_GivenValidParameters_ShouldReturnOk() {
    OrganizationCreateCommand organizationCreateCommand = DomainFactory.organizationCreateCommand();
    OrganizationCreateEvent organizationCreateEvent =
        organizationClient.organizationCreate(REQUESTER_ID, organizationCreateCommand).block();
    assertNotNull(organizationCreateEvent);
    String organizationId = organizationCreateEvent.getId();

    DepartmentCreateCommand createCommand = DomainFactory.departmentCreateCommand();
    createCommand.setOrganizationId(organizationId);
    DepartmentCreateEvent departmentCreateEvent =
        client.departmentCreate(REQUESTER_ID, organizationId, createCommand).block();
    assertNotNull(departmentCreateEvent);
    assertNotNull(departmentCreateEvent.getId());
    assertNotNull(departmentCreateEvent.getCreatedAt());
    assertTrue(DateFormat.isValid(departmentCreateEvent.getCreatedAt()));
    assertTrue(DateFormat.isValid(departmentCreateEvent.getContactId().getCreatedAt()));
    assertEquals(createCommand.getName(), departmentCreateEvent.getName());
    assertEquals(createCommand.getLocation(), departmentCreateEvent.getLocation());
    assertEquals(createCommand.getParentId(), departmentCreateEvent.getParentId());
    assertEquals(createCommand.getOrganizationId(), departmentCreateEvent.getOrganizationId());
    assertEquals(
        createCommand.getContactId().getLocale(), departmentCreateEvent.getContactId().getLocale());
    assertEquals(
        createCommand.getContactId().getEmail(), departmentCreateEvent.getContactId().getEmail());
    assertEquals(
        createCommand.getContactId().getPhoneNumber(),
        departmentCreateEvent.getContactId().getPhoneNumber());
    assertEquals(
        createCommand.getContactId().getCountry(),
        departmentCreateEvent.getContactId().getCountry());
    assertEquals(
        createCommand.getContactId().getCity(), departmentCreateEvent.getContactId().getCity());
    assertEquals(
        createCommand.getContactId().getCurrency(),
        departmentCreateEvent.getContactId().getCurrency());
    assertEquals(
        createCommand.getContactId().getZoneId(), departmentCreateEvent.getContactId().getZoneId());
    assertEquals(
        createCommand.getContactId().getFirstName(),
        departmentCreateEvent.getContactId().getFirstName());
    assertEquals(
        createCommand.getContactId().getLastName(),
        departmentCreateEvent.getContactId().getLastName());
    assertEquals(
        createCommand.getContactId().getPosition(),
        departmentCreateEvent.getContactId().getPosition());
  }

  @Test
  void findDepartment_GivenAnExistingDepartmentId_ShouldReturnOk() {
    OrganizationCreateCommand organizationCreateCommand = DomainFactory.organizationCreateCommand();
    OrganizationCreateEvent organizationCreateEvent =
        organizationClient.organizationCreate(REQUESTER_ID, organizationCreateCommand).block();
    assertNotNull(organizationCreateEvent);
    String organizationId = organizationCreateEvent.getId();

    DepartmentCreateCommand departmentCreateCommand = DomainFactory.departmentCreateCommand();
    departmentCreateCommand.setOrganizationId(organizationId);
    DepartmentCreateEvent departmentCreateEvent =
        client.departmentCreate(REQUESTER_ID, organizationId, departmentCreateCommand).block();
    assertNotNull(departmentCreateEvent);
    DepartmentPresenter departmentPresenter =
        client.findDepartment(REQUESTER_ID, organizationId, departmentCreateEvent.getId()).block();
    assertNotNull(departmentPresenter);
    assertNotNull(departmentPresenter.getId());
    assertNotNull(departmentPresenter.getCreatedAt());
    assertTrue(DateFormat.isValid(departmentPresenter.getCreatedAt()));
    assertEquals(departmentCreateCommand.getName(), departmentPresenter.getName());
    assertEquals(departmentCreateCommand.getLocation(), departmentPresenter.getLocation());
    assertEquals(departmentCreateCommand.getParentId(), departmentPresenter.getParentId());
    assertEquals(
        departmentCreateCommand.getOrganizationId(), departmentPresenter.getOrganizationId());
    assertEquals(
        departmentCreateCommand.getContactId().getLocale(),
        departmentPresenter.getContactId().getLocale());
    assertEquals(
        departmentCreateCommand.getContactId().getEmail(),
        departmentPresenter.getContactId().getEmail());
    assertEquals(
        departmentCreateCommand.getContactId().getPhoneNumber(),
        departmentPresenter.getContactId().getPhoneNumber());
    assertEquals(
        departmentCreateCommand.getContactId().getCountry(),
        departmentPresenter.getContactId().getCountry());
    assertEquals(
        departmentCreateCommand.getContactId().getCity(),
        departmentPresenter.getContactId().getCity());
    assertEquals(
        departmentCreateCommand.getContactId().getCurrency(),
        departmentPresenter.getContactId().getCurrency());
    assertEquals(
        departmentCreateCommand.getContactId().getZoneId(),
        departmentPresenter.getContactId().getZoneId());
    assertEquals(
        departmentCreateCommand.getContactId().getFirstName(),
        departmentPresenter.getContactId().getFirstName());
    assertEquals(
        departmentCreateCommand.getContactId().getLastName(),
        departmentPresenter.getContactId().getLastName());
    assertEquals(
        departmentCreateCommand.getContactId().getPosition(),
        departmentPresenter.getContactId().getPosition());
  }

  @Test
  void findChildDepartments_OnFoundChildDepartments_ShouldReturnArrayOk() {
    OrganizationCreateCommand organizationCreateCommand = DomainFactory.organizationCreateCommand();
    OrganizationCreateEvent organizationCreateEvent =
        organizationClient.organizationCreate(REQUESTER_ID, organizationCreateCommand).block();
    assertNotNull(organizationCreateEvent);
    String organizationId = organizationCreateEvent.getId();

    DepartmentCreateCommand parentDepartmentCreateCommand = DomainFactory.departmentCreateCommand();
    parentDepartmentCreateCommand.setOrganizationId(organizationId);
    DepartmentCreateEvent parentDepartmentCreateEvent =
        client
            .departmentCreate(REQUESTER_ID, organizationId, parentDepartmentCreateCommand)
            .block();
    assertNotNull(parentDepartmentCreateEvent);
    String departmentId = parentDepartmentCreateEvent.getId();

    DepartmentCreateCommand createCommandFirst = DomainFactory.departmentCreateCommand();
    createCommandFirst.setOrganizationId(organizationId);
    createCommandFirst.setParentId(departmentId);
    DepartmentCreateEvent createEventResponseFirst =
        client.departmentCreate(REQUESTER_ID, organizationId, createCommandFirst).block();
    assertNotNull(createEventResponseFirst);
    DepartmentCreateCommand createCommandSecond = DomainFactory.departmentCreateCommand();
    createCommandSecond.setOrganizationId(organizationId);
    createCommandSecond.setParentId(departmentId);
    DepartmentCreateEvent createEventResponseSecond =
        client.departmentCreate(REQUESTER_ID, organizationId, createCommandSecond).block();
    assertNotNull(createEventResponseSecond);

    List<DepartmentPresenter> childDepartments =
        client
            .findChildDepartments(REQUESTER_ID, organizationId, departmentId)
            .collectList()
            .block();

    assertNotNull(childDepartments);
    assertFalse(childDepartments.isEmpty());
    assertEquals(2, childDepartments.size());
    childDepartments.forEach(
        organization -> {
          assertEquals(organizationId, organization.getOrganizationId());
          assertEquals(departmentId, organization.getParentId());
        });
  }
}
