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
package pcc.puppet.enforcer.realm.passport.domain.service;

import io.micronaut.security.authentication.UsernamePasswordCredentials;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import pcc.puppet.enforcer.keycloak.domain.service.KeycloakService;
import pcc.puppet.enforcer.realm.common.adapters.http.RealmClient;
import pcc.puppet.enforcer.realm.common.contact.ports.command.CreateContactInformationCommand;
import pcc.puppet.enforcer.realm.department.adapters.http.DepartmentClient;
import pcc.puppet.enforcer.realm.department.ports.command.DepartmentCreateCommand;
import pcc.puppet.enforcer.realm.department.ports.event.DepartmentCreateEvent;
import pcc.puppet.enforcer.realm.member.adapters.http.MemberClient;
import pcc.puppet.enforcer.realm.member.ports.command.MemberCreateCommand;
import pcc.puppet.enforcer.realm.member.ports.event.MemberCreateEvent;
import pcc.puppet.enforcer.realm.organization.adapters.http.OrganizationClient;
import pcc.puppet.enforcer.realm.organization.ports.command.OrganizationCreateCommand;
import pcc.puppet.enforcer.realm.organization.ports.event.OrganizationCreateEvent;
import pcc.puppet.enforcer.realm.passport.adapters.gateway.rest_countries.RestCountriesApiClient;
import pcc.puppet.enforcer.realm.passport.adapters.gateway.rest_countries.response.CountryCurrency;
import pcc.puppet.enforcer.realm.passport.adapters.gateway.rest_countries.response.RestCountriesResponse;
import pcc.puppet.enforcer.realm.passport.ports.command.ConsumerPassportCreateCommand;
import pcc.puppet.enforcer.realm.passport.ports.event.ConsumerPassportCreateEvent;
import reactor.core.publisher.Mono;

@Singleton
@RequiredArgsConstructor
public class DefaultConsumerPassportService implements ConsumerPassportService {

  private static final String NOT_DEFINED = "NOT_DEFINED";
  private final OrganizationClient organizationClient;
  private final DepartmentClient departmentClient;
  private final MemberClient memberClient;
  private final RealmClient realmClient;
  private final RestCountriesApiClient countriesApiClient;
  private final KeycloakService keycloakService;

  @Override
  public Mono<ConsumerPassportCreateEvent> createConsumerPassport(
      String requester, ConsumerPassportCreateCommand passportCommand) {
    ConsumerPassportCreateEvent consumerPassportEvent =
        ConsumerPassportCreateEvent.builder().build();
    return countriesApiClient
        .getByName(passportCommand.getCountry())
        .flatMap(
            arrayResponse -> {
              RestCountriesResponse restCountriesResponse = arrayResponse.get(0);
              CreateContactInformationCommand createContactInformationCommand =
                  getCreateContactInformationCommand(passportCommand, restCountriesResponse);
              return createOrganization(requester, passportCommand, createContactInformationCommand)
                  .map(consumerPassportEvent::organization)
                  .flatMap(
                      passportEvent ->
                          createDepartment(
                              requester,
                              passportEvent,
                              passportCommand,
                              createContactInformationCommand))
                  .map(consumerPassportEvent::department)
                  .flatMap(
                      passportEvent ->
                          createMember(
                              requester,
                              passportEvent,
                              passportCommand,
                              createContactInformationCommand))
                  .map(consumerPassportEvent::member)
                  .flatMap(
                      passportCreateEvent -> {
                        passportCreateEvent.setMemberId(passportCreateEvent.memberId());
                        passportCreateEvent.setUsername(
                            passportCreateEvent.getMember().getUsername());
                        return keycloakService
                            .createUser(
                                consumerPassportEvent,
                                passportCommand.getEmail(),
                                passportCommand.getPassword())
                            .flatMap(
                                optional ->
                                    realmClient.login(
                                        requester,
                                        new UsernamePasswordCredentials(
                                            passportCommand.getEmail(),
                                            passportCommand.getPassword())));
                      })
                  .map(consumerPassportEvent::token);
            });
  }

  private Mono<MemberCreateEvent> createMember(
      String requester,
      ConsumerPassportCreateEvent passportEvent,
      ConsumerPassportCreateCommand passportCommand,
      CreateContactInformationCommand contactInformationCommand) {
    MemberCreateCommand memberCreateCommand =
        MemberCreateCommand.builder()
            .contactId(contactInformationCommand)
            .departmentId(passportEvent.departmentId())
            .organizationId(passportEvent.organizationId())
            .username(passportCommand.getEmail())
            .password(passportCommand.getPassword())
            .build();
    return memberClient.memberCreate(
        requester,
        passportEvent.organizationId(),
        passportEvent.departmentId(),
        memberCreateCommand);
  }

  private Mono<OrganizationCreateEvent> createOrganization(
      String requester,
      ConsumerPassportCreateCommand passportCommand,
      CreateContactInformationCommand contactInformationCommand) {
    OrganizationCreateCommand organizationCreateCommand =
        getOrganizationCreateCommand(passportCommand, contactInformationCommand);
    return organizationClient.organizationCreate(requester, organizationCreateCommand);
  }

  private Mono<DepartmentCreateEvent> createDepartment(
      String requester,
      ConsumerPassportCreateEvent passportEvent,
      ConsumerPassportCreateCommand passportCommand,
      CreateContactInformationCommand contactInformationCommand) {
    DepartmentCreateCommand departmentCreateCommand =
        DepartmentCreateCommand.builder()
            .organizationId(passportEvent.organizationId())
            .name(passportCommand.getDepartmentName())
            .location(passportCommand.getLocation())
            .contactId(contactInformationCommand)
            .build();
    return departmentClient.departmentCreate(
        requester, passportEvent.organizationId(), departmentCreateCommand);
  }

  private static OrganizationCreateCommand getOrganizationCreateCommand(
      ConsumerPassportCreateCommand passportCommand,
      CreateContactInformationCommand createContactInformationCommand) {
    return OrganizationCreateCommand.builder()
        .name(passportCommand.getOrganizationName())
        .country(passportCommand.getCountry())
        .city(passportCommand.getCity())
        .location(passportCommand.getLocation())
        .taxId(passportCommand.getTaxId())
        .contactId(createContactInformationCommand)
        .build();
  }

  private static CreateContactInformationCommand getCreateContactInformationCommand(
      ConsumerPassportCreateCommand passportCommand, RestCountriesResponse restCountriesResponse) {
    return CreateContactInformationCommand.builder()
        .firstName(passportCommand.getFirstName())
        .lastName(passportCommand.getLastName())
        .country(passportCommand.getCountry())
        .email(passportCommand.getEmail())
        .phoneNumber(passportCommand.getPhoneNumber())
        .city(passportCommand.getCity())
        .position(passportCommand.getPosition())
        .locale(
            Optional.ofNullable(restCountriesResponse.getLanguages())
                .orElse(Map.of())
                .values()
                .stream()
                .findFirst()
                .orElse(NOT_DEFINED))
        .zoneId(
            Optional.ofNullable(restCountriesResponse.getTimezones()).orElse(List.of()).stream()
                .findFirst()
                .orElse(NOT_DEFINED))
        .currency(
            Optional.ofNullable(restCountriesResponse.getCurrencies())
                .orElse(Map.of())
                .values()
                .stream()
                .findFirst()
                .orElse(CountryCurrency.builder().name(NOT_DEFINED).build())
                .getName())
        .build();
  }
}
