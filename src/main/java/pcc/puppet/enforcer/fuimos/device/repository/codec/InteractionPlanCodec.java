/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.device.repository.codec;

import jakarta.inject.Singleton;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import pcc.puppet.enforcer.fuimos.payload.property.InteractionPlan;

@Singleton
public class InteractionPlanCodec implements Codec<InteractionPlan> {

  @Override
  public InteractionPlan decode(BsonReader reader, DecoderContext decoderContext) {
    String enumString = reader.readString();
    return InteractionPlan.valueOf(enumString);
  }

  @Override
  public void encode(BsonWriter writer, InteractionPlan value, EncoderContext encoderContext) {
    String enumString = value.name();
    writer.writeString(enumString);
  }

  @Override
  public Class<InteractionPlan> getEncoderClass() {
    return InteractionPlan.class;
  }
}
