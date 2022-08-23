/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.payload;

import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Serdeable
public class Message {
  private String id;
  private String address;
  private String payload;
  private MessageStatus status;
  private Instant createDate;
  private Instant updateDate;
}
