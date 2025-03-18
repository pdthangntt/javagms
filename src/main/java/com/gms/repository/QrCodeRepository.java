package com.gms.repository;

import com.gms.entity.db.QrCodeEntity;
import java.util.Optional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvThành
 */
@Repository
public interface QrCodeRepository extends CrudRepository<QrCodeEntity, String> {

    @Query("SELECT count(e)> 0 FROM QrCodeEntity e WHERE e.idCard=:idCard")
    @Cacheable(value = "qr_code_existID")
    public boolean existID(@Param("idCard") String idCard);

    @Query("SELECT e FROM QrCodeEntity e WHERE e.idCard=:idCard")
    @Cacheable(value = "qr_code_findByIDCard")
    public QrCodeEntity findByIDCard(@Param("idCard") String idCard);

    @Override
    @Cacheable(value = "qr_code_findById")
    public Optional<QrCodeEntity> findById(String ID);

    @Override
    @Caching(evict = {
        @CacheEvict(value = "qr_code_findById", allEntries = true)
        ,@CacheEvict(value = "qr_code_existID", allEntries = true)
        ,@CacheEvict(value = "qr_code_findByIDCard", allEntries = true)
    })
    public QrCodeEntity save(QrCodeEntity model);
}
