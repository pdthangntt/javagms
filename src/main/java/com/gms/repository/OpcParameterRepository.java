package com.gms.repository;

import com.gms.entity.db.OpcParameterEntity;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vvThành
 */
@Repository
public interface OpcParameterRepository extends CrudRepository<OpcParameterEntity, Long> {

    @Override
    public OpcParameterEntity save(OpcParameterEntity model);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM opc_parameter WHERE TYPE=:type AND OBJECT_ID =:objectID AND ATTR_NAME =:attrName", nativeQuery = true)
    public void removeByObjectID(@Param("type") String type, @Param("attrName") String attrName, @Param("objectID") Long objectID);

    @Query("SELECT e FROM OpcParameterEntity e WHERE e.type=:type AND e.objectID IN (:objectID)")
    public List<OpcParameterEntity> findByTypeAndObjectID(@Param("type") String type, @Param("objectID") Set<Long> objectIDs);

}
