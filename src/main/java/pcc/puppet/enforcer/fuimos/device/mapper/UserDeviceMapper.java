/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.device.mapper;

import org.mapstruct.Mapper;
import pcc.puppet.enforcer.fuimos.api.output.presenter.UserDevicePresenter;
import pcc.puppet.enforcer.fuimos.device.repository.UserDevice;

@Mapper
public interface UserDeviceMapper {

  UserDevice presenterToDomain(UserDevicePresenter presenter);

  UserDevicePresenter domainToPresenter(UserDevice entity);
}
