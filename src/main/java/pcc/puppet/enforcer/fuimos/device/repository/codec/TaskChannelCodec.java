/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.device.repository.codec;

import jakarta.inject.Singleton;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import pcc.puppet.enforcer.fuimos.task.TaskChannel;

@Singleton
public class TaskChannelCodec implements Codec<TaskChannel> {

  @Override
  public TaskChannel decode(BsonReader reader, DecoderContext decoderContext) {
    String enumString = reader.readString();
    return TaskChannel.valueOf(enumString);
  }

  @Override
  public void encode(BsonWriter writer, TaskChannel value, EncoderContext encoderContext) {
    String enumString = value.name();
    writer.writeString(enumString);
  }

  @Override
  public Class<TaskChannel> getEncoderClass() {
    return TaskChannel.class;
  }
}
