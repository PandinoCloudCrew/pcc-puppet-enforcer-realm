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

package pcc.puppet.enforcer.realm.passport.ports.api;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.REQUESTER;

import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.annotation.SpanTag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pcc.puppet.enforcer.realm.passport.domain.ConsumerPassportOperations;
import pcc.puppet.enforcer.realm.passport.domain.service.ConsumerPassportService;
import pcc.puppet.enforcer.realm.passport.ports.command.ConsumerPassportCreateCommand;
import pcc.puppet.enforcer.realm.passport.ports.event.ConsumerPassportCreateEvent;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "${spring.http.services.pcc-realm-passport.path}")
public class ConsumerPassportController implements ConsumerPassportOperations {

  private final ConsumerPassportService passportService;

  @PostMapping(
      produces = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE},
      consumes = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE})
  @Observed(name = "consumer-passport-controller::create-consumer-passport")
  public Mono<ConsumerPassportCreateEvent> createConsumerPassport(
      @NotNull @SpanTag(REQUESTER) @RequestHeader(REQUESTER) String requester,
      @NotNull @Valid @RequestBody ConsumerPassportCreateCommand passportCommand) {
    return passportService.createConsumerPassport(requester, passportCommand);
  }
}
