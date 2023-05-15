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

package pcc.puppet.enforcer.realm.passport.ports.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static pcc.puppet.enforcer.realm.TestDomainGenerator.REQUESTER_ID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.vault.VaultContainer;
import pcc.puppet.enforcer.app.Application;
import pcc.puppet.enforcer.realm.common.generator.DomainFactory;
import pcc.puppet.enforcer.realm.passport.adapters.http.ConsumerPassportClient;
import pcc.puppet.enforcer.realm.passport.ports.command.ConsumerPassportCreateCommand;
import pcc.puppet.enforcer.realm.passport.ports.event.ConsumerPassportCreateEvent;

@ActiveProfiles("test")
@SpringBootTest(
    webEnvironment = WebEnvironment.DEFINED_PORT,
    classes = Application.class,
    properties = {"server.port=59992"})
class ConsumerPassportControllerTest {

  @Autowired private ConsumerPassportClient client;

  private static final String VAULT_TOKEN = "test-token";

  @Container
  private static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

  @Container
  private static final VaultContainer<?> vaultContainer =
      new VaultContainer<>("hashicorp/vault:1.13")
          .withVaultToken(VAULT_TOKEN)
          .withSecretInVault("secret/testing1", "top_secret=password123")
          .withInitCommand(
              "secrets enable -version=2 -path=secret kv",
              "secrets enable -version=2 -path=credentials kv");

  @DynamicPropertySource
  static void mongoDbProperties(DynamicPropertyRegistry registry) {
    mongoDBContainer.start();
    vaultContainer.start();
    registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    registry.add("provider.vault.token", () -> VAULT_TOKEN);
    registry.add("provider.vault.uri", vaultContainer::getHttpHostAddress);
    registry.add("spring.http.services.provider-vault.url", vaultContainer::getHttpHostAddress);
  }

  @Test
  void createConsumerPassport() {
    ConsumerPassportCreateCommand passportCreateCommand =
        DomainFactory.consumerPassportCreateCommand();
    ConsumerPassportCreateEvent passportCreateEvent =
        client.createConsumerPassport(REQUESTER_ID, passportCreateCommand).block();
    assertNotNull(passportCreateEvent);
    assertNotNull(passportCreateEvent.getUsername());
    assertNotNull(passportCreateEvent.getMemberId());
    assertNotNull(passportCreateEvent.getOrganization());
    assertNotNull(passportCreateEvent.getDepartment());
    assertNotNull(passportCreateEvent.getMember());
    assertNotNull(passportCreateEvent.getAccessRefreshToken());

    assertEquals(
        passportCreateCommand.getFirstName(),
        passportCreateEvent.getMember().getContactId().getFirstName());
    assertEquals(
        passportCreateCommand.getLastName(),
        passportCreateEvent.getMember().getContactId().getLastName());
  }
}
