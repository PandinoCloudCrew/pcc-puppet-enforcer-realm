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
package pcc.puppet.enforcer.realm.organization.ports.event;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import lombok.Builder;
import lombok.Data;
import pcc.puppet.enforcer.realm.common.contact.ports.event.CreateContactInformationEvent;
import pcc.puppet.enforcer.realm.common.generator.values.AddressStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.CityNameStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.CompanyNameStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.CountryNameStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.ObjectIdStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.TaxIdStrategy;
import uk.co.jemos.podam.common.PodamStrategyValue;

@Data
@Builder
@Introspected
public class OrganizationCreateEvent {

  @NonNull
  @PodamStrategyValue(ObjectIdStrategy.class)
  private String id;

  @Nullable
  @PodamStrategyValue(ObjectIdStrategy.class)
  private String parentId;

  @NonNull
  @PodamStrategyValue(CompanyNameStrategy.class)
  private String name;

  @NonNull
  @PodamStrategyValue(AddressStrategy.class)
  private String location;

  @NonNull
  @PodamStrategyValue(CountryNameStrategy.class)
  private String country;

  @NonNull
  @PodamStrategyValue(CityNameStrategy.class)
  private String city;

  @NonNull
  @PodamStrategyValue(TaxIdStrategy.class)
  private String taxId;

  @NonNull private CreateContactInformationEvent contactId;

  private String createdAt;
}
