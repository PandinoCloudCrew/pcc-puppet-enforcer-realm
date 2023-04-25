package pcc.puppet.enforcer.realm.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import pcc.puppet.enforcer.keycloak.adapters.http.KeycloakAdminClient;
import pcc.puppet.enforcer.realm.common.adapters.http.RealmClient;
import pcc.puppet.enforcer.realm.department.adapters.http.DepartmentClient;
import pcc.puppet.enforcer.realm.member.adapters.http.MemberClient;
import pcc.puppet.enforcer.realm.organization.adapters.http.OrganizationClient;
import pcc.puppet.enforcer.realm.passport.adapters.gateway.rest_countries.RestCountriesApiClient;
import pcc.puppet.enforcer.realm.passport.adapters.http.ConsumerPassportClient;
import pcc.puppet.enforcer.vault.adapters.http.VaultSecretsClient;

@Configuration
public class HttpClientFactory {

  @Bean
  public KeycloakAdminClient keycloakAdminClient(WebClient.Builder builder) {
    HttpServiceProxyFactory factory = getFactory(builder);
    return factory.createClient(KeycloakAdminClient.class);
  }

  @Bean
  public DepartmentClient departmentClient(WebClient.Builder builder) {
    HttpServiceProxyFactory factory = getFactory(builder);
    return factory.createClient(DepartmentClient.class);
  }

  @Bean
  public OrganizationClient organizationClient(WebClient.Builder builder) {
    HttpServiceProxyFactory factory = getFactory(builder);
    return factory.createClient(OrganizationClient.class);
  }

  @Bean
  public MemberClient memberClient(WebClient.Builder builder) {
    HttpServiceProxyFactory factory = getFactory(builder);
    return factory.createClient(MemberClient.class);
  }

  @Bean
  public ConsumerPassportClient consumerPassportClient(WebClient.Builder builder) {
    HttpServiceProxyFactory factory = getFactory(builder);
    return factory.createClient(ConsumerPassportClient.class);
  }

  @Bean
  public VaultSecretsClient vaultSecretsClient(WebClient.Builder builder) {
    HttpServiceProxyFactory factory = getFactory(builder);
    return factory.createClient(VaultSecretsClient.class);
  }

  @Bean
  public RealmClient realmClient(WebClient.Builder builder) {
    HttpServiceProxyFactory factory = getFactory(builder);
    return factory.createClient(RealmClient.class);
  }

  @Bean
  public RestCountriesApiClient restCountriesApiClient(WebClient.Builder builder) {
    HttpServiceProxyFactory factory = getFactory(builder);
    return factory.createClient(RestCountriesApiClient.class);
  }

  private static HttpServiceProxyFactory getFactory(Builder builder) {
    WebClient webClient = builder.build();
    return HttpServiceProxyFactory.builder().clientAdapter(
        WebClientAdapter.forClient(webClient)).build();
  }
}
