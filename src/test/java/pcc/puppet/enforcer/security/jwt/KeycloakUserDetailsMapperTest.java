package pcc.puppet.enforcer.security.jwt;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.AuthenticationResponse;
import io.micronaut.security.oauth2.endpoint.authorization.state.DefaultState;
import io.micronaut.security.oauth2.endpoint.token.response.TokenResponse;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

@MicronautTest(transactional = false)
class KeycloakUserDetailsMapperTest {

  @Inject
  private KeycloakUserDetailsMapper userDetailsMapper;

  @Test
  void createAuthenticationResponse() {
    TokenResponse tokenResponse = new TokenResponse();
    DefaultState defaultState = new DefaultState();
    AuthenticationResponse authenticationResponse = Mono.from(
        userDetailsMapper.createAuthenticationResponse(tokenResponse, defaultState)).block();
    Authentication authentication = authenticationResponse.getAuthentication().orElseThrow();
    Assertions.assertEquals(3, authentication.getAttributes().size());
  }
}