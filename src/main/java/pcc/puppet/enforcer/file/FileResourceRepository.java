/* Pandino Cloud Crew (C) 2022 */
package pcc.puppet.enforcer.file;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;

@Repository
public interface FileResourceRepository extends ReactorPageableRepository<FileResource, String> {}
