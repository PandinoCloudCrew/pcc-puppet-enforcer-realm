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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import pcc.puppet.enforcer.keycloak.adapters.http.KeycloakAdminClient;
import pcc.puppet.enforcer.realm.department.adapters.http.DepartmentClient;
import pcc.puppet.enforcer.realm.member.adapters.http.MemberClient;
import pcc.puppet.enforcer.realm.organization.adapters.http.OrganizationClient;
import pcc.puppet.enforcer.realm.passport.adapters.gateway.rest_countries.RestCountriesApiClient;
import pcc.puppet.enforcer.realm.passport.adapters.http.ConsumerPassportClient;
import pcc.puppet.enforcer.vault.adapters.http.VaultSecretsClient;
import reactor.netty.http.client.HttpClient;

@Configuration
@RequiredArgsConstructor
public class HttpClientFactory {

  private final HttpServiceConfiguration serviceConfiguration;

  @Bean
  public HttpClient httpClient() {
    return HttpClient.create().followRedirect(true).compress(true).wiretap(true);
  }

  @Bean
  public KeycloakAdminClient keycloakAdminClient(WebClient.Builder builder, HttpClient httpClient) {
    HttpServiceProxyFactory factory =
        getFactory(builder, httpClient, serviceConfiguration.getProviderKeycloak().getUri());
    return factory.createClient(KeycloakAdminClient.class);
  }

  @Bean
  public DepartmentClient departmentClient(WebClient.Builder builder, HttpClient httpClient) {
    HttpServiceProxyFactory factory =
        getFactory(builder, httpClient, serviceConfiguration.getPccRealmDepartment().getUri());
    return factory.createClient(DepartmentClient.class);
  }

  @Bean
  public OrganizationClient organizationClient(WebClient.Builder builder, HttpClient httpClient) {
    HttpServiceProxyFactory factory =
        getFactory(builder, httpClient, serviceConfiguration.getPccRealmOrganization().getUri());
    return factory.createClient(OrganizationClient.class);
  }

  @Bean
  public MemberClient memberClient(WebClient.Builder builder, HttpClient httpClient) {
    HttpServiceProxyFactory factory =
        getFactory(builder, httpClient, serviceConfiguration.getPccRealmMember().getUri());
    return factory.createClient(MemberClient.class);
  }

  @Bean
  public ConsumerPassportClient consumerPassportClient(
      WebClient.Builder builder, HttpClient httpClient) {
    HttpServiceProxyFactory factory =
        getFactory(builder, httpClient, serviceConfiguration.getPccRealmPassport().getUri());
    return factory.createClient(ConsumerPassportClient.class);
  }

  @Bean
  public VaultSecretsClient vaultSecretsClient(WebClient.Builder builder, HttpClient httpClient) {
    HttpServiceProxyFactory factory =
        getFactory(builder, httpClient, serviceConfiguration.getProviderVault().getUri());
    return factory.createClient(VaultSecretsClient.class);
  }

  @Bean
  public RestCountriesApiClient restCountriesApiClient(
      WebClient.Builder builder, HttpClient httpClient) {
    HttpServiceProxyFactory factory =
        getFactory(builder, httpClient, serviceConfiguration.getProviderRestCountries().getUri());
    return factory.createClient(RestCountriesApiClient.class);
  }

  private static HttpServiceProxyFactory getFactory(
      WebClient.Builder builder, HttpClient httpClient, String url) {
    WebClient webClient =
        builder.baseUrl(url).clientConnector(new ReactorClientHttpConnector(httpClient)).build();
    return HttpServiceProxyFactory.builder()
        .clientAdapter(WebClientAdapter.forClient(webClient))
        .build();
  }
}
