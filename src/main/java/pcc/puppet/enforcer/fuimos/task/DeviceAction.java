/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.task;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Serdeable
public class DeviceAction {

  private String id;
  private TaskType taskType;
  private TaskChannel channel;
  @Nullable private Instant createDate;
}
