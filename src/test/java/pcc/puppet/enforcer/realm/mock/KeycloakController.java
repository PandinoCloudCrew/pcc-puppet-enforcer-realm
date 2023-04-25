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

import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientCredentials;
import pcc.puppet.enforcer.keycloak.domain.KeycloakClientRepresentation;
import pcc.puppet.enforcer.keycloak.domain.KeycloakIntrospection;
import pcc.puppet.enforcer.keycloak.domain.KeycloakTokenDetails;
import pcc.puppet.enforcer.keycloak.domain.KeycloakUserRepresentation;
import pcc.puppet.enforcer.realm.common.generator.DomainFactory;
import reactor.core.publisher.Mono;

@RestController
    @RequestMapping("/keycloak")
public class KeycloakController {

  @PostMapping(
      value = "/realms/{realm}/protocol/openid-connect/token",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<BearerAccessToken> getToken(
      String realm, @RequestBody KeycloakClientCredentials credentials) {
    return Mono.just(new BearerAccessToken());
  }

  @PostMapping(
      value = "/realms/{realm}/protocol/openid-connect/token/introspect",
      consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<KeycloakTokenDetails> tokenIntrospect(
      @PathVariable String realm, @RequestBody KeycloakIntrospection introspection) {
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
