package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.constant.ConfirmTestResultEnum;
import com.gms.entity.constant.InitCodeEnum;
import com.gms.entity.constant.ServiceTestEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.db.HtcConfirmLogEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.input.HtcConfirmEarlySearch;
import com.gms.entity.input.HtcConfirmSearch;
import com.gms.entity.input.LogSearch;
import com.gms.repository.DistrictRepository;
import com.gms.repository.HtcConfirmLogRepository;
import com.gms.repository.HtcConfirmRepository;
import com.gms.repository.ProvinceRepository;
import com.gms.repository.WardRepository;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author pdThang
 */
@Service
public class HtcConfirmService extends BaseService implements IBaseService<HtcConfirmEntity, Long> {

    @Autowired
    private HtcConfirmRepository htcConfirmRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private HtcConfirmLogRepository logRepository;
    @Autowired
    private QrCodeService qrCodeService;

    @Override
    public List<HtcConfirmEntity> findAll() {
        return htcConfirmRepository.findAll();
    }

    @Override
    public HtcConfirmEntity findOne(Long ID) {
        Optional<HtcConfirmEntity> optional = htcConfirmRepository.findById(ID);
        HtcConfirmEntity entity = null;
        if (optional.isPresent()) {
            List<HtcConfirmEntity> models = new ArrayList<>();
            models.add(optional.get());
            entity = getAddress(models).get(0);
        }
        return entity;
    }

    public void delete(Long ID) {
        Date currentDate = new Date();
        HtcConfirmEntity e = findOne(ID);
        if (e != null) {
            e.setRemove(true);
            e.setUpdatedAt(currentDate);
        }

        htcConfirmRepository.save(e);
    }

    public HtcConfirmEntity update(HtcConfirmEntity condition) {
        return htcConfirmRepository.save(condition);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HtcConfirmEntity save(HtcConfirmEntity model) {
        try {
            Date currentDate = new Date();
            if (model.getID() == null) {
                model.setCreatedAt(currentDate);
                if (getCurrentUser() != null && getCurrentUser().getSite().getID().equals(model.getSiteID())) {
                    model.setCreatedBy(getCurrentUser().getUser().getID());
                }
            }
            if (getCurrentUser() != null) {
                model.setProjectID(getCurrentUser().getSite().getProjectID());
            }
            if (getCurrentUser() != null && getCurrentUser().getSite().getID().equals(model.getSiteID())) {
                model.setUpdatedBy(getCurrentUser().getUser().getID());
            }
            model.setUpdatedAt(currentDate);

            model.setFullname(TextUtils.toFullname(model.getFullname()));
            // Điều kiện cập nhật trường ngày tiếp nhận
            if (model.getAcceptDate() == null && model.getSampleReceiveTime() != null && StringUtils.isEmpty(model.getResultsID())
                    && model.getConfirmTime() == null) {
                model.setAcceptDate(model.getSampleReceiveTime());
            }

        } catch (Exception ex) {
            Logger.getLogger(HtcConfirmService.class.getName()).log(Level.SEVERE, null, ex);
        }
        model.setServiceID(StringUtils.isEmpty(model.getServiceID()) ? ServiceTestEnum.CD.getKey() : model.getServiceID());
        if (!StringUtils.isEmpty(model.getResultsID()) && model.getResultsID().equals(ConfirmTestResultEnum.DUONG_TINH)) {
            model.setQrcode(qrCodeService.generate(model.getPatientID(), model.getFullname(), model.getGenderID(), null, null));
        }
        //Nhiễm mới

        if (model.getEarlyHivDate() != null
                || StringUtils.isNotEmpty(model.getEarlyBioName())
                || StringUtils.isNotEmpty(model.getEarlyHiv())
                || model.getVirusLoadDate() != null
                || StringUtils.isNotEmpty(model.getVirusLoadNumber())
                || StringUtils.isNotEmpty(model.getVirusLoad())
                || StringUtils.isNotEmpty(model.getEarlyDiagnose())) {
            model.setEarlyJob(true);
        }
        return htcConfirmRepository.save(model);
    }

    private boolean soSanh(String s1, String s2) {
        s1 = StringUtils.isEmpty(s1) ? "" : s1;
        s2 = StringUtils.isEmpty(s2) ? "" : s2;
        return !s1.equals(s2);
    }

    /**
     * Tìm kiếm khách hàng
     *
     * @author PDTHANG
     * @param search
     * @return
     */
    public DataPage<HtcConfirmEntity> find(HtcConfirmSearch search) {
        DataPage<HtcConfirmEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"fullname"});
        if (search.getSortable() != null) {
            sortable = search.getSortable();
        }
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);
        Page<HtcConfirmEntity> pages = htcConfirmRepository.find(
                search.getSiteID(),
                search.getCode(),
                search.getFullname(),
                search.getSourceID(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getConfirmTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getAcceptDateFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getAcceptDateTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getSourceSampleDateFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getSourceSampleDateTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getRequestHtcTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getRequestHtcTimeTo()),
                search.getResultsID(),
                search.getSourceSiteID(),
                search.getConfirmStatus(),
                search.isWait(),
                search.isReceived(),
                search.isResult(),
                search.isRemove(),
                search.isUpdate(),
                search.getConfirmFeedback(),
                search.getGsphStatus(),
                search.getRequestStatus(),
                search.isIsRequestAdditional(),
                search.getVirusLoad(),
                search.getEarlyDiagnose(),
                search.getEarlyHivStatus(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getEarlyHivDateFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getEarlyHivDateTo()),
                search.getEarlyHiv(),
                pageable
        );

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(getAddress(pages.getContent()));
        return dataPage;
    }

    public DataPage<HtcConfirmEntity> findEarlyHIV(HtcConfirmEarlySearch search) {
        DataPage<HtcConfirmEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"earlyHivDate"});
        if (search.getSortable() != null) {
            sortable = search.getSortable();
        }
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<HtcConfirmEntity> pages = htcConfirmRepository.findEarlyHIV(
                search.getSiteID(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getEarlyHivTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getEarlyHivTimeTo()),
                search.getSourceSiteID(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(getAddress(pages.getContent()));
        return dataPage;
    }

    /**
     * Ghép để lấy full địa chỉ
     *
     * @auth vvThành
     * @param entities
     * @return
     */
    public List<HtcConfirmEntity> getAddress(List<HtcConfirmEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        Set<String> pIDs = new HashSet<>();
        pIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getProvinceID")));
        pIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getCurrentProvinceID")));
        Map<String, ProvinceEntity> provinces = new HashMap<>();
        for (ProvinceEntity model : provinceRepository.findByIDs(pIDs)) {
            provinces.put(model.getID(), model);
        }

        Set<String> dIDs = new HashSet<>();
        dIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getDistrictID")));
        dIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getCurrentDistrictID")));
        Map<String, DistrictEntity> districts = new HashMap<>();
        for (DistrictEntity model : districtRepository.findByIDs(dIDs)) {
            districts.put(model.getID(), model);
        }

        Set<String> wIDs = new HashSet<>();
        wIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getWardID")));
        wIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getCurrentWardID")));
        Map<String, WardEntity> wards = new HashMap<>();
        for (WardEntity model : wardRepository.findByIDs(wIDs)) {
            wards.put(model.getID(), model);
        }

        for (HtcConfirmEntity entity : entities) {
            //Địa chỉ thường trú
            entity.setAddressFull(buildAddress(
                    entity.getAddress(),
                    entity.getPermanentAddressStreet(),
                    entity.getPermanentAddressGroup(),
                    provinces.get(entity.getProvinceID()),
                    districts.get(entity.getDistrictID()),
                    wards.get(entity.getWardID())
            ));

            if (StringUtils.isNotEmpty(entity.getCurrentProvinceID())) {
                entity.setCurrentAddressFull(buildAddress(
                        entity.getCurrentAddress(),
                        entity.getCurrentAddressStreet(),
                        entity.getCurrentAddressGroup(),
                        provinces.get(entity.getCurrentProvinceID()),
                        districts.get(entity.getCurrentDistrictID()),
                        wards.get(entity.getCurrentWardID())
                ));
            }
        }
        return entities;
    }

    /**
     * Validate existence for code and siteID
     *
     * @param code
     * @return
     */
    public boolean existCodes(String code) {
        if (StringUtils.isEmpty(code)) {
            return false;
        }
        return htcConfirmRepository.countByCodes(code.trim()) > 0;
    }

    /**
     * Validate existence for code and siteID in case update customer from test
     *
     * @param code
     * @param siteID
     * @param id
     * @return
     */
    public boolean existCodeAndSiteID(String code, Long siteID, Long id) {
        if (siteID == null || StringUtils.isEmpty(code) || id == null) {
            return false;
        }
        return htcConfirmRepository.countByCodeSiteID(siteID, code.trim(), id) > Long.valueOf("0");
    }

    /**
     * @author DSNAnh Check tồn tại mã XN tại cơ sở XN
     * @param sourceID
     * @param siteID
     * @return
     */
    public boolean existBySourceIDAndSiteID(String sourceID, Long siteID) {
        if (siteID == null || StringUtils.isEmpty(sourceID)) {
            return false;
        }
        return htcConfirmRepository.countBySourceIDAndSiteID(siteID, sourceID.trim()) > 0;
    }

    /**
     * Validate existence for code and siteID in case update customer from test
     *
     * @param code
     * @param siteID
     * @return
     */
    public boolean existSourceIDAndSiteID(String code, Long siteID) {
        if (siteID == null || StringUtils.isEmpty(code)) {
            return false;
        }
        return htcConfirmRepository.countBySourceIDAndSiteID(siteID, code.trim()) > 0;
    }

    /**
     * Validate existence for code and siteID in case update customer from test
     *
     * @param code
     * @param siteID
     * @param id
     * @return
     */
    public boolean existSourceIDAndSiteID(String code, Long siteID, Long id) {
        if (siteID == null || StringUtils.isEmpty(code) || id == null) {
            return false;
        }
        return htcConfirmRepository.countBySourceIDSiteID(siteID, code.trim(), id) > 0;
    }

    /**
     * Find one by source
     *
     * @auth vvThành
     * @param siteID
     * @param sourceID
     * @param sourceSiteID
     * @return
     */
    public HtcConfirmEntity findBySouce(Long siteID, String sourceID, Long sourceSiteID) {
        return htcConfirmRepository.findBySiteIDAndSourceIDAndSourceSiteID(siteID, sourceID, sourceSiteID);
    }

    public HtcConfirmEntity findBySouce(Long siteID, String sourceID, Long sourceSiteID, String confirmCode) {
        return htcConfirmRepository.findBySiteIDAndSourceIDAndSourceSiteID(siteID, sourceID, sourceSiteID, confirmCode);
    }

    public HtcConfirmEntity findBySourceIDSourceSiteID(String sourceID, Long sourceSiteID) {
        return htcConfirmRepository.findBySourceIDSourceSiteID(sourceID, sourceSiteID);
    }

    /**
     * Log các hành động của HTC confirm theo khách hàng
     *
     * @param confirmID
     * @param content
     * @return
     */
    public HtcConfirmLogEntity log(Long confirmID, String content) {
        HtcConfirmLogEntity model = new HtcConfirmLogEntity();
        model.setStaffID(getCurrentUserID());
        model.setCreateAt(new Date());
        model.setHtcConfirmID(confirmID);
        model.setContent(content);
        return logRepository.save(model);
    }

    /**
     * Lấy danh sách KHXNKĐ dương tính
     *
     * @auth DSNAnh
     * @param startDate
     * @param endDate
     * @param siteID
     * @return list HtcConfirmEntity
     */
    public List<HtcConfirmEntity> findPositive(Date startDate, Date endDate, Long siteID) {
        return htcConfirmRepository.findPositive(startDate, endDate, siteID);
    }

    /**
     * Lấy danh sách khách hàng sàng lọc theo danh sách ID
     *
     * @author pdThang
     * @param ids
     * @return
     */
    public List<HtcConfirmEntity> findAllByIds(List<Long> ids) {
        return htcConfirmRepository.findAllById(ids);
    }

    public List<HtcConfirmEntity> findAllByIdOrderByConfirmTime(List<Long> ids) {
        return htcConfirmRepository.findAllByIdOrderByConfirmTime(ids);
    }

    /**
     * Lưu tất cả khi chọn nhiều
     *
     * @author pdThang
     * @param entities
     * @return
     */
    public List<HtcConfirmEntity> saveAll(List<HtcConfirmEntity> entities) {
        Iterable<HtcConfirmEntity> all = htcConfirmRepository.saveAll(entities);
        List<HtcConfirmEntity> copy = ImmutableList.copyOf(all);
        return copy;
    }

    /**
     * Danh sách log theo bệnh nhân
     *
     * @auth pdThang
     * @param oID
     * @return
     */
    public List<HtcConfirmLogEntity> getLogs(Long oID) {
        return logRepository.findByConfirmID(oID);
    }

    /**
     * Xóa khách hàng vĩnh viễn
     *
     * @author pdThang
     * @param ID
     */
    public void remove(Long ID) {
        HtcConfirmEntity e = findOne(ID);
        htcConfirmRepository.delete(e);
    }

    /**
     * Generate mã khách hàng theo cơ sở
     *
     * @auth TrangBN
     * @return
     */
    public String getCode() {

        String code = getCurrentUser().getSite().getCode();
        HtcConfirmEntity visit = htcConfirmRepository.findLastCodeBySiteID(getCurrentUser().getSite().getID(), getCurrentUser().getSite().getCode());
        if (visit == null || StringUtils.isEmpty(visit.getCode()) || !visit.getCode().contains("-") || !StringUtils.containsIgnoreCase(visit.getCode(), code)) {
            return String.format("%s-%s", code, InitCodeEnum.RIGHT_PADDING.getKey()).toUpperCase();
        }

        try {
            String[] split = visit.getCode().split("-", -1);
            Long identity = Long.valueOf(split[split.length - 1]);
            if (identity.toString().length() < 6) {
                String leftPad = StringUtils.leftPad(String.valueOf((identity + 1)), 6, "0");
                return String.format("%s-%s", code, leftPad).toUpperCase();
            } else {
                return String.format("%s-%s", code, String.valueOf((identity + 1))).toUpperCase();
            }
        } catch (Exception e) {
            return String.format("%s-%s", code, InitCodeEnum.RIGHT_PADDING.getKey()).toUpperCase();
        }
    }

    /**
     * Lấy dữ liệu sổ xét nghiệm HIV
     *
     * @author DSNAnh
     * @param startDate
     * @param endDate
     * @param siteID
     * @param fullname
     * @param code
     * @param sourceID
     * @param sourceSiteID
     * @param resultsID
     * @return
     */
    public List<HtcConfirmEntity> findConfirmBook(Date startDate, Date endDate, Long siteID, String fullname, String code, String sourceID, Long sourceSiteID, String resultsID) {
        List<HtcConfirmEntity> models = htcConfirmRepository.findConfirmBook(startDate, endDate, siteID, fullname, code, sourceID, sourceSiteID, resultsID);
        return getAddress(models);
    }

    /**
     * Lấy danh sách khách hàng khẳng định cho sổ nhận và trả kết quả HIV
     *
     * @param receiveStartDate
     * @param receiveEndDate
     * @param testStartDate
     * @param testEndDate
     * @param returnStartDate
     * @param returnEndDate
     * @param sourceSiteID
     * @param siteID
     * @return
     */
//    public List<HtcConfirmEntity> findConfirmReceiveBook(Date receiveStartDate, Date receiveEndDate, Date testStartDate, Date testEndDate,
//                                                           Date returnStartDate, Date returnEndDate, String sourceSiteID, Long siteID) {
//
//        return htcConfirmRepository.findConfirmReceiveBook(receiveStartDate, receiveEndDate, testStartDate,
//                testEndDate, returnStartDate, returnEndDate, sourceSiteID, siteID);
//    }
    /**
     * Lấy ra bản ghi đầu tiên
     *
     * @auth vvThành
     * @param siteIds
     * @return
     */
    public HtcConfirmEntity getFirst(Set<Long> siteIds) {
        Page<HtcConfirmEntity> page = htcConfirmRepository.getFirst(siteIds, PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, new String[]{"confirmTime"})));
        return page.getTotalElements() == 0 ? null : page.getContent().get(0);
    }

    /**
     * pdThang
     *
     * @param search
     * @return
     */
    public DataPage<HtcConfirmLogEntity> findAllLog(LogSearch search) {
        DataPage<HtcConfirmLogEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"htcConfirmID"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<HtcConfirmLogEntity> pages = logRepository.findAll(search.getID(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTo()),
                search.getStaffID(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());
        return dataPage;
    }

    /**
     * @auth vvThành
     * @param code
     * @return
     */
    public HtcConfirmEntity findByQR(String code) {
        List<HtcConfirmEntity> items = htcConfirmRepository.findByQR(code);
        return items == null || items.isEmpty() ? null : items.get(0);
    }

    public List<HtcConfirmEntity> findByEarlyJob(boolean earlyJob) {
        return htcConfirmRepository.findByEarlyJob(earlyJob);
    }
}
