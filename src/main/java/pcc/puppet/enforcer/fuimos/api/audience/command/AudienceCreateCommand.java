/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.api.audience.command;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Serdeable
public class AudienceCreateCommand {
  @NonNull private Integer size;
  @Nullable private String audienceId;
}
