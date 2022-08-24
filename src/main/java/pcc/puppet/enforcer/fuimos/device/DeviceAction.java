/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.device;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;
import pcc.puppet.enforcer.fuimos.payload.property.InteractionChannel;
import pcc.puppet.enforcer.fuimos.payload.property.InteractionPlan;

@Data
@Builder
@Serdeable
public class DeviceAction {
  private String id;
  private String payload;
  private InteractionPlan plan;
  private InteractionChannel channel;
  @Nullable private Instant createDate;
}
