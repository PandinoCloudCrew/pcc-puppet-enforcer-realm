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

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.REQUESTER;

import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.annotation.SpanTag;
import jakarta.validation.constraints.NotNull;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import pcc.puppet.enforcer.app.Project;
import pcc.puppet.enforcer.realm.common.util.JwtTool;
import pcc.puppet.enforcer.realm.organization.adapters.presenter.OrganizationPresenter;
import pcc.puppet.enforcer.realm.organization.domain.OrganizationOperations;
import pcc.puppet.enforcer.realm.organization.ports.command.OrganizationCreateCommand;
import pcc.puppet.enforcer.realm.organization.ports.event.OrganizationCreateEvent;
import pcc.puppet.enforcer.realm.passport.domain.UsernamePasswordCredentials;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@HttpExchange
public interface OrganizationClient extends OrganizationOperations {
  String USER_AGENT = "OrganizationClient/" + Project.VERSION + " (" + Project.NAME + ")";

  @PostExchange(
      accept = MediaType.APPLICATION_JSON_VALUE,
      contentType = MediaType.APPLICATION_JSON_VALUE)
  Mono<OrganizationCreateEvent> organizationCreate(
      @RequestHeader(name = HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @SpanTag(REQUESTER) @RequestHeader(REQUESTER) String requester,
      @NotNull @RequestBody @Valid OrganizationCreateCommand createCommand);

  @Override
  @Observed(name = "organization-client::organization-create")
  default Mono<OrganizationCreateEvent> organizationCreate(
      @NotNull String requester, @NotNull @Valid OrganizationCreateCommand createCommand) {
    return JwtTool.authentication()
        .flatMap(
            token ->
                organizationCreate(USER_AGENT, JwtTool.toBearer(token), requester, createCommand));
  }

  @GetExchange(value = "/{organizationId}", accept = MediaType.APPLICATION_JSON_VALUE)
  Mono<OrganizationPresenter> findOrganization(
      @RequestHeader(name = HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @SpanTag(REQUESTER) @RequestHeader(REQUESTER) String requester,
      @NotNull @SpanTag @PathVariable String organizationId);

  @Override
  @Observed(name = "organization-client::find-organization")
  default Mono<OrganizationPresenter> findOrganization(
      @NotNull String requester, @NotNull String organizationId) {
    return JwtTool.authentication()
        .flatMap(
            token ->
                findOrganization(USER_AGENT, JwtTool.toBearer(token), requester, organizationId));
  }

  @GetExchange(value = "/{organizationId}/child", accept = MediaType.APPLICATION_JSON_VALUE)
  Flux<OrganizationPresenter> findChildOrganizations(
      @RequestHeader(name = HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @SpanTag(REQUESTER) @RequestHeader(REQUESTER) String requester,
      @NotNull @SpanTag @PathVariable String organizationId);

  @Override
  @Observed(name = "organization-client::find-child-organizations")
  default Flux<OrganizationPresenter> findChildOrganizations(
      @NotNull String requester, @NotNull String organizationId) {
    return JwtTool.authentication()
        .flatMapMany(
            token ->
                findChildOrganizations(
                    USER_AGENT, JwtTool.toBearer(token), requester, organizationId));
  }

  @PostExchange(
      value = "/{organizationId}/login",
      accept = MediaType.APPLICATION_JSON_VALUE,
      contentType = MediaType.APPLICATION_JSON_VALUE)
  Mono<OAuth2AccessTokenResponse> organizationLogin(
      @RequestHeader(name = HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @PathVariable String organizationId,
      @NotNull @RequestBody UsernamePasswordCredentials credentials);

  @Override
  @Observed(name = "organization-client::organization-login")
  default Mono<OAuth2AccessTokenResponse> organizationLogin(
      @NotNull String organizationId, @NotNull UsernamePasswordCredentials credentials) {
    return organizationLogin(USER_AGENT, organizationId, credentials);
  }
}
