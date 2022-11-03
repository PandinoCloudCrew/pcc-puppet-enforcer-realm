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

import io.micronaut.cache.annotation.CacheConfig;
import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;
import jakarta.inject.Singleton;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pcc.puppet.enforcer.realm.common.contact.domain.service.ContactInformationService;
import pcc.puppet.enforcer.realm.common.generator.DomainFactory;
import pcc.puppet.enforcer.realm.member.adapters.mapper.MemberOutputMapper;
import pcc.puppet.enforcer.realm.member.adapters.presenter.MemberPresenter;
import pcc.puppet.enforcer.realm.member.adapters.repository.MemberRepository;
import pcc.puppet.enforcer.realm.member.domain.Member;
import pcc.puppet.enforcer.realm.member.ports.command.MemberCreateCommand;
import pcc.puppet.enforcer.realm.member.ports.event.MemberCreateEvent;
import pcc.puppet.enforcer.realm.member.ports.mapper.MemberInputMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Singleton
@CacheConfig("member")
@RequiredArgsConstructor
public class DefaultMemberService implements MemberService {

  private final MemberRepository repository;
  private final MemberOutputMapper outputMapper;
  private final MemberInputMapper inputMapper;
  private final ContactInformationService contactInformationService;

  @NewSpan
  @Override
  public Mono<MemberCreateEvent> create(
      @SpanTag String requester, MemberCreateCommand createCommand) {
    Member member = inputMapper.commandToDomain(createCommand);
    member.setId(DomainFactory.id());
    member.setCreatedBy(requester);
    member.setCreatedAt(Instant.now());
    return contactInformationService
        .save(requester, member.getId(), createCommand.getContactId())
        .flatMap(
            contactInformation -> {
              member.setContactId(contactInformation);
              return repository.save(member);
            })
        .map(inputMapper::domainToEvent);
  }

  @NewSpan
  @Override
  @Cacheable
  public Mono<MemberPresenter> findById(@SpanTag String requester, @SpanTag String memberId) {
    return contactInformationService
        .findByOwnerId(memberId)
        .flatMap(
            contactInformation ->
                repository
                    .findById(memberId)
                    .map(
                        member -> {
                          member.setContactId(contactInformation);
                          return member;
                        })
                    .map(outputMapper::domainToPresenter));
  }

  @NewSpan
  @Override
  public Flux<MemberPresenter> findByOrganizationId(
      @SpanTag String requester, @SpanTag String organizationId) {
    return repository
        .findByOrganizationId(organizationId)
        .flatMap(
            member ->
                contactInformationService
                    .findById(member.getContactId().getId())
                    .map(member::setContact))
        .map(outputMapper::domainToPresenter);
  }

  @NewSpan
  @Override
  public Flux<MemberPresenter> findByDepartmentId(
      @SpanTag String requester, @SpanTag String departmentId) {
    return repository
        .findByDepartmentId(departmentId)
        .flatMap(
            member ->
                contactInformationService
                    .findById(member.getContactId().getId())
                    .map(member::setContact))
        .map(outputMapper::domainToPresenter);
  }
}
