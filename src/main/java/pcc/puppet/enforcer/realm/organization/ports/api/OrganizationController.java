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

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON_VALUE;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.REQUESTER;

import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.annotation.SpanTag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pcc.puppet.enforcer.keycloak.domain.BearerTokenResponse;
import pcc.puppet.enforcer.keycloak.domain.service.KeycloakService;
import pcc.puppet.enforcer.realm.organization.adapters.presenter.OrganizationPresenter;
import pcc.puppet.enforcer.realm.organization.domain.OrganizationOperations;
import pcc.puppet.enforcer.realm.organization.domain.service.OrganizationService;
import pcc.puppet.enforcer.realm.organization.ports.command.OrganizationCreateCommand;
import pcc.puppet.enforcer.realm.organization.ports.event.OrganizationCreateEvent;
import pcc.puppet.enforcer.realm.passport.domain.UsernamePasswordCredentials;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(value = "${spring.http.services.pcc-realm-organization.path}")
@RequiredArgsConstructor
public class OrganizationController implements OrganizationOperations {
  private final OrganizationService organizationService;
  private final KeycloakService keycloakService;

  @PostMapping(
      produces = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE},
      consumes = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE})
  @Observed(name = "organization-controller::organization-create")
  public Mono<OrganizationCreateEvent> organizationCreate(
      @NotNull @SpanTag(REQUESTER) @RequestHeader(REQUESTER) String requester,
      @NotNull @Valid @RequestBody OrganizationCreateCommand createCommand) {
    log.debug("create organization {} request by {}", createCommand.getName(), requester);
    return organizationService.create(requester, createCommand);
  }

  @GetMapping(
      value = "/{organizationId}",
      produces = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE})
  @Observed(name = "organization-controller::find-organization")
  public Mono<OrganizationPresenter> findOrganization(
      @NotNull @SpanTag(REQUESTER) @RequestHeader(REQUESTER) String requester,
      @NotNull @SpanTag @PathVariable String organizationId) {
    return organizationService.findById(requester, organizationId);
  }

  @GetMapping(
      value = "/{organizationId}/child",
      produces = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE})
  @Observed(name = "organization-controller::find-child-organizations")
  public Flux<OrganizationPresenter> findChildOrganizations(
      @NotNull @SpanTag(REQUESTER) @RequestHeader(REQUESTER) String requester,
      @NotNull @SpanTag @PathVariable String organizationId) {
    return organizationService.findByParentId(requester, organizationId);
  }

  @PostMapping(
      value = "/{organizationId}/login",
      produces = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE},
      consumes = {APPLICATION_JSON_VALUE, APPLICATION_PROBLEM_JSON_VALUE})
  @Observed(name = "organization-controller::organization-login")
  public Mono<BearerTokenResponse> organizationLogin(
      @NotNull @SpanTag @PathVariable String organizationId,
      @NotNull @RequestBody UsernamePasswordCredentials credentials) {
    return keycloakService.clientLogin(credentials.getUsername(), credentials.getPassword());
  }
}
