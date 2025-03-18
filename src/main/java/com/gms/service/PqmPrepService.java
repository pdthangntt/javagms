package com.gms.service;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmPrepEntity;
import com.gms.entity.input.PqmPrepSearch;
import com.gms.repository.PqmPrepRepository;
import com.gms.repository.PqmPrepVisitRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
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
public class PqmPrepService extends BaseService implements IBaseService<PqmPrepEntity, Long> {

    @Autowired
    private PqmPrepRepository prepRepository;
    @Autowired
    private PqmPrepVisitRepository pqmPrepVisitRepository;
    @Autowired
    private PqmPrepVisitRepository visitRepository;

    @Override
    public List<PqmPrepEntity> findAll() {
        return prepRepository.findAll();
    }

    public List<PqmPrepEntity> findBySiteID(Long siteID) {
        List<PqmPrepEntity> sites = prepRepository.findBySiteID(siteID);
        return sites == null || sites.isEmpty() ? null : sites;
    }

    public List<PqmPrepEntity> findBySiteIDs(Set<Long> siteID) {
        List<PqmPrepEntity> sites = prepRepository.findBySiteIDs(siteID);
        return sites == null || sites.isEmpty() ? null : sites;
    }

    @Override
    public PqmPrepEntity findOne(Long ID) {
        Optional<PqmPrepEntity> optional = prepRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    public PqmPrepEntity findBySiteAndCode(Long siteID, String code) {
        PqmPrepEntity optional = prepRepository.findBySiteAndCode(siteID, code);
        return optional;
    }

    public PqmPrepEntity findByCustomer_id(Long siteID, String code) {
        List<PqmPrepEntity> optional = prepRepository.findByCustomer_id(siteID, code);
        return optional != null && !optional.isEmpty() ? optional.get(0) : null;
    }

    public void deleteAll(Set<Long> IDs) {
        if (!IDs.isEmpty()) {
            for (Long ID : IDs) {
                prepRepository.deleteById(ID);
            }
        }
    }

    public void deleteByID(Long ID) {
        prepRepository.deleteById(ID);
    }

    @Override
    public PqmPrepEntity save(PqmPrepEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
                try {
                    model.setCreatedBY(getCurrentUser().getUser().getID());
                } catch (Exception e) {
                    model.setCreatedBY(Long.valueOf(0));
                }
            }
            model.setUpdateAt(currentDate);
            model.setType(StringUtils.isEmpty(model.getType()) ? "9999" : model.getType());
            try {
                model.setUpdatedBY(getCurrentUser().getUser().getID());
            } catch (Exception e) {
                model.setUpdatedBY(Long.valueOf(0));
            }
            prepRepository.save(model);
            return model;
        } catch (Exception e) {
            throw e;
        }
    }

    public DataPage<PqmPrepEntity> find(PqmPrepSearch search) {

        DataPage<PqmPrepEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"examinationTime"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PqmPrepEntity> pages = prepRepository.find(search, pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

}
