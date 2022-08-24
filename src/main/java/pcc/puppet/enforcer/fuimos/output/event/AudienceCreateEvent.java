/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.output.event;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import pcc.puppet.enforcer.fuimos.device.UserDevicePresenter;

@Data
@Builder
@Serdeable
public class AudienceCreateEvent {

  @NonNull private Integer size;
  @NonNull private Optional<String> audienceId;
  @Nullable private List<UserDevicePresenter> devices;
}
