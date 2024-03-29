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

import com.nimbusds.oauth2.sdk.token.BearerAccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pcc.puppet.enforcer.realm.passport.domain.UsernamePasswordCredentials;
import reactor.core.publisher.Mono;

@RestController
public class LoginController {

  @PostMapping(value = "/login")
  public Mono<BearerAccessToken> login(@RequestBody UsernamePasswordCredentials credentials) {
    return Mono.just(new BearerAccessToken());
  }
}
