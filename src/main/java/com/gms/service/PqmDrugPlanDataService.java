package com.gms.service;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmDrugPlanDataEntity;
import com.gms.entity.input.PqmDrugSearch;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.gms.repository.PqmDrugPlanDataRepository;
import java.util.Set;

/**
 *
 * @author pdThang
 */
@Service
public class PqmDrugPlanDataService extends BaseService implements IBaseService<PqmDrugPlanDataEntity, Long> {

    @Autowired
    private PqmDrugPlanDataRepository pqmDrugDataRepository;

    @Override
    public List<PqmDrugPlanDataEntity> findAll() {
        return pqmDrugDataRepository.findAll();
    }

    @Override
    public PqmDrugPlanDataEntity findOne(Long ID) {
        Optional<PqmDrugPlanDataEntity> optional = pqmDrugDataRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public PqmDrugPlanDataEntity save(PqmDrugPlanDataEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
            }
            model.setUpdateAt(currentDate);
            pqmDrugDataRepository.save(model);
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public DataPage<PqmDrugPlanDataEntity> find(PqmDrugSearch search) {

        DataPage<PqmDrugPlanDataEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"provinceID"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PqmDrugPlanDataEntity> pages = pqmDrugDataRepository.find(search, pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    public PqmDrugPlanDataEntity findByUniqueConstraints(int month,
            int year,
            String provinceID,
            String siteCode,
            String drugName,
            String unit
    ) {
        PqmDrugPlanDataEntity model = pqmDrugDataRepository.findByUniqueConstraints(month, year, provinceID, siteCode, drugName, unit);
        return model;
    }

    public List<PqmDrugPlanDataEntity> findByEx(int month,
            int year,
            String provinceID
    ) {
           List<PqmDrugPlanDataEntity> model = pqmDrugDataRepository.findByEx(month, year, provinceID);
        return model;
    }

    public void resetDataProvince(
            int month,
            int year,
            String provinceID
    ) {
        pqmDrugDataRepository.resetDataProvince(
                month,
                year,
                provinceID);
    }

    public List<PqmDrugPlanDataEntity> getDataDetail(
            int month,
            int year,
            String provinceID
    ) {
        return pqmDrugDataRepository.getDataDetail(
                month,
                year,
                provinceID);
    }

}
