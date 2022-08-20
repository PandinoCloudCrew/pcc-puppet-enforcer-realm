/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.file;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Introspected
@Serdeable
@Table(name = "file_resource")
public class FileResource {
  @Id
  @GeneratedValue(generator = "sequence_xid")
  private String id;

  private String name;
}
