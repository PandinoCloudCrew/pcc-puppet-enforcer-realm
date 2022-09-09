/*
 * Copyright 2022 Pandino Cloud Crew (C)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pcc.puppet.enforcer.realm.member.repository;

import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;
import pcc.puppet.enforcer.realm.member.domain.Member;
import reactor.core.publisher.Flux;

@R2dbcRepository
public interface MemberRepository extends ReactorPageableRepository<Member, String> {

  Flux<Member> findByOrganizationId(String organizationId);

  Flux<Member> findByDepartmentId(String departmentId);

  Flux<Member> findByUsername(String username);
}
