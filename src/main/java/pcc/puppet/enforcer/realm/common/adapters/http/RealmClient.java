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
package pcc.puppet.enforcer.realm.common.adapters.http;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static pcc.puppet.enforcer.app.Project.NAME;
import static pcc.puppet.enforcer.app.Project.VERSION;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.REQUESTER;

import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import io.micrometer.tracing.annotation.SpanTag;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import pcc.puppet.enforcer.realm.passport.domain.UsernamePasswordCredentials;
import reactor.core.publisher.Mono;

@HttpExchange("/")
public interface RealmClient {
  String USER_AGENT = "RealmClient/" + VERSION + " (" + NAME + ")";
  @PostExchange(value = "/login", accept = APPLICATION_JSON_VALUE, contentType = APPLICATION_JSON_VALUE)
  Mono<BearerAccessToken> login(
      @RequestHeader(name = USER_AGENT) String userAgent,
      @NotNull @SpanTag(REQUESTER) @RequestHeader(REQUESTER) String requester,
      @NotNull @RequestBody UsernamePasswordCredentials passportCommand);

  default Mono<BearerAccessToken> login(
      @NotNull String requester,
      @NotNull UsernamePasswordCredentials passportCommand) {
    return login(USER_AGENT, requester, passportCommand);
  }
}
