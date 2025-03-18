package com.gms.service;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmDrugeLMISEDataEntity;
import com.gms.entity.db.PqmDrugeLMISEDataEntity;
import com.gms.entity.input.PqmDrugeLMISEDataSearch;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.gms.repository.PqmDrugEstimateDataRepository;
import com.gms.repository.PqmDrugeLMISEDataRepository;
import com.gms.repository.impl.PqmDrugEstimateImpl;
import java.util.ArrayList;

/**
 *
 * @author pdThang
 */
@Service
public class PqmDrugeLMISEDataService extends BaseService implements IBaseService<PqmDrugeLMISEDataEntity, Long> {

    @Autowired
    private PqmDrugeLMISEDataRepository pqmDrugDataRepository;
//    @Autowired
//    private PqmDrugEstimateImpl pqmDrugEstimateImpl;

    @Override
    public List<PqmDrugeLMISEDataEntity> findAll() {
        return pqmDrugDataRepository.findAll();
    }

    @Override
    public PqmDrugeLMISEDataEntity findOne(Long ID) {
        Optional<PqmDrugeLMISEDataEntity> optional = pqmDrugDataRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public PqmDrugeLMISEDataEntity save(PqmDrugeLMISEDataEntity model) {
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

    public DataPage<PqmDrugeLMISEDataEntity> find(PqmDrugeLMISEDataSearch search) {

        DataPage<PqmDrugeLMISEDataEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"provinceID"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PqmDrugeLMISEDataEntity> pages = pqmDrugDataRepository.find(search, pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    public PqmDrugeLMISEDataEntity findByUniqueConstraints(
            int quarter,
            int year,
            String provinceID,
            String drugName,
            Long siteID
    ) {
        PqmDrugeLMISEDataEntity model = pqmDrugDataRepository.findByUniqueConstraints(
                quarter,
                year,
                provinceID,
                drugName,
                siteID
        );
        return model;
    }

    public List<PqmDrugeLMISEDataEntity> findData(
            int quarter,
            int year,
            String provinceCode
    ) {
        List<PqmDrugeLMISEDataEntity> model = pqmDrugDataRepository.findData(
                quarter,
                year,
                provinceCode);
        return model == null || model.isEmpty() ? null : model;
    }

    public void resetDataProvince(
            int quarter,
            int year,
            String provinceCode
    ) {
        pqmDrugDataRepository.resetDataProvince(
                quarter,
                year,
                provinceCode);
    }

//    public void getDrugEstimate(int quarters, int year, String provinceID) {
//        List<Object[]> items = pqmDrugEstimateImpl.getDrugEstimate(year, quarters, provinceID);
//        List<PqmDrugeLMISEDataEntity> list = new ArrayList<>();
//        PqmDrugeLMISEDataEntity model;
//
//        pqmDrugDataRepository.resetDataProvince("drugs_plan", quarters, year, provinceID);
//
//        for (Object[] item : items) {
//            String province_id = item[0] == null ? "" : item[0].toString();
//            String drug_name = item[1] == null ? "" : item[1].toString();
//            String unit = item[2] == null ? "" : item[2].toString();
//            String site_code = item[3] == null ? "" : item[3].toString();
//            String quarter = item[4] == null ? "" : item[4].toString();
//            String dispensedQuantity = item[5] == null ? "" : item[5].toString();
//            String plannedQuantity = item[6] == null ? "" : item[6].toString();
//
//            model = new PqmDrugeLMISEDataEntity();
//
//            model.setProvinceCode(provinceID);
//            model.setDataCode("drugs_plan");
//            model.setFacilityName("");
//            model.setFacilityCode(site_code);
//            model.setVersion("1");
//            model.setQuarter(quarters);
//            model.setYear(year);
//            model.setDrugName(drug_name == null ? "" : drug_name);
//            model.setDrugUom(unit == null ? "" : unit);
//            model.setDrugNameLowercase(drug_name == null ? "" : drug_name.toLowerCase());
//            model.setDrugUomLowercase(unit == null ? "" : unit.toLowerCase());
//            model.setPlannedQuantity(Long.valueOf(dispensedQuantity));//mẫu 
//            model.setDispensedQuantity(Long.valueOf(plannedQuantity));//tử 
//
//            save(model);
//
//        }
//    }
}
