package com.gms.service;

import com.gms.entity.db.DistrictEntity;
import com.gms.repository.DistrictRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service cho huyện
 * @author TrangBN
 */
@Service
public class DistrictService extends BaseService {

    @Autowired
    DistrictRepository districtRepository;
    
    /**
     * Validate existence for districtId 
     * @param value
     * @param provinceID
     * @return 
     */
    public boolean existsDistrictByID(String value, String provinceID) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        return districtRepository.existsByProvinceID(value, provinceID);
    }
    
    /**
     * Lấy danh sách các huyện ra map
     * 
     * @param ids
     * @return 
     */
    public HashMap<String, DistrictEntity> findByIDs(Set<String> ids){
        
        if (ids.isEmpty()) {
            return null;
        }
        
        List<DistrictEntity> findByIDs = districtRepository.findByIDs(ids);
        HashMap<String, DistrictEntity> map = new HashMap<>();
        
        if (findByIDs.isEmpty()) {
            return null;
        }
        
        for (DistrictEntity entity : findByIDs) {
            map.put(entity.getID(), entity);
        }
        return map;
    }
    
     public DistrictEntity findDistrictByID(String ID) {
         Optional<DistrictEntity> optional = districtRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }
    
    
    
}
