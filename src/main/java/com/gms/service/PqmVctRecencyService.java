package com.gms.service;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmVctEntity;
import com.gms.entity.db.PqmVctLogEntity;
import com.gms.entity.db.PqmVctRecencyEntity;
import com.gms.entity.input.PqmVctRecencySearch;
import com.gms.entity.input.PqmVctSearch;
import com.gms.repository.PqmVctLogRepository;
import com.gms.repository.PqmVctRecencyRepository;
import com.gms.repository.PqmVctRepository;
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
public class PqmVctRecencyService extends BaseService implements IBaseService<PqmVctRecencyEntity, Long> {

    @Autowired
    private PqmVctRecencyRepository vctRecencyRepository;
    @Autowired
    private PqmVctLogRepository logRepository;

    public DataPage<PqmVctRecencyEntity> find(PqmVctRecencySearch search) {

        DataPage<PqmVctRecencyEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"ID"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PqmVctRecencyEntity> pages = vctRecencyRepository.find(search, pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    @Override
    public List<PqmVctRecencyEntity> findAll() {
        return vctRecencyRepository.findAll();
    }

    @Override
    public PqmVctRecencyEntity findOne(Long ID) {
        Optional<PqmVctRecencyEntity> optional = vctRecencyRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    public List<PqmVctRecencyEntity> findBySiteID(Long siteID) {
        List<PqmVctRecencyEntity> list = vctRecencyRepository.findBySiteID(siteID);
        return list.isEmpty() ? null : list;
    }

    public List<PqmVctRecencyEntity> findByProvinceIDAndVctID(Long provinceID) {
        List<PqmVctRecencyEntity> list = vctRecencyRepository.findByProvinceIDAndVctID(provinceID);
        return list.isEmpty() ? null : list;
    }

    public List<PqmVctRecencyEntity> findBySiteIDs(Set<Long> siteID) {
        List<PqmVctRecencyEntity> list = vctRecencyRepository.findBySiteIDs(siteID);
        return list.isEmpty() ? null : list;
    }

    public void remove(Long ID) {
        PqmVctRecencyEntity entity = findOne(ID);
        if (entity != null) {
            vctRecencyRepository.delete(entity);
        }
    }

    @Override
    public PqmVctRecencyEntity save(PqmVctRecencyEntity model) {
        try {
            Date currentDate = new Date();
            vctRecencyRepository.save(model);
            return model;
        } catch (Exception e) {
            throw e;
        }
    }

    public PqmVctRecencyEntity findByCode(Long siteId, String code) {
        return vctRecencyRepository.findByCodeAndSiteID(code, siteId);
    }

    public PqmVctRecencyEntity findBySiteAndCode(Long siteId, String code) {
        return vctRecencyRepository.findBySiteAndCode(siteId, code);
    }

    public PqmVctRecencyEntity findByCodeAndSiteIDAndEarlyHivDate(String confirmTestNo, Long siteId, Date earlyHivDate) {
        return vctRecencyRepository.findByCodeAndSiteIDAndEarlyHivDate(confirmTestNo, siteId, earlyHivDate);
    }

    public PqmVctLogEntity log(Long targetId, String content) {
        PqmVctLogEntity model = new PqmVctLogEntity();
        model.setStaffID(getCurrentUserID());
        model.setCreateAt(new Date());
        model.setTargetID(targetId);
        model.setContent(content);
        return logRepository.save(model);
    }
    
    
    public void resetDataBySite(Long siteID) {
        vctRecencyRepository.resetDataBySite(siteID);
    }

}
