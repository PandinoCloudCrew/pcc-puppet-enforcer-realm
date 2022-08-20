/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import java.util.Collections;
import java.util.Map;

@Controller("/gru")
@Secured(SecurityRule.IS_ANONYMOUS)
public class HelloWorldGruController {

  @Get
  public Map<String, Object> index() {
    return Collections.singletonMap("message", "Hello World");
  }
}
