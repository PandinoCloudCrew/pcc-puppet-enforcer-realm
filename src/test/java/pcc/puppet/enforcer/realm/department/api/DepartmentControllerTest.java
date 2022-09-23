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
import static pcc.puppet.enforcer.realm.TestDomainGenerator.REQUESTER_ID;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import java.util.List;
import org.junit.jupiter.api.Test;
import pcc.puppet.enforcer.realm.TestDomainGenerator;
import pcc.puppet.enforcer.realm.common.format.DateFormat;
import pcc.puppet.enforcer.realm.common.generator.DomainFactory;
import pcc.puppet.enforcer.realm.department.adapters.http.DepartmentClient;
import pcc.puppet.enforcer.realm.department.adapters.presenter.DepartmentPresenter;
import pcc.puppet.enforcer.realm.department.ports.command.DepartmentCreateCommand;
import pcc.puppet.enforcer.realm.department.ports.event.DepartmentCreateEvent;
import pcc.puppet.enforcer.realm.organization.ports.event.OrganizationCreateEvent;

@MicronautTest(transactional = false)
class DepartmentControllerTest {
  @Inject private DepartmentClient client;
  @Inject private TestDomainGenerator generator;

  @Test
  void departmentCreate_GivenValidParameters_ShouldReturnOk() {
    OrganizationCreateEvent organizationCreateEvent = generator.organization();
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
    OrganizationCreateEvent organizationCreateEvent = generator.organization();
    String organizationId = organizationCreateEvent.getId();

    DepartmentCreateEvent departmentCreateEvent = generator.department(organizationId);
    assertNotNull(departmentCreateEvent);
    DepartmentPresenter departmentPresenter =
        client.findDepartment(REQUESTER_ID, organizationId, departmentCreateEvent.getId()).block();
    assertNotNull(departmentPresenter);
    assertNotNull(departmentPresenter.getId());
    assertNotNull(departmentPresenter.getCreatedAt());
    assertTrue(DateFormat.isValid(departmentPresenter.getCreatedAt()));
    assertEquals(departmentCreateEvent.getName(), departmentPresenter.getName());
    assertEquals(departmentCreateEvent.getLocation(), departmentPresenter.getLocation());
    assertEquals(departmentCreateEvent.getParentId(), departmentPresenter.getParentId());
    assertEquals(
        departmentCreateEvent.getOrganizationId(), departmentPresenter.getOrganizationId());
    assertEquals(
        departmentCreateEvent.getContactId().getLocale(),
        departmentPresenter.getContactId().getLocale());
    assertEquals(
        departmentCreateEvent.getContactId().getEmail(),
        departmentPresenter.getContactId().getEmail());
    assertEquals(
        departmentCreateEvent.getContactId().getPhoneNumber(),
        departmentPresenter.getContactId().getPhoneNumber());
    assertEquals(
        departmentCreateEvent.getContactId().getCountry(),
        departmentPresenter.getContactId().getCountry());
    assertEquals(
        departmentCreateEvent.getContactId().getCity(),
        departmentPresenter.getContactId().getCity());
    assertEquals(
        departmentCreateEvent.getContactId().getCurrency(),
        departmentPresenter.getContactId().getCurrency());
    assertEquals(
        departmentCreateEvent.getContactId().getZoneId(),
        departmentPresenter.getContactId().getZoneId());
    assertEquals(
        departmentCreateEvent.getContactId().getFirstName(),
        departmentPresenter.getContactId().getFirstName());
    assertEquals(
        departmentCreateEvent.getContactId().getLastName(),
        departmentPresenter.getContactId().getLastName());
    assertEquals(
        departmentCreateEvent.getContactId().getPosition(),
        departmentPresenter.getContactId().getPosition());
  }

  @Test
  void findChildDepartments_OnFoundChildDepartments_ShouldReturnArrayOk() {
    OrganizationCreateEvent organizationCreateEvent = generator.organization();
    String organizationId = organizationCreateEvent.getId();

    DepartmentCreateEvent parentDepartmentCreateEvent = generator.department(organizationId);
    assertNotNull(parentDepartmentCreateEvent);
    String departmentId = parentDepartmentCreateEvent.getId();

    DepartmentCreateEvent createEventResponseFirst =
        generator.department(departmentId, organizationId);
    assertNotNull(createEventResponseFirst);

    DepartmentCreateEvent createEventResponseSecond =
        generator.department(departmentId, organizationId);
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
