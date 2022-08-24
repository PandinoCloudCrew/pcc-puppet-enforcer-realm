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
package pcc.puppet.enforcer.fuimos.api.message;

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
import pcc.puppet.enforcer.fuimos.api.message.command.MessageSendCommand;
import pcc.puppet.enforcer.fuimos.api.message.event.MessageSendEvent;
import pcc.puppet.enforcer.fuimos.payload.Message;
import pcc.puppet.enforcer.fuimos.payload.MessageStatus;
import pcc.puppet.enforcer.fuimos.payload.mapper.MessageMapper;
import pcc.puppet.enforcer.fuimos.util.OId;
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
