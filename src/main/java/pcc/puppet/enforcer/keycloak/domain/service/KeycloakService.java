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
package pcc.puppet.enforcer.keycloak.domain.service;

import java.util.Optional;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import pcc.puppet.enforcer.keycloak.domain.KeycloakTokenDetails;
import pcc.puppet.enforcer.realm.passport.ports.event.ConsumerPassportCreateEvent;
import reactor.core.publisher.Mono;

public interface KeycloakService {
  Mono<OAuth2AccessTokenResponse> adminLogin();

  Mono<OAuth2AccessTokenResponse> clientLogin(String clientId, String clientSecret);

  Mono<OAuth2AccessTokenResponse> userLogin(String username, String password);

  Mono<KeycloakTokenDetails> introspect(String token);

  Mono<Optional<String>> createClient(
      String name, String description, String clientId, String clientSecret);

  Mono<Optional<String>> createUser(
      ConsumerPassportCreateEvent createEvent, String username, String password);
}
