/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.api;

import static pcc.puppet.enforcer.fuimos.network.UserEmulatorConsumer.DEVICE_MESSAGES;

import com.agorapulse.worker.JobManager;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.tracing.annotation.NewSpan;
import java.time.Instant;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pcc.puppet.enforcer.fuimos.OId;
import pcc.puppet.enforcer.fuimos.api.input.command.MessageSendCommand;
import pcc.puppet.enforcer.fuimos.api.output.event.MessageSendEvent;
import pcc.puppet.enforcer.fuimos.payload.Message;
import pcc.puppet.enforcer.fuimos.payload.MessageStatus;
import pcc.puppet.enforcer.fuimos.payload.mapper.MessageMapper;
import reactor.core.publisher.Mono;

@Slf4j
@Controller("/fuimos/message")
@Secured(SecurityRule.IS_ANONYMOUS)
@RequiredArgsConstructor
public class MessageController {

  private final MessageMapper messageMapper;
  private final JobManager jobManager;

  @NewSpan
  @Post(produces = MediaType.APPLICATION_JSON, consumes = MediaType.APPLICATION_JSON)
  public Mono<MessageSendEvent> messageSend(@NonNull @Body @Valid MessageSendCommand command) {
    return Mono.fromCallable(
        () -> {
          Message message = messageMapper.commandToDomain(command);
          message.setId(OId.string());
          message.setCreateDate(Instant.now());
          message.setStatus(MessageStatus.SENT);
          jobManager.enqueue(DEVICE_MESSAGES, message);
          return messageMapper.domainToEvent(message);
        });
  }
}
