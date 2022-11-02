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
package pcc.puppet.enforcer.realm.common.generator;

import com.github.shamil.Xid;
import io.micronaut.core.annotation.Introspected;
import lombok.experimental.UtilityClass;
import pcc.puppet.enforcer.keycloak.domain.KeycloakTokenDetails;
import pcc.puppet.enforcer.realm.department.ports.command.DepartmentCreateCommand;
import pcc.puppet.enforcer.realm.member.ports.command.MemberCreateCommand;
import pcc.puppet.enforcer.realm.organization.ports.command.OrganizationCreateCommand;
import pcc.puppet.enforcer.realm.passport.ports.command.ConsumerPassportCreateCommand;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@UtilityClass
@Introspected
public class DomainFactory {

  private final PodamFactory factory = new PodamFactoryImpl();

  public String id() {
    return Xid.string();
  }

  public OrganizationCreateCommand organizationCreateCommand() {
    return getPojoWithFullData(OrganizationCreateCommand.class);
  }

  public DepartmentCreateCommand departmentCreateCommand() {
    return getPojoWithFullData(DepartmentCreateCommand.class);
  }

  public MemberCreateCommand memberCreateCommand() {
    return getPojoWithFullData(MemberCreateCommand.class);
  }

  public ConsumerPassportCreateCommand consumerPassportCreateCommand() {
    return getPojoWithFullData(ConsumerPassportCreateCommand.class);
  }

  public KeycloakTokenDetails keycloakTokenDetails() {
    return getPojoWithFullData(KeycloakTokenDetails.class);
  }

  private <T> T getPojoWithFullData(Class<T> classType) {
    return factory.manufacturePojoWithFullData(classType);
  }
}
