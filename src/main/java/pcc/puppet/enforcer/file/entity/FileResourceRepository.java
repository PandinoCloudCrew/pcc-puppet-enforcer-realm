/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.file.entity;

import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;
import org.bson.types.ObjectId;

@MongoRepository
public interface FileResourceRepository extends ReactorPageableRepository<FileResource, ObjectId> {}
