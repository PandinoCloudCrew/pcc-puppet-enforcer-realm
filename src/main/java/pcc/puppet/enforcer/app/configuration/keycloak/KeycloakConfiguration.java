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
package pcc.puppet.enforcer.app.configuration.keycloak;

import java.util.Collection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import pcc.puppet.enforcer.keycloak.ports.configuration.KeycloakProperties;
import reactor.core.publisher.Mono;

@Configuration
public class KeycloakConfiguration {

  @Bean
  Converter<Jwt, Collection<GrantedAuthority>> keycloakGrantedAuthoritiesConverter(
      KeycloakProperties properties) {
    return new KeycloakGrantedAuthoritiesConverter(properties.getAdminClientId());
  }

  @Bean
  Converter<Jwt, Mono<AbstractAuthenticationToken>> keycloakJwtAuthenticationConverter(
      Converter<Jwt, Collection<GrantedAuthority>> converter) {
    return new ReactiveKeycloakJwtAuthenticationConverter(converter);
  }
}
