package com.gms.service;

import com.gms.entity.db.PqmProportionEntity;
import com.gms.repository.PqmProportionRepository;
import java.util.List;
import java.util.Set;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author vvthanh
 */
@Service
public class PqmProportionService extends BaseService implements IBaseService<PqmProportionEntity, Long> {

    @Autowired
    private PqmProportionRepository proportionRepository;

    /**
     * Tìm province
     *
     * @param provinceID
     * @return
     */
    public List<PqmProportionEntity> findByProvince(Set<String> provinceID) {
        return proportionRepository.findByProvince(provinceID);
    }

    @Override
    public List<PqmProportionEntity> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PqmProportionEntity findOne(Long ID) {
        Optional<PqmProportionEntity> optional = proportionRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public PqmProportionEntity save(PqmProportionEntity model) {
        try {
            proportionRepository.save(model);
            return model;
        } catch (Exception e) {
            throw e;
        }
    }
}
