/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.device.repository;

import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;
import org.bson.types.ObjectId;
import reactor.core.publisher.Mono;

@MongoRepository
public interface UserDeviceRepository extends ReactorPageableRepository<UserDevice, ObjectId> {
  Mono<UserDevice> findByAddress(String address);
}
