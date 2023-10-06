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

package pcc.puppet.enforcer.app.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@Profile("!test")
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebFluxSecurityConfiguration {

  @Value("${spring.security.oauth2.resourceserver.jwk.issuer-uri}")
  private String issuerUri;
  @Bean
  public ReactiveAuthenticationManager userDetailsService() {
    UserDetails admin = User.builder()
        .username("admin")
        .password("{noop}admin")
        .roles("ADMIN")
        .build();
    return new UserDetailsRepositoryReactiveAuthenticationManager(new MapReactiveUserDetailsService(admin));
  }
  @Bean
  @Order(1)
  SecurityWebFilterChain basicSecurityFilterChain( ServerHttpSecurity http) {
    http.csrf(CsrfSpec::disable)
        .authorizeExchange(
            authorizeExchangeSpec ->
                authorizeExchangeSpec
                    .pathMatchers("/system/**")
                    .hasAnyAuthority("ADMIN")
                    .anyExchange()
                    .authenticated())
        .httpBasic(httpBasicSpec -> httpBasicSpec.authenticationManager(userDetailsService()));
    return http.build();
  }
  @Bean
  @Order(2)
  SecurityWebFilterChain springSecurityFilterChain(
      ServerHttpSecurity http,
      Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter) {
    http.csrf(CsrfSpec::disable)
        .authorizeExchange(
            authorizeExchangeSpec ->
                authorizeExchangeSpec
                    .anyExchange()
                    .authenticated())
        .oauth2ResourceServer(
            auth2ResourceServerSpec ->
                auth2ResourceServerSpec.jwt(
                    jwtSpec -> jwtSpec.jwtAuthenticationConverter(jwtAuthenticationConverter)));
    return http.build();
  }

  @Bean
  public ReactiveJwtDecoder jwtDecoder() {
    return ReactiveJwtDecoders.fromOidcIssuerLocation(issuerUri);
  }
}
