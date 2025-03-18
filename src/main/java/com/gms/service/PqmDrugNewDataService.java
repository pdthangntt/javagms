package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmDrugNewDataEntity;
import com.gms.entity.input.PqmDrugNewSearch;
import com.gms.repository.PqmDrugNewDataRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang.StringUtils;
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
public class PqmDrugNewDataService extends BaseService implements IBaseService<PqmDrugNewDataEntity, Long> {

    @Autowired
    private PqmDrugNewDataRepository pqmDrugNewRepository;

    @Override
    public List<PqmDrugNewDataEntity> findAll() {
        return pqmDrugNewRepository.findAll();
    }

    @Override
    public PqmDrugNewDataEntity findOne(Long ID) {
        Optional<PqmDrugNewDataEntity> optional = pqmDrugNewRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public PqmDrugNewDataEntity save(PqmDrugNewDataEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
            }
            model.setUpdateAt(currentDate);
            pqmDrugNewRepository.save(model);
            return model;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteBySiteIDAndMonthAndYear(Long siteID, int quarter, int year) {
        pqmDrugNewRepository.deleteBySiteIDAndMonthAndYear(siteID, quarter, year);
    }

    public void deleteByProvinceIDAndMonthAndYear(String provinceID, int quarter, int year) {
        pqmDrugNewRepository.deleteByProvinceIDAndMonthAndYear(provinceID, quarter, year);
    }

    public void deleteByID(Long ID) {
        pqmDrugNewRepository.deleteById(ID);
    }

    public DataPage<PqmDrugNewDataEntity> find(PqmDrugNewSearch search) {

        DataPage<PqmDrugNewDataEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"siteID"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PqmDrugNewDataEntity> pages = pqmDrugNewRepository.find(search, pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    public PqmDrugNewDataEntity findByUniqueConstraints(
            String siteID,
            int year,
            int quarter,
            String provinceID,
            String drugName,
            String source) {
        PqmDrugNewDataEntity model = pqmDrugNewRepository.findByUniqueConstraints(
                Long.valueOf(siteID),
                year,
                quarter,
                provinceID,
                drugName,
                source);
        return model;
    }

    public List<PqmDrugNewDataEntity> findByIndex(
            String siteID,
            int year,
            int quarter,
            String provinceID) {
        List<PqmDrugNewDataEntity> model = pqmDrugNewRepository.findByIndex(
                StringUtils.isEmpty(siteID) ? null : Long.valueOf(siteID),
                year,
                quarter,
                provinceID);
        return model;
    }
    
    public List<PqmDrugNewDataEntity> findByExport(
            int year,
            int quarter,
            String provinceID) {
        List<PqmDrugNewDataEntity> model = pqmDrugNewRepository.findByExport(
                year,
                quarter,
                provinceID);
        return model;
    }

    public List<PqmDrugNewDataEntity> findAllByProvinceID(
            String provinceID) {
        List<PqmDrugNewDataEntity> model = pqmDrugNewRepository.findAllByProvinceID(provinceID);
        return model == null || model.isEmpty() ? null : model;
    }

}
