/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.api;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.tracing.annotation.NewSpan;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pcc.puppet.enforcer.fuimos.OId;
import pcc.puppet.enforcer.fuimos.api.input.command.DeviceInteractionCreateCommand;
import pcc.puppet.enforcer.fuimos.api.output.event.DeviceInteractionCreateEvent;

@Slf4j
@Controller("/fuimos/interaction")
@Secured(SecurityRule.IS_ANONYMOUS)
@RequiredArgsConstructor
public class InteractionController {

  @NewSpan
  @Post(produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
  public DeviceInteractionCreateEvent receivedInteraction(
      @Valid @NonNull @Body DeviceInteractionCreateCommand deviceInteractionCreateCommand) {
    log.info("{}", deviceInteractionCreateCommand);
    return DeviceInteractionCreateEvent.builder()
        .id(OId.string())
        .interactionId(deviceInteractionCreateCommand.getId())
        .build();
  }
}
