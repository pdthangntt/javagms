package com.gms.service;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.PqmShiArtEntity;
import com.gms.entity.input.PqmShiArtSearch;
import com.gms.repository.PqmShiArtRepository;
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
public class PqmShiArtService extends BaseService implements IBaseService<PqmShiArtEntity, Long> {

    @Autowired
    private PqmShiArtRepository shiArtRepository;

    @Override
    public List<PqmShiArtEntity> findAll() {
        return shiArtRepository.findAll();
    }

    public DataPage<PqmShiArtEntity> find(PqmShiArtSearch search) {

        DataPage<PqmShiArtEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"month"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<PqmShiArtEntity> pages = shiArtRepository.find(search, pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }
    
     public void deleteBySiteIDAndMonthAndYear(Long siteID, int quarter, int year) {
        shiArtRepository.deleteBySiteIDAndMonthAndYear(siteID, quarter, year);
    }

    public PqmShiArtEntity findByProvinceIdAndMonthAndYearAndSiteID(Long siteID, String provinceID, Integer month, Integer year) {
        return shiArtRepository.findByProvinceIdAndMonthAndYearAndSiteID(siteID, provinceID, month, year);
    }
    
    public List<PqmShiArtEntity> findByProvinceIdAndMonthAndYear(String provinceID, Integer month, Integer year) {
        return shiArtRepository.findByProvinceIdAndMonthAndYear(provinceID, month, year);
    }


    @Override
    public PqmShiArtEntity findOne(Long ID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PqmShiArtEntity save(PqmShiArtEntity model) {
        try {
            shiArtRepository.save(model);
            return model;
        } catch (Exception e) {
            throw e;
        }
    }

}
