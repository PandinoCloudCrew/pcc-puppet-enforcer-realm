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
package pcc.puppet.enforcer.realm.passport.ports.event;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import lombok.Builder;
import lombok.Data;
import pcc.puppet.enforcer.realm.department.ports.event.DepartmentCreateEvent;
import pcc.puppet.enforcer.realm.member.ports.event.MemberCreateEvent;
import pcc.puppet.enforcer.realm.organization.ports.event.OrganizationCreateEvent;

@Data
@Builder
@Introspected
public class ConsumerPassportCreateEvent {

  @NonNull private String memberId;
  @NonNull private String username;
  @NonNull private MemberCreateEvent member;
  @NonNull private OrganizationCreateEvent organization;
  @NonNull private DepartmentCreateEvent department;
  @NonNull private AccessRefreshToken accessRefreshToken;

  public ConsumerPassportCreateEvent organization(OrganizationCreateEvent organizationCreateEvent) {
    this.organization = organizationCreateEvent;
    return this;
  }

  public ConsumerPassportCreateEvent department(DepartmentCreateEvent departmentCreateEvent) {
    this.department = departmentCreateEvent;
    return this;
  }

  public ConsumerPassportCreateEvent member(MemberCreateEvent memberCreateEvent) {
    this.member = memberCreateEvent;
    return this;
  }

  public ConsumerPassportCreateEvent token(AccessRefreshToken token) {
    this.accessRefreshToken = token;
    return this;
  }

  public String organizationId() {
    return this.organization.getId();
  }

  public String departmentId() {
    return this.department.getId();
  }

  public String memberId() {
    return this.member.getId();
  }
}
