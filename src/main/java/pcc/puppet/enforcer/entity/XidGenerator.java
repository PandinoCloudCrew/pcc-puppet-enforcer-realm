/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.entity;

import com.github.shamil.Xid;
import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

@GenericGenerator(name = "sequence_xid", strategy = "pcc.puppet.enforcer.entity.XidGenerator")
public class XidGenerator implements IdentifierGenerator {

  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object object)
      throws HibernateException {
    return Xid.string();
  }
}
