package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmLogApiEntity;
import com.gms.entity.input.ApiArvLogSearch;
import com.gms.repository.PqmLogApiRepository;
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
public class PqmLogApiService extends OpcBaseService {

    @Autowired
    private PqmLogApiRepository apiRepository;

    
    public DataPage<PqmLogApiEntity> find(ApiArvLogSearch search) {
        DataPage<PqmLogApiEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"time"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PqmLogApiEntity> pages = apiRepository.find(
                search.getStatus(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTo()),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());
        return dataPage;
    }

    

}
