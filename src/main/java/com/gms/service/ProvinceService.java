package com.gms.service;

import com.gms.repository.ProvinceRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service cho tỉnh
 * @author TrangBN
 */
@Service
public class ProvinceService extends BaseService {

    @Autowired
    ProvinceRepository provinceRepository;
    
    /**
     * Validate existence for provinceId 
     * @param value
     * @return 
     */
    public boolean existsProvinceByID(String value) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        return provinceRepository.existsByID(value);
    }
    
    
}
