package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ArvExchangeEnum;
import com.gms.entity.constant.InitCodeEnum;
import com.gms.entity.constant.StaffConfigEnum;
import com.gms.entity.constant.TestResultEnum;
import com.gms.entity.constant.TherapyExchangeStatusEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.HtcVisitLaytestEntity;
import com.gms.entity.db.HtcVisitLogEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.input.HtcElogSearch;
import com.gms.entity.input.HtcSearch;
import com.gms.entity.input.LogSearch;
import com.gms.repository.DistrictRepository;
import com.gms.repository.HtcVisitLogRepository;
import com.gms.repository.HtcVisitRepository;
import com.gms.repository.ProvinceRepository;
import com.gms.repository.WardRepository;
import com.gms.repository.impl.HtcVisitRepositoryImpl;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 *
 * @author vvthanh
 */
@Service
public class HtcVisitService extends BaseService implements IBaseService<HtcVisitEntity, Long> {

    @Autowired
    private HtcVisitRepository htcVisitRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private HtcVisitLogRepository logRepository;
    @Autowired
    private HtcVisitRepositoryImpl visitRepositoryImpl;
    @Autowired
    private HtcVisitLaytestService htcVisitLaytestService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private QrCodeService qrCodeService;

    /**
     * Log các hành động của HTC theo khách hàng
     *
     * @auth vvThành
     * @param visitID
     * @param content
     * @return
     */
    public HtcVisitLogEntity log(Long visitID, String content) {
        HtcVisitLogEntity model = new HtcVisitLogEntity();
        model.setStaffID(getCurrentUserID());
        model.setCreateAt(new Date());
        model.setHtcVisitID(visitID);
        model.setContent(content);
        return logRepository.save(model);
    }

    /**
     * Tìm kiếm khách hàng
     *
     * @param searchInput
     * @return
     */
    public DataPage<HtcVisitEntity> find(HtcSearch searchInput) {
        DataPage<HtcVisitEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(searchInput.getPageIndex());
        dataPage.setMaxResult(searchInput.getPageSize());
        searchInput.setID(searchInput.getID() == null || searchInput.getID() < 0 ? 0 : searchInput.getID());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"updateAt"});
        if (searchInput.getSortable() != null) {
            sortable = searchInput.getSortable();
        }
        Pageable pageable = PageRequest.of(searchInput.getPageIndex() - 1, searchInput.getPageSize(), sortable);

        Page<HtcVisitEntity> pages = htcVisitRepository.find(
                searchInput.getSiteID(),
                searchInput.getCode(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getAdvisoryeTime()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getAdvisoryeTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getAdvisoryeTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getConfirmTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getConfirmTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getExchangeTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getExchangeTimeTo()),
                searchInput.getName(),
                searchInput.getServiceID(),
                searchInput.getSearchServiceID(),
                searchInput.getObjectGroupID(),
                searchInput.getTherapyExchangeStatus(),
                searchInput.getConfirmTestStatus(),
                searchInput.getFeedbackStatus(),
                searchInput.getConfirmResultsID(),
                searchInput.getTestResultsID(),
                searchInput.isConfirm(),
                searchInput.isOpc(),
                searchInput.isRemove(),
                searchInput.getGsphStatus(),
                searchInput.getObjectGroupID(),
                searchInput.getFilter(),
                searchInput.getCreatedBY(),
                searchInput.getRequestStatus(),
                searchInput.getCustomerType(),
                searchInput.isNotReceive(),
                searchInput.isCofirmCreated(),
                searchInput.getEarlyDiagnose(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(getAddress(pages.getContent()));
        return dataPage;
    }

    /**
     * Xóa khách hàng
     *
     * @param ID
     */
    public void delete(Long ID) {
        HtcVisitEntity e = htcVisitRepository.findOne(ID);
        if (e != null) {
            e.setIsRemove(true);
        }
        htcVisitRepository.save(e);
    }

    /**
     * Validate existence for code and siteID
     *
     * @param code
     * @param siteID
     * @return
     */
    public boolean existCodeAndSiteID(String code, Long siteID) {
        if (siteID == null || StringUtils.isEmpty(code)) {
            return false;
        }
        return htcVisitRepository.countByCodeSiteID(siteID, code.trim()) > 0;
    }

    @Override
    public HtcVisitEntity findOne(Long ID) {
        Optional<HtcVisitEntity> optional = htcVisitRepository.findById(ID);
        HtcVisitEntity entity = null;
        if (optional.isPresent()) {
            List<HtcVisitEntity> models = new ArrayList<>();
            models.add(optional.get());
            entity = getAddress(models).get(0);
        }
        return entity;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HtcVisitEntity save(HtcVisitEntity condition) {
        try {
            return saveValidate(condition);
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }
    }

    /**
     * @author DSNAnh
     * @return
     */
    @Override
    public List<HtcVisitEntity> findAll() {
        return (List<HtcVisitEntity>) htcVisitRepository.findAll();
    }

    /**
     * @auth vvThành
     * @param siteID
     * @return
     */
    public HtcVisitEntity findLastBysiteID(Long siteID, String code) {
        return htcVisitRepository.findLastCodeBySiteID(siteID, code);
    }

    /**
     * Get HtcPositive by condition (range of time, service)
     *
     * @param startDate
     * @param endDate
     * @param services
     * @param objects
     * @param siteID
     * @param code
     * @return
     */
    public List<HtcVisitEntity> findHtcPositive(Date startDate, Date endDate, List<String> services, List<String> objects, Long siteID, String code) {
        List<HtcVisitEntity> models = htcVisitRepository.findPositive(startDate, endDate, services, objects, siteID, code);
        return getAddress(models);
    }

    /**
     * Tìm danh sách khách hàng xuất sổ tvxn
     *
     * @param startDate
     * @param endDate
     * @param services
     * @param objects
     * @param siteID
     * @param code
     * @return
     */
    public List<HtcVisitEntity> findHtcVisitBook(Date startDate, Date endDate, List<String> services, List<String> objects, Long siteID, String code) {
        List<HtcVisitEntity> models = htcVisitRepository.findHtcVisitBook(startDate, endDate, services, objects, siteID, code);
        return getAddress(models);
    }

    //Sổ xét nghiệm sàng lọc
    public List<HtcVisitEntity> findHtcTestBook(Date startDate, Date endDate, List<String> services, List<String> objects, Long siteID, String code) {
        List<HtcVisitEntity> models = htcVisitRepository.findHtcTestBook(startDate, endDate, services, objects, siteID, code);
        return getAddress(models);
    }

    /**
     * Search danh sách chuyển gửi thành công
     *
     * @param startDate
     * @param endDate
     * @param services
     * @param objects
     * @param siteID
     * @param code
     * @return
     */
    public List<HtcVisitEntity> findTransferSuccess(Date startDate, Date endDate, List<String> services, List<String> objects, Long siteID, String code) {
        return getAddress(htcVisitRepository.findTransferSuccess(startDate, endDate, services, objects, siteID, code));
    }

    /**
     * Lấy full thông tin địa chỉ
     *
     * @auth vvThành
     * @param entities
     * @return
     */
    public List<HtcVisitEntity> getAddress(List<HtcVisitEntity> entities) {

        if (entities == null || entities.isEmpty()) {
            return null;
        }
        Set<String> pIDs = new HashSet<>();
        pIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getPermanentProvinceID")));
        pIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getCurrentProvinceID")));
        Map<String, ProvinceEntity> provinces = new HashMap<>();
        for (ProvinceEntity model : provinceRepository.findByIDs(pIDs)) {
            provinces.put(model.getID(), model);
        }

        Set<String> dIDs = new HashSet<>();
        dIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getPermanentDistrictID")));
        dIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getCurrentDistrictID")));
        Map<String, DistrictEntity> districts = new HashMap<>();
        for (DistrictEntity model : districtRepository.findByIDs(dIDs)) {
            districts.put(model.getID(), model);
        }

        Set<String> wIDs = new HashSet<>();
        wIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getPermanentWardID")));
        wIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getCurrentWardID")));
        Map<String, WardEntity> wards = new HashMap<>();
        for (WardEntity model : wardRepository.findByIDs(wIDs)) {
            wards.put(model.getID(), model);
        }

        for (HtcVisitEntity entity : entities) {
            //Địa chỉ thường trú
            entity.setPermanentAddressFull(buildAddress(
                    entity.getPermanentAddress(),
                    entity.getPermanentAddressStreet(),
                    entity.getPermanentAddressGroup(),
                    provinces.getOrDefault(entity.getPermanentProvinceID(), null),
                    districts.getOrDefault(entity.getPermanentDistrictID(), null),
                    wards.getOrDefault(entity.getPermanentWardID(), null)
            ));
            //Địa chỉ hiện tại
            entity.setCurrentAddressFull(buildAddress(
                    entity.getCurrentAddress(),
                    entity.getCurrentAddressStreet(),
                    entity.getCurrentAddressGroup(),
                    provinces.getOrDefault(entity.getCurrentProvinceID(), null),
                    districts.getOrDefault(entity.getCurrentDistrictID(), null),
                    wards.getOrDefault(entity.getCurrentWardID(), null)
            ));
        }

        return entities;
    }

    /**
     * Check existing code and site id when update customer
     *
     * @param code
     * @param id
     * @param siteID
     * @return
     */
    public Boolean checkExistCodeUpdate(String code, Long id, Long siteID) {
        return htcVisitRepository.checkExistingCode(code, id, siteID) > 0;
    }

    /**
     * Danh sách số người được xét nghiệm HIV dương tính sử dụng cho báo cáo
     * TT03
     *
     * @auth vvThành
     * @param siteID
     * @param serviceID
     * @param objectGroupID
     * @param testResultsID
     * @param startTime
     * @param endTime
     * @return
     */
    public List<HtcVisitEntity> findSoNguoiDuocXetNghiemHIV(Set<Long> siteID, Set<String> serviceID, Set<String> objectGroupID, Set<String> testResultsID, String startTime, String endTime, String customerType) {
        List<HtcVisitEntity> models = null;
        if (testResultsID == null) {
            models = htcVisitRepository.findSoNguoiDuocXetNghiemHIV(
                    siteID, serviceID, objectGroupID,
                    TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, startTime),
                    TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, endTime), customerType);
        } else {
            models = htcVisitRepository.findSoNguoiDuocXetNghiemDuongTinhHIV(
                    siteID, serviceID, objectGroupID, testResultsID,
                    TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, startTime),
                    TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, endTime), customerType);
        }
        return getAddress(models);
    }

    /**
     * Danh sách nhận kết quả theo thông tư 03 - bảng 02
     *
     * @auth vvThành
     * @param siteID
     * @param serviceID
     * @param objectGroupID
     * @param testResultsID
     * @param startTime
     * @param endTime
     * @return
     */
    public List<HtcVisitEntity> findNhanKetQuaXetNghiemHIV(Set<Long> siteID, Set<String> serviceID, Set<String> objectGroupID, String testResultsID, String startTime, String endTime, String customerType) {
        List<HtcVisitEntity> models = null;
        if (testResultsID == null) {
            models = htcVisitRepository.findNhanKetQuaXetNghiemHIV(
                    siteID, serviceID, objectGroupID,
                    TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, startTime),
                    TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, endTime), customerType);
        } else {
            models = htcVisitRepository.findNhanKetQuaXetNghiemDuongTinhHIV(
                    siteID, serviceID, objectGroupID,
                    TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, startTime),
                    TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, endTime), customerType);
        }
        return getAddress(models);
    }

    /**
     * Danh sách khách hàng lấy theo tt09 phụ lục 02
     *
     * @param siteID
     * @param serviceID
     * @param objectGroupID
     * @param confirmResultsID
     * @param yearOfBirthFrom
     * @param gender
     * @param yearOfBirthTo
     * @param startTime
     * @param endTime
     * @return
     */
    public List<HtcVisitEntity> findPhuLuc01TT09(Set<Long> siteID, Set<String> serviceID, Set<String> objectGroupID, String confirmResultsID, int yearOfBirthFrom, int yearOfBirthTo, Set<String> gender, String startTime, String endTime, String customerType) {
        return getAddress(htcVisitRepository.findPhuLuc01TT09(siteID, serviceID, objectGroupID, confirmResultsID, yearOfBirthFrom, yearOfBirthTo, gender,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, startTime),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, endTime), customerType));
    }

    /**
     * Danh sách htc visit theo ids
     *
     * @auth vvThành
     * @param IDs
     * @return
     */
    public List<HtcVisitEntity> findAllByIDs(Set<Long> IDs) {
        List<HtcVisitEntity> models = IteratorUtils.toList(htcVisitRepository.findAllById(IDs).iterator());
        return models;
    }

    /**
     * Danh sách log theo bệnh nhân
     *
     * @auth vvThành
     * @param oID
     * @return
     */
    public List<HtcVisitLogEntity> getLogs(Long oID) {
        return logRepository.findByVisitID(oID);
    }

    /**
     * @author DSNAnh
     * @param confirmTestNo
     * @param id
     * @param siteID
     * @return
     */
    public boolean checkExistingConfirmTestNo(String confirmTestNo, Long id, Long siteID) {
        return htcVisitRepository.countByConfirmTestNoAndSiteID(confirmTestNo, id, siteID) > 0;
    }

    /**
     * @auth vvThành
     * @return
     */
    public List<HtcVisitEntity> findJobCanUpdateStatus() {
        return visitRepositoryImpl.findJobCanUpdateStatus();
    }

    /**
     * Xóa khách hàng vĩnh viễn
     *
     * @author pdThang
     * @param ID
     */
    public void remove(Long ID) {
        htcVisitRepository.delete(findOne(ID));
    }

    /**
     * Thêm mới khách hàng từ tiếp nhận không chuyên
     *
     * Thêm dữ liệu vào bảng HtcVisitLaytest, HtcVisit
     *
     * @param siteName
     * @auth TrangBN
     * @param htcVisitEntity
     * @param laytest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public HtcVisitEntity save(HtcVisitEntity htcVisitEntity, HtcVisitLaytestEntity laytest, String siteName) {
        try {
            // Lưu thông tin vào HtcService
            htcVisitEntity = saveValidate(htcVisitEntity);
            log(htcVisitEntity.getID(), String.format("Tiếp nhận xét nghiệm tại cộng đồng từ cơ sở %s", siteName));
            if (htcVisitEntity == null) {
                throw new Exception("HtcVisitEntity cannot be null");
            }
            // Lưu thông tin và HtcVisitLaytest
            laytest.setID(htcVisitEntity.getID());
            laytest = htcVisitLaytestService.save(laytest);
            if (laytest == null) {
                throw new Exception("HtcVisitLaytestEntity cannot be null");
            }
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return htcVisitEntity;
    }

    /**
     * Lưu bảng HtcVisitEntity không có transactional
     *
     * @param condition
     * @return
     * @throws Exception
     */
    private HtcVisitEntity saveValidate(HtcVisitEntity condition) throws Exception {
        // Nguồn tiếp cận cộng đồng
        final String APPROACHER_NO = "1";
        // Bạn tình bạn chích
        final String INJECTION_PARTNER = "2";
        // Kết quả có phản ứng
        final String REACTIVE_RESULT = "2";
        // Kết quả không xác định
        final String UN_SPECIFIED = "3";
        // Kết quả không phản ứng
        final String NON_REACTIVE_RESULT = "1";
        // Có thể BHYT 
        final String NO_HEALTH_INSURANCE = "0";

        HtcVisitEntity model = new HtcVisitEntity();
        model.setSiteID(condition.getSiteID());

        HtcVisitEntity entityLoad;
        try {
            Date currentDate = new Date();
            String existCode = "";
            if (condition.getID() == null || condition.getID() == 0) {
                model.setCreateAT(currentDate);
                if (getCurrentUser() != null && getCurrentUser().getSite().getID().equals(model.getSiteID())) {
                    model.setCreatedBY(getCurrentUserID());
                }
                model.setCode(TextUtils.removemarks(condition.getCode()).toUpperCase());
            } else {
                entityLoad = findOne(condition.getID());
                model = entityLoad;
                existCode = model.getCode();
            }

            model.set(condition);

            if (getCurrentUser() != null) {
                model.setProjectID(getCurrentUser().getSite().getProjectID());
            }
            if (getCurrentUser() != null && getCurrentUser().getSite().getID().equals(model.getSiteID())) {
                model.setUpdatedBY(getCurrentUserID());
            }
            model.setUpdateAt(currentDate);

            // Set dữ liệu sau khi validate
            if (StringUtils.isNotEmpty(existCode)) {
                model.setCode(existCode);
            } else {
                model.setCode(condition.getCode());
            }

            // Không có BHYT set null
            if (StringUtils.isBlank(condition.getHasHealthInsurance()) || NO_HEALTH_INSURANCE.equals(condition.getHasHealthInsurance())) {
                model.setHealthInsuranceNo(null);
            } else {
                model.setHealthInsuranceNo(condition.getHealthInsuranceNo());
            }

            // Set dữ liệu ràng buộc cho nguồn tiếp cận
            if (model.getReferralSource() != null && !model.getReferralSource().isEmpty() && model.getReferralSource().contains(INJECTION_PARTNER)) {
                model.setYouInjectCode(condition.getYouInjectCode());
            } else if (model.getReferralSource() != null && !model.getReferralSource().isEmpty()) {
                model.setYouInjectCode(null);
            }

            if (model.getReferralSource() != null && !model.getReferralSource().isEmpty() && model.getReferralSource().contains(APPROACHER_NO)) {
                model.setApproacherNo(condition.getApproacherNo());
            } else if (model.getReferralSource() != null && !model.getReferralSource().isEmpty()) {
                model.setApproacherNo(null);
            }

            if (REACTIVE_RESULT.equals(model.getTestResultsID()) && model.getIsAgreeTest()) {
                model.setTestNoFixSite(StringUtils.isNotEmpty(model.getTestNoFixSite()) ? model.getTestNoFixSite() : model.getCode().toUpperCase());
            }

            if (NON_REACTIVE_RESULT.equals(model.getTestResultsID())) {
                model.setIsAgreeTest(null);
            }

            if (TestResultEnum.KHONG_PHAN_UNG.getKey().equals(model.getTestResultsID()) || TestResultEnum.KHONG_RO.getKey().equals(model.getTestResultsID())) {
                model.setResultsTime(condition.getResultsTime());
            } else {
                model.setResultsTime(null);
            }

            model.setConfirmTime(condition.getConfirmTime());
            model.setResultsSiteTime(condition.getResultsSiteTime());
            model.setResultsPatienTime(condition.getResultsPatienTime());

            // B1. Đồng ý XN sàng lọc * là không đồng ý
            if (StringUtils.isBlank(model.getIsAgreePreTest()) || "0".equals(model.getIsAgreePreTest())) {
                model.setPreTestTime(null);
                model.setTestResultsID(null);
                model.setIsAgreeTest(null);
            }

            // Không đồng ý xét nghiệm khẳng định
            if (model.getIsAgreeTest() == null
                    || ((REACTIVE_RESULT.equals(model.getTestResultsID()) || (UN_SPECIFIED.equals(model.getTestResultsID()))) && model.getIsAgreeTest() != null && !model.getIsAgreeTest())
                    || NON_REACTIVE_RESULT.equals(model.getTestResultsID())) {

                model.setSiteConfirmTest(null);
                model.setTestNoFixSite(null);
                model.setConfirmTestNo(null);
                model.setConfirmResultsID(null);
                model.setConfirmTime(null);
                model.setResultsSiteTime(null);
                model.setResultsPatienTime(null);
                model.setEarlyHiv(null);
                model.setEarlyHivDate(null);
                model.setVirusLoad(null);
                model.setVirusLoadDate(null);
                model.setExchangeConsultTime(null);
                model.setPartnerProvideResult(null);
                model.setArvExchangeResult(null);
                model.setExchangeTime(null);
                model.setExchangeProvinceID(null);
                model.setExchangeDistrictID(null);
                model.setArrivalSite(null);
                model.setArrivalSiteID(null);
                model.setRegisterTherapyTime(null);
                model.setTherapyRegistProvinceID(null);
                model.setTherapyRegistDistrictID(null);
                model.setRegisteredTherapySite(null);
                model.setTherapyNo(null);
            }

            // Kết quả xét nghiệm khẳng định là âm tính hoặc không xác định hoặc chưa nhập
            if (StringUtils.isEmpty(model.getArvExchangeResult()) && model.getExchangeConsultTime() == null) {
                model.setExchangeConsultTime(null);
                model.setPartnerProvideResult(null);
                model.setArvExchangeResult(null);
                model.setExchangeTime(null);
                model.setExchangeProvinceID(null);
                model.setExchangeDistrictID(null);
                model.setArrivalSite(null);
                model.setArrivalSiteID(null);
                model.setRegisterTherapyTime(null);
                model.setTherapyRegistProvinceID(null);
                model.setTherapyRegistDistrictID(null);
                model.setRegisteredTherapySite(null);
                model.setTherapyNo(null);
            }

            // Set giá trị Mã XN khẳng định ( trống -> copy Mã do CS XN sàng lọc cấp)
            if (StringUtils.isEmpty(model.getConfirmTestNo()) && StringUtils.isNotEmpty(model.getTestNoFixSite())) {
                model.setConfirmTestNo(model.getTestNoFixSite().toUpperCase());
            }

            // Trường hợp không có ngày khách hàng nhận kết quả
            if (model.getResultsPatienTime() == null) {
                model.setExchangeConsultTime(null);
                model.setPartnerProvideResult(null);
                model.setArvExchangeResult(null);
                model.setExchangeTime(null);
                model.setExchangeProvinceID(null);
                model.setExchangeDistrictID(null);
                model.setArrivalSite(null);
                model.setArrivalSiteID(null);
                model.setRegisterTherapyTime(null);
                model.setTherapyRegistProvinceID(null);
                model.setTherapyRegistDistrictID(null);
                model.setRegisteredTherapySite(null);
                model.setTherapyNo(null);
            }

            // Trường hợp D1a. Kết quả TV CGĐT ARV: * chưa đồng ý
            if ("2".equals(model.getArvExchangeResult())) {
                model.setExchangeProvinceID(null);
                model.setExchangeDistrictID(null);
                model.setArrivalSite(null);
                model.setArrivalSiteID(null);
                model.setExchangeTime(null);
                model.setRegisteredTherapySite(null);
                model.setTherapyRegistProvinceID(null);
                model.setTherapyRegistDistrictID(null);
                model.setRegisterTherapyTime(null);
                model.setTherapyNo(null);
            }

            // Set trạng thái chuyển gửi điều trị
            if (StringUtils.isNotBlank(condition.getArvExchangeResult()) && "1".equals(condition.getArvExchangeResult()) && StringUtils.isNotBlank(condition.getArrivalSite())
                    && condition.getExchangeTime() != null) {
                model.setTherapyExchangeStatus(TherapyExchangeStatusEnum.DA_CHUYEN.getKey()); // Set trạng thái sang đã chuyển
            }
            if (condition.getRegisterTherapyTime() != null
                    && StringUtils.isNotBlank(condition.getRegisteredTherapySite())
                    && StringUtils.isNotBlank(condition.getTherapyNo())) {
                model.setTherapyExchangeStatus(TherapyExchangeStatusEnum.CHUYEN_THANH_CONG.getKey()); // Trạng thái chuyển gửi thành công
            }

            //12/05/2020
            if (condition.getFeedbackReceiveTime() == null && condition.getArrivalTime() != null && StringUtils.isNotEmpty(condition.getArvExchangeResult()) && condition.getArvExchangeResult().equals(ArvExchangeEnum.DONG_Y.getKey())) {
                model.setTherapyExchangeStatus(TherapyExchangeStatusEnum.DA_TIEP_NHAN.getKey());
            }

            model.setBioName(condition.getBioName());
            model.setPatientName(TextUtils.toFullname(condition.getPatientName()));
            model.setConfirmResutl(condition.isConfirmResutl());

            model.setStaffKC(condition.getStaffKC());
            model.setLaoVariable(condition.getLaoVariable());
            model.setLaoVariableName(condition.getLaoVariableName());
            model.setCustomerType(condition.getCustomerType());

            model.setPqmCode(null);
            model.setEarlyJob(false);
            htcVisitRepository.save(model);
            model.setQrcode(qrCodeService.generate(model.getPatientID(), model.getPatientName(), model.getGenderID(), null, model.getRaceID()));
        } catch (Exception e) {
            throw e;
        }
        return model;
    }

    /**
     * Find by code
     *
     * @auth vvThành
     * @param code
     * @param isAddress
     * @return
     */
    public HtcVisitEntity findCode(String code, boolean isAddress) {
        HtcVisitEntity entity = htcVisitRepository.findByCode(code);
        if (entity != null && isAddress) {
            List<HtcVisitEntity> models = new ArrayList<>();
            models.add(entity);
            entity = getAddress(models).get(0);
        }
        return entity;
    }

    /**
     * Generate mã khách hàng theo cơ sở
     *
     * @auth TrangBN
     * @return
     */
    public String getCode() {
        LoggedUser currentUser = getCurrentUser();
        String code = currentUser.getSite().getCode();
        HashMap<String, String> staffConfig = parameterService.getStaffConfig(currentUser.getUser().getID());
        if (!staffConfig.getOrDefault(StaffConfigEnum.HTC_STAFF_CODE.getKey(), "").equals("")) {
            code = staffConfig.get(StaffConfigEnum.HTC_STAFF_CODE.getKey());
        }

        HtcVisitEntity visit = htcVisitRepository.findLastCodeBySiteID(currentUser.getSite().getID(), code);
        if (visit == null || StringUtils.isEmpty(visit.getCode()) || !visit.getCode().contains("-") || !StringUtils.containsIgnoreCase(visit.getCode(), code)) {
            return String.format("%s-%s", code, InitCodeEnum.RIGHT_PADDING.getKey()).toUpperCase();
        }

        try {
            String[] split = visit.getCode().split("-", -1);
            Long identity = Long.valueOf(split[split.length - 1]);
            if (identity.toString().length() < 7) {
                String leftPad = StringUtils.leftPad(String.valueOf(identity + 1), 7, "0");
                return String.format("%s-%s", code, leftPad).toUpperCase();
            } else {
                return String.format("%s-%s", code, String.valueOf((identity + 1))).toUpperCase();
            }
        } catch (Exception e) {
            return String.format("%s-%s", code, InitCodeEnum.RIGHT_PADDING.getKey()).toUpperCase();
        }
    }

    /**
     * Find by code
     *
     * @auth pdThang
     * @param siteID
     * @param code
     * @param isAddress
     * @return
     */
    public HtcVisitEntity findByCodeAndSite(String code, Long siteID, boolean isAddress) {
        HtcVisitEntity entity = htcVisitRepository.findByCodeAndSiteID(code, siteID);
        if (entity != null && isAddress) {
            List<HtcVisitEntity> models = new ArrayList<>();
            models.add(entity);
            entity = getAddress(models).get(0);
        }
        return entity;
    }

    /**
     * Lưu thông tin danh sách khách hàng HTC
     *
     * @author TrangBN
     * @param entities
     * @return
     */
    public List<HtcVisitEntity> saveAll(List<HtcVisitEntity> entities) {
        Iterable<HtcVisitEntity> all = htcVisitRepository.saveAll(entities);
        List<HtcVisitEntity> copy = ImmutableList.copyOf(all);
        return copy;
    }

    /**
     * Tìm kiếm khách hàng
     *
     * @author pdThang
     * @param searchInput
     * @return
     */
    public DataPage<HtcVisitEntity> findElog(HtcElogSearch searchInput) {
        DataPage<HtcVisitEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(searchInput.getPageIndex());
        dataPage.setMaxResult(searchInput.getPageSize());
        searchInput.setID(searchInput.getID() == null || searchInput.getID() < 0 ? 0 : searchInput.getID());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"updateAt"});
        if (searchInput.getSortable() != null) {
            sortable = searchInput.getSortable();
        }
        Pageable pageable = PageRequest.of(searchInput.getPageIndex() - 1, searchInput.getPageSize(), sortable);

        Page<HtcVisitEntity> pages = htcVisitRepository.findElog(
                searchInput.getSiteID(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getAdvisoryeTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getAdvisoryeTimeTo()),
                searchInput.getService(),
                searchInput.getResultID(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(getAddress(pages.getContent()));
        return dataPage;
    }

    /**
     * Lấy ra bản ghi đầu tiên
     *
     * @auth vvThành
     * @param siteIds
     * @return
     */
    public HtcVisitEntity getFirst(Set<Long> siteIds) {
        Page<HtcVisitEntity> page = htcVisitRepository.getFirst(siteIds, PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, new String[]{"advisoryeTime"})));
        return page.getTotalElements() == 0 ? null : page.getContent().get(0);
    }

    /**
     * pdThang
     *
     * @param search
     * @return
     */
    public DataPage<HtcVisitLogEntity> findAllLog(LogSearch search) {
        DataPage<HtcVisitLogEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"htcVisitID"});
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<HtcVisitLogEntity> pages = logRepository.findAll(search.getID(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getTo()),
                search.getStaffID(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());
        return dataPage;
    }

    public List<HtcVisitEntity> findTable01MERIntroduced(Long siteID, String genderID, Set<String> serviceID, Set<String> objectGroupIDs, String startTime, String endTime, Integer fromAge, Integer toAge, String flag) {
        List<HtcVisitEntity> models = null;
        models = htcVisitRepository.findTable01MERIntroduced(
                siteID, genderID, serviceID, objectGroupIDs,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, startTime),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, endTime),
                fromAge, toAge, flag);
        return getAddress(models);
    }

    public List<HtcVisitEntity> findTable01MERAgreed(Long siteID, String genderID, Set<String> serviceID, Set<String> objectGroupIDs, String startTime, String endTime, Integer fromAge, Integer toAge, String flag) {
        List<HtcVisitEntity> models = null;
        models = htcVisitRepository.findTable01MERAgreed(
                siteID, genderID, serviceID, objectGroupIDs,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, startTime),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, endTime),
                fromAge, toAge, flag);
        return getAddress(models);
    }

    //Bảng 2 MER
    public List<HtcVisitEntity> findTable02MER(Long siteID, String earlyHiv, String genderID, String serviceID, Set<String> objectGroupID, String startTime, String endTime, Integer fromAge, Integer toAge, String flag) {
        List<HtcVisitEntity> models = null;
        models = htcVisitRepository.findTable02MER(
                siteID, earlyHiv, genderID, serviceID, objectGroupID,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, startTime),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, endTime),
                fromAge, toAge, flag);
        return getAddress(models);
    }

    public List<HtcVisitEntity> findTable03MER(Long siteID, String earlyHiv, String serviceID, String objectGroupID, Set<String> objectGroupIDs, String startTime, String endTime) {
        List<HtcVisitEntity> models = null;
        models = htcVisitRepository.findTable03MER(
                siteID, earlyHiv, serviceID, objectGroupID, objectGroupIDs,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, startTime),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, endTime));
        return getAddress(models);
    }

    public List<HtcVisitEntity> findTable04MERPositive(Long siteID, String genderID, String serviceID, Set<String> objectGroupIDs, String startTime, String endTime, Integer fromAge, Integer toAge, String flag) {
        List<HtcVisitEntity> models = null;
        models = htcVisitRepository.findTable04MERPositive(
                siteID, genderID, serviceID, objectGroupIDs,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, startTime),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, endTime),
                fromAge, toAge, flag);
        return getAddress(models);
    }

    public List<HtcVisitEntity> findTable04MERNegative(Long siteID, String genderID, String serviceID, Set<String> objectGroupIDs, String startTime, String endTime, Integer fromAge, Integer toAge, String flag) {
        List<HtcVisitEntity> models = null;
        models = htcVisitRepository.findTable04MERNegative(
                siteID, genderID, serviceID, objectGroupIDs,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, startTime),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, endTime),
                fromAge, toAge, flag);
        return getAddress(models);
    }

    public List<HtcVisitEntity> findTable05MERPositive(Long siteID, String serviceID, String objectGroupID, Set<String> objectGroupIDs, String startTime, String endTime) {
        List<HtcVisitEntity> models = null;
        models = htcVisitRepository.findTable05MERPositive(
                siteID, serviceID, objectGroupID, objectGroupIDs,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, startTime),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, endTime));
        return getAddress(models);
    }

    public List<HtcVisitEntity> findTable05MERNegative(Long siteID, String serviceID, String objectGroupID, Set<String> objectGroupIDs, String startTime, String endTime) {
        List<HtcVisitEntity> models = null;
        models = htcVisitRepository.findTable05MERNegative(
                siteID, serviceID, objectGroupID, objectGroupIDs,
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, startTime),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, endTime));
        return getAddress(models);
    }

    /**
     * DS KH chuyển gửi sang cơ sở điều trị
     *
     * @author DSNAnh
     * @param searchInput
     * @return
     */
    public DataPage<HtcVisitEntity> findReceiveHtc(HtcSearch searchInput) {
        DataPage<HtcVisitEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(searchInput.getPageIndex());
        dataPage.setMaxResult(searchInput.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"exchangeTime"});
        if (searchInput.getSortable() != null) {
            sortable = searchInput.getSortable();
        }
        Pageable pageable = PageRequest.of(searchInput.getPageIndex() - 1, searchInput.getPageSize(), sortable);

        Page<HtcVisitEntity> pages = htcVisitRepository.findReceiveHtc(
                searchInput.isRemove(),
                searchInput.isRemoveTranfer(),
                searchInput.getName(),
                searchInput.getConfirmTestNo(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getConfirmTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getExchangeTimeFrom()),
                searchInput.getSiteID(),
                searchInput.getReceiveStatus(),
                getCurrentUser().getSite().getID(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(getAddress(pages.getContent()));
        return dataPage;
    }

    public DataPage<HtcVisitEntity> findReceiveHtcConnect(HtcSearch searchInput) {
        DataPage<HtcVisitEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(searchInput.getPageIndex());
        dataPage.setMaxResult(searchInput.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"exchangeTime"});
        if (searchInput.getSortable() != null) {
            sortable = searchInput.getSortable();
        }
        Pageable pageable = PageRequest.of(searchInput.getPageIndex() - 1, searchInput.getPageSize(), sortable);

        Page<HtcVisitEntity> pages = htcVisitRepository.findReceiveHtcConnect(
                false,
                false,
                searchInput.getReceiveStatus(),
                getCurrentUser().getSite().getID(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(getAddress(pages.getContent()));
        return dataPage;
    }

    /**
     * Xóa khách hàng chuyển gửi điều trị
     *
     * @param ID
     */
    public void deleteReceive(Long ID) {
        HtcVisitEntity e = htcVisitRepository.findOne(ID);
        if (e != null) {
            e.setRemoveTranfer(true);
        }
        htcVisitRepository.save(e);
    }

    /**
     * Tính số bản ghi theo điều kiện tìm kiếm
     *
     * @param searchInput
     * @return
     */
    public Long count(HtcSearch searchInput) {
        return htcVisitRepository.find(
                searchInput.getSiteID(),
                searchInput.getCode(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getAdvisoryeTime()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getAdvisoryeTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getAdvisoryeTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getConfirmTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getConfirmTimeTo()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getExchangeTimeFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, searchInput.getExchangeTimeTo()),
                searchInput.getName(),
                searchInput.getServiceID(),
                searchInput.getObjectGroupID(),
                searchInput.getTherapyExchangeStatus(),
                searchInput.getConfirmTestStatus(),
                searchInput.getFeedbackStatus(),
                searchInput.getConfirmResultsID(),
                searchInput.getTestResultsID(),
                searchInput.isConfirm(),
                searchInput.isOpc(),
                searchInput.isRemove(),
                searchInput.isNotReceive(),
                searchInput.isCofirmCreated(),
                searchInput.getGsphStatus(),
                searchInput.getObjectGroupID(),
                searchInput.getFilter(),
                searchInput.getCreatedBY());
    }

    /**
     * @auth vvThành
     * @param code
     * @return
     */
    public HtcVisitEntity findByQR(String code) {
        List<HtcVisitEntity> visits = htcVisitRepository.findByQR(code);
        if (visits != null) {
            getAddress(visits);
        }
        return visits == null || visits.isEmpty() ? null : visits.get(0);
    }

    public List<HtcVisitEntity> findByHub(Set<Long> siteIds, int size) {
        return htcVisitRepository.getForUpdateHub(siteIds, PageRequest.of(0, size));
    }

    public HtcVisitEntity update(HtcVisitEntity condition) {
        return htcVisitRepository.save(condition);
    }

    public List<HtcVisitEntity> findByEarlyJob(boolean earlyJob) {
        return htcVisitRepository.findByEarlyJob(earlyJob);
    }
}
