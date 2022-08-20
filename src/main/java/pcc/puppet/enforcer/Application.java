/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer;

import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.context.ApplicationContextConfigurer;
import io.micronaut.context.annotation.ContextConfigurer;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "files", version = "0.0"))
public class Application {
  /**
   * Entry point for app
   *
   * @param args program arguments
   */
  public static void main(final String[] args) {
    Micronaut.run(Application.class, args);
  }

  @ContextConfigurer
  public static class DefaultEnvironmentConfigurer implements ApplicationContextConfigurer {
    @Override
    public void configure(@NonNull final ApplicationContextBuilder builder) {
      builder.defaultEnvironments("dev");
    }
  }
}
