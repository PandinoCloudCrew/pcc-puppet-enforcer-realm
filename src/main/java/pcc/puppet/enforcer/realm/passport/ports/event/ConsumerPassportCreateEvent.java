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

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import pcc.puppet.enforcer.realm.department.ports.event.DepartmentCreateEvent;
import pcc.puppet.enforcer.realm.member.ports.event.MemberCreateEvent;
import pcc.puppet.enforcer.realm.organization.ports.event.OrganizationCreateEvent;

@Data
@Builder
@Jacksonized
public class ConsumerPassportCreateEvent {

  @NotNull private String memberId;
  @NotNull private String username;
  @NotNull private MemberCreateEvent member;
  @NotNull private OrganizationCreateEvent organization;
  @NotNull private DepartmentCreateEvent department;
  @NotNull private OAuth2AccessToken accessRefreshToken;

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

  public ConsumerPassportCreateEvent token(OAuth2AccessTokenResponse token) {
    this.accessRefreshToken = token.getAccessToken();
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
