/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.input;

import io.micronaut.serde.annotation.Serdeable;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Serdeable
public class AudienceCreateCommand {
  private int size;
  private Optional<String> audienceId;
}
