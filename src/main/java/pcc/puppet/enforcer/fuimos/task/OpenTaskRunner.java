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
package pcc.puppet.enforcer.fuimos.task;

import io.micronaut.context.annotation.Value;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.reactor.http.client.ReactorHttpClient;
import jakarta.inject.Singleton;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import pcc.puppet.enforcer.fuimos.api.interaction.command.DeviceInteractionCreateCommand;
import pcc.puppet.enforcer.fuimos.api.interaction.event.DeviceInteractionCreateEvent;
import pcc.puppet.enforcer.fuimos.device.DeviceAction;
import pcc.puppet.enforcer.fuimos.device.repository.UserDevice;
import pcc.puppet.enforcer.fuimos.payload.Message;
import pcc.puppet.enforcer.fuimos.payload.property.InteractionDirection;
import pcc.puppet.enforcer.fuimos.payload.property.InteractionPlan;
import pcc.puppet.enforcer.fuimos.payload.property.InteractionSource;
import pcc.puppet.enforcer.fuimos.util.OId;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Singleton
public class OpenTaskRunner implements DeviceTaskRunner {

  private final ReactorHttpClient client;
  private final String webhookUrl;

  public OpenTaskRunner(
      ReactorHttpClient client, @Value("${fuimos.services.interaction}") String webhookUrl) {
    this.client = client;
    this.webhookUrl = webhookUrl;
  }

  @Override
  public InteractionPlan getType() {
    return InteractionPlan.OPEN;
  }

  @Override
  public void run(DeviceAction action, UserDevice device, Message message) {
    DeviceInteractionCreateCommand deviceInteractionCreateCommand =
        DeviceInteractionCreateCommand.builder()
            .id(OId.string())
            .messageId(message.getId())
            .deviceId(device.getId().toHexString())
            .payload(message.getPayload())
            .address(message.getAddress())
            .interactionPlan(action.getPlan())
            .channel(action.getChannel())
            .direction(InteractionDirection.OUTPUT)
            .source(InteractionSource.REACTED)
            .createAt(message.getCreateDate())
            .receivedAt(Instant.now())
            .build();

    Flux<HttpResponse<DeviceInteractionCreateEvent>> exchange =
        client
            .exchange(
                HttpRequest.POST(webhookUrl, deviceInteractionCreateCommand)
                    .header("PLAN", action.getPlan().name())
                    .header("CHANNEL", action.getChannel().name())
                    .header("CALLER", this.getClass().getSimpleName()),
                Argument.of(DeviceInteractionCreateEvent.class))
            .subscribeOn(Schedulers.boundedElastic());

    exchange.log().subscribe();
  }
}
