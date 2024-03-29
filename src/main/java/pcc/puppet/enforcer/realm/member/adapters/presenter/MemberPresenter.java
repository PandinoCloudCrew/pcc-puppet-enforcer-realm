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

package pcc.puppet.enforcer.realm.member.adapters.presenter;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import pcc.puppet.enforcer.realm.common.contact.adapters.presenter.ContactInformationPresenter;

@Data
@Builder
@Jacksonized
public class MemberPresenter {
  @NotNull private String id;
  @NotNull private String organizationId;
  @NotNull private String departmentId;
  @NotNull private String username;
  @NotNull private ContactInformationPresenter contactId;
  @NotNull private String createdAt;
  @NotNull private Integer version;
}
