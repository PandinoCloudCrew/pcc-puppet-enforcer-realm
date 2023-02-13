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
package pcc.puppet.enforcer.realm.organization.ports.api;

import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.REQUESTER;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.token.jwt.render.AccessRefreshToken;
import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pcc.puppet.enforcer.keycloak.domain.service.KeycloakService;
import pcc.puppet.enforcer.realm.organization.adapters.presenter.OrganizationPresenter;
import pcc.puppet.enforcer.realm.organization.domain.OrganizationOperations;
import pcc.puppet.enforcer.realm.organization.domain.service.OrganizationService;
import pcc.puppet.enforcer.realm.organization.ports.command.OrganizationCreateCommand;
import pcc.puppet.enforcer.realm.organization.ports.event.OrganizationCreateEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@ExecuteOn(TaskExecutors.IO)
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("${micronaut.http.services.pcc-realm-organization.path}")
@RequiredArgsConstructor
public class OrganizationController implements OrganizationOperations {
  private final OrganizationService organizationService;
  private final KeycloakService keycloakService;

  @Timed
  @Counted
  @NewSpan
  @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  public Mono<OrganizationCreateEvent> organizationCreate(
      @NonNull @SpanTag(REQUESTER) @Header(REQUESTER) String requester,
      @NonNull @Valid @Body OrganizationCreateCommand createCommand) {
    return organizationService.create(requester, createCommand);
  }

  @Timed
  @Counted
  @NewSpan
  @Get(uri = "/{organizationId}", produces = MediaType.APPLICATION_JSON)
  public Mono<OrganizationPresenter> findOrganization(
      @NonNull @SpanTag(REQUESTER) @Header(REQUESTER) String requester,
      @NonNull @SpanTag String organizationId) {
    return organizationService.findById(requester, organizationId);
  }

  @Timed
  @Counted
  @NewSpan
  @Get(uri = "/{organizationId}/child", produces = MediaType.APPLICATION_JSON)
  public Flux<OrganizationPresenter> findChildOrganizations(
      @NonNull @SpanTag(REQUESTER) @Header(REQUESTER) String requester,
      @NonNull @SpanTag String organizationId) {
    return organizationService.findByParentId(requester, organizationId);
  }

  @Timed
  @Counted
  @NewSpan
  @Post(
      uri = "/{organizationId}/login",
      consumes = MediaType.APPLICATION_JSON,
      produces = MediaType.APPLICATION_JSON)
  public Mono<AccessRefreshToken> organizationLogin(
      @NonNull @SpanTag String organizationId,
      @NonNull @Body UsernamePasswordCredentials credentials) {
    return keycloakService.token(credentials.getUsername(), credentials.getPassword());
  }
}
