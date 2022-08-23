/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.payload;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Serdeable
public class ReceivedInteraction {
  private String id;
  private String interactionId;
}
