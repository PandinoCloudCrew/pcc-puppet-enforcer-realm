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
package pcc.puppet.enforcer.file.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pcc.puppet.enforcer.file.command.FileResourceCreateCommand;
import pcc.puppet.enforcer.file.entity.FileResource;

@Mapper
public interface FileResourceMapper {
  FileResourceMapper INSTANCE = Mappers.getMapper(FileResourceMapper.class);

  FileResource createCommandToFile(FileResourceCreateCommand fileResourceCreateCommand);
}
