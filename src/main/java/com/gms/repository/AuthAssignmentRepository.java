package com.gms.repository;

import com.gms.entity.db.AuthAssignmentEntity;
import java.util.List;
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
public interface AuthAssignmentRepository extends CrudRepository<AuthAssignmentEntity, Long> {

    public List<AuthAssignmentEntity> findByUserID(Long userID);

    public AuthAssignmentEntity findByUserIDAndRoleID(Long userID, Long roleID);

    public AuthAssignmentEntity findByRoleID(Long roleID);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM auth_assignment WHERE ROLE_ID=:roleID", nativeQuery = true)
    public void removeByRoleID(@Param("roleID") Long roleID);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM auth_assignment WHERE USER_ID=:userID", nativeQuery = true)
    public void removeByUserID(@Param("userID") Long userID);

}
