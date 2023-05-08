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

package pcc.puppet.enforcer.realm.passport.adapters.gateway.rest_countries;

import io.micrometer.observation.annotation.Observed;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import pcc.puppet.enforcer.realm.passport.adapters.gateway.rest_countries.response.RestCountriesResponse;
import reactor.core.publisher.Mono;

@HttpExchange
public interface RestCountriesApiClient {

  @GetExchange(value = "/name/{name}", accept = MediaType.APPLICATION_JSON_VALUE)
  @Observed(name = "rest-countries-api-client::get-by-name")
  Mono<List<RestCountriesResponse>> getByName(@PathVariable String name);
}
