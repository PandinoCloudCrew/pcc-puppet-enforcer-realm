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
package pcc.puppet.enforcer.realm.common.filter;

import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import io.micronaut.tracing.annotation.ContinueSpan;
import io.micronaut.tracing.annotation.SpanTag;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import pcc.puppet.enforcer.app.Project;

@Slf4j
@Filter(Filter.MATCH_ALL_PATTERN)
public class HttpVersionFilter implements HttpServerFilter {

  @Override
  public Publisher<MutableHttpResponse<?>> doFilter(
      HttpRequest<?> request, ServerFilterChain chain) {
    return Publishers.map(
        chain.proceed(request),
        response -> {
          addHeaders(response, Project.VERSION, Project.NAME);
          return response;
        });
  }

  @ContinueSpan
  public void addHeaders(
      MutableHttpResponse<?> response, @SpanTag String version, @SpanTag String service) {
    response.header("Version", version);
    response.header("Service", service);
  }
}
