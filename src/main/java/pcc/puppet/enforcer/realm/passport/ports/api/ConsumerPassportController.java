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

import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.REQUESTER;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;
import lombok.RequiredArgsConstructor;
import pcc.puppet.enforcer.realm.passport.domain.ConsumerPassportOperations;
import pcc.puppet.enforcer.realm.passport.domain.service.ConsumerPassportService;
import pcc.puppet.enforcer.realm.passport.ports.command.ConsumerPassportCreateCommand;
import pcc.puppet.enforcer.realm.passport.ports.event.ConsumerPassportCreateEvent;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("${micronaut.http.services.pcc-realm-passport.path}")
public class ConsumerPassportController implements ConsumerPassportOperations {

  private final ConsumerPassportService passportService;

  @Timed
  @Counted
  @NewSpan
  @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  public Mono<ConsumerPassportCreateEvent> createConsumerPassport(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @NonNull ConsumerPassportCreateCommand passportCommand) {
    return passportService.createConsumerPassport(requester, passportCommand);
  }
}
