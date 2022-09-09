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
package pcc.puppet.enforcer.realm.member.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pcc.puppet.enforcer.realm.member.api.command.MemberCreateCommand;
import pcc.puppet.enforcer.realm.member.api.event.MemberCreateEvent;
import pcc.puppet.enforcer.realm.member.api.presenter.MemberPresenter;
import pcc.puppet.enforcer.realm.member.domain.Member;

@Mapper(imports = ObjectId.class)
public interface MemberMapper {
  Member commandToDomain(MemberCreateCommand command);

  @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd hh:mm:ss")
  MemberCreateEvent domainToEvent(Member command);

  @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd hh:mm:ss")
  MemberPresenter domainToPresenter(Member command);
}
