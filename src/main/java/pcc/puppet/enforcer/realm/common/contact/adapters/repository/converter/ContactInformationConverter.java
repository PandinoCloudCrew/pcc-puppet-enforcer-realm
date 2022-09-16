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
package pcc.puppet.enforcer.realm.common.contact.adapters.repository.converter;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.convert.ConversionContext;
import io.micronaut.data.model.runtime.convert.AttributeConverter;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import pcc.puppet.enforcer.realm.common.contact.domain.ContactInformation;

@Singleton
@RequiredArgsConstructor
public class ContactInformationConverter implements AttributeConverter<ContactInformation, String> {

  @Override
  public String convertToPersistedValue(
      ContactInformation entityValue, @NonNull ConversionContext context) {
    return entityValue.getId();
  }

  @SneakyThrows
  @Override
  public ContactInformation convertToEntityValue(
      String persistedValue, @NonNull ConversionContext context) {
    return ContactInformation.builder().id(persistedValue).build();
  }
}
