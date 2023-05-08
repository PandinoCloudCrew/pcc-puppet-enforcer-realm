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
package pcc.puppet.enforcer.vault.adapters.http;

import io.micrometer.observation.annotation.Observed;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import pcc.puppet.enforcer.app.Project;
import pcc.puppet.enforcer.vault.adapters.http.request.VaultSecretCreateRequest;
import reactor.core.publisher.Mono;

@HttpExchange
public interface VaultSecretsClient {
  String USER_AGENT = "VaultSecretsClient/" + Project.VERSION + " (" + Project.NAME + ")";

  @PostExchange(
      contentType = MediaType.APPLICATION_JSON_VALUE,
      accept = MediaType.APPLICATION_JSON_VALUE)
  Mono<ResponseEntity<String>> createSecret(
      URI uri,
      @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @RequestHeader("X-Vault-Token") String token,
      @NotNull @PathVariable String backend,
      @NotNull @PathVariable String secretKey,
      @NotNull @RequestBody VaultSecretCreateRequest request);

  @Observed(name = "vault-secrets-client::create-secret")
  default Mono<ResponseEntity<String>> createSecret(
      @NotNull String baseUrl,
      @NotNull String token,
      @NotNull String backend,
      @NotNull String secretKey,
      @NotNull VaultSecretCreateRequest request) {
    String requestPath = String.format("%s/v1/%s/data%s", baseUrl, backend, secretKey);
    return createSecret(URI.create(requestPath), USER_AGENT, token, backend, secretKey, request);
  }
}
