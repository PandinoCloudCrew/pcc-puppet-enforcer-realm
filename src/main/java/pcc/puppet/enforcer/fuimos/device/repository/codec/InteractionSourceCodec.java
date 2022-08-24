/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.device.repository.codec;

import jakarta.inject.Singleton;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import pcc.puppet.enforcer.fuimos.payload.property.InteractionSource;

@Singleton
public class InteractionSourceCodec implements Codec<InteractionSource> {

  @Override
  public InteractionSource decode(BsonReader reader, DecoderContext decoderContext) {
    String enumString = reader.readString();
    return InteractionSource.valueOf(enumString);
  }

  @Override
  public void encode(BsonWriter writer, InteractionSource value, EncoderContext encoderContext) {
    String enumString = value.name();
    writer.writeString(enumString);
  }

  @Override
  public Class<InteractionSource> getEncoderClass() {
    return InteractionSource.class;
  }
}
