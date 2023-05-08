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

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
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
@Jacksonized
public class ConsumerPassportCreateCommand {

  @NotNull
  @PodamStrategyValue(FirstNameStrategy.class)
  private String firstName;

  @NotNull
  @PodamStrategyValue(LastNameStrategy.class)
  private String lastName;

  @NotNull
  @PodamStrategyValue(PasswordStrategy.class)
  private String password;

  @NotNull
  @PodamStrategyValue(EmailStrategy.class)
  private String email;

  @NotNull
  @PodamStrategyValue(PhoneNumberStrategy.class)
  private String phoneNumber;

  @NotNull
  @PodamStrategyValue(JobPositionStrategy.class)
  private String position;

  @NotNull
  @PodamStrategyValue(CountryNameStrategy.class)
  private String country;

  @NotNull
  @PodamStrategyValue(CityNameStrategy.class)
  private String city;

  @NotNull
  @PodamStrategyValue(CompanyNameStrategy.class)
  private String organizationName;

  @NotNull
  @PodamStrategyValue(AddressStrategy.class)
  private String location;

  @NotNull
  @PodamStrategyValue(TaxIdStrategy.class)
  private String taxId;

  @NotNull
  @PodamStrategyValue(CompanyDepartmentStrategy.class)
  private String departmentName;
}
