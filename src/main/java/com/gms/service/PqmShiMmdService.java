package com.gms.service;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmShiArtEntity;
import com.gms.entity.db.PqmShiMmdEntity;
import com.gms.entity.input.PqmShiArtSearch;
import com.gms.entity.input.PqmShiMmdSearch;
import com.gms.repository.PqmShiArtRepository;
import com.gms.repository.PqmShiMmdRepository;
import java.util.Date;
import java.util.List;
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
public class PqmShiMmdService extends BaseService implements IBaseService<PqmShiMmdEntity, Long> {

    @Autowired
    private PqmShiMmdRepository shiMmdRepository;

    @Override
    public List<PqmShiMmdEntity> findAll() {
        return shiMmdRepository.findAll();
    }

    public DataPage<PqmShiMmdEntity> find(PqmShiMmdSearch search) {

        DataPage<PqmShiMmdEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"month"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PqmShiMmdEntity> pages = shiMmdRepository.find(search, pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    public PqmShiMmdEntity findByProvinceIdAndMonthAndYear(Long siteID, String provinceID, Integer month, Integer year) {
        return shiMmdRepository.findByProvinceIdAndMonthAndYear(siteID, provinceID, month, year);
    }

    public List<PqmShiMmdEntity> findDataReport(String provinceID, Integer month, Integer year) {
        return shiMmdRepository.findDataReport(provinceID, month, year);
    }

    @Override
    public PqmShiMmdEntity findOne(Long ID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PqmShiMmdEntity save(PqmShiMmdEntity model) {
        try {
            shiMmdRepository.save(model);
            return model;
        } catch (Exception e) {
            throw e;
        }
    }

    public void deleteBySiteIDAndMonthAndYear(Long siteID, int quarter, int year) {
        shiMmdRepository.deleteBySiteIDAndMonthAndYear(siteID, quarter, year);
    }

}
