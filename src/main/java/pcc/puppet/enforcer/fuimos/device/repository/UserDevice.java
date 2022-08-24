/*
 * Copyright 2022 Pandino Cloud Crew (C)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pcc.puppet.enforcer.fuimos.device.repository;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.Index;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Version;
import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant;
import java.util.List;
import lombok.Data;
import org.bson.types.ObjectId;
import pcc.puppet.enforcer.fuimos.device.DeviceAction;
import pcc.puppet.enforcer.fuimos.device.DeviceStatus;
import pcc.puppet.enforcer.fuimos.network.NetworkCarrier;

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
  @Version @AutoPopulated private Integer version;
}
