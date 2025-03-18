package com.gms.service;

import com.gms.entity.db.WardEntity;
import com.gms.repository.WardRepository;
import java.util.Optional;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service cho xã phường
 * @author TrangBN
 */
@Service
public class WardService extends BaseService {

    @Autowired
    WardRepository wardRepository;
    
    /**
     * Validate existence for districtId 
     * @param value
     * @param districtID
     * @return 
     */
    public boolean existsWardByID(String value, String districtID) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        return wardRepository.existsByDistrictID(value, districtID);
    }
    
    public WardEntity findWardByID(String ID) {
        Optional<WardEntity> optional = wardRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }
}
