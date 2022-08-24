/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.config;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import pcc.puppet.enforcer.fuimos.device.mapper.UserDeviceMapper;
import pcc.puppet.enforcer.fuimos.payload.mapper.MessageMapper;

@Factory
@RequiredArgsConstructor
public class FuimosFactory {

  @Singleton
  public UserDeviceMapper userDeviceMapper() {
    return Mappers.getMapper(UserDeviceMapper.class);
  }

  @Singleton
  public MessageMapper messageMapper() {
    return Mappers.getMapper(MessageMapper.class);
  }
}
