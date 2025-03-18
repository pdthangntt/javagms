package com.gms.service;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmDrugPlanEntity;
import com.gms.entity.input.PqmDrugSearch;
import com.gms.repository.PqmDrugPlanRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author pdThang
 */
@Service
public class PqmDrugPlanService extends BaseService implements IBaseService<PqmDrugPlanEntity, Long> {

    @Autowired
    private PqmDrugPlanRepository pqmDrugPlanRepository;

    @Override
    public List<PqmDrugPlanEntity> findAll() {
        return pqmDrugPlanRepository.findAll();
    }

    @Override
    public PqmDrugPlanEntity findOne(Long ID) {
        Optional<PqmDrugPlanEntity> optional = pqmDrugPlanRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public PqmDrugPlanEntity save(PqmDrugPlanEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
            }
            model.setUpdateAt(currentDate);
            pqmDrugPlanRepository.save(model);
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public DataPage<PqmDrugPlanEntity> find(PqmDrugSearch search) {

        DataPage<PqmDrugPlanEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"provinceID"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PqmDrugPlanEntity> pages = pqmDrugPlanRepository.find(search, pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    public PqmDrugPlanEntity findByUniqueConstraints(int month,
            int year,
            String provinceID,
            String drugName,
            String siteCode,
            String source,
            String lotNumber,
            String unit
    ) {
        PqmDrugPlanEntity model = pqmDrugPlanRepository.findByUniqueConstraints(month, year, provinceID, drugName, siteCode, source, lotNumber, unit);
        return model;
    }

    public List<PqmDrugPlanEntity> findUpdateUnitToEstimate(
            String provinceID,
            String drugName,
            String siteCode,
            int year
    ) {
        return pqmDrugPlanRepository.findUpdateUnitToEstimate(provinceID, drugName, siteCode, year);
    }

    public List<PqmDrugPlanEntity> findAllByProvinceID(
            String provinceID) {
        List<PqmDrugPlanEntity> model = pqmDrugPlanRepository.findAllByProvinceID(provinceID);
        return model == null || model.isEmpty() ? null : model;
    }

}
