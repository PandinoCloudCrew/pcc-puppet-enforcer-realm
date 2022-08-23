/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.payload;

import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import pcc.puppet.enforcer.fuimos.task.TaskType;

@Data
@Builder
@Serdeable
public class UserInteraction {

  private String id;
  private String messageId;
  private ObjectId deviceId;
  private String address;
  private String payload;
  private TaskType taskType;
  private Instant createAt;
  private Instant receivedAt;
}
