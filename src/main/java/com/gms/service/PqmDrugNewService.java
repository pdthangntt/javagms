package com.gms.service;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmDrugNewEntity;
import com.gms.entity.input.PqmDrugNewSearch;
import com.gms.repository.PqmDrugNewRepository;
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
public class PqmDrugNewService extends BaseService implements IBaseService<PqmDrugNewEntity, Long> {

    @Autowired
    private PqmDrugNewRepository pqmDrugNewRepository;

    @Override
    public List<PqmDrugNewEntity> findAll() {
        return pqmDrugNewRepository.findAll();
    }

    @Override
    public PqmDrugNewEntity findOne(Long ID) {
        Optional<PqmDrugNewEntity> optional = pqmDrugNewRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public PqmDrugNewEntity save(PqmDrugNewEntity model) {
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
    
    public void deleteByID(Long ID) {
        pqmDrugNewRepository.deleteById(ID);
    }

    public DataPage<PqmDrugNewEntity> find(PqmDrugNewSearch search) {

        DataPage<PqmDrugNewEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"provinceID"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PqmDrugNewEntity> pages = pqmDrugNewRepository.find(search, pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    public PqmDrugNewEntity findByUniqueConstraints(
            String siteID,
            int year,
            int quarter,
            String provinceID,
            String drugName,
            String source) {
        PqmDrugNewEntity model = pqmDrugNewRepository.findByUniqueConstraints(
                Long.valueOf(siteID),
                year,
                quarter,
                provinceID,
                drugName,
                source);
        return model;
    }

    public List<PqmDrugNewEntity> findAllByProvinceID(
            String provinceID) {
        List<PqmDrugNewEntity> model = pqmDrugNewRepository.findAllByProvinceID(provinceID);
        return model == null || model.isEmpty() ? null : model;
    }

}
