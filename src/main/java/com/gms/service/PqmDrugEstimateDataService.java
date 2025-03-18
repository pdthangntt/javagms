package com.gms.service;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmDrugEstimateDataEntity;
import com.gms.entity.input.PqmDrugEstimateSearch;
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
import com.gms.repository.impl.PqmDrugEstimateImpl;
import java.util.ArrayList;

/**
 *
 * @author pdThang
 */
@Service
public class PqmDrugEstimateDataService extends BaseService implements IBaseService<PqmDrugEstimateDataEntity, Long> {

    @Autowired
    private PqmDrugEstimateDataRepository pqmDrugDataRepository;
    @Autowired
    private PqmDrugEstimateImpl pqmDrugEstimateImpl;

    @Override
    public List<PqmDrugEstimateDataEntity> findAll() {
        return pqmDrugDataRepository.findAll();
    }

    @Override
    public PqmDrugEstimateDataEntity findOne(Long ID) {
        Optional<PqmDrugEstimateDataEntity> optional = pqmDrugDataRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public PqmDrugEstimateDataEntity save(PqmDrugEstimateDataEntity model) {
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

    public DataPage<PqmDrugEstimateDataEntity> find(PqmDrugEstimateSearch search) {

        DataPage<PqmDrugEstimateDataEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"provinceID"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PqmDrugEstimateDataEntity> pages = pqmDrugDataRepository.find(search, pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    public PqmDrugEstimateDataEntity findByUniqueConstraints(
            String dataCode,
            int year,
            int quarter,
            String provinceCode,
            String drugName,
            String unit,
            String facilityCode
    ) {
        PqmDrugEstimateDataEntity model = pqmDrugDataRepository.findByUniqueConstraints(
                dataCode,
                quarter,
                year,
                provinceCode,
                drugName,
                unit,
                facilityCode);
        return model;
    }

    public List<PqmDrugEstimateDataEntity> findData(
            String dataCode,
            int quarter,
            int year,
            String provinceCode
    ) {
        List<PqmDrugEstimateDataEntity> model = pqmDrugDataRepository.findData(
                dataCode,
                quarter,
                year,
                provinceCode);
        return model == null || model.isEmpty() ? null : model;
    }

    public void getDrugEstimate(int quarters, int year, String provinceID) {
        List<Object[]> items = pqmDrugEstimateImpl.getDrugEstimate(year, quarters, provinceID);
        List<PqmDrugEstimateDataEntity> list = new ArrayList<>();
        PqmDrugEstimateDataEntity model;

        pqmDrugDataRepository.resetDataProvince("drugs_plan", quarters, year, provinceID);

        for (Object[] item : items) {
            String province_id = item[0] == null ? "" : item[0].toString();
            String drug_name = item[1] == null ? "" : item[1].toString();
            String unit = item[2] == null ? "" : item[2].toString();
            String site_code = item[3] == null ? "" : item[3].toString();
            String quarter = item[4] == null ? "" : item[4].toString();
            String dispensedQuantity = item[5] == null ? "" : item[5].toString();
            String plannedQuantity = item[6] == null ? "" : item[6].toString();

            model = new PqmDrugEstimateDataEntity();

            model.setProvinceCode(provinceID);
            model.setDataCode("drugs_plan");
            model.setFacilityName("");
            model.setFacilityCode(site_code);
            model.setVersion("1");
            model.setQuarter(quarters);
            model.setYear(year);
            model.setDrugName(drug_name == null ? "" : drug_name);
            model.setDrugUom(unit == null ? "" : unit);
            model.setDrugNameLowercase(drug_name == null ? "" : drug_name.toLowerCase());
            model.setDrugUomLowercase(unit == null ? "" : unit.toLowerCase());
            model.setPlannedQuantity(Long.valueOf(dispensedQuantity));//mẫu 
            model.setDispensedQuantity(Long.valueOf(plannedQuantity));//tử 

            save(model);

        }
    }

}
