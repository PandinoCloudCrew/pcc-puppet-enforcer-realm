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

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pcc.puppet.enforcer.realm.common.contact.adapters.mapper.ContactInformationOutputMapper;
import pcc.puppet.enforcer.realm.common.contact.ports.mapper.ContactInformationInputMapper;
import pcc.puppet.enforcer.realm.department.adapters.mapper.DepartmentOutputMapper;
import pcc.puppet.enforcer.realm.department.ports.mapper.DepartmentInputMapper;
import pcc.puppet.enforcer.realm.member.adapters.mapper.MemberOutputMapper;
import pcc.puppet.enforcer.realm.member.ports.mapper.MemberInputMapper;
import pcc.puppet.enforcer.realm.organization.adapters.mapper.OrganizationOutputMapper;
import pcc.puppet.enforcer.realm.organization.ports.mapper.OrganizationInputMapper;

@Configuration
public class RealmFactory {
  @Bean
  public OrganizationInputMapper organizationMapper() {
    return Mappers.getMapper(OrganizationInputMapper.class);
  }

  @Bean
  public OrganizationOutputMapper organizationOutputMapper() {
    return Mappers.getMapper(OrganizationOutputMapper.class);
  }

  @Bean
  public DepartmentOutputMapper departmentOutputMapper() {
    return Mappers.getMapper(DepartmentOutputMapper.class);
  }

  @Bean
  public DepartmentInputMapper departmentInputMapper() {
    return Mappers.getMapper(DepartmentInputMapper.class);
  }

  @Bean
  public MemberOutputMapper memberOutputMapper() {
    return Mappers.getMapper(MemberOutputMapper.class);
  }

  @Bean
  public MemberInputMapper memberInputMapper() {
    return Mappers.getMapper(MemberInputMapper.class);
  }

  @Bean
  public ContactInformationOutputMapper contactInformationOutputMapper() {
    return Mappers.getMapper(ContactInformationOutputMapper.class);
  }

  @Bean
  public ContactInformationInputMapper contactInformationInputMapper() {
    return Mappers.getMapper(ContactInformationInputMapper.class);
  }
}
