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
package pcc.puppet.enforcer.file.service;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import pcc.puppet.enforcer.file.command.FileResourceCreateCommand;
import pcc.puppet.enforcer.file.entity.FileResource;
import pcc.puppet.enforcer.file.entity.FileResourceRepository;
import pcc.puppet.enforcer.file.mapper.FileResourceMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Singleton
@RequiredArgsConstructor
public class FileResourceServiceImpl implements FileResourceService {

  private static final FileResourceMapper fileResourceMapper = FileResourceMapper.INSTANCE;
  private final FileResourceRepository fileResourceRepository;

  @Override
  public Flux<FileResource> findAll() {
    return fileResourceRepository.findAll();
  }

  @Override
  public Mono<FileResource> save(FileResourceCreateCommand createCommand) {
    FileResource fileResource = fileResourceMapper.createCommandToFile(createCommand);
    return fileResourceRepository.save(fileResource);
  }

  @Override
  public Mono<FileResource> update(final String id, final FileResourceCreateCommand createCommand) {
    return fileResourceRepository
        .findById(new ObjectId(id))
        .log()
        .flatMap(
            fileResource -> {
              fileResource.setName(createCommand.getName());
              return fileResourceRepository.update(fileResource).log();
            });
  }
}
