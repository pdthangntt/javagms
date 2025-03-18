package com.gms.repository;

import com.gms.entity.db.AuthRoleServiceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author My PC
 */
@Repository
public interface AuthRoleServiceRepository extends CrudRepository<AuthRoleServiceEntity, Long> {

    public void removeByRoleID(Long id);

}
