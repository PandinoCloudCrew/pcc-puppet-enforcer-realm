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
package pcc.puppet.enforcer.realm.keycloak.domain;

import io.micronaut.core.annotation.Introspected;
import java.util.ArrayList;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Introspected
@NoArgsConstructor
public class KeycloakTokenDetails {

  private int exp;
  private int iat;
  private String jti;
  private String iss;
  private ArrayList<String> aud;
  private String sub;
  private String typ;
  private String azp;
  private String preferred_username;
  private boolean email_verified;
  private String acr;
  private RolesArray realm_access;
  private Map<String, RolesArray> resource_access;
  private String scope;
  private String clientId;
  private String clientHost;
  private String clientAddress;
  private String client_id;
  private String username;
  private boolean active;
}