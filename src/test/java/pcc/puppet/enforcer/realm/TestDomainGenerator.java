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
package pcc.puppet.enforcer.realm;

import io.micronaut.context.annotation.Requires;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.Optional;
import pcc.puppet.enforcer.realm.common.generator.DomainFactory;
import pcc.puppet.enforcer.realm.department.adapters.http.DepartmentClient;
import pcc.puppet.enforcer.realm.department.ports.command.DepartmentCreateCommand;
import pcc.puppet.enforcer.realm.department.ports.event.DepartmentCreateEvent;
import pcc.puppet.enforcer.realm.member.adapters.http.MemberClient;
import pcc.puppet.enforcer.realm.member.ports.command.MemberCreateCommand;
import pcc.puppet.enforcer.realm.member.ports.event.MemberCreateEvent;
import pcc.puppet.enforcer.realm.organization.adapters.http.OrganizationClient;
import pcc.puppet.enforcer.realm.organization.ports.command.OrganizationCreateCommand;
import pcc.puppet.enforcer.realm.organization.ports.event.OrganizationCreateEvent;

@Singleton
@Requires(env = "test")
public class TestDomainGenerator {
  @Inject private MemberClient memberClient;
  @Inject private DepartmentClient departmentClient;
  @Inject private OrganizationClient organizationClient;
  public static final String REQUESTER_ID = "test@pandino.co";

  public OrganizationCreateEvent organization() {
    return organization(REQUESTER_ID, Optional.empty());
  }

  public OrganizationCreateEvent organization(String partenId) {
    return organization(REQUESTER_ID, Optional.of(partenId));
  }

  public OrganizationCreateEvent organization(String requester, Optional<String> parentId) {
    OrganizationCreateCommand organizationCreateCommand = DomainFactory.organizationCreateCommand();
    parentId.ifPresent(organizationCreateCommand::setParentId);
    return organizationClient.organizationCreate(requester, organizationCreateCommand).block();
  }

  public DepartmentCreateEvent department(String organizationId) {
    return department(REQUESTER_ID, Optional.empty(), organizationId);
  }

  public DepartmentCreateEvent department(String parentId, String organizationId) {
    return department(REQUESTER_ID, Optional.of(parentId), organizationId);
  }

  public DepartmentCreateEvent department(
      String requester, Optional<String> parentId, String organizationId) {
    DepartmentCreateCommand departmentCreateCommand = DomainFactory.departmentCreateCommand();
    parentId.ifPresent(departmentCreateCommand::setParentId);
    departmentCreateCommand.setOrganizationId(organizationId);

    return departmentClient
        .departmentCreate(requester, organizationId, departmentCreateCommand)
        .block();
  }

  public MemberCreateEvent member(String requester, String organizationId, String departmentId) {
    MemberCreateCommand memberCreateCommand = DomainFactory.memberCreateCommand();
    memberCreateCommand.setOrganizationId(organizationId);
    memberCreateCommand.setDepartmentId(departmentId);
    return memberClient
        .memberCreate(
            requester,
            memberCreateCommand.getOrganizationId(),
            memberCreateCommand.getDepartmentId(),
            memberCreateCommand)
        .block();
  }
}
