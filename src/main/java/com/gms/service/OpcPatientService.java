package com.gms.service;

import com.gms.entity.db.OpcPatientEntity;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author vvthanh
 */
@Service
public class OpcPatientService extends OpcBaseService {

    /**
     * @auth vvThành
     * @param code
     * @return
     */
    public OpcPatientEntity findByQR(String code) {
        List<OpcPatientEntity> items = patientRepository.findByQR(code);
        return items == null || items.isEmpty() ? null : items.get(0);
    }
}
