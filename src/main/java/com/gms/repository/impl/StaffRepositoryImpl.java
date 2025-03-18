package com.gms.repository.impl;

import com.gms.entity.db.StaffEntity;
import com.gms.repository.StaffRepository;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vvthanh
 */
@Repository
@Transactional
public class StaffRepositoryImpl {

    @PersistenceContext
    private EntityManager entityManager;

    
}
