package pcc.puppet.enforcer.app.configuration.hints;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;

public class VaultRuntimeHints implements RuntimeHintsRegistrar {
  @Override
  public void registerHints(org.springframework.aot.hint.RuntimeHints hints, ClassLoader classLoader) {
    hints.reflection().registerType(TypeReference.of("org.springframework.cloud.vault.config.VaultHealthIndicator"), builder -> builder.withMembers(
        MemberCategory.INVOKE_DECLARED_CONSTRUCTORS));
  }
}
