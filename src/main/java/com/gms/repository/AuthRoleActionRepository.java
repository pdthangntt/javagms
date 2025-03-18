package com.gms.repository;

import com.gms.entity.db.AuthRoleActionEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author My PC
 */
@Repository
public interface AuthRoleActionRepository extends CrudRepository<AuthRoleActionEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM auth_role_action WHERE ROLE_ID=:roleID", nativeQuery = true)
    public void removeByRoleID(@Param("roleID") Long roleID);

}
