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
package pcc.puppet.enforcer.realm.authentication.domain.service;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import pcc.puppet.enforcer.realm.authentication.adapters.gateway.rest_countries.RestCountriesApiClient;
import pcc.puppet.enforcer.realm.authentication.adapters.gateway.rest_countries.response.CountryCurrency;
import pcc.puppet.enforcer.realm.authentication.adapters.gateway.rest_countries.response.RestCountriesResponse;
import pcc.puppet.enforcer.realm.authentication.ports.command.ConsumerPassportCreateCommand;
import pcc.puppet.enforcer.realm.authentication.ports.event.ConsumerPassportCreateEvent;
import pcc.puppet.enforcer.realm.common.contact.ports.command.CreateContactInformationCommand;
import pcc.puppet.enforcer.realm.department.adapters.http.DepartmentClient;
import pcc.puppet.enforcer.realm.department.ports.command.DepartmentCreateCommand;
import pcc.puppet.enforcer.realm.member.adapters.http.MemberClient;
import pcc.puppet.enforcer.realm.member.ports.command.MemberCreateCommand;
import pcc.puppet.enforcer.realm.organization.adapters.http.OrganizationClient;
import pcc.puppet.enforcer.realm.organization.ports.command.OrganizationCreateCommand;
import reactor.core.publisher.Mono;

@Singleton
@RequiredArgsConstructor
public class DefaultConsumerPassportService implements ConsumerPassportService {

  private static final String NOT_DEFINED = "NOT_DEFINED";
  private final OrganizationClient organizationClient;
  private final DepartmentClient departmentClient;
  private final MemberClient memberClient;
  private final RestCountriesApiClient countriesApiClient;

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
                  CreateContactInformationCommand.builder()
                      .firstName(passportCommand.getFirstName())
                      .lastName(passportCommand.getLastName())
                      .country(passportCommand.getCountry())
                      .email(passportCommand.getEmail())
                      .phoneNumber(passportCommand.getPhoneNumber())
                      .city(passportCommand.getCity())
                      .position(passportCommand.getPosition())
                      .locale(
                          restCountriesResponse.getLanguages().values().stream()
                              .findFirst()
                              .orElse(NOT_DEFINED))
                      .zoneId(
                          restCountriesResponse.getTimezones().stream()
                              .findFirst()
                              .orElse(NOT_DEFINED))
                      .currency(
                          restCountriesResponse.getCurrencies().values().stream()
                              .findFirst()
                              .orElse(CountryCurrency.builder().name(NOT_DEFINED).build())
                              .getName())
                      .build();
              OrganizationCreateCommand organizationCreateCommand =
                  OrganizationCreateCommand.builder()
                      .name(passportCommand.getOrganizationName())
                      .country(passportCommand.getCountry())
                      .city(passportCommand.getCity())
                      .location(passportCommand.getLocation())
                      .taxId(passportCommand.getTaxId())
                      .contactId(createContactInformationCommand)
                      .build();

              return organizationClient
                  .organizationCreate(requester, organizationCreateCommand)
                  .map(consumerPassportEvent::organization)
                  .flatMap(
                      passportEvent -> {
                        DepartmentCreateCommand departmentCreateCommand =
                            DepartmentCreateCommand.builder()
                                .organizationId(passportEvent.organizationId())
                                .name(passportCommand.getDepartmentName())
                                .location(passportCommand.getLocation())
                                .contactId(createContactInformationCommand)
                                .build();
                        return departmentClient.departmentCreate(
                            requester, passportEvent.organizationId(), departmentCreateCommand);
                      })
                  .map(consumerPassportEvent::department)
                  .flatMap(
                      passportEvent -> {
                        MemberCreateCommand memberCreateCommand =
                            MemberCreateCommand.builder()
                                .contactId(createContactInformationCommand)
                                .departmentId(passportEvent.departmentId())
                                .organizationId(passportEvent.organizationId())
                                .username(passportCommand.getUsername())
                                .password(passportCommand.getPassword())
                                .build();
                        return memberClient.memberCreate(
                            requester,
                            passportEvent.organizationId(),
                            passportEvent.departmentId(),
                            memberCreateCommand);
                      })
                  .map(consumerPassportEvent::member)
                  .map(
                      passportCreateEvent -> {
                        passportCreateEvent.setMemberId(passportCreateEvent.memberId());
                        return passportCreateEvent;
                      });
            });
  }
}
