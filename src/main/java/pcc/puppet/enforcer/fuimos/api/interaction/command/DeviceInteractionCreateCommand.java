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
package pcc.puppet.enforcer.fuimos.api.interaction.command;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;
import pcc.puppet.enforcer.fuimos.payload.property.InteractionChannel;
import pcc.puppet.enforcer.fuimos.payload.property.InteractionDirection;
import pcc.puppet.enforcer.fuimos.payload.property.InteractionPlan;
import pcc.puppet.enforcer.fuimos.payload.property.InteractionSource;

@Data
@Builder
@Serdeable
public class DeviceInteractionCreateCommand {
  @NonNull private String id;
  @NonNull private String messageId;
  @NonNull private String deviceId;
  @NonNull private String address;
  @Nullable private String payload;
  @NonNull private InteractionPlan interactionPlan;
  @NonNull private InteractionChannel channel;
  @NonNull private InteractionSource source;
  @NonNull private InteractionDirection direction;
  @Nullable private Instant createAt;
  @Nullable private Instant receivedAt;
}
