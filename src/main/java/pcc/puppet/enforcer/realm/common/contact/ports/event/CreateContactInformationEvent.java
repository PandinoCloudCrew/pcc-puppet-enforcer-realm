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
package pcc.puppet.enforcer.realm.common.contact.ports.event;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import pcc.puppet.enforcer.realm.common.generator.values.CityNameStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.CountryNameStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.CurrencyCodeStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.EmailStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.FirstNameStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.JobPositionStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.LastNameStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.LocaleStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.ObjectIdStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.PhoneNumberStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.TimeZoneStrategy;
import uk.co.jemos.podam.common.PodamStrategyValue;

@Data
@Builder
public class CreateContactInformationEvent {

  @NotNull
  @PodamStrategyValue(ObjectIdStrategy.class)
  private String id;

  @NotNull private String ownerId;

  @NotNull
  @PodamStrategyValue(FirstNameStrategy.class)
  private String firstName;

  @NotNull
  @PodamStrategyValue(LastNameStrategy.class)
  private String lastName;

  @NotNull
  @PodamStrategyValue(PhoneNumberStrategy.class)
  private String phoneNumber;

  @NotNull
  @PodamStrategyValue(EmailStrategy.class)
  private String email;

  @NotNull
  @PodamStrategyValue(JobPositionStrategy.class)
  private String position;

  @NotNull
  @PodamStrategyValue(TimeZoneStrategy.class)
  private String zoneId;

  @NotNull
  @PodamStrategyValue(LocaleStrategy.class)
  private String locale;

  @NotNull
  @PodamStrategyValue(CountryNameStrategy.class)
  private String country;

  @NotNull
  @PodamStrategyValue(CityNameStrategy.class)
  private String city;

  @NotNull
  @PodamStrategyValue(CurrencyCodeStrategy.class)
  private String currency;

  @NotNull private String createdAt;
}
