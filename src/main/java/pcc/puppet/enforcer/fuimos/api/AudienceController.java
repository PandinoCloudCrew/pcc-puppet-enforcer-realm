/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.api;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.tracing.annotation.NewSpan;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import pcc.puppet.enforcer.fuimos.api.input.command.AudienceCreateCommand;
import pcc.puppet.enforcer.fuimos.api.output.event.AudienceCreateEvent;
import pcc.puppet.enforcer.fuimos.api.output.presenter.UserDevicePresenter;
import pcc.puppet.enforcer.fuimos.network.NetworkCarrier;
import pcc.puppet.enforcer.fuimos.network.UserNetwork;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller("/fuimos/audience")
@Secured(SecurityRule.IS_ANONYMOUS)
@RequiredArgsConstructor
public class AudienceController {

  private final UserNetwork userNetwork;
  private final Faker faker = new Faker();

  @NewSpan
  @Get(produces = MediaType.APPLICATION_JSON)
  public Flux<UserDevicePresenter> findAudienceDevices(
      @QueryValue("audienceId") @NonNull String audienceId) {
    return userNetwork.findByAudience(audienceId);
  }

  @NewSpan
  @Post(produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
  public Mono<AudienceCreateEvent> enrollDevices(
      @NonNull @Body @Valid AudienceCreateCommand createCommand) {
    Optional<String> audienceId = Optional.ofNullable(createCommand.getAudienceId());
    return Flux.range(0, createCommand.getSize())
        .flatMap(
            number ->
                userNetwork.enroll(
                    faker.phoneNumber().cellPhone(), NetworkCarrier.getRandomCarrier(), audienceId))
        .collectList()
        .map(
            userDevices ->
                AudienceCreateEvent.builder()
                    .size(userDevices.size())
                    .audienceId(audienceId)
                    .devices(userDevices)
                    .build());
  }
}
