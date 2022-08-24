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

import io.micronaut.serde.ObjectMapper;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import pcc.puppet.enforcer.fuimos.device.DeviceAction;

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
