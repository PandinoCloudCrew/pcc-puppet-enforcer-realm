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
