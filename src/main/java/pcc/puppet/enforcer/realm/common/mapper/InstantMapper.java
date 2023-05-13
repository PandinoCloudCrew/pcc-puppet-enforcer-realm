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

package pcc.puppet.enforcer.realm.common.mapper;

import java.time.Instant;
import java.util.Objects;
import javax.annotation.processing.Generated;
import pcc.puppet.enforcer.realm.common.format.DateFormat;

@Generated(
    value = "pcc.puppet.enforcer.realm.common.mapper.InstantMapper",
    date = "2023-05-12T20:17:02-0500"
)
public class InstantMapper {

  public String asString(Instant timestamp) {
    if (Objects.isNull(timestamp)) return null;
    return DateFormat.FORMATTER.format(timestamp);
  }
}
