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
package pcc.puppet.enforcer.realm.common.contact.event;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import lombok.Data;
import pcc.puppet.enforcer.realm.common.generator.CityNameStrategy;
import pcc.puppet.enforcer.realm.common.generator.CountryNameStrategy;
import pcc.puppet.enforcer.realm.common.generator.CurrencyCodeStrategy;
import pcc.puppet.enforcer.realm.common.generator.EmailStrategy;
import pcc.puppet.enforcer.realm.common.generator.FirstNameStrategy;
import pcc.puppet.enforcer.realm.common.generator.JobPositionStrategy;
import pcc.puppet.enforcer.realm.common.generator.LastNameStrategy;
import pcc.puppet.enforcer.realm.common.generator.LocaleStrategy;
import pcc.puppet.enforcer.realm.common.generator.ObjectIdStrategy;
import pcc.puppet.enforcer.realm.common.generator.PhoneNumberStrategy;
import pcc.puppet.enforcer.realm.common.generator.TimeZoneStrategy;
import uk.co.jemos.podam.common.PodamStrategyValue;

@Data
@Builder
@Serdeable
public class CreateContactInformationEvent {

  @NonNull
  @PodamStrategyValue(ObjectIdStrategy.class)
  private String id;

  @NonNull private String ownerId;

  @NonNull
  @PodamStrategyValue(FirstNameStrategy.class)
  private String firstName;

  @NonNull
  @PodamStrategyValue(LastNameStrategy.class)
  private String lastName;

  @NonNull
  @PodamStrategyValue(PhoneNumberStrategy.class)
  private String phoneNumber;

  @NonNull
  @PodamStrategyValue(EmailStrategy.class)
  private String email;

  @NonNull
  @PodamStrategyValue(JobPositionStrategy.class)
  private String position;

  @NonNull
  @PodamStrategyValue(TimeZoneStrategy.class)
  private String zoneId;

  @NonNull
  @PodamStrategyValue(LocaleStrategy.class)
  private String locale;

  @NonNull
  @PodamStrategyValue(CountryNameStrategy.class)
  private String country;

  @NonNull
  @PodamStrategyValue(CityNameStrategy.class)
  private String city;

  @NonNull
  @PodamStrategyValue(CurrencyCodeStrategy.class)
  private String currency;

  @NonNull private String createdAt;
}
