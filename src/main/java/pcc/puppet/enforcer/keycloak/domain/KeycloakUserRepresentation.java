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

package pcc.puppet.enforcer.keycloak.domain;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;
import pcc.puppet.enforcer.realm.member.domain.Member;

@Data
@Builder
@Jacksonized
public class KeycloakUserRepresentation {
  private String id;
  @Singular private Map<String, Object> attributes;
  private Map<String, String> clientRoles;
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private boolean emailVerified;
  private boolean enabled;
  private boolean totp;
  private int notBefore;
  private Map<String, String> access;
  private List<KeycloakCredentialRepresentation> credentials;
  private Instant createdTimestamp;

  public static KeycloakUserRepresentation fromMember(Member member) {
    return KeycloakUserRepresentation.builder()
        .firstName(member.getContactId().getFirstName())
        .lastName(member.getContactId().getLastName())
        .email(member.getContactId().getEmail())
        .username(member.getUsername())
        .credentials(
            List.of(
                KeycloakCredentialRepresentation.builder()
                    .type("password")
                    .temporary(false)
                    .value(member.getPassword())
                    .build()))
        .emailVerified(true)
        .enabled(true)
        .attributes(
            KeycloakUserInfoRepresentation.builder()
                .website(member.getId())
                .phoneNumber(member.getContactId().getPhoneNumber())
                .phoneNumberVerified(true)
                .email(member.getContactId().getEmail())
                .emailVerified(true)
                .locale(member.getContactId().getLocale())
                .zoneinfo(member.getContactId().getZoneId())
                .address(
                    KeycloakAddressClaimSetRepresentation.builder()
                        .locality(member.getContactId().getCity())
                        .country(member.getContactId().getCountry())
                        .build())
                .build()
                .asAttributes())
        .attribute("memberId", member.getId())
        .attribute("organizationId", member.getOrganizationId())
        .attribute("departmentId", member.getDepartmentId())
        .attribute("currency", member.getContactId().getCurrency())
        .build();
  }
}
