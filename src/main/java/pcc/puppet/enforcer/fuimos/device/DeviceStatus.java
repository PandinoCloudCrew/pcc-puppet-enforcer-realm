/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.device;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public enum DeviceStatus {
  ACTIVE,
  INACTIVE,
  BLOCKED
}
