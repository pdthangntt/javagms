package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.db.QrCodeEntity;
import com.gms.repository.QrCodeRepository;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 *
 * @author vvthanh
 */
@Service
public class QrCodeService extends BaseService {
    
    @Autowired
    private QrCodeRepository qrCodeRepository;

    /**
     * Mã QR
     *
     * @param code
     * @return
     */
    public String generateCode(String code) {
        Optional<QrCodeEntity> optional = qrCodeRepository.findById(code);
        if (optional == null) {
            getLogger().warn(String.format("Mã %s không tồn tại", code));
            return null;
        }
        QrCodeEntity model = optional.get();
        String key = String.format("%s-%s", model.getID(), model.getSecret());
        return String.format("%s.%s", model.getID(), DigestUtils.md5Hex(key).toLowerCase());
    }

    /**
     * Tạo mã trong db
     *
     * @param idCard
     * @param fullName
     * @param gender
     * @param dob
     * @param race
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String generate(String idCard, String fullName, String gender, Date dob, String race) {
        try {
            if (idCard == null && fullName == null && gender == null && dob == null && race == null) {
                return null;
            }
            QrCodeEntity model;
            Date currentDate = new Date();
            if (idCard == null || "".equals(idCard)) {
                String key = fullName != null ? fullName : "";
                key = gender != null ? key.concat(gender) : key;
                key = dob != null ? key.concat(dob.toString()) : key;
                key = race != null ? key.concat(race) : key;
                String id = DigestUtils.md5Hex(key).toUpperCase();
                Optional<QrCodeEntity> optional = qrCodeRepository.findById(id);
                model = optional.isPresent() ? optional.get() : null;
                if (model == null) {
                    model = new QrCodeEntity();
                    model.setActive(true);
                    model.setID(id);
                    model.setCreateAT(currentDate);
                    model.setIdCard(id);
                }
            } else {
                model = qrCodeRepository.findByIDCard(idCard);
                if (model == null) {
                    model = new QrCodeEntity();
                    model.setActive(true);
                    model.setID(UUID.randomUUID().toString());
                    model.setCreateAT(currentDate);
                    model.setIdCard(idCard);
                }
            }
            if (fullName != null) {
                model.setFullName(TextUtils.toFullname(fullName));
            }
            if (dob != null) {
                model.setDob(dob);
            }
            if (gender != null) {
                model.setGenderID(gender);
            }
            if (race != null) {
                model.setRaceID(race);
            }
            model.setSecret("@imsgms#");
            
            qrCodeRepository.save(model);
            return model.getID();
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            throw new Exception(e.getMessage());
            return null;
        }
    }

    /**
     * @auth vvThành
     * @param code
     * @return
     */
    public QrCodeEntity findOne(String code) {
        Optional<QrCodeEntity> optional = qrCodeRepository.findById(code);
        return optional.isPresent() ? optional.get() : null;
    }
    
}
