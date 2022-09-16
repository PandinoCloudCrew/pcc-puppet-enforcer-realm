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
package pcc.puppet.enforcer.realm.common.contact.domain.service;

import io.micronaut.tracing.annotation.NewSpan;
import jakarta.inject.Singleton;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import pcc.puppet.enforcer.realm.common.contact.adapters.repository.ContactInformationRepository;
import pcc.puppet.enforcer.realm.common.contact.domain.ContactInformation;
import pcc.puppet.enforcer.realm.common.contact.ports.command.CreateContactInformationCommand;
import pcc.puppet.enforcer.realm.common.contact.ports.mapper.ContactInformationInputMapper;
import pcc.puppet.enforcer.realm.common.generator.DomainFactory;
import reactor.core.publisher.Mono;

@Singleton
@RequiredArgsConstructor
public class DefaultContactInformationService implements ContactInformationService {

  private final ContactInformationRepository repository;
  private final ContactInformationInputMapper inputMapper;

  @NewSpan
  @Override
  public Mono<ContactInformation> save(
      String requester, String ownerId, CreateContactInformationCommand command) {
    ContactInformation contactInformation = inputMapper.commandToDomain(command);
    contactInformation.setId(DomainFactory.id());
    contactInformation.setCreatedBy(requester);
    contactInformation.setCreatedAt(Instant.now());
    contactInformation.setOwnerId(ownerId);
    return repository.save(contactInformation);
  }

  @NewSpan
  @Override
  public Mono<ContactInformation> findById(String contactInformationId) {
    return repository.findById(contactInformationId);
  }

  @NewSpan
  @Override
  public Mono<ContactInformation> findByOwnerId(String ownerId) {
    return repository.findByOwnerId(ownerId);
  }
}
