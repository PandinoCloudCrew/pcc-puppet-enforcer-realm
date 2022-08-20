/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer;

import com.agorapulse.gru.Gru;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

@MicronautTest
class HelloWorldGruControllerTest {

  @Inject Gru gru;

  @Test
  void testGet() throws Throwable {
    gru.verify(test -> test.get("/gru").expect(response -> response.json("gruIndex.json")));
  }
}
