/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.device.repository.codec;

import io.micronaut.serde.ObjectMapper;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import pcc.puppet.enforcer.fuimos.task.DeviceAction;

@Singleton
@RequiredArgsConstructor
public class DeviceActionCodec implements Codec<DeviceAction> {

  private final ObjectMapper mapper;

  @SneakyThrows
  @Override
  public DeviceAction decode(BsonReader reader, DecoderContext decoderContext) {
    String enumString = reader.readString();
    return mapper.readValue(enumString, DeviceAction.class);
  }

  @SneakyThrows
  @Override
  public void encode(BsonWriter writer, DeviceAction value, EncoderContext encoderContext) {
    writer.writeString(mapper.writeValueAsString(value));
  }

  @Override
  public Class<DeviceAction> getEncoderClass() {
    return DeviceAction.class;
  }
}
