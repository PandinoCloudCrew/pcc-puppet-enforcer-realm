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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Profile("test")
@RestController
@RequestMapping("/rest-countries")
public class RestCountriesController {

  @GetMapping(value = "/name/{name}")
  public Mono<String> getCountryResponse(@PathVariable String name) {
    return Mono.just(getJsonResponse());
  }

  private String getJsonResponse() {
    try {
      return new ClassPathResource("rest-countries.json")
          .getContentAsString(StandardCharsets.UTF_8);
    } catch (IOException e) {
      return e.getMessage();
    }
  }
}
