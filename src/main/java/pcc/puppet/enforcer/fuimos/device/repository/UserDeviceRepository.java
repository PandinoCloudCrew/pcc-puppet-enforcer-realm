/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.fuimos.device.repository;

import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;
import io.micronaut.tracing.annotation.ContinueSpan;
import io.micronaut.tracing.annotation.SpanTag;
import org.bson.types.ObjectId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@MongoRepository
public interface UserDeviceRepository extends ReactorPageableRepository<UserDevice, ObjectId> {

  @ContinueSpan
  Mono<UserDevice> findByAddress(@SpanTag("device.address") String address);

  @ContinueSpan
  Flux<UserDevice> findByAudienceId(@SpanTag("network.audience") String audienceId);
}
