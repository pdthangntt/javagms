package com.gms.service;

import com.gms.entity.bean.DataPage;
import com.gms.entity.db.HtcLaytestAgencyEntity;
import com.gms.entity.input.LaytestAgencySearch;
import com.gms.repository.HtcLaytestAgencyRepository;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * HtcLaytestAgencyService
 *
 * @author TrangBN
 */
@Service
public class HtcLaytestAgencyService extends BaseService implements IBaseService<HtcLaytestAgencyEntity, Long> {

    @Autowired
    private HtcLaytestAgencyRepository agencyRepository;

    @Override
    public List<HtcLaytestAgencyEntity> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HtcLaytestAgencyEntity findOne(Long ID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HtcLaytestAgencyEntity save(HtcLaytestAgencyEntity condition) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Lây thông tin người được giới thiệu bằng laytestID
     *
     * @param id
     * @return
     */
    public List<HtcLaytestAgencyEntity> findByLaytestID(Long id) {
        return agencyRepository.findByLaytestID(id);
    }

    public List<HtcLaytestAgencyEntity> findAllByLaytestID(Set<Long> IDs) {
        return agencyRepository.findAllByLaytestIDs(IDs);
    }

    /**
     * Lưu tất cả các thông tin của người nhiễm được giới thiệu
     *
     * @param id
     * @param agencies
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Iterable<HtcLaytestAgencyEntity> saveAll(Long id, List<HtcLaytestAgencyEntity> agencies) {
        try {
            // Delete all existing người được giới thiệu
            agencyRepository.deleteByLaytestID(id);
            if (agencies != null) {
                return agencyRepository.saveAll(agencies);
            }
            return null;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }
    }

    /**
     * Tìm kiếm khách hàng
     *
     * @author pdThang
     * @param searchInput
     * @return
     */
    public DataPage<HtcLaytestAgencyEntity> find(LaytestAgencySearch searchInput) {
        DataPage<HtcLaytestAgencyEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(searchInput.getPageIndex());
        dataPage.setMaxResult(searchInput.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"ID"});
        if (searchInput.getSortable() != null) {
            sortable = searchInput.getSortable();
        }
        Pageable pageable = PageRequest.of(searchInput.getPageIndex() - 1, searchInput.getPageSize(), sortable);

        Page<HtcLaytestAgencyEntity> pages = agencyRepository.find(
                searchInput.getCode(),
                searchInput.getFullname(),
                searchInput.getStatus(),
                searchInput.getCreatedBY(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());
        return dataPage;
    }
}
