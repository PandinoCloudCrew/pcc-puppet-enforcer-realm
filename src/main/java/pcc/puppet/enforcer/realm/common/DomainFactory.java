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
package pcc.puppet.enforcer.realm.common;

import io.micronaut.core.annotation.Introspected;
import lombok.experimental.UtilityClass;
import org.bson.types.ObjectId;
import pcc.puppet.enforcer.realm.department.api.command.DepartmentCreateCommand;
import pcc.puppet.enforcer.realm.organization.api.command.OrganizationCreateCommand;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@UtilityClass
@Introspected
public class DomainFactory {

  private final PodamFactory factory = new PodamFactoryImpl();

  public String id() {
    return ObjectId.get().toHexString();
  }

  public OrganizationCreateCommand organizationCreateCommand() {
    return getPojoWithFullData(OrganizationCreateCommand.class);
  }

  public DepartmentCreateCommand departmentCreateCommand() {
    return getPojoWithFullData(DepartmentCreateCommand.class);
  }

  private <T> T getPojoWithFullData(Class<T> classType) {
    return factory.manufacturePojoWithFullData(classType);
  }
}
