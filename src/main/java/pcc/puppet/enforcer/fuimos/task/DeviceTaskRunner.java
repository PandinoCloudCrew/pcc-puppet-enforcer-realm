/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.task;

import pcc.puppet.enforcer.fuimos.device.DeviceAction;
import pcc.puppet.enforcer.fuimos.device.repository.UserDevice;
import pcc.puppet.enforcer.fuimos.payload.Message;
import pcc.puppet.enforcer.fuimos.payload.property.InteractionPlan;

public interface DeviceTaskRunner {

  InteractionPlan getType();

  void run(DeviceAction action, UserDevice device, Message message);
}
