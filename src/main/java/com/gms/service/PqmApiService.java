package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmApiLogEntity;
import com.gms.entity.input.PacPatientNewSearch;
import com.gms.entity.input.PqmApiSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.gms.repository.PqmApiRepository;
import static com.gms.service.BaseService.FORMATDATE;
import java.util.Date;

/**
 *
 * @author pdThang
 */
@Service
public class PqmApiService extends BaseService {

    @Autowired
    private PqmApiRepository pqmApiRepository;

    public DataPage<PqmApiLogEntity> find(PqmApiSearch search) {
        DataPage<PqmApiLogEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"createAT"});
        if (search.getSortable() != null) {
            sortable = search.getSortable();
        }
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PqmApiLogEntity> pages = pqmApiRepository.find(
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTo()),
                search.getProvince(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    public PqmApiLogEntity save(PqmApiLogEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreateAT(currentDate);
            }
            pqmApiRepository.save(model);
            return model;
        } catch (Exception e) {
            throw e;
        }
    }

}
