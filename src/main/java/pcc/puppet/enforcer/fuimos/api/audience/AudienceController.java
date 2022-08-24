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
package pcc.puppet.enforcer.fuimos.api.audience;

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
import pcc.puppet.enforcer.fuimos.api.audience.command.AudienceCreateCommand;
import pcc.puppet.enforcer.fuimos.api.audience.event.AudienceCreateEvent;
import pcc.puppet.enforcer.fuimos.api.audience.presenter.UserDevicePresenter;
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
