/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.device;

import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.bson.types.ObjectId;
import pcc.puppet.enforcer.fuimos.network.NetworkCarrier;

@Data
@Builder
@Serdeable
public class UserDevicePresenter {

  private ObjectId id;
  private String address;
  private String owner;
  private String audienceId;
  private NetworkCarrier carrier;
  private DeviceStatus status;
  @Singular private List<DeviceAction> tasks;
  private Instant createDate;
  private Instant lastActiveDate;
}
