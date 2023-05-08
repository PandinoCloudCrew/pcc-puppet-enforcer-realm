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
package pcc.puppet.enforcer.realm.member.domain;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
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
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import pcc.puppet.enforcer.realm.common.contact.domain.ContactInformation;

@Data
@Builder
@Document
@Jacksonized
public class Member {
  @Id private String id;

  @NotNull
  @Indexed(background = true)
  private String organizationId;

  @NotNull
  @Indexed(background = true)
  private String departmentId;

  @NotNull
  @Indexed(background = true)
  private String username;

  @NotNull private String password;

  @NotNull @DocumentReference private ContactInformation contactId;

  @NotNull @CreatedBy private String createdBy;
  @NotNull @CreatedDate private Instant createdAt;
  @Nullable @LastModifiedBy private String updatedBy;
  @Nullable @LastModifiedDate private Instant updatedAt;
  @Version private Integer version;

  public Member setContact(ContactInformation contactInformation) {
    this.contactId = contactInformation;
    return this;
  }
}
