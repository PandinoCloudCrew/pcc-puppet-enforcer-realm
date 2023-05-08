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
package pcc.puppet.enforcer.realm.department.ports.event;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import javax.validation.Valid;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import pcc.puppet.enforcer.realm.common.contact.ports.event.CreateContactInformationEvent;

@Data
@Builder
@Jacksonized
public class DepartmentCreateEvent {
  @NotNull private String id;
  @Nullable private String parentId;
  @NotNull private String organizationId;
  @NotNull private String name;
  @NotNull private String location;
  @NotNull private String createdAt;
  @NotNull @Valid private CreateContactInformationEvent contactId;
}
