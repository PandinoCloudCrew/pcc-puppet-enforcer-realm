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
package pcc.puppet.enforcer.realm.organization.adapters.http;

import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.REQUESTER;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import io.micronaut.tracing.annotation.SpanTag;
import javax.validation.Valid;
import pcc.puppet.enforcer.app.Project;
import pcc.puppet.enforcer.realm.organization.adapters.presenter.OrganizationPresenter;
import pcc.puppet.enforcer.realm.organization.domain.OrganizationOperations;
import pcc.puppet.enforcer.realm.organization.ports.command.OrganizationCreateCommand;
import pcc.puppet.enforcer.realm.organization.ports.event.OrganizationCreateEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Client("pcc-realm-organization")
@Header(name = HttpHeaders.ACCEPT_ENCODING, value = "gzip, deflate")
@Header(
    name = HttpHeaders.USER_AGENT,
    value = "OrganizationClient/" + Project.VERSION + " (" + Project.NAME + ")")
public interface OrganizationClient extends OrganizationOperations {

  @Override
  @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  Mono<OrganizationCreateEvent> organizationCreate(
      @NonNull @SpanTag(REQUESTER) @Header(REQUESTER) String requester,
      @NonNull @Body @Valid OrganizationCreateCommand createCommand);

  @Override
  @Get(uri = "/{organizationId}", produces = MediaType.APPLICATION_JSON)
  Mono<OrganizationPresenter> findOrganization(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag @NonNull String organizationId);

  @Override
  @Get(uri = "/{organizationId}/child", produces = MediaType.APPLICATION_JSON)
  Flux<OrganizationPresenter> findChildOrganizations(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag @NonNull String organizationId);

  @Post(
      uri = "/{organizationId}/login",
      consumes = MediaType.APPLICATION_JSON,
      produces = MediaType.APPLICATION_JSON)
  Mono<AccessRefreshToken> organizationLogin(
      @NonNull String organizationId, @NonNull @Body UsernamePasswordCredentials credentials);
}
