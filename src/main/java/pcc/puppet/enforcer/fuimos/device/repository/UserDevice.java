/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.device.repository;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Index;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant;
import java.util.List;
import lombok.Data;
import org.bson.types.ObjectId;
import pcc.puppet.enforcer.fuimos.device.DeviceStatus;
import pcc.puppet.enforcer.fuimos.network.NetworkCarrier;
import pcc.puppet.enforcer.fuimos.task.DeviceAction;

@Data
@Serdeable
@MappedEntity(value = "user_device")
public class UserDevice {

  @Id @GeneratedValue private ObjectId id;

  @Index(unique = true, columns = "address")
  private String address;

  private String owner;
  private String audienceId;
  private NetworkCarrier carrier;
  private DeviceStatus status;

  private List<DeviceAction> tasks;

  private Instant createDate;
  @Nullable private Instant lastActiveDate;

  @DateCreated @AutoPopulated private Instant createdAt;
  @DateCreated @AutoPopulated private Instant updatedAt;
}
