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

import io.micronaut.core.annotation.NonNull;
import io.micronaut.discovery.vault.config.v2.VaultResponseData;
import io.micronaut.discovery.vault.config.v2.VaultResponseV2;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import pcc.puppet.enforcer.vault.adapters.http.request.VaultSecretCreateRequest;
import reactor.core.publisher.Mono;

@Controller("/vault")
public class VaultController {

  @Post(
      uri = "/v1/{backend}/data/{secretKey}",
      consumes = MediaType.APPLICATION_JSON,
      produces = MediaType.APPLICATION_JSON)
  public Mono<VaultResponseV2> getToken(
      @NonNull @Header("X-Vault-Token") String token,
      @NonNull String backend,
      @NonNull String secretKey,
      @NonNull @Body VaultSecretCreateRequest credentials) {
    VaultResponseData responseData = new VaultResponseData(Map.of(), Map.of());
    return Mono.just(
        new VaultResponseV2(
            responseData,
            1000L,
            UUID.randomUUID().toString(),
            UUID.randomUUID().toString(),
            Map.of(),
            false,
            List.of()));
  }
}
