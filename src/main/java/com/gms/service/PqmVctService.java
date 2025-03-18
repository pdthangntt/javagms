package com.gms.service;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmVctEntity;
import com.gms.entity.db.PqmVctLogEntity;
import com.gms.entity.input.PqmVctSearch;
import com.gms.repository.PqmVctLogRepository;
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
public class PqmVctService extends BaseService implements IBaseService<PqmVctEntity, Long> {

    @Autowired
    private PqmVctRepository vctRepository;
    @Autowired
    private PqmVctLogRepository logRepository;

    public DataPage<PqmVctEntity> find(PqmVctSearch search) {

        DataPage<PqmVctEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"updateAt"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PqmVctEntity> pages = vctRepository.find(search, pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    @Override
    public List<PqmVctEntity> findAll() {
        return vctRepository.findAll();
    }

    @Override
    public PqmVctEntity findOne(Long ID) {
        Optional<PqmVctEntity> optional = vctRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    public List<PqmVctEntity> findBySiteID(Long siteID) {
        List<PqmVctEntity> list = vctRepository.findBySiteID(siteID);
        return list.isEmpty() ? null : list;
    }

    public List<PqmVctEntity> findBySiteIDs(Set<Long> siteID) {
        List<PqmVctEntity> list = vctRepository.findBySiteIDs(siteID);
        return list.isEmpty() ? null : list;
    }

    public void remove(Long ID) {
        PqmVctEntity entity = findOne(ID);
        if (entity != null) {
            vctRepository.delete(entity);
        }
    }

    @Override
    public PqmVctEntity save(PqmVctEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
                model.setCreatedBY(getCurrentUserID());
            }
            model.setUpdateAt(currentDate);
            model.setUpdatedBY(getCurrentUserID());
            vctRepository.save(model);
            return model;
        } catch (Exception e) {
            throw e;
        }
    }

    public PqmVctEntity findByCode(Long siteId, String code) {
        return vctRepository.findByCodeAndSiteID(code, siteId);
    }

    public List<PqmVctEntity> findByCodeAndSiteIDs(String code, Set<Long> siteId) {
        return vctRepository.findByCodeAndSiteIDs(code, siteId);
    }

    public PqmVctEntity findBySiteAndCode(Long siteId, String code) {
        return vctRepository.findBySiteAndCode(siteId, code);
    }

    public PqmVctEntity findByCustomerId(String CustomerId) {
        List<PqmVctEntity> items = vctRepository.findByCustomerId(CustomerId);
        return items != null && !items.isEmpty() ? items.get(0) : null;
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
        vctRepository.resetDataBySite(siteID);
    }

}
