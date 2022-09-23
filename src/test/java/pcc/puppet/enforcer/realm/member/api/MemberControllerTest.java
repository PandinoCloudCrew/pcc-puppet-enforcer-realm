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
package pcc.puppet.enforcer.realm.member.api;

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
import pcc.puppet.enforcer.realm.department.ports.event.DepartmentCreateEvent;
import pcc.puppet.enforcer.realm.member.adapters.http.MemberClient;
import pcc.puppet.enforcer.realm.member.adapters.presenter.MemberPresenter;
import pcc.puppet.enforcer.realm.member.ports.command.MemberCreateCommand;
import pcc.puppet.enforcer.realm.member.ports.event.MemberCreateEvent;
import pcc.puppet.enforcer.realm.organization.ports.event.OrganizationCreateEvent;

@MicronautTest(transactional = false)
class MemberControllerTest {

  @Inject private MemberClient client;
  @Inject private TestDomainGenerator generator;

  @Test
  void memberCreate_GivenValidParameters_ShouldReturnOk() {
    OrganizationCreateEvent organization = generator.organization();
    DepartmentCreateEvent department = generator.department(organization.getId());

    MemberCreateCommand memberCreateCommand = DomainFactory.memberCreateCommand();
    memberCreateCommand.setOrganizationId(organization.getId());
    memberCreateCommand.setDepartmentId(department.getId());

    MemberCreateEvent memberCreateEvent =
        client
            .memberCreate(
                REQUESTER_ID, organization.getId(), department.getId(), memberCreateCommand)
            .block();

    assertNotNull(memberCreateEvent);
    assertNotNull(memberCreateEvent.getId());
    assertNotNull(memberCreateEvent.getCreatedAt());
    assertTrue(DateFormat.isValid(memberCreateEvent.getCreatedAt()));
    assertTrue(DateFormat.isValid(memberCreateEvent.getContactId().getCreatedAt()));

    assertEquals(memberCreateCommand.getOrganizationId(), memberCreateEvent.getOrganizationId());
    assertEquals(memberCreateCommand.getDepartmentId(), memberCreateEvent.getDepartmentId());
    assertEquals(memberCreateCommand.getUsername(), memberCreateEvent.getUsername());

    assertEquals(
        memberCreateCommand.getContactId().getFirstName(),
        memberCreateEvent.getContactId().getFirstName());
    assertEquals(
        memberCreateCommand.getContactId().getLastName(),
        memberCreateEvent.getContactId().getLastName());
    assertEquals(
        memberCreateCommand.getContactId().getPhoneNumber(),
        memberCreateEvent.getContactId().getPhoneNumber());
    assertEquals(
        memberCreateCommand.getContactId().getEmail(), memberCreateEvent.getContactId().getEmail());
    assertEquals(
        memberCreateCommand.getContactId().getPosition(),
        memberCreateEvent.getContactId().getPosition());
    assertEquals(
        memberCreateCommand.getContactId().getZoneId(),
        memberCreateEvent.getContactId().getZoneId());
    assertEquals(
        memberCreateCommand.getContactId().getLocale(),
        memberCreateEvent.getContactId().getLocale());
    assertEquals(
        memberCreateCommand.getContactId().getCountry(),
        memberCreateEvent.getContactId().getCountry());
    assertEquals(
        memberCreateCommand.getContactId().getCity(), memberCreateEvent.getContactId().getCity());
    assertEquals(
        memberCreateCommand.getContactId().getCurrency(),
        memberCreateEvent.getContactId().getCurrency());
  }

  @Test
  void findMember_GivenAnExistingRecord_ShouldReturnOk() {
    OrganizationCreateEvent organization = generator.organization();
    DepartmentCreateEvent department = generator.department(organization.getId());

    MemberCreateEvent memberCreateEvent =
        generator.member(REQUESTER_ID, organization.getId(), department.getId());

    MemberPresenter memberPresenter =
        client
            .findMember(
                REQUESTER_ID, organization.getId(), department.getId(), memberCreateEvent.getId())
            .block();

    assertNotNull(memberPresenter);
    assertNotNull(memberPresenter.getId());
    assertNotNull(memberPresenter.getCreatedAt());
    assertTrue(DateFormat.isValid(memberPresenter.getCreatedAt()));
    assertEquals(memberCreateEvent.getId(), memberPresenter.getId());
    assertEquals(memberCreateEvent.getCreatedAt(), memberPresenter.getCreatedAt());
    assertEquals(memberCreateEvent.getOrganizationId(), memberPresenter.getOrganizationId());
    assertEquals(memberCreateEvent.getDepartmentId(), memberPresenter.getDepartmentId());
    assertEquals(memberCreateEvent.getUsername(), memberPresenter.getUsername());
    assertEquals(
        memberCreateEvent.getContactId().getFirstName(),
        memberPresenter.getContactId().getFirstName());
    assertEquals(
        memberCreateEvent.getContactId().getLastName(),
        memberPresenter.getContactId().getLastName());
    assertEquals(
        memberCreateEvent.getContactId().getPhoneNumber(),
        memberPresenter.getContactId().getPhoneNumber());
    assertEquals(
        memberCreateEvent.getContactId().getEmail(), memberPresenter.getContactId().getEmail());
    assertEquals(
        memberCreateEvent.getContactId().getPosition(),
        memberPresenter.getContactId().getPosition());
    assertEquals(
        memberCreateEvent.getContactId().getZoneId(), memberPresenter.getContactId().getZoneId());
    assertEquals(
        memberCreateEvent.getContactId().getLocale(), memberPresenter.getContactId().getLocale());
    assertEquals(
        memberCreateEvent.getContactId().getCountry(), memberPresenter.getContactId().getCountry());
    assertEquals(
        memberCreateEvent.getContactId().getCity(), memberPresenter.getContactId().getCity());
    assertEquals(
        memberCreateEvent.getContactId().getCurrency(),
        memberPresenter.getContactId().getCurrency());
  }

  @Test
  void findOrganizationMembers() {
    OrganizationCreateEvent organization = generator.organization();
    DepartmentCreateEvent department = generator.department(organization.getId());

    MemberCreateEvent memberCreateEventFirst =
        generator.member(REQUESTER_ID, organization.getId(), department.getId());
    assertNotNull(memberCreateEventFirst);
    MemberCreateEvent memberCreateEventSecond =
        generator.member(REQUESTER_ID, organization.getId(), department.getId());
    assertNotNull(memberCreateEventSecond);
    List<MemberPresenter> departmentMembers =
        client
            .findOrganizationMembers(
                REQUESTER_ID, organization.getId(), organization.getId(), organization.getId())
            .collectList()
            .block();
    assertNotNull(departmentMembers);
    assertFalse(departmentMembers.isEmpty());
    assertEquals(2, departmentMembers.size());
    departmentMembers.forEach(
        member -> assertEquals(organization.getId(), member.getOrganizationId()));
  }

  @Test
  void findDepartmentMembers() {
    OrganizationCreateEvent organization = generator.organization();
    DepartmentCreateEvent department = generator.department(organization.getId());

    MemberCreateEvent memberCreateEventFirst =
        generator.member(REQUESTER_ID, organization.getId(), department.getId());
    assertNotNull(memberCreateEventFirst);
    MemberCreateEvent memberCreateEventSecond =
        generator.member(REQUESTER_ID, organization.getId(), department.getId());
    assertNotNull(memberCreateEventSecond);
    List<MemberPresenter> departmentMembers =
        client
            .findDepartmentMembers(
                REQUESTER_ID, department.getId(), organization.getId(), department.getId())
            .collectList()
            .block();
    assertNotNull(departmentMembers);
    assertFalse(departmentMembers.isEmpty());
    assertEquals(2, departmentMembers.size());
    departmentMembers.forEach(member -> assertEquals(department.getId(), member.getDepartmentId()));
  }
}
