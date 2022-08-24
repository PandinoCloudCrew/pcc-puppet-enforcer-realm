/*
 * Copyright 2022 Pandino Cloud Crew (C)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
