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
package pcc.puppet.enforcer.realm.department.api.command;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import javax.validation.Valid;
import lombok.Builder;
import lombok.Data;
import pcc.puppet.enforcer.realm.common.contact.command.CreateContactInformationCommand;
import pcc.puppet.enforcer.realm.common.generator.CompanyDepartmentStrategy;
import pcc.puppet.enforcer.realm.common.generator.InternalAddressStrategy;
import pcc.puppet.enforcer.realm.common.generator.ObjectIdStrategy;
import uk.co.jemos.podam.common.PodamStrategyValue;

@Data
@Builder
@Serdeable
public class DepartmentCreateCommand {

  @Nullable
  @PodamStrategyValue(ObjectIdStrategy.class)
  private String parentId;

  @NonNull
  @PodamStrategyValue(ObjectIdStrategy.class)
  private String organizationId;

  @NonNull
  @PodamStrategyValue(CompanyDepartmentStrategy.class)
  private String name;

  @NonNull
  @PodamStrategyValue(InternalAddressStrategy.class)
  private String location;

  @NonNull @Valid private CreateContactInformationCommand contactId;
}
