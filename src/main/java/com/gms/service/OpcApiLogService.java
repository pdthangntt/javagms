package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.db.OpcApiLogEntity;
import com.gms.repository.OpcApiLogRepository;
import static com.gms.service.BaseService.FORMATDATE;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author pdThang
 */
@Service
public class OpcApiLogService extends BaseService {

    @Autowired
    private OpcApiLogRepository apiLogRepository;

    /**
     * @param ID
     * @param status
     * @return
     */
    public DataPage<OpcApiLogEntity> findAll(String ID, String status, String from, String to, int page, int size) {
        DataPage<OpcApiLogEntity> dataPage = new DataPage<>();

        dataPage.setCurrentPage(page);
        dataPage.setMaxResult(size);
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"time"});
        Pageable pageable = PageRequest.of(page - 1, size, sortable);

        Page<OpcApiLogEntity> pages = apiLogRepository.findAll(
                ID,
                status,
                StringUtils.isEmpty(from) ? null : TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, from),
                StringUtils.isEmpty(to) ? null : TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, to),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());
        return dataPage;
    }

    /**
     * @param ID
     * @param status
     * @return
     */
    public DataPage<OpcApiLogEntity> findAlls(String ID, String status, String from, String to) {
        DataPage<OpcApiLogEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(1);
        dataPage.setMaxResult(999999999);
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"time"});
        Pageable pageable = PageRequest.of(0, 999999999, sortable);

        Page<OpcApiLogEntity> pages = apiLogRepository.findAll(
                ID,
                status,
                StringUtils.isEmpty(from) ? null : TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, from),
                StringUtils.isEmpty(to) ? null : TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, to),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());
        return dataPage;
    }

    public List<OpcApiLogEntity> countARV(String ID, String status, String from, String to) {
        List<OpcApiLogEntity> list = apiLogRepository.findARVs(
                ID,
                status,
                StringUtils.isEmpty(from) ? null : TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, from),
                StringUtils.isEmpty(to) ? null : TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, to));

        return list;
    }

    public int countAllLogs(String ID, String status, String from, String to) {
        int n = apiLogRepository.countAllLogs(
                ID,
                status,
                StringUtils.isEmpty(from) ? null : TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, from),
                StringUtils.isEmpty(to) ? null : TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, to));
        return n;
    }

}
