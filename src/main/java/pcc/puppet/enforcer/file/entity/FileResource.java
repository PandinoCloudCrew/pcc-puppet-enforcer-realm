/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.file.entity;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Version;
import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@Serdeable
@Introspected
@MappedEntity(value = "file_resource")
public class FileResource {
  @Id @GeneratedValue private ObjectId id;

  private String name;

  private String createdBy;
  private String updatedBy;
  private Instant createdAt;
  private Instant updatedAt;
  private boolean deleted;
  @Version private int version;
}
