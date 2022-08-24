/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.payload.mapper;

import org.mapstruct.Mapper;
import pcc.puppet.enforcer.fuimos.api.input.command.MessageSendCommand;
import pcc.puppet.enforcer.fuimos.api.output.event.MessageSendEvent;
import pcc.puppet.enforcer.fuimos.payload.Message;

@Mapper
public interface MessageMapper {
  Message commandToDomain(MessageSendCommand command);

  MessageSendEvent domainToEvent(Message command);
}
