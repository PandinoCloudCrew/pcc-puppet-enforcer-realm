/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.input;

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
  private int size;
  private Optional<String> audienceId;
  private List<UserDevicePresenter> devices;
}
