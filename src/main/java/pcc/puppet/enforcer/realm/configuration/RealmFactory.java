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
package pcc.puppet.enforcer.realm.configuration;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import org.mapstruct.factory.Mappers;
import pcc.puppet.enforcer.realm.common.contact.mapper.ContactInformationMapper;
import pcc.puppet.enforcer.realm.department.adapters.mapper.DepartmentOutputMapper;
import pcc.puppet.enforcer.realm.department.ports.mapper.DepartmentInputMapper;
import pcc.puppet.enforcer.realm.member.mapper.MemberMapper;
import pcc.puppet.enforcer.realm.organization.adapters.mapper.OrganizationOutputMapper;
import pcc.puppet.enforcer.realm.organization.ports.mapper.OrganizationInputMapper;

@Factory
public class RealmFactory {
  @Singleton
  public OrganizationInputMapper organizationMapper() {
    return Mappers.getMapper(OrganizationInputMapper.class);
  }

  @Singleton
  public OrganizationOutputMapper organizationOutputMapper() {
    return Mappers.getMapper(OrganizationOutputMapper.class);
  }

  @Singleton
  public ContactInformationMapper contactInformationMapper() {
    return Mappers.getMapper(ContactInformationMapper.class);
  }

  @Singleton
  public DepartmentOutputMapper departmentOutputMapper() {
    return Mappers.getMapper(DepartmentOutputMapper.class);
  }

  @Singleton
  public DepartmentInputMapper departmentInputMapper() {
    return Mappers.getMapper(DepartmentInputMapper.class);
  }

  @Singleton
  public MemberMapper memberMapper() {
    return Mappers.getMapper(MemberMapper.class);
  }
}
