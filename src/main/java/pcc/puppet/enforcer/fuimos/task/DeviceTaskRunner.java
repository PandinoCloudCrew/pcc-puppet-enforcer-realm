/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.task;

import pcc.puppet.enforcer.fuimos.device.UserDevicePresenter;
import pcc.puppet.enforcer.fuimos.payload.Message;

public interface DeviceTaskRunner {
  void run(UserDevicePresenter device, Message message);
}
