package com.gms.repository;

import com.gms.entity.db.PacLocationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author vvthành
 */
@Repository
public interface PacLocationRepository extends CrudRepository<PacLocationEntity, Long> {

}
