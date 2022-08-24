/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.input;

import static pcc.puppet.enforcer.fuimos.network.UserEmulatorConsumer.DEVICE_MESSAGES;

import com.agorapulse.worker.JobManager;
import com.agorapulse.worker.annotation.Produces;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.tracing.annotation.NewSpan;
import java.time.Instant;
import java.util.Optional;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import pcc.puppet.enforcer.fuimos.OId;
import pcc.puppet.enforcer.fuimos.device.UserDevicePresenter;
import pcc.puppet.enforcer.fuimos.input.command.AudienceCreateCommand;
import pcc.puppet.enforcer.fuimos.input.command.MessageSendCommand;
import pcc.puppet.enforcer.fuimos.network.NetworkCarrier;
import pcc.puppet.enforcer.fuimos.network.UserNetwork;
import pcc.puppet.enforcer.fuimos.output.event.AudienceCreateEvent;
import pcc.puppet.enforcer.fuimos.output.event.MessageSendEvent;
import pcc.puppet.enforcer.fuimos.payload.DeviceInteraction;
import pcc.puppet.enforcer.fuimos.payload.Message;
import pcc.puppet.enforcer.fuimos.payload.MessageStatus;
import pcc.puppet.enforcer.fuimos.payload.ReceivedInteraction;
import pcc.puppet.enforcer.fuimos.payload.mapper.MessageMapper;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller("/fuimos")
@Secured(SecurityRule.IS_ANONYMOUS)
@RequiredArgsConstructor
public class FuimosController {

  private final UserNetwork userNetwork;
  private final MessageMapper messageMapper;
  private final JobManager jobManager;
  private final Faker faker = new Faker();

  @NewSpan
  @Post(
      uri = "/interaction",
      produces = MediaType.APPLICATION_JSON,
      consumes = MediaType.APPLICATION_JSON)
  public ReceivedInteraction receivedInteraction(
      @Valid @NonNull @Body DeviceInteraction deviceInteraction) {
    log.info("{}", deviceInteraction);
    return ReceivedInteraction.builder()
        .id(OId.string())
        .interactionId(deviceInteraction.getId())
        .build();
  }

  @NewSpan
  @Post(
      uri = "/message",
      produces = MediaType.APPLICATION_JSON,
      consumes = MediaType.APPLICATION_JSON)
  public Mono<MessageSendEvent> messageSend(@NonNull @Body @Valid MessageSendCommand command) {
    return Mono.fromCallable(
        () -> {
          Message message = messageMapper.commandToDomain(command);
          message.setId(OId.string());
          message.setCreateDate(Instant.now());
          message.setStatus(MessageStatus.SENT);
          jobManager.enqueue(DEVICE_MESSAGES, message);
          //          enqueue(message);
          return messageMapper.domainToEvent(message);
        });
  }

  @Produces(DEVICE_MESSAGES)
  public Message enqueue(Message message) {
    return message;
  }

  @NewSpan
  @Get(uri = "/audience", produces = MediaType.APPLICATION_JSON)
  public Flux<UserDevicePresenter> findAudienceDevices(
      @QueryValue("audienceId") @NonNull String audienceId) {
    return userNetwork.findByAudience(audienceId);
  }

  @NewSpan
  @Post(
      uri = "/audience",
      produces = MediaType.APPLICATION_JSON,
      consumes = MediaType.APPLICATION_JSON)
  public Mono<AudienceCreateEvent> enrollDevices(
      @NonNull @Body @Valid AudienceCreateCommand createCommand) {
    Optional<String> audienceId = Optional.ofNullable(createCommand.getAudienceId());
    return Flux.range(0, createCommand.getSize())
        .flatMap(
            number ->
                userNetwork.enroll(
                    faker.phoneNumber().cellPhone(), NetworkCarrier.getRandomCarrier(), audienceId))
        .collectList()
        .map(
            userDevices ->
                AudienceCreateEvent.builder()
                    .size(userDevices.size())
                    .audienceId(audienceId)
                    .devices(userDevices)
                    .build());
  }
}
