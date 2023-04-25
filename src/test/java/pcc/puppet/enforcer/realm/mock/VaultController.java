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

import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pcc.puppet.enforcer.vault.adapters.http.request.VaultSecretCreateRequest;
import reactor.core.publisher.Mono;

@RestController("/vault")
public class VaultController {

  @PostMapping(
      value = "/v1/{backend}/data/{secretKey}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Mono<String> getToken(
      @NotNull @RequestHeader("X-Vault-Token") String token,
      @NotNull @PathVariable String backend,
      @NotNull @PathVariable String secretKey,
      @NotNull @RequestBody VaultSecretCreateRequest credentials) {
    return Mono.just("created");
  }
}
