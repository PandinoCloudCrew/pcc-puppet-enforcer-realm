/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.util;

import lombok.experimental.UtilityClass;
import org.bson.types.ObjectId;

@UtilityClass
public class OId {
  public String string() {
    return ObjectId.get().toHexString();
  }
}
