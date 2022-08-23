/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.device.repository.codec;

import jakarta.inject.Singleton;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import pcc.puppet.enforcer.fuimos.device.DeviceStatus;

@Singleton
public class DeviceStatusCodec implements Codec<DeviceStatus> {

  @Override
  public DeviceStatus decode(BsonReader reader, DecoderContext decoderContext) {
    String enumString = reader.readString();
    return DeviceStatus.valueOf(enumString);
  }

  @Override
  public void encode(BsonWriter writer, DeviceStatus value, EncoderContext encoderContext) {
    String enumString = value.name();
    writer.writeString(enumString);
  }

  @Override
  public Class<DeviceStatus> getEncoderClass() {
    return DeviceStatus.class;
  }
}
