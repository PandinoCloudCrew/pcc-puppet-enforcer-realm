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
package pcc.puppet.enforcer.security.jwt;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.oauth2.endpoint.authorization.state.State;
import io.micronaut.security.oauth2.endpoint.token.response.OauthAuthenticationMapper;
import io.micronaut.security.oauth2.endpoint.token.response.TokenResponse;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import pcc.puppet.enforcer.keycloak.domain.service.KeycloakService;

@Singleton
@Named("keycloak")
@RequiredArgsConstructor
public class KeycloakUserDetailsMapper implements OauthAuthenticationMapper {
  private final KeycloakService keycloakService;

  @Override
  public Publisher<AuthenticationResponse> createAuthenticationResponse(
      TokenResponse tokenResponse, State state) {
    return keycloakService
        .introspect(tokenResponse.getAccessToken())
        .map(
            details -> {
              Map<String, Object> attributes =
                  CollectionUtils.mapOf(
                      "username", details.getUsername(),
                      "scope", details.getScope(),
                      "tokenResponse", tokenResponse);
              return AuthenticationResponse.success(
                  details.getSub(), details.getRealm_access().getRoles(), attributes);
            });
  }
}
