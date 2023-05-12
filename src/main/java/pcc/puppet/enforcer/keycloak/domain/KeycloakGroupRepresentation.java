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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import pcc.puppet.enforcer.realm.department.domain.Department;
import pcc.puppet.enforcer.realm.organization.domain.Organization;

@Data
@Builder
@Jacksonized
public class KeycloakGroupRepresentation {

  private String id;
  private String name;
  private String path;
  private Map<String, String[]> attributes;
  private KeycloakGroupRepresentation[] subGroups;

  @Data
  @Builder
  @Jacksonized
  public static class Attributes {
    private String id;
    private String type;
    private String name;
    private String parentId;
    private String country;
    private String city;
    private String createdBy;
    private String createdAt;

    public Map<String, String[]> toMap() {
      Map<String, String[]> attributes = new HashMap<>();
      attributes.put("id", new String[] {id});
      attributes.put("type", new String[] {type});
      attributes.put("name", new String[] {name});
      attributes.put("parentId", new String[] {parentId});
      attributes.put("country", new String[] {country});
      attributes.put("city", new String[] {city});
      attributes.put("createdBy", new String[] {createdBy});
      attributes.put("createdAt", new String[] {createdAt});
      return attributes;
    }
  }

  public static KeycloakGroupRepresentation fromOrganization(Organization organization) {
    return KeycloakGroupRepresentation.builder()
        .name(organization.getId())
        .path(organization.getId())
        .attributes(
            Attributes.builder()
                .id(organization.getId())
                .name(organization.getName())
                .type("ORGANIZATION")
                .parentId(Optional.ofNullable(organization.getParentId()).orElse(""))
                .country(organization.getCountry())
                .city(organization.getCity())
                .createdAt(organization.getCreatedAt().toString())
                .createdBy(organization.getCreatedBy())
                .build()
                .toMap())
        .build();
  }

  public static KeycloakGroupRepresentation fromDepartment(Department department) {
    return KeycloakGroupRepresentation.builder()
        .name(department.getId())
        .path(department.getId())
        .attributes(
            Attributes.builder()
                .id(department.getId())
                .name(department.getName())
                .type("DEPARTMENT")
                .parentId(Optional.ofNullable(department.getParentId()).orElse(""))
                .createdAt(department.getCreatedAt().toString())
                .createdBy(department.getCreatedBy())
                .build()
                .toMap())
        .build();
  }
}
