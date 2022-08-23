/* Pandino Cloud Crew (C) 2022 */
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
