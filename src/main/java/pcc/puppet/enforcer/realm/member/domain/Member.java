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

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.TypeDef;
import io.micronaut.data.annotation.Version;
import io.micronaut.data.model.DataType;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;
import pcc.puppet.enforcer.realm.common.contact.adapters.repository.converter.ContactInformationConverter;
import pcc.puppet.enforcer.realm.common.contact.domain.ContactInformation;

@Data
@Builder
@Introspected
@MappedEntity(value = "member")
public class Member {
  @Id private String id;
  @NonNull private String organizationId;
  @NonNull private String departmentId;
  @NonNull private String username;
  @NonNull private String password;

  @NonNull
  @TypeDef(type = DataType.STRING, converter = ContactInformationConverter.class)
  private ContactInformation contactId;

  @NonNull private String createdBy;
  @NonNull private Instant createdAt;
  @Nullable private String updatedBy;
  @Nullable private Instant updatedAt;
  @Version @AutoPopulated private Integer version;

  public Member setContact(ContactInformation contactInformation) {
    this.contactId = contactInformation;
    return this;
  }
}
