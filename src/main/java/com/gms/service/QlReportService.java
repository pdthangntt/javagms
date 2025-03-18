package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.db.QlReport;
import com.gms.repository.QlReportRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author vvthanh
 */
@Service
public class QlReportService extends BaseService implements IBaseService<QlReport, Long> {

    @Autowired
    private QlReportRepository qlReportRepository;

    @Override
    public List<QlReport> findAll() {
        return qlReportRepository.findAll();
    }

    @Override
    public QlReport findOne(Long ID) {
        Optional<QlReport> model = qlReportRepository.findById(ID);
        return model.isPresent() ? model.get() : null;
    }

    public QlReport findOne(String provinceID, String districtID, String wardID, int month, int year) {
        return qlReportRepository.findOne(provinceID, districtID, wardID, month, year);
    }

    public String getLastUpdateTime() {
        QlReport item = qlReportRepository.find();
        if (item == null) {
            return null;
        } else {
            return TextUtils.formatDate(item.getUpdateAt(), "hh:mm dd/MM/yyyy");
        }
    }

    @Override
    public QlReport save(QlReport entity) {
        try {
            Date date = new Date();
            if (entity.getID() == null) {
                entity.setCreateAT(date);
            }
            entity.setUpdateAt(date);
            return qlReportRepository.save(entity);
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            return null;
        }
    }

}
