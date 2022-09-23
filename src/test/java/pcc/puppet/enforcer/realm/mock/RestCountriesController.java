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

import io.micronaut.core.io.ResourceLoader;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import java.io.IOException;
import reactor.core.publisher.Mono;

@Controller("/rest-countries")
public class RestCountriesController {

  private final ResourceLoader loader;

  public RestCountriesController(ResourceLoader loader) {
    this.loader = loader;
  }

  @Get(uri = "/name/{name}", produces = MediaType.APPLICATION_JSON)
  public Mono<String> getCountryResponse(String name) {
    return Mono.just(getJsonResponse());
  }

  private String getJsonResponse() {
    try {
      return new String(
          loader.getResourceAsStream("rest-countries.json").orElseThrow().readAllBytes());
    } catch (IOException e) {
      return e.getMessage();
    }
  }
}
