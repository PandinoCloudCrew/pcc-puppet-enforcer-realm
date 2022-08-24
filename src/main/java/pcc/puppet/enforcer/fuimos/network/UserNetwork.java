/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.network;

import io.micronaut.tracing.annotation.ContinueSpan;
import io.micronaut.tracing.annotation.SpanTag;
import jakarta.inject.Singleton;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pcc.puppet.enforcer.fuimos.api.audience.presenter.UserDevicePresenter;
import pcc.puppet.enforcer.fuimos.device.DeviceAction;
import pcc.puppet.enforcer.fuimos.device.DeviceStatus;
import pcc.puppet.enforcer.fuimos.device.mapper.UserDeviceMapper;
import pcc.puppet.enforcer.fuimos.device.repository.UserDevice;
import pcc.puppet.enforcer.fuimos.device.repository.UserDeviceRepository;
import pcc.puppet.enforcer.fuimos.payload.property.InteractionChannel;
import pcc.puppet.enforcer.fuimos.payload.property.InteractionPlan;
import pcc.puppet.enforcer.fuimos.util.OId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class UserNetwork {

  private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private final UserDeviceRepository deviceRepository;
  private final UserDeviceMapper deviceMapper;

  @ContinueSpan
  public Mono<UserDevicePresenter> enroll(
      @SpanTag("device.address") String address,
      NetworkCarrier carrier,
      Optional<String> audienceId) {
    DeviceAction openTask =
        DeviceAction.builder()
            .plan(InteractionPlan.OPEN)
            .channel(InteractionChannel.SMS)
            .payload("device-open")
            .createDate(Instant.now())
            .id(OId.string())
            .build();
    UserDevicePresenter userDevice =
        UserDevicePresenter.builder()
            .status(DeviceStatus.ACTIVE)
            .address(address)
            .carrier(carrier)
            .audienceId(audienceId.orElse(dateFormat.format(LocalDate.now())))
            .task(openTask)
            .createDate(Instant.now())
            .build();
    UserDevice userDeviceEntity = deviceMapper.presenterToDomain(userDevice);
    return deviceRepository.save(userDeviceEntity).map(deviceMapper::domainToPresenter);
  }

  @ContinueSpan
  public Mono<UserDevicePresenter> find(@SpanTag("device.address") String address) {
    return deviceRepository.findByAddress(address).map(deviceMapper::domainToPresenter);
  }

  @ContinueSpan
  public Flux<UserDevicePresenter> findByAudience(@SpanTag("network.audience") String audienceId) {
    return deviceRepository.findByAudienceId(audienceId).map(deviceMapper::domainToPresenter);
  }
}
