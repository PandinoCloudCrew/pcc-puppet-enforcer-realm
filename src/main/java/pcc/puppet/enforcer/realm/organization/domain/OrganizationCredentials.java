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

package pcc.puppet.enforcer.realm.organization.domain;

import java.time.Instant;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.vault.repository.mapping.Secret;

@Data
@Builder
@Jacksonized
@Secret(backend = "credentials", value = "organization")
public class OrganizationCredentials {

  @Id private String id;
  private String clientName;
  private String clientId;
  private String clientSecret;

  @CreatedBy private String createdBy;
  @CreatedDate private Instant createdAt;
  @LastModifiedBy private String updatedBy;
  @LastModifiedDate private Instant updatedAt;
  @Version private Integer version;
}
