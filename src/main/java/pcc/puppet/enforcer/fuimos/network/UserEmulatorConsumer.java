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
package pcc.puppet.enforcer.fuimos.network;

import com.agorapulse.worker.annotation.Consumes;
import com.agorapulse.worker.annotation.FixedRate;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import pcc.puppet.enforcer.fuimos.device.mapper.UserDeviceMapper;
import pcc.puppet.enforcer.fuimos.device.repository.UserDeviceRepository;
import pcc.puppet.enforcer.fuimos.payload.Message;
import pcc.puppet.enforcer.fuimos.payload.property.InteractionPlan;
import pcc.puppet.enforcer.fuimos.task.DeviceTaskRunner;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Singleton
public final class UserEmulatorConsumer {
  public static final String DEVICE_MESSAGES = "device-messages";

  private final Map<InteractionPlan, DeviceTaskRunner> taskRunners;
  private final UserDeviceRepository deviceRepository;
  private final UserDeviceMapper deviceMapper;

  public UserEmulatorConsumer(
      List<DeviceTaskRunner> taskRunners,
      UserDeviceRepository deviceRepository,
      UserDeviceMapper deviceMapper) {
    this.deviceRepository = deviceRepository;
    this.deviceMapper = deviceMapper;
    this.taskRunners =
        taskRunners.stream().collect(Collectors.toMap(DeviceTaskRunner::getType, task -> task));
  }

  @Named(DEVICE_MESSAGES)
  @Consumes(DEVICE_MESSAGES)
  @FixedRate("50ms")
  public void createAudience(Message message) {
    deviceRepository
        .findByAddress(message.getAddress())
        .subscribeOn(Schedulers.boundedElastic())
        .doOnSuccess(
            userDevice -> {
              userDevice
                  .getTasks()
                  .forEach(
                      action -> taskRunners.get(action.getPlan()).run(action, userDevice, message));
            })
        .subscribe();
    log.info("test message: {}", message);
  }
}
