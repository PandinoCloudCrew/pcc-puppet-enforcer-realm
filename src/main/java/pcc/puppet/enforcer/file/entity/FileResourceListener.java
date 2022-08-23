/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.file.entity;

import io.micronaut.context.annotation.Factory;
import io.micronaut.data.event.listeners.PrePersistEventListener;
import io.micronaut.data.event.listeners.PreUpdateEventListener;
import jakarta.inject.Singleton;
import java.time.Instant;

@Factory
public class FileResourceListener {
  @Singleton
  PrePersistEventListener<FileResource> fileResourcePrePersistEventListener() {
    return fileResource -> {
      fileResource.setCreatedAt(Instant.now());
      fileResource.setCreatedBy("joseybv");
      return true;
    };
  }

  @Singleton
  PreUpdateEventListener<FileResource> fileResourcePreUpdateEventListener() {
    return fileResource -> {
      fileResource.setUpdatedAt(Instant.now());
      fileResource.setUpdatedBy("joseybv");
      return true;
    };
  }
}
