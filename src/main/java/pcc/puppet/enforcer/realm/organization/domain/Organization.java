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
import pcc.puppet.enforcer.realm.common.generator.values.AddressStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.CityNameStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.CompanyNameStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.CountryNameStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.ObjectIdStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.TaxIdStrategy;
import uk.co.jemos.podam.common.PodamStrategyValue;

@Data
@Builder
@Document
@Jacksonized
public class Organization {
  @Id
  @NotNull
  @PodamStrategyValue(ObjectIdStrategy.class)
  private String id;

  @Nullable
  @Indexed(background = true)
  @PodamStrategyValue(ObjectIdStrategy.class)
  private String parentId;

  @NotNull
  @PodamStrategyValue(CompanyNameStrategy.class)
  private String name;

  @NotNull
  @PodamStrategyValue(AddressStrategy.class)
  private String location;

  @NotNull
  @PodamStrategyValue(CountryNameStrategy.class)
  private String country;

  @NotNull
  @PodamStrategyValue(CityNameStrategy.class)
  private String city;

  @NotNull
  @PodamStrategyValue(TaxIdStrategy.class)
  private String taxId;

  @NotNull @DocumentReference private ContactInformation contactId;

  @NotNull @CreatedBy private String createdBy;
  @NotNull @CreatedDate private Instant createdAt;
  @Nullable @LastModifiedBy private String updatedBy;
  @Nullable @LastModifiedDate private Instant updatedAt;
  @Version private Integer version;

  public Organization setContact(ContactInformation contactInformation) {
    this.contactId = contactInformation;
    return this;
  }
}
