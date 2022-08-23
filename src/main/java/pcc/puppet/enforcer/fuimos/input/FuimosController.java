/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.input;

import com.github.shamil.Xid;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import pcc.puppet.enforcer.fuimos.network.NetworkCarrier;
import pcc.puppet.enforcer.fuimos.network.UserNetwork;
import pcc.puppet.enforcer.fuimos.payload.ReceivedInteraction;
import pcc.puppet.enforcer.fuimos.payload.UserInteraction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller("/fuimos")
@Secured(SecurityRule.IS_ANONYMOUS)
@RequiredArgsConstructor
public class FuimosController {
  private final UserNetwork userNetwork;
  private final Faker faker = new Faker();

  @Post(
      uri = "/interaction",
      produces = MediaType.APPLICATION_JSON,
      consumes = MediaType.APPLICATION_JSON)
  public ReceivedInteraction receivedInteraction(@NotNull @Body UserInteraction userInteraction) {
    return ReceivedInteraction.builder()
        .id(Xid.string())
        .interactionId(userInteraction.getId())
        .build();
  }

  @Post(
      uri = "/audience",
      produces = MediaType.APPLICATION_JSON,
      consumes = MediaType.APPLICATION_JSON)
  public Mono<AudienceCreateEvent> enrollDevices(
      @NotNull @Body AudienceCreateCommand createCommand) {
    return Flux.range(0, createCommand.getSize())
        .flatMap(
            number ->
                userNetwork.enroll(
                    faker.phoneNumber().cellPhone(),
                    NetworkCarrier.getRandomCarrier(),
                    createCommand.getAudienceId()))
        .collectList()
        .map(
            userDevices ->
                AudienceCreateEvent.builder()
                    .size(userDevices.size())
                    .audienceId(createCommand.getAudienceId())
                    .devices(userDevices)
                    .build());
  }
}
