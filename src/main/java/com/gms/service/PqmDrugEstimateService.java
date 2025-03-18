package com.gms.service;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmDrugEstimateEntity;
import com.gms.entity.input.PqmDrugSearch;
import com.gms.repository.PqmDrugEstimateRepository;
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
public class PqmDrugEstimateService extends BaseService implements IBaseService<PqmDrugEstimateEntity, Long> {

    @Autowired
    private PqmDrugEstimateRepository pqmDrugEstimateRepository;

    @Override
    public List<PqmDrugEstimateEntity> findAll() {
        return pqmDrugEstimateRepository.findAll();
    }

    @Override
    public PqmDrugEstimateEntity findOne(Long ID) {
        Optional<PqmDrugEstimateEntity> optional = pqmDrugEstimateRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public PqmDrugEstimateEntity save(PqmDrugEstimateEntity model) {
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

    public DataPage<PqmDrugEstimateEntity> find(PqmDrugSearch search) {

        DataPage<PqmDrugEstimateEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"provinceID"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PqmDrugEstimateEntity> pages = pqmDrugEstimateRepository.find(search, pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    public PqmDrugEstimateEntity findByUniqueConstraints(
            int year,
            String provinceID,
            String drugName,
            String siteCode,
            String source) {
        PqmDrugEstimateEntity model = pqmDrugEstimateRepository.findByUniqueConstraints(year, provinceID,
                drugName,
                siteCode,
                source);
        return model;
    }

    public List<PqmDrugEstimateEntity> findUpdateUnit(
            String provinceID,
            String drugName,
            String siteCode,
            int year
    ) {
        List<PqmDrugEstimateEntity> model = pqmDrugEstimateRepository.findUpdateUnit(
                provinceID,
                drugName,
                siteCode, year);
        return model;
    }

    public List<PqmDrugEstimateEntity> findAllByProvinceID(
            String provinceID) {
        List<PqmDrugEstimateEntity> model = pqmDrugEstimateRepository.findAllByProvinceID(provinceID);
        return model == null || model.isEmpty() ? null : model;
    }

}
