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
package pcc.puppet.enforcer.realm.department.domain;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;
import org.apache.logging.log4j.message.AsynchronouslyFormattable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;
import pcc.puppet.enforcer.realm.common.contact.domain.ContactInformation;
import pcc.puppet.enforcer.realm.common.generator.values.CompanyDepartmentStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.InternalAddressStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.ObjectIdStrategy;
import uk.co.jemos.podam.common.PodamStrategyValue;

@Data
@Builder
@Table
public class Department {

  @Id
  @NotNull
  @PodamStrategyValue(ObjectIdStrategy.class)
  private String id;

  @Nullable
  @PodamStrategyValue(ObjectIdStrategy.class)
  private String parentId;

  @NotNull
  @PodamStrategyValue(ObjectIdStrategy.class)
  private String organizationId;

  @NotNull
  @PodamStrategyValue(CompanyDepartmentStrategy.class)
  private String name;

  @NotNull
  @PodamStrategyValue(InternalAddressStrategy.class)
  private String location;

  @NotNull
  private ContactInformation contactId;

  @NotNull private String createdBy;
  @NotNull private Instant createdAt;
  @Nullable private String updatedBy;
  @Nullable private Instant updatedAt;
  @Version private Integer version;

  public Department setContact(ContactInformation contactInformation) {
    this.contactId = contactInformation;
    return this;
  }
}
