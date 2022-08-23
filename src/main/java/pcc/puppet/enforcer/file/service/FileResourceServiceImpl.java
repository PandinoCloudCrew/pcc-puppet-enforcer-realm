/* Pandino Cloud Crew (C) 2022 */
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
