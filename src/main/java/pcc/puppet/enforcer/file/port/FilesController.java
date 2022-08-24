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
package pcc.puppet.enforcer.file.port;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import pcc.puppet.enforcer.file.command.FileResourceCreateCommand;
import pcc.puppet.enforcer.file.entity.FileResource;
import pcc.puppet.enforcer.file.service.FileResourceService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller("/files")
@RequiredArgsConstructor
@Secured(SecurityRule.IS_ANONYMOUS)
public class FilesController {
  private final FileResourceService fileResourceService;

  @Get(produces = MediaType.APPLICATION_JSON)
  public Flux<FileResource> index() {
    return fileResourceService.findAll();
  }

  @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  public Mono<FileResource> create(@NotNull @Body FileResourceCreateCommand createCommand) {
    return fileResourceService.save(createCommand);
  }

  @Put(uri = "/{id}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  public Mono<FileResource> update(
      @PathVariable @NotNull String id, @NotNull @Body FileResourceCreateCommand createCommand) {
    return fileResourceService.update(id, createCommand);
  }
}
