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
package pcc.puppet.enforcer.realm.passport.ports.command;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import lombok.Builder;
import lombok.Data;
import pcc.puppet.enforcer.realm.common.generator.values.AddressStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.CityNameStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.CompanyDepartmentStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.CompanyNameStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.CountryNameStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.EmailStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.FirstNameStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.JobPositionStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.LastNameStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.PasswordStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.PhoneNumberStrategy;
import pcc.puppet.enforcer.realm.common.generator.values.TaxIdStrategy;
import uk.co.jemos.podam.common.PodamStrategyValue;

@Data
@Builder
@Introspected
public class ConsumerPassportCreateCommand {

  @NonNull
  @PodamStrategyValue(FirstNameStrategy.class)
  private String firstName;

  @NonNull
  @PodamStrategyValue(LastNameStrategy.class)
  private String lastName;

  @NonNull
  @PodamStrategyValue(PasswordStrategy.class)
  private String password;

  @NonNull
  @PodamStrategyValue(EmailStrategy.class)
  private String email;

  @NonNull
  @PodamStrategyValue(PhoneNumberStrategy.class)
  private String phoneNumber;

  @NonNull
  @PodamStrategyValue(JobPositionStrategy.class)
  private String position;

  @NonNull
  @PodamStrategyValue(CountryNameStrategy.class)
  private String country;

  @NonNull
  @PodamStrategyValue(CityNameStrategy.class)
  private String city;

  @NonNull
  @PodamStrategyValue(CompanyNameStrategy.class)
  private String organizationName;

  @NonNull
  @PodamStrategyValue(AddressStrategy.class)
  private String location;

  @NonNull
  @PodamStrategyValue(TaxIdStrategy.class)
  private String taxId;

  @NonNull
  @PodamStrategyValue(CompanyDepartmentStrategy.class)
  private String departmentName;
}
