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

package pcc.puppet.enforcer.realm.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import pcc.puppet.enforcer.keycloak.adapters.http.KeycloakAdminClient;
import pcc.puppet.enforcer.realm.department.adapters.http.DepartmentClient;
import pcc.puppet.enforcer.realm.member.adapters.http.MemberClient;
import pcc.puppet.enforcer.realm.organization.adapters.http.OrganizationClient;
import pcc.puppet.enforcer.realm.passport.adapters.gateway.rest_countries.RestCountriesApiClient;
import pcc.puppet.enforcer.realm.passport.adapters.http.ConsumerPassportClient;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class HttpClientFactory {

  private final HttpServiceConfiguration serviceConfiguration;

  public HttpClient httpClient(String baseUrl) {
    return HttpClient.create().baseUrl(baseUrl).followRedirect(true).compress(true).wiretap(true);
  }

  @Bean
  public KeycloakAdminClient keycloakAdminClient(Builder builder) {
    String baseUrl = serviceConfiguration.getProviderKeycloak().getUri();
    log.info("created KeycloakAdminClient with host: " + baseUrl);
    HttpServiceProxyFactory factory = getFactory(builder, httpClient(baseUrl), baseUrl);
    return factory.createClient(KeycloakAdminClient.class);
  }

  @Bean
  public DepartmentClient departmentClient(Builder builder) {
    String baseUrl = serviceConfiguration.getPccRealmDepartment().getUri();
    log.info("created DepartmentClient with host: " + baseUrl);
    HttpServiceProxyFactory factory = getFactory(builder, httpClient(baseUrl), baseUrl);
    return factory.createClient(DepartmentClient.class);
  }

  @Bean
  public OrganizationClient organizationClient(Builder builder) {
    String baseUrl = serviceConfiguration.getPccRealmOrganization().getUri();
    log.info("created OrganizationClient with host: " + baseUrl);
    HttpServiceProxyFactory factory = getFactory(builder, httpClient(baseUrl), baseUrl);
    return factory.createClient(OrganizationClient.class);
  }

  @Bean
  public MemberClient memberClient(Builder builder) {
    String baseUrl = serviceConfiguration.getPccRealmMember().getUri();
    log.info("created MemberClient with host: " + baseUrl);
    HttpServiceProxyFactory factory = getFactory(builder, httpClient(baseUrl), baseUrl);
    return factory.createClient(MemberClient.class);
  }

  @Bean
  public ConsumerPassportClient consumerPassportClient(Builder builder) {
    String baseUrl = serviceConfiguration.getPccRealmPassport().getUri();
    log.info("created ConsumerPassportClient with host: " + baseUrl);
    HttpServiceProxyFactory factory = getFactory(builder, httpClient(baseUrl), baseUrl);
    return factory.createClient(ConsumerPassportClient.class);
  }

  @Bean
  public RestCountriesApiClient restCountriesApiClient(Builder builder) {
    String baseUrl = serviceConfiguration.getProviderRestCountries().getUri();
    log.info("created RestCountriesApiClient with host: " + baseUrl);
    HttpServiceProxyFactory factory = getFactory(builder, httpClient(baseUrl), baseUrl);
    return factory.createClient(RestCountriesApiClient.class);
  }

  private static HttpServiceProxyFactory getFactory(
      Builder builder, HttpClient httpClient, String url) {
    WebClient webClient =
        builder.baseUrl(url).clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    return HttpServiceProxyFactory.builder()
        .clientAdapter(WebClientAdapter.forClient(webClient))
        .build();
  }
}
