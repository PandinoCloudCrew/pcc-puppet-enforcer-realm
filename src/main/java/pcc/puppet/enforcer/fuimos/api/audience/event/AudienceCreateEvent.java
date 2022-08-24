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
package pcc.puppet.enforcer.fuimos.api.audience.event;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import pcc.puppet.enforcer.fuimos.api.audience.presenter.UserDevicePresenter;

@Data
@Builder
@Serdeable
public class AudienceCreateEvent {

  @NonNull private Integer size;
  @NonNull private Optional<String> audienceId;
  @Nullable private List<UserDevicePresenter> devices;
}
