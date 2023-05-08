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

package pcc.puppet.enforcer.realm.passport.adapters.http;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pcc.puppet.enforcer.app.Project.NAME;
import static pcc.puppet.enforcer.app.Project.VERSION;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.CONSUMER;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.REQUESTER;

import io.micrometer.tracing.annotation.SpanTag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import pcc.puppet.enforcer.realm.common.util.JwtTool;
import pcc.puppet.enforcer.realm.passport.domain.ConsumerPassportOperations;
import pcc.puppet.enforcer.realm.passport.ports.command.ConsumerPassportCreateCommand;
import pcc.puppet.enforcer.realm.passport.ports.event.ConsumerPassportCreateEvent;
import reactor.core.publisher.Mono;

@HttpExchange
public interface ConsumerPassportClient extends ConsumerPassportOperations {
  String USER_AGENT = "ConsumerPassportClient/" + VERSION + " (" + NAME + ")";

  @PostExchange(accept = APPLICATION_JSON_VALUE, contentType = APPLICATION_JSON_VALUE)
  Mono<ConsumerPassportCreateEvent> createConsumerPassport(
      @NotNull @SpanTag(CONSUMER) @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @SpanTag(REQUESTER) @RequestHeader(REQUESTER) String requester,
      @NotNull @RequestBody ConsumerPassportCreateCommand passportCommand);

  @Override
  default Mono<ConsumerPassportCreateEvent> createConsumerPassport(
      @NotNull String requester, @NotNull @Valid ConsumerPassportCreateCommand passportCommand) {
    return JwtTool.authentication()
        .flatMap(
            token ->
                createConsumerPassport(
                    USER_AGENT, JwtTool.toBearer(token), requester, passportCommand));
  }
}
