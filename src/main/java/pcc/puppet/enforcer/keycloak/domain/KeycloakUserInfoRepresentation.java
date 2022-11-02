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
package pcc.puppet.enforcer.keycloak.domain;

import io.micronaut.core.annotation.Introspected;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Introspected
@NoArgsConstructor
@AllArgsConstructor
public class KeycloakUserInfoRepresentation {
  private KeycloakAddressClaimSetRepresentation address;
  private Map<String, Object> otherClaims;
  private String claimsLocales;
  private String birthdate;
  private String email;
  private boolean emailVerified;
  private String familyName;
  private String middleName;
  private String givenName;
  private String nickName;
  private String gender;
  private String phoneNumber;
  private boolean phoneNumberVerified;
  private String locale;
  private String zoneinfo;
  private String picture;
  private String profile;
  private String website;
  private String[] audience;

  public Map<String, Object> asAttributes() {
    HashMap<String, Object> attributes = new HashMap<>();
    attributes.put("address", getAddress());
    attributes.put("otherClaims", getOtherClaims());
    attributes.put("claimsLocales", getClaimsLocales());
    attributes.put("birthdate", getBirthdate());
    attributes.put("email", getEmail());
    attributes.put("emailVerified", isEmailVerified());
    attributes.put("familyName", getFamilyName());
    attributes.put("middleName", getMiddleName());
    attributes.put("givenName", getGivenName());
    attributes.put("nickName", getNickName());
    attributes.put("gender", getGender());
    attributes.put("phoneNumber", getPhoneNumber());
    attributes.put("phoneNumberVerified", isPhoneNumberVerified());
    attributes.put("locale", getLocale());
    attributes.put("zoneinfo", getZoneinfo());
    attributes.put("picture", getPicture());
    attributes.put("profile", getProfile());
    attributes.put("website", getWebsite());
    attributes.put("audience", getAudience());
    return attributes;
  }
}
