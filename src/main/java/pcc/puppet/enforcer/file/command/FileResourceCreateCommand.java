/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.file.command;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Data;

@Data
@Introspected
@Serdeable
public class FileResourceCreateCommand {
  private String name;
}
