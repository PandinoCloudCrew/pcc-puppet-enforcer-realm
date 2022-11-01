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
package pcc.puppet.enforcer.security.password;

import jakarta.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordData;
import org.passay.PasswordGenerator;
import org.passay.PasswordValidator;

@Slf4j
@Singleton
public class PassaySecurePasswordGenerator implements SecurePasswordGenerator {

  public final PasswordGenerator passwordGenerator;
  public final PasswordValidator passwordValidator;
  public final List<CharacterRule> characterRules;

  public PassaySecurePasswordGenerator() {
    this.passwordGenerator = new PasswordGenerator();
    this.characterRules = new ArrayList<>();
    addRules(this.characterRules);
    this.passwordValidator = new PasswordValidator(this.characterRules);
  }

  @Override
  public String password(int length) {
    return passwordGenerator.generatePassword(length, this.characterRules);
  }

  @Override
  public boolean validate(String password) {
    return this.passwordValidator.validate(new PasswordData(password)).isValid();
  }

  private void addRules(List<CharacterRule> rules) {
    CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
    CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
    lowerCaseRule.setNumberOfCharacters(2);
    rules.add(lowerCaseRule);

    CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
    CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
    upperCaseRule.setNumberOfCharacters(2);
    rules.add(upperCaseRule);

    CharacterData digitChars = EnglishCharacterData.Digit;
    CharacterRule digitRule = new CharacterRule(digitChars);
    digitRule.setNumberOfCharacters(2);
    rules.add(digitRule);

    CharacterData specialChars =
        new CharacterData() {
          public String getErrorCode() {
            return "INSUFFICIENT_SPECIAL";
          }

          public String getCharacters() {
            return "!@#$%^&*()_+";
          }
        };
    CharacterRule splCharRule = new CharacterRule(specialChars);
    splCharRule.setNumberOfCharacters(2);
    rules.add(splCharRule);
  }
}
