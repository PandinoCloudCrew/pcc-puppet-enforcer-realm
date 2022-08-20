/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import pcc.puppet.enforcer.file.FileResource;
import pcc.puppet.enforcer.file.FileResourceCreateCommand;
import pcc.puppet.enforcer.file.FileResourceMapper;
import pcc.puppet.enforcer.file.FileResourceRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller("/files")
@RequiredArgsConstructor
@Secured(SecurityRule.IS_ANONYMOUS)
public class FilesController {
  private static final FileResourceMapper fileResourceMapper = FileResourceMapper.INSTANCE;
  private final FileResourceRepository fileResourceRepository;

  @Get(uri = "/", produces = MediaType.APPLICATION_JSON)
  public Flux<FileResource> index() {
    return fileResourceRepository.findAll();
  }

  @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  public Mono<FileResource> create(@NotNull @Body FileResourceCreateCommand createCommand) {
    FileResource fileResource = fileResourceMapper.createCommandToFile(createCommand);
    return fileResourceRepository.save(fileResource);
  }
}
