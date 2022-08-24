/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.api.output.event;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Serdeable
public class DeviceInteractionCreateEvent {
  private String id;
  private String interactionId;
}
