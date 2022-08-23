/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.task;

import com.github.shamil.Xid;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.reactor.http.client.ReactorHttpClient;
import jakarta.inject.Singleton;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pcc.puppet.enforcer.fuimos.device.UserDevicePresenter;
import pcc.puppet.enforcer.fuimos.payload.Message;
import pcc.puppet.enforcer.fuimos.payload.ReceivedInteraction;
import pcc.puppet.enforcer.fuimos.payload.UserInteraction;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class OpenTaskRunner implements DeviceTaskRunner {
  private final ReactorHttpClient client;

  @Value("${fuimos.services.interaction}")
  private final String webhookUrl;

  @Override
  public void run(UserDevicePresenter device, Message message) {
    DeviceAction openAction = device.getTasks().get(TaskType.OPEN);
    UserInteraction userInteraction =
        UserInteraction.builder()
            .id(Xid.string())
            .messageId(message.getId())
            .deviceId(device.getId())
            .address(message.getAddress())
            .taskType(openAction.getTaskType())
            .createAt(message.getCreateDate())
            .receivedAt(Instant.now())
            .build();

    Flux<HttpResponse<ReceivedInteraction>> exchange =
        client
            .exchange(
                HttpRequest.POST(webhookUrl, userInteraction),
                Argument.of(ReceivedInteraction.class))
            .subscribeOn(Schedulers.boundedElastic());

    exchange.log().subscribe();
  }
}
