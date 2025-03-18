package com.gms.service;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmLogEntity;
import com.gms.entity.input.PqmLogSearch;
import com.gms.repository.PqmLogRepository;
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
public class PqmLogService extends BaseService implements IBaseService<PqmLogEntity, Long> {

    @Autowired
    private PqmLogRepository logRepository;

    public DataPage<PqmLogEntity> find(PqmLogSearch search) {

        DataPage<PqmLogEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"time"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PqmLogEntity> pages = logRepository.find(search, pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    @Override
    public List<PqmLogEntity> findAll() {
        return logRepository.findAll();
    }

    @Override
    public PqmLogEntity findOne(Long ID) {
        Optional<PqmLogEntity> optional = logRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    public List<PqmLogEntity> findByProvinceID(String provinceID) {
        List<PqmLogEntity> list = logRepository.findByProvinceID(provinceID);
        return list.isEmpty() ? null : list;
    }

    public List<PqmLogEntity> findBySiteIDAndService(Set<Long> siteIDs, String service, String from, String to) {
        List<PqmLogEntity> list = logRepository.findBySiteIDAndService(siteIDs, service, from, to);
        return list.isEmpty() ? null : list;
    }

    public List<PqmLogEntity> findBySiteID(Set<Long> siteIDs, String from, String to) {
        List<PqmLogEntity> list = logRepository.findBySiteID(siteIDs, from, to);
        return list.isEmpty() ? null : list;
    }

    public void remove(Long ID) {
        PqmLogEntity entity = findOne(ID);
        if (entity != null) {
            logRepository.delete(entity);
        }
    }

    @Override
    public PqmLogEntity save(PqmLogEntity model) {
        try {
            Date currentDate = new Date();
            model.setTime(currentDate);
            model.setProvinceID(getCurrentUser().getSite().getProvinceID());
            model.setStaffID(getCurrentUser().getUser().getID());
            model.setStaffName(getCurrentUser().getUser().getName());
            logRepository.save(model);
            return model;
        } catch (Exception e) {
            throw e;
        }
    }

    public PqmLogEntity log(String action, int total, int success, int error, String object, Long siteID, String service) {
        try {
            PqmLogEntity model = new PqmLogEntity();
            model.setAction(action);
            model.setTotal(total);
            model.setSuccess(success);
            model.setError(error);
            model.setObject(object);
            model.setSiteID(siteID);
            model.setService(service);
            return save(model);
        } catch (Exception e) {
            throw e;
        }

    }

}
