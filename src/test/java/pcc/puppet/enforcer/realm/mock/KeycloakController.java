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
package pcc.puppet.enforcer.realm.mock;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakTokenDetails;
import pcc.puppet.enforcer.keycloak.domain.KeycloakUserRepresentation;
import pcc.puppet.enforcer.realm.common.generator.DomainFactory;
import pcc.puppet.enforcer.realm.common.util.JwtTool;
import reactor.core.publisher.Mono;

@Profile("test")
@RestController
@RequestMapping("/keycloak")
public class KeycloakController {

  @PostMapping(
      value = "/realms/{realm}/protocol/openid-connect/token",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Map<String, Object>> getToken(@PathVariable String realm) {

    return JwtTool.authentication()
        .map(
            token ->
                Map.of(
                    "access_token",
                    token.getTokenValue(),
                    "expires_in",
                    10800,
                    "refresh_expires_in",
                    0,
                    "token_type",
                    "Bearer",
                    "not-before-policy",
                    0,
                    "scope",
                    "email profile"));
  }

  @PostMapping(
      value = "/realms/{realm}/protocol/openid-connect/token/introspect",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<KeycloakTokenDetails> tokenIntrospect(@PathVariable String realm) {
    return Mono.just(DomainFactory.keycloakTokenDetails());
  }

  @PostMapping(
      value = "/admin/realms/{realm}/clients",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Optional<String>> createClient(
      @NotNull @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @PathVariable String realm,
      @Valid @RequestBody KeycloakClientRepresentation request) {
    return Mono.just(Optional.of(request.getDescription()));
  }

  @PostMapping(
      value = "/admin/realms/{realm}/users",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  public Mono<Optional<String>> createUser(
      @NotNull @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @PathVariable String realm,
      @Valid @RequestBody KeycloakUserRepresentation request) {
    return Mono.just(Optional.of(request.getEmail()));
  }
}
