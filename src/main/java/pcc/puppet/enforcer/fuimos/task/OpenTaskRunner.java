/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.task;

import io.micronaut.context.annotation.Value;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.reactor.http.client.ReactorHttpClient;
import jakarta.inject.Singleton;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import pcc.puppet.enforcer.fuimos.OId;
import pcc.puppet.enforcer.fuimos.device.DeviceAction;
import pcc.puppet.enforcer.fuimos.device.repository.UserDevice;
import pcc.puppet.enforcer.fuimos.payload.DeviceInteraction;
import pcc.puppet.enforcer.fuimos.payload.Message;
import pcc.puppet.enforcer.fuimos.payload.ReceivedInteraction;
import pcc.puppet.enforcer.fuimos.payload.property.InteractionDirection;
import pcc.puppet.enforcer.fuimos.payload.property.InteractionPlan;
import pcc.puppet.enforcer.fuimos.payload.property.InteractionSource;
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
    DeviceInteraction deviceInteraction =
        DeviceInteraction.builder()
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

    Flux<HttpResponse<ReceivedInteraction>> exchange =
        client
            .exchange(
                HttpRequest.POST(webhookUrl, deviceInteraction)
                    .header("PLAN", action.getPlan().name())
                    .header("CHANNEL", action.getChannel().name())
                    .header("CALLER", this.getClass().getSimpleName()),
                Argument.of(ReceivedInteraction.class))
            .subscribeOn(Schedulers.boundedElastic());

    exchange.log().subscribe();
  }
}
