/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.file.service;

import pcc.puppet.enforcer.file.command.FileResourceCreateCommand;
import pcc.puppet.enforcer.file.entity.FileResource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileResourceService {
  Flux<FileResource> findAll();

  Mono<FileResource> save(FileResourceCreateCommand createCommand);

  Mono<FileResource> update(String id, FileResourceCreateCommand createCommand);
}
