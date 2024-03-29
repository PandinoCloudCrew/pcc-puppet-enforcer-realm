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

package pcc.puppet.enforcer.realm.member.domain.service;

import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.annotation.SpanTag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.keycloak.domain.KeycloakUserRepresentation;
import pcc.puppet.enforcer.keycloak.domain.service.KeycloakService;
import pcc.puppet.enforcer.realm.common.contact.domain.service.ContactInformationService;
import pcc.puppet.enforcer.realm.common.generator.DomainFactory;
import pcc.puppet.enforcer.realm.member.adapters.mapper.MemberOutputMapper;
import pcc.puppet.enforcer.realm.member.adapters.presenter.MemberPresenter;
import pcc.puppet.enforcer.realm.member.adapters.repository.MemberRepository;
import pcc.puppet.enforcer.realm.member.domain.Member;
import pcc.puppet.enforcer.realm.member.ports.command.MemberCreateCommand;
import pcc.puppet.enforcer.realm.member.ports.event.MemberCreateEvent;
import pcc.puppet.enforcer.realm.member.ports.mapper.MemberInputMapper;
import pcc.puppet.enforcer.realm.organization.domain.OrganizationCredentials;
import pcc.puppet.enforcer.vault.domain.KVSecretService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMemberService implements MemberService {

  private final MemberRepository repository;
  private final MemberOutputMapper outputMapper;
  private final MemberInputMapper inputMapper;
  private final ContactInformationService contactInformationService;
  private final KeycloakService keycloakService;
  private final KVSecretService secretService;

  @Override
  @Observed(name = "default-member-service::create")
  public Mono<MemberCreateEvent> create(
      @SpanTag String requester, MemberCreateCommand createCommand) {
    Member member = inputMapper.commandToDomain(createCommand);
    member.setId(DomainFactory.id());
    return contactInformationService
        .save(requester, member.getId(), createCommand.getContactId())
        .map(member::setContact)
        .flatMap(repository::save)
        .zipWith(secretService.getCredentials(member.getOrganizationId()))
        .flatMap(tuple -> createUser(tuple.getT1(), tuple.getT2()))
        .flatMap(this::linkMemberToGroup)
        .map(inputMapper::domainToEvent);
  }

  @Observed(name = "default-member-service::create-user")
  private Mono<Member> createUser(Member member, OrganizationCredentials credentials) {
    return keycloakService
        .createUser(
            credentials.getClientId(),
            credentials.getClientSecret(),
            KeycloakUserRepresentation.fromMember(member))
        .map(response -> member);
  }

  @Observed(name = "default-member-service::link-member-to-group")
  private Mono<Member> linkMemberToGroup(Member member) {
    return keycloakService
        .findUserByUsername(member.getUsername())
        .zipWith(
            keycloakService.findChildGroupByPath(
                member.getOrganizationId(), member.getDepartmentId()))
        .flatMap(
            tuple ->
                keycloakService.attachUserToGroup(tuple.getT1().getId(), tuple.getT2().getId()))
        .map(response -> member);
  }

  @Override
  @Observed(name = "default-member-service::find-by-id")
  public Mono<MemberPresenter> findById(@SpanTag String requester, @SpanTag String memberId) {
    return contactInformationService
        .findByOwnerId(memberId)
        .zipWith(repository.findById(memberId))
        .map(tuple -> tuple.getT2().setContact(tuple.getT1()))
        .map(outputMapper::domainToPresenter);
  }

  @Override
  @Observed(name = "default-member-service::find-by-organization-id")
  public Flux<MemberPresenter> findByOrganizationId(
      @SpanTag String requester, @SpanTag String organizationId) {
    return repository
        .findByOrganizationId(organizationId)
        .flatMap(
            member ->
                contactInformationService.findByOwnerId(member.getId()).map(member::setContact))
        .map(outputMapper::domainToPresenter);
  }

  @Override
  @Observed(name = "default-member-service::find-by-department-id")
  public Flux<MemberPresenter> findByDepartmentId(
      @SpanTag String requester, @SpanTag String departmentId) {
    return repository
        .findByDepartmentId(departmentId)
        .flatMap(
            member ->
                contactInformationService.findByOwnerId(member.getId()).map(member::setContact))
        .map(outputMapper::domainToPresenter);
  }
}
