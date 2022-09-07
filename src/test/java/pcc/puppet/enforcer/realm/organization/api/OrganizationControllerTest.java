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
package pcc.puppet.enforcer.realm.organization.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import pcc.puppet.enforcer.realm.organization.api.command.OrganizationCreateCommand;
import pcc.puppet.enforcer.realm.organization.api.event.OrganizationCreateEvent;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@MicronautTest
class OrganizationControllerTest {

  @Inject
  private OrganizationClient client;

  private final PodamFactory factory = new PodamFactoryImpl();
  private final String REQUESTER_ID = "yesid.bocanegra@pandino.co";

  @Test
  void organizationCreate_ShouldSucceedWithCleanData_ReturnHttpOkWithDetails() {
    OrganizationCreateCommand createCommand = getPojoWithFullData(OrganizationCreateCommand.class);
    OrganizationCreateEvent createEventResponse =
        client.organizationCreate(REQUESTER_ID, createCommand).block();
    assertNotNull(createEventResponse);
    assertEquals(createCommand.getCity(), createEventResponse.getCity());
    assertEquals(createCommand.getName(), createEventResponse.getCity());
    assertEquals(createCommand.getContact(), createEventResponse.getContact());
    assertEquals(createCommand.getCountry(), createEventResponse.getCountry());
    assertEquals(createCommand.getLocation(), createEventResponse.getLocation());
  }

  private <T> T getPojoWithFullData(Class<T> classType) {
    return factory.manufacturePojoWithFullData(classType);
  }

  @Test
  void findOrganization() {
  }

  @Test
  void findChildOrganizations() {
  }
}
