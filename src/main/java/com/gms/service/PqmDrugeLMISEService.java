package com.gms.service;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmDrugeLMISEEntity;
import com.gms.entity.input.PqmDrugeLMISESearch;
import com.gms.repository.PqmDrugeLMISERepository;
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
public class PqmDrugeLMISEService extends BaseService implements IBaseService<PqmDrugeLMISEEntity, Long> {

    @Autowired
    private PqmDrugeLMISERepository pqmDrugEstimateRepository;

    @Override
    public List<PqmDrugeLMISEEntity> findAll() {
        return pqmDrugEstimateRepository.findAll();
    }

    @Override
    public PqmDrugeLMISEEntity findOne(Long ID) {
        Optional<PqmDrugeLMISEEntity> optional = pqmDrugEstimateRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }
    
     public void deleteById(Long ID) {
        pqmDrugEstimateRepository.deleteById(ID);
    }

    @Override
    public PqmDrugeLMISEEntity save(PqmDrugeLMISEEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
            }
            model.setUpdateAt(currentDate);
            pqmDrugEstimateRepository.save(model);
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public DataPage<PqmDrugeLMISEEntity> find(PqmDrugeLMISESearch search) {

        DataPage<PqmDrugeLMISEEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"provinceID"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PqmDrugeLMISEEntity> pages = pqmDrugEstimateRepository.find(search, pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    public PqmDrugeLMISEEntity findByUniqueConstraints(
            int year,
            int quarter,
            String provinceID,
            String siteCode,
            String drugName,
            String drugCode,
            String ttThau
    ) {
        PqmDrugeLMISEEntity model = pqmDrugEstimateRepository.findByUniqueConstraints(
                year,
                quarter,
                provinceID,
                siteCode,
                drugName,
                drugCode,
                ttThau);
        return model;
    }

    public List<PqmDrugeLMISEEntity> findAllByProvinceID(
            String provinceID) {
        List<PqmDrugeLMISEEntity> model = pqmDrugEstimateRepository.findAllByProvinceID(provinceID);
        return model == null || model.isEmpty() ? null : model;
    }

}
