package com.gms.service;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmArvEntity;
import com.gms.entity.db.PqmArvEntity;
import com.gms.entity.input.PqmArvSearch;
import com.gms.entity.input.PqmPrepSearch;
import com.gms.repository.PqmArvRepository;
import com.gms.repository.PqmArvStageRepository;
import com.gms.repository.PqmArvTestRepository;
import com.gms.repository.PqmArvViralRepository;
import com.gms.repository.PqmArvVisitRepository;
import com.gms.repository.PqmPrepRepository;
import com.gms.repository.PqmPrepVisitRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author vvthanh
 */
@Service
public class PqmArvService extends BaseService implements IBaseService<PqmArvEntity, Long> {

    @Autowired
    private PqmArvRepository arvRepository;
    @Autowired
    private PqmArvStageRepository stageRepository;
    @Autowired
    private PqmArvVisitRepository visitRepository;
    @Autowired
    private PqmArvViralRepository viralRepository;
    @Autowired
    private PqmArvTestRepository testRepository;

    @Override
    public List<PqmArvEntity> findAll() {
        return arvRepository.findAll();
    }

    public void deleteAll(Set<Long> IDs) {
        if (!IDs.isEmpty()) {
            for (Long ID : IDs) {
                arvRepository.deleteById(ID);
            }
        }
    }

    public void deleteById(Long ID) {
        arvRepository.deleteById(ID);
    }

    public List<PqmArvEntity> findBySiteID(Long siteID) {
        List<PqmArvEntity> arvs = arvRepository.findBySiteID(siteID);
        return arvs == null || arvs.isEmpty() ? null : arvs;
    }

    public List<PqmArvEntity> findByCodes(Set<String> code) {
        List<PqmArvEntity> arvs = arvRepository.findByCodes(code);
        return arvs == null || arvs.isEmpty() ? null : arvs;
    }

    public PqmArvEntity findBySiteIDAndCode(Long siteID, String code) {
        PqmArvEntity arvs = arvRepository.findBySiteIDAndCode(siteID, code);
        return arvs;
    }

    public PqmArvEntity findBySiteIDAndCustomer_system_id(String code) {
        List<PqmArvEntity> arvs = arvRepository.findBySiteIDAndCustomer_system_id(code);
        return !arvs.isEmpty() ? arvs.get(0) : null;
    }

    public List<PqmArvEntity> findBySiteAndInsuranceNo(Set<Long> siteID, String insuranceNo) {
        List<PqmArvEntity> arvs = arvRepository.findBySiteAndInsuranceNo(siteID, insuranceNo);
        return arvs == null || arvs.isEmpty() ? null : arvs;
    }

    public List<PqmArvEntity> findBySiteIDAndCodes(Long siteID, Set<String> code) {
        List<PqmArvEntity> arvs = arvRepository.findBySiteIDAndCodes(siteID, code);
        return arvs == null || arvs.isEmpty() ? null : arvs;
    }

    public List<PqmArvEntity> findBySiteIDsAndCodes(Set<Long> siteID, Set<String> code) {
        List<PqmArvEntity> arvs = arvRepository.findBySiteIDsAndCodes(siteID, code);
        return arvs == null || arvs.isEmpty() ? null : arvs;
    }

    @Override
    public PqmArvEntity findOne(Long ID) {
        Optional<PqmArvEntity> optional = arvRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public PqmArvEntity save(PqmArvEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
                model.setCreatedBY(getCurrentUser().getUser().getID());
            }
            model.setUpdateAt(currentDate);
            model.setUpdatedBY(getCurrentUser().getUser().getID());
            arvRepository.save(model);
            return model;
        } catch (Exception e) {
            throw e;
        }
    }

    public PqmArvEntity saveApi(PqmArvEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
            }
            model.setUpdateAt(currentDate);
            arvRepository.save(model);
            return model;
        } catch (Exception e) {
            throw e;
        }
    }

    public DataPage<PqmArvEntity> find(PqmArvSearch search) {

        DataPage<PqmArvEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"registrationTime"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PqmArvEntity> pages = arvRepository.find(search, pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    public void resetDataBySite(Long siteID) {
        arvRepository.resetDataBySite(siteID);
    }

}
