/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.network;

import io.micronaut.serde.annotation.Serdeable;
import java.security.SecureRandom;

@Serdeable
public enum NetworkCarrier {
  CLARO,
  MOVISTAR,
  TIGO,
  VIRGIN,
  AVANTEL;

  public static NetworkCarrier getRandomCarrier() {
    SecureRandom random = new SecureRandom();
    return values()[random.nextInt(values().length)];
  }
}
