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
package pcc.puppet.enforcer.realm.passport.adapters.gateway.rest_countries.response;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.Nullable;
import java.util.List;
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
public class RestCountriesResponse {

  @Nullable private CountryName name;
  @Nullable private List<String> tld;
  private String cca2;
  @Nullable private String ccn3;
  @Nullable private String cca3;
  @Nullable private String cioc;
  @Nullable private Boolean independent;
  @Nullable private String status;
  @Nullable private Boolean unMember;
  @Nullable private Map<String, CountryCurrency> currencies;
  @Nullable private CountryIdd idd;
  @Nullable private List<String> capital;
  @Nullable private List<String> altSpellings;
  @Nullable private String region;
  @Nullable private String subregion;
  @Nullable private Map<String, String> languages;
  @Nullable private Map<String, CountryNameTranslation> translations;
  @Nullable private List<String> latlng;
  @Nullable private Boolean landlocked;
  @Nullable private List<String> borders;
  @Nullable private String area;
  @Nullable private Map<String, DemonymGender> demonyms;
  @Nullable private String flag;
  @Nullable private CountryOnlineMaps maps;
  @Nullable private String population;
  @Nullable private Map<String, String> gini;
  @Nullable private String fifa;
  @Nullable private CountryCarInfo car;
  @Nullable private List<String> timezones;
  @Nullable private List<String> continents;
  @Nullable private CountryOnlineImage flags;
  @Nullable private CountryOnlineImage coatOfArms;
  @Nullable private String startOfWeek;
  @Nullable private CountryCapitalInfo capitalInfo;
  @Nullable private CountryPostalCode postalCode;
}
