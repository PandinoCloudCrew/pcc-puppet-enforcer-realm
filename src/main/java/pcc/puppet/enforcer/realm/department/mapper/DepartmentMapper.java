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
package pcc.puppet.enforcer.realm.department.mapper;

import org.mapstruct.Mapper;
import pcc.puppet.enforcer.realm.common.contact.mapper.ContactInformationMapper;
import pcc.puppet.enforcer.realm.common.mapper.InstantMapper;
import pcc.puppet.enforcer.realm.department.api.command.DepartmentCreateCommand;
import pcc.puppet.enforcer.realm.department.api.event.DepartmentCreateEvent;
import pcc.puppet.enforcer.realm.department.api.presenter.DepartmentPresenter;
import pcc.puppet.enforcer.realm.department.domain.Department;

@Mapper(uses = {ContactInformationMapper.class, InstantMapper.class})
public interface DepartmentMapper {
  Department commandToDomain(DepartmentCreateCommand command);

  DepartmentCreateEvent domainToEvent(Department command);

  DepartmentPresenter domainToPresenter(Department command);
}
