/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.network;

import jakarta.inject.Singleton;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pcc.puppet.enforcer.fuimos.OId;
import pcc.puppet.enforcer.fuimos.device.DeviceStatus;
import pcc.puppet.enforcer.fuimos.device.UserDevicePresenter;
import pcc.puppet.enforcer.fuimos.device.mapper.UserDeviceMapper;
import pcc.puppet.enforcer.fuimos.device.repository.UserDevice;
import pcc.puppet.enforcer.fuimos.device.repository.UserDeviceRepository;
import pcc.puppet.enforcer.fuimos.payload.Message;
import pcc.puppet.enforcer.fuimos.payload.UserInteraction;
import pcc.puppet.enforcer.fuimos.task.DeviceAction;
import pcc.puppet.enforcer.fuimos.task.TaskChannel;
import pcc.puppet.enforcer.fuimos.task.TaskType;
import reactor.core.publisher.Mono;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class UserNetwork {

  private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private final UserDeviceRepository deviceRepository;
  private final UserDeviceMapper deviceMapper;

  private final Map<String, List<Message>> messages = new HashMap<>();
  private final Map<String, List<UserInteraction>> interactions = new HashMap<>();

  public Mono<UserDevicePresenter> enroll(
      String address, NetworkCarrier carrier, Optional<String> audienceId) {
    DeviceAction openTask =
        DeviceAction.builder()
            .taskType(TaskType.OPEN)
            .channel(TaskChannel.SMS)
            .createDate(Instant.now())
            .id(OId.string())
            .build();
    UserDevicePresenter userDevice =
        UserDevicePresenter.builder()
            .status(DeviceStatus.ACTIVE)
            .address(address)
            .carrier(carrier)
            .audienceId(audienceId.orElse(dateFormat.format(LocalDate.now())))
            .task(TaskType.OPEN.name(), openTask)
            .createDate(Instant.now())
            .build();
    UserDevice userDeviceEntity = deviceMapper.presenterToDomain(userDevice);
    Mono<UserDevice> save = deviceRepository.save(userDeviceEntity);
    return save.map(deviceMapper::domainToPresenter);
  }

  public Mono<UserDevicePresenter> find(String address) {
    return deviceRepository.findByAddress(address).map(deviceMapper::domainToPresenter);
  }

  public void sendMessage(final UserDevicePresenter device, final Message message) {
    if (!messages.containsKey(device.getAddress())) {
      messages.put(device.getAddress(), new ArrayList<>());
    }
    messages.get(device.getAddress()).add(message);
    // hand off to interaction runner
  }
}
