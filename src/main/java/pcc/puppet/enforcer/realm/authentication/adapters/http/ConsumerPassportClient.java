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
package pcc.puppet.enforcer.realm.authentication.adapters.http;

import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.REQUESTER;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;
import pcc.puppet.enforcer.realm.Project;
import pcc.puppet.enforcer.realm.authentication.domain.ConsumerPassportOperations;
import pcc.puppet.enforcer.realm.authentication.ports.command.ConsumerPassportCreateCommand;
import pcc.puppet.enforcer.realm.authentication.ports.event.ConsumerPassportCreateEvent;
import reactor.core.publisher.Mono;

@Client("${micronaut.http.services.pcc-realm-passport.path}")
@Header(name = HttpHeaders.ACCEPT_ENCODING, value = "gzip, deflate")
@Header(
    name = HttpHeaders.USER_AGENT,
    value = "ConsumerPassportClient/" + Project.VERSION + " (" + Project.NAME + ")")
public interface ConsumerPassportClient extends ConsumerPassportOperations {

  @NewSpan
  @Override
  @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  Mono<ConsumerPassportCreateEvent> createConsumerPassport(
      @NonNull @SpanTag(REQUESTER) @Header(REQUESTER) String requester,
      @NonNull ConsumerPassportCreateCommand passportCommand);
}
