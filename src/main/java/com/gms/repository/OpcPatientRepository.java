package com.gms.repository;

import com.gms.entity.db.OpcPatientEntity;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvThành
 */
@Repository
public interface OpcPatientRepository extends CrudRepository<OpcPatientEntity, Long> {

    @Override
    public Optional<OpcPatientEntity> findById(Long ID);

    @Query("SELECT e FROM OpcPatientEntity e WHERE e.ID IN :ID")
    public List<OpcPatientEntity> findByIds(@Param("ID") Set<Long> Ids);

    @Query("SELECT e FROM OpcPatientEntity e WHERE e.qrcode = :code")
    public List<OpcPatientEntity> findByQR(@Param("code") String code);
    
    /**
     * pdthang
     * @param Ids
     * @return 
     */
    @Query("SELECT e FROM OpcPatientEntity e WHERE e.ID IN :ID")
    public List<OpcPatientEntity> findAllByIds(@Param("ID") Set<Long> Ids);

    @Override
    public OpcPatientEntity save(OpcPatientEntity model);

}
