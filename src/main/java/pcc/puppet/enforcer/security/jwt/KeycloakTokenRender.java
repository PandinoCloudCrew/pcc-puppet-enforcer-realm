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

import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.http.HttpHeaderValues;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.oauth2.endpoint.token.response.TokenResponse;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.security.token.jwt.render.BearerTokenRenderer;
import io.micronaut.security.token.jwt.render.TokenRenderer;
import jakarta.inject.Singleton;

@Primary
@Singleton
@Replaces(BearerTokenRenderer.class)
public class KeycloakTokenRender implements TokenRenderer {
  private static final String BEARER_TOKEN_TYPE = HttpHeaderValues.AUTHORIZATION_PREFIX_BEARER;

  @Override
  public AccessRefreshToken render(Integer expiresIn, String accessToken, String refreshToken) {
    return new AccessRefreshToken(accessToken, refreshToken, BEARER_TOKEN_TYPE, expiresIn);
  }

  @Override
  public AccessRefreshToken render(
      Authentication authentication, Integer expiresIn, String accessToken, String refreshToken) {
    TokenResponse tokenResponse =
        (TokenResponse) authentication.getAttributes().get("tokenResponse");
    return new BearerAccessRefreshToken(
        authentication.getName(),
        authentication.getRoles(),
        tokenResponse.getExpiresIn(),
        tokenResponse.getAccessToken(),
        tokenResponse.getRefreshToken(),
        tokenResponse.getTokenType());
  }
}
