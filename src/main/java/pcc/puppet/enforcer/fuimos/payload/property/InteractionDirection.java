/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.payload.property;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public enum InteractionDirection {
  INPUT,
  OUTPUT
}
