package com.gms.service;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.SiteVnptFkEntity;
import com.gms.entity.input.VNPTConfigSearch;
import com.gms.repository.SiteVnptFkRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author pdThang
 */
@Service
public class VNPTConfigService extends BaseService implements IBaseService<SiteVnptFkEntity, Long> {

    @Autowired
    private SiteVnptFkRepository configRepository;

    /**
     * @param configSearch
     * @return
     */
    public DataPage<SiteVnptFkEntity> findAll(VNPTConfigSearch configSearch) {
        DataPage<SiteVnptFkEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(configSearch.getPageIndex());
        dataPage.setMaxResult(configSearch.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"ID"});
        Pageable pageable = PageRequest.of(configSearch.getPageIndex() - 1, configSearch.getPageSize(), sortable);

        Page<SiteVnptFkEntity> pages = configRepository.findAll(
                configSearch.getID(),
                configSearch.getSite(),
                configSearch.getVnptSiteID(),
                configSearch.getActive(),
                configSearch.getSiteID(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());
        return dataPage;
    }

    @Override
    public List<SiteVnptFkEntity> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SiteVnptFkEntity findOne(Long ID) {
        Optional<SiteVnptFkEntity> optional = configRepository.findById(ID);
        SiteVnptFkEntity model = null;
        if (optional.isPresent()) {
            List<SiteVnptFkEntity> entitys = new ArrayList<>();
            entitys.add(optional.get());
            model = entitys.get(0);
        }
        return model;
    }

    @Override
    public SiteVnptFkEntity save(SiteVnptFkEntity model) {
        Date currentDate = new Date();
        if (model.getCreateAT() == null) {
            model.setCreateAT(currentDate);
            model.setCreatedBY(getCurrentUserID());
        }
        model.setUpdateAt(currentDate);
        model.setUpdatedBY(getCurrentUserID());
        configRepository.save(model);
        return model;
    }
    

    
    public boolean existID(Long ID) {
        if (ID == null || ID == 0) {
            return false;
        }
        return configRepository.countByID(ID) > 0;
    }
    
    public boolean existVpntSiteID(String vpntSiteID) {
        if (StringUtils.isEmpty(vpntSiteID)) {
            return false;
        }
        return  configRepository.countByVpntSiteID(vpntSiteID.trim()) > 0;
    }

}
