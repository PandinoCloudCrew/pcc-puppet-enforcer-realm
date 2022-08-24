/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.input.command;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Serdeable
public class MessageSendCommand {
  private String address;
  private String payload;
}
