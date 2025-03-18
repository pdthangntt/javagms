package com.gms.service;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmApiTokenEntity;
import com.gms.entity.input.PqmApiTokenSearch;
import com.gms.repository.PqmApiTokenRepository;
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
 * @author pdthang
 */
@Service
public class PqmApiTokenService extends BaseService implements IBaseService<PqmApiTokenEntity, Long> {

    @Autowired
    private PqmApiTokenRepository repository;

    @Override
    public List<PqmApiTokenEntity> findAll() {
        return repository.findAll();
    }

    public List<PqmApiTokenEntity> findBySiteID(Long siteID) {
        List<PqmApiTokenEntity> sites = repository.findBySiteID(siteID);
        return sites == null || sites.isEmpty() ? null : sites;
    }

    @Override
    public PqmApiTokenEntity findOne(Long ID) {
        Optional<PqmApiTokenEntity> optional = repository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    public PqmApiTokenEntity findByKeyToken(String key) {
        return repository.findByKeyToken(key);
    }

    public void deleteAll(Set<Long> IDs) {
        if (!IDs.isEmpty()) {
            for (Long ID : IDs) {
                repository.deleteById(ID);
            }
        }
    }

    public void delete(Long ID) {
        repository.deleteById(ID);
    }

    @Override
    public PqmApiTokenEntity save(PqmApiTokenEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
                model.setCreatedBY(getCurrentUser().getUser().getID());
            }
            model.setUpdateAt(currentDate);
            model.setUpdatedBY(getCurrentUser().getUser().getID());
            repository.save(model);
            return model;
        } catch (Exception e) {
            throw e;
        }
    }

    public DataPage<PqmApiTokenEntity> find(PqmApiTokenSearch search) {

        DataPage<PqmApiTokenEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"createAT"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PqmApiTokenEntity> pages = repository.find(search, pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

}
