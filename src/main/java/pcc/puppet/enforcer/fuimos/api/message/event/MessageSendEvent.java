/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.api.message.event;

import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;
import pcc.puppet.enforcer.fuimos.payload.MessageStatus;

@Data
@Builder
@Serdeable
public class MessageSendEvent {
  private String id;
  private String address;
  private String payload;
  private MessageStatus status;
  private Instant createDate;
}
