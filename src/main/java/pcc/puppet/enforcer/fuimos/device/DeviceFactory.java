/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.device;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import pcc.puppet.enforcer.fuimos.device.mapper.UserDeviceMapper;

@Factory
@RequiredArgsConstructor
public class DeviceFactory {

  @Singleton
  public UserDeviceMapper userDeviceMapper() {
    return Mappers.getMapper(UserDeviceMapper.class);
  }
}
