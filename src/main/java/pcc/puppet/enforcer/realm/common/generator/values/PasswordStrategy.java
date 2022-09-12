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
package pcc.puppet.enforcer.realm.common.generator.values;

import java.lang.annotation.Annotation;
import java.util.List;
import net.datafaker.Faker;
import uk.co.jemos.podam.common.AttributeStrategy;

public class PasswordStrategy implements AttributeStrategy<String> {
  private final Faker faker = new Faker();

  @Override
  public String getValue(Class<?> attrType, List<Annotation> attrAnnotations) {
    return faker.internet().password();
  }
}
