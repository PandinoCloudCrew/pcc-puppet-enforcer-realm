/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.device.repository.codec;

import jakarta.inject.Singleton;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import pcc.puppet.enforcer.fuimos.task.TaskType;

@Singleton
public class TaskTypeCodec implements Codec<TaskType> {

  @Override
  public TaskType decode(BsonReader reader, DecoderContext decoderContext) {
    String enumString = reader.readString();
    return TaskType.valueOf(enumString);
  }

  @Override
  public void encode(BsonWriter writer, TaskType value, EncoderContext encoderContext) {
    String enumString = value.name();
    writer.writeString(enumString);
  }

  @Override
  public Class<TaskType> getEncoderClass() {
    return TaskType.class;
  }
}
