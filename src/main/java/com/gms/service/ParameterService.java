package com.gms.service;

import com.gms.components.annotation.interf.FieldParameterExists;
import com.gms.components.annotation.interf.FieldValueExists;
import com.gms.entity.bean.DataPage;
import com.gms.entity.constant.BooleanEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.constant.SiteConfigEnum;
import com.gms.entity.constant.StaffConfigEnum;
import com.gms.entity.db.ParameterEntity;
import com.gms.repository.ParameterRepository;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
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
 * @author vvthanh
 */
@Service
public class ParameterService extends BaseService implements FieldValueExists, FieldParameterExists {

    @Autowired
    private ParameterRepository parameterRepository;

    /**
     * ParentID, Position, Status mặc định bằng 0. CreateAT và UpdateAT tự động
     * theo current Date. CreateBy và UpdateBy tự động theo current User đăng
     * nhập.
     *
     * @author vvthanh
     * @param entity
     * @return Boolean
     */
    public ParameterEntity save(ParameterEntity entity) {
        try {
            ParameterEntity model = null;
            if (entity.getID() == null) {
                model = new ParameterEntity();
                model.setCreateAT(new Date());
                model.setCreatedBY(getCurrentUserID());
                model.setCode(entity.getCode());
            } else {
                model = findOne(entity.getID());
            }
            model.setUpdateAt(new Date());
            model.setUpdatedBY(getCurrentUserID());
            model.set(entity);

            model.setParentID(model.getParentID() == null ? 0 : model.getParentID());
            model.setPosition(model.getPosition() == null ? 0 : model.getPosition());
            model.setStatus(model.getStatus() == null ? 0 : model.getStatus());

            return parameterRepository.save(model);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            getLogger().error(e.getMessage());
            return null;
        }
    }

    /**
     * Danh sách Parameter theo mảng Type
     *
     * @author vvthanh
     * @param types
     * @return List<ParameterEntity>
     */
    public List<ParameterEntity> findByTypes(Set<String> types) {
        return parameterRepository.findByTypes(types);
    }

    /**
     * Danh sách Parameter theo type
     *
     * @author vvthanh
     * @param type
     * @return
     */
    public List<ParameterEntity> findByType(String type) {
        return parameterRepository.findByType(type);
    }

    /**
     * Phân trang dữ liệu thep parameter type
     *
     * @author vvthanh
     * @param code
     * @param value
     * @param siteID
     * @param type
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public DataPage<ParameterEntity> findByType(String code, String value, Long siteID, String type, String attribute01, String attribute02,String attribute03,String attribute04,String attribute05, int pageIndex, int pageSize) {
        DataPage<ParameterEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(pageIndex);
        dataPage.setMaxResult(pageSize);

        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"position"});
        Pageable pageable = PageRequest.of(dataPage.getCurrentPage() - 1, dataPage.getMaxResult(), sortable);
        Page<ParameterEntity> pages = parameterRepository.findByType(code, value, siteID, type, attribute01, attribute02,attribute03,attribute04,attribute05, pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    public DataPage<ParameterEntity> findByTypeAndParent(String code, String value, Long siteID, Long parentID, String type, int pageIndex, int pageSize) {
        DataPage<ParameterEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(pageIndex);
        dataPage.setMaxResult(pageSize);

        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"position"});
        Pageable pageable = PageRequest.of(dataPage.getCurrentPage() - 1, dataPage.getMaxResult(), sortable);
        Page<ParameterEntity> pages = parameterRepository.findByType(code, value, siteID, parentID, type, pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    /**
     * Tham số theo type và code
     *
     * @auth vvThành
     * @param type
     * @param code
     * @return
     */
    public ParameterEntity findOne(String type, String code) {
        return parameterRepository.findByTypeAndCode(type, code);
    }

    /**
     * Danh sách tham số theo danh sách code
     *
     * @auth vvThành
     * @param type
     * @param codes
     * @return
     */
    public List<ParameterEntity> findByCodes(String type, Set<String> codes) {
        return parameterRepository.findByTypeAndCode(type, codes);
    }

    /**
     * Tham số theo ID
     *
     * @auth vvThành
     * @param ID
     * @return
     */
    public ParameterEntity findOne(Long ID) {
        Optional<ParameterEntity> optional = parameterRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     * Xoá tham số
     *
     * @auth vvThành
     * @param entity
     */
    public void remove(ParameterEntity entity) {
        parameterRepository.delete(entity);
    }

    /**
     * Validate: Kiểm tra tồn tại FK
     *
     * @author vvthanh
     * @param value
     * @param fieldName
     * @return
     * @throws UnsupportedOperationException
     */
    @Override
    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {
        if (value != null && !"".equals(value) && !value.toString().equals("0") && fieldName.equals("parentID")) {
            Optional<ParameterEntity> optional = parameterRepository.findById(new Long(value.toString()));
            return optional.isPresent();
        }
        return true;
    }

    /**
     * Validate code exits
     *
     * @param code
     * @param type
     * @param mutiple
     * @param fieldName
     * @return
     * @throws UnsupportedOperationException
     */
    @Override
    public boolean fieldParameterExists(Object code, String type, boolean mutiple, String fieldName) throws UnsupportedOperationException {
        if (code != null && !"".equals(code) && !mutiple) {
            return parameterRepository.existsByTypeAndCode(type, String.valueOf(code));
        } else if (code != null && !"".equals(code) && mutiple) {
            try {
                List<String> codes = (List<String>) code;
                List<ParameterEntity> entitys = parameterRepository.findByTypeAndCode(type, new HashSet<>(codes));
                for (ParameterEntity entity : entitys) {
                    if (!codes.contains(entity.getCode())) {
                        return false;
                    }
                }
            } catch (Exception e) {
                //getLogger().error(e.getMessage());
                return false;
            }

        }
        return true;
    }

    /**
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getServices() {
        return findByType(ParameterEntity.SERVICE);
    }

    /**
     * @auth vvThành
     * @param code
     * @return
     */
    public ParameterEntity getServiceByID(String code) {
        return findOne(ParameterEntity.SERVICE, code);
    }

    /**
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getGenders() {
        return findByType(ParameterEntity.GENDER);
    }

    /**
     * @auth vvThành
     * @param code
     * @return
     */
    public ParameterEntity getGender(String code) {
        return findOne(ParameterEntity.GENDER, code);
    }

    /**
     * @auth vvThành
     * @param entitys
     * @return
     */
    public Iterable<ParameterEntity> save(List<ParameterEntity> entitys) {
        for (ParameterEntity entity : entitys) {
            if (entity.getID() == null) {
                entity.setCreateAT(new Date());
                entity.setCreatedBY(getCurrentUserID());
                entity.setCode(entity.getCode());
            }
            entity.setUpdateAt(new Date());
            entity.setUpdatedBY(getCurrentUserID());

            entity.setParentID(entity.getParentID() == null ? 0 : entity.getParentID());
            entity.setPosition(entity.getPosition() == null ? 0 : entity.getPosition());
            entity.setStatus(entity.getStatus() == null ? 0 : entity.getStatus());
        }
        return parameterRepository.saveAll(entitys);
    }

    /**
     * Kết quả xét nghiệm
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getTestResult() {
        return findByType(ParameterEntity.TEST_RESULTS);
    }

    /**
     * Dịch vụ sau xét nghiệm
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getServiceAfterTest() {
        return findByType(ParameterEntity.SERVICE_AFTER_TEST);
    }

    /**
     * Dịch vụ Elog
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getServiceTest() {
        return findByType(ParameterEntity.SERVICE_TEST);
    }

    /**
     * Công việc
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getJob() {
        return findByType(ParameterEntity.JOB);
    }

    /**
     * Chủng tộc
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getRaces() {
        return findByType(ParameterEntity.RACE);
    }

    /**
     * Đường lây nhiễm
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getModesOfTransmision() {
        return findByType(ParameterEntity.MODE_OF_TRANSMISSION);
    }

    /**
     * Nguy cơ lây nhiễm
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getRiskBehavior() {
        return findByType(ParameterEntity.RISK_BEHAVIOR);

    }

    /**
     * Nơi xét nghiệm
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getPlaceTest() {
        return findByType(ParameterEntity.PLACE_TEST);
    }

    /**
     * Danh sách triệu trứng
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getSysmptom() {
        return findByType(ParameterEntity.SYSMPTOM);
    }

    /**
     * Phác đồ điều trị
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getTreatmentRegimen() {
        return findByType(ParameterEntity.TREATMENT_REGIMEN);
    }

    /**
     * Lấy phác đồ điều trị theo OPC
     *
     * @param child trẻ con
     * @param adults người lớn
     * @return
     */
    public List<ParameterEntity> getTreatmentRegimen(boolean child, boolean adults) {
        return parameterRepository.findByType(ParameterEntity.TREATMENT_REGIMEN, child ? "1" : null, adults ? "1" : null);
    }
    
    public List<ParameterEntity> getTreatmentRegimen(boolean level1, boolean level2, boolean level3) {
        return parameterRepository.findByType(ParameterEntity.TREATMENT_REGIMEN, level1 ? "1" : null, level2 ? "1" : null, level3 ? "1" : null);
    }

    /**
     * Cơ sở điều trị
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getTreatmentFacility() {
        return findByType(ParameterEntity.TREATMENT_FACILITY);
    }

    /**
     * Nguyên nhân tử vong
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getCauseOfDeath() {
        return findByType(ParameterEntity.CAUSE_OF_DEATH);
    }

    /**
     * Nơi lấy máu
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getBloodBase() {
        return findByType(ParameterEntity.BLOOD_BASE);
    }

    /**
     * Địa điểm giám sát
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getLocationMonitoring() {
        return findByType(ParameterEntity.LOCATION_MONITORING);
    }

    /**
     * Sinh phẩm xét nghiệm
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getBiologyProductTest() {
        return findByType(ParameterEntity.BIOLOGY_PRODUCT_TEST);
    }

    /**
     * Sinh kết quả XN khẳng định
     *
     * @auth TrangBN
     * @return
     */
    public List<ParameterEntity> getConfirmTestResult() {
        return findByType(ParameterEntity.TEST_RESULTS_CONFIRM);
    }

    /**
     * Bệnh lây truyền
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getCommunicableDiseas() {
        return findByType(ParameterEntity.COMMUNICABLE_DISEAS);
    }

    /**
     * Vị trí lấy mẫu
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getLocationOfBlood() {
        return findByType(ParameterEntity.LOCATION_OF_BLOOD);
    }

    /**
     * Đối tượng xét nghiệm
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getTestObjectGroup() {
        return findByType(ParameterEntity.TEST_OBJECT_GROUP);
    }

    /**
     * Nguồn giới thiệu tham số hệ thống
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getReferentSource() {
        return findByType(ParameterEntity.REFERENT_SOURCE);
    }

    /**
     * Hướng dẫn sử dụng
     *
     * @return
     */
    public List<ParameterEntity> getUserManual() {
        return findByType(ParameterEntity.SYSTEMS_USER_MANUAL);
    }

    /**
     * Hiện trạng cư trú - tình trạng hiện tại
     *
     * @return
     */
    public List<ParameterEntity> getStatusOfResident() {
        return findByType(ParameterEntity.STATUS_OF_RESIDENT);
    }

    /**
     * Loại bệnh nhân
     *
     * @return
     */
    public List<ParameterEntity> getTypeOfPatient() {
        return findByType(ParameterEntity.TYPE_OF_PATIENT);
    }

    /**
     * Trạng thái điều trị
     *
     * @return
     */
    public List<ParameterEntity> getStatusOfTreatment() {
        return findByType(ParameterEntity.STATUS_OF_TREATMENT);
    }

    /**
     * Lấy kết quả sinh phẩm test
     *
     * @return
     */
    public List<ParameterEntity> getBioNameResult() {
        return findByType(ParameterEntity.BIO_NAME_RESULT);
    }

    /**
     * Dự án tài trợ
     *
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getSiteProject() {
        return findByType(ParameterEntity.SITE_PROJECT);
    }

    /**
     * Lấy tên sinh phẩm
     *
     * @return
     */
    public List<ParameterEntity> getBioProduct() {
        return findByType(ParameterEntity.BIOLOGY_PRODUCT_TEST);
    }

    /**
     * Danh sách theo option trạng thái active
     *
     * @param parameterTypes
     * @param defaultOptions
     * @return
     */
    public HashMap<String, HashMap<String, String>> getOptionsByTypes(Set<String> parameterTypes, HashMap<String, String> defaultOptions) {
        return getOptionsByTypes(parameterTypes, defaultOptions, true);
    }

    /**
     * Danh sách giá trị tham số theo type
     *
     * @param type
     * @param active
     * @return
     */
    public HashMap<String, String> getOptionsByType(String type, boolean active) {
        Set<String> parameterTypes = new HashSet<>();
        parameterTypes.add(type);
        return getOptionsByTypes(parameterTypes, null, active).get(type);
    }

    /**
     * Chuyển đổi về dạng option
     *
     * @param parameterTypes
     * @param defaultOptions
     * @param active
     * @return
     */
    public HashMap<String, HashMap<String, String>> getOptionsByTypes(Set<String> parameterTypes, HashMap<String, String> defaultOptions, boolean active) {
        HashMap<String, HashMap<String, String>> options = new HashMap<>();
        List<ParameterEntity> entities = findByTypes(parameterTypes);
        for (ParameterEntity entity : entities) {
            if (active && entity.getStatus() == 0) {
                continue;
            }
            if (options.get(entity.getType()) == null) {
                options.put(entity.getType(), new LinkedHashMap<String, String>());
                if (defaultOptions != null && !defaultOptions.isEmpty()) {
                    options.get(entity.getType()).put("", String.format("Chọn %s", defaultOptions.get(entity.getType()) == null ? "" : defaultOptions.get(entity.getType()).toLowerCase()));
                }
            }
            if (entity.getType().equals(ParameterEntity.SERVICE_TEST)) {
                options.get(entity.getType()).put(entity.getCode(), entity.getValue());
            } else {
                options.get(entity.getType()).put(entity.getCode(), entity.getValue());
            }
        }
        return options;
    }

    /**
     * Site config
     *
     * @auth vvThành
     * @param siteID
     * @return
     */
    public HashMap<String, String> getSiteConfig(Long siteID) {
        HashMap<String, String> optionsByType = getOptionsByType(String.format("site_%s", siteID), !true);
        if (optionsByType == null) {
            optionsByType = new HashMap<>();
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.BIOLOGY_PRODUCT_TEST.getKey(), null) == null) {
            optionsByType.put(SiteConfigEnum.BIOLOGY_PRODUCT_TEST.getKey(), "");
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.HTC_BIOLOGY_PRODUCT.getKey(), null) == null) {
            optionsByType.put(SiteConfigEnum.HTC_BIOLOGY_PRODUCT.getKey(), "");
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.LAYTEST_BIOLOGY_PRODUCT.getKey(), null) == null) {
            optionsByType.put(SiteConfigEnum.LAYTEST_BIOLOGY_PRODUCT.getKey(), "");
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.PAC_FROM_CONFIRM.getKey(), "").equals("")) {
            optionsByType.put(SiteConfigEnum.PAC_FROM_CONFIRM.getKey(), String.valueOf(BooleanEnum.TRUE.getKey()));
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.PAC_FROM_HTC.getKey(), "").equals("")) {
            optionsByType.put(SiteConfigEnum.PAC_FROM_HTC.getKey(), String.valueOf(BooleanEnum.TRUE.getKey()));
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.HTC_TICKET_CODE.getKey(), "").equals("")) {
            optionsByType.put(SiteConfigEnum.HTC_TICKET_CODE.getKey(), String.valueOf(BooleanEnum.FALSE.getKey()));
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.HTC_TICKET_CSCQ.getKey(), "").equals("")) {
            optionsByType.put(SiteConfigEnum.HTC_TICKET_CSCQ.getKey(), String.valueOf(BooleanEnum.TRUE.getKey()));
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.HTC_PHIEU_XN.getKey(), "").equals("")) {
            optionsByType.put(SiteConfigEnum.HTC_PHIEU_XN.getKey(), "1");
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.PAC_PDF_KINHGUI.getKey(), "").equals("")) {
            optionsByType.put(SiteConfigEnum.PAC_PDF_KINHGUI.getKey(), String.valueOf(BooleanEnum.FALSE.getKey()));
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.PAC_PDF_SO_NGUOI_NHIEM.getKey(), "").equals("")) {
            optionsByType.put(SiteConfigEnum.PAC_PDF_SO_NGUOI_NHIEM.getKey(), String.valueOf(BooleanEnum.FALSE.getKey()));
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.PAC_PDF_CK2.getKey(), "").equals("")) {
            optionsByType.put(SiteConfigEnum.PAC_PDF_CK2.getKey(), null);
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.CONFIRM_ANSWER_RESULT.getKey(), "").equals("")) {
            optionsByType.put(SiteConfigEnum.CONFIRM_ANSWER_RESULT.getKey(), "1"); //Mặc định mẫu 6B
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.HTC_PRINT_ANSWER_RESULT.getKey(), "").equals("")) {
            optionsByType.put(SiteConfigEnum.HTC_PRINT_ANSWER_RESULT.getKey(), ""); //Mặc định trống
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.CONFIRM_QR.getKey(), "").equals("")) {
            optionsByType.put(SiteConfigEnum.CONFIRM_QR.getKey(), ""); //Mặc định trống
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.VISIT_QR.getKey(), "").equals("")) {
            optionsByType.put(SiteConfigEnum.VISIT_QR.getKey(), ""); //Mặc định trống
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.OPC_QR.getKey(), "").equals("")) {
            optionsByType.put(SiteConfigEnum.OPC_QR.getKey(), ""); //Mặc định trống
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.HTC_RQ_ID.getKey(), "").equals("")) {
            optionsByType.put(SiteConfigEnum.HTC_RQ_ID.getKey(), "0"); //Mặc định 0
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.OPC_REGIMEN.getKey(), "").equals("")) {
            optionsByType.put(SiteConfigEnum.OPC_REGIMEN.getKey(), "");
        }
        return optionsByType;
    }

    /**
     * @auth vvThanh
     * @param siteID
     * @param key
     * @return
     */
    public String getSiteConfig(Long siteID, String key) {
        HashMap<String, String> config = getSiteConfig(siteID);
        return config.getOrDefault(key, "");
    }

    /**
     * Nhân viên cấu hình
     *
     * @auth vvThành
     * @param siteID
     * @return
     */
    public HashMap<String, String> getStaffConfig(Long siteID) {
        HashMap<String, String> optionsByType = getOptionsByType(String.format("staff_%s", siteID), !true);
        if (optionsByType == null) {
            optionsByType = new HashMap<>();
        }
        if (optionsByType.getOrDefault(StaffConfigEnum.LAYTEST_OBJECT_GROUP.getKey(), null) == null) {
            optionsByType.put(StaffConfigEnum.LAYTEST_OBJECT_GROUP.getKey(), "");
        }
        if (optionsByType.getOrDefault(StaffConfigEnum.LAYTEST_SITE_CONFIRM.getKey(), null) == null) {
            optionsByType.put(StaffConfigEnum.LAYTEST_SITE_CONFIRM.getKey(), "");
        }
        if (optionsByType.getOrDefault(StaffConfigEnum.HTC_STAFF_CODE.getKey(), null) == null) {
            optionsByType.put(StaffConfigEnum.HTC_STAFF_CODE.getKey(), "");
        }
        return optionsByType;
    }

    /**
     * Nhân viên cấu hình
     *
     * @auth vvThành
     * @param staffID
     * @param key
     * @return
     */
    public String getStaffConfig(Long staffID, String key) {
        HashMap<String, String> config = getStaffConfig(staffID);
        return config.getOrDefault(key, "");
    }

    /**
     * Search code staff laytest config
     *
     * @param value
     * @return
     */
    public List<ParameterEntity> findStaffCode(String value) {
        return parameterRepository.findStaffCode(StaffConfigEnum.LAYTEST_STAFF_CODE.getKey(), value.toLowerCase().trim());
    }

    /**
     * Site config
     *
     * @auth vvThành
     * @param provinceID
     * @return
     */
    public HashMap<String, String> getProvinceConfig(String provinceID) {
        HashMap<String, String> optionsByType = getOptionsByType(String.format("province_%s", provinceID), !true);
        if (optionsByType == null) {
            optionsByType = new HashMap<>();
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.PAC_FROM_CONFIRM.getKey(), "").equals("")) {
            optionsByType.put(SiteConfigEnum.PAC_FROM_CONFIRM.getKey(), String.valueOf(BooleanEnum.TRUE.getKey()));
        }
        if (optionsByType.getOrDefault(SiteConfigEnum.PAC_FROM_HTC.getKey(), "").equals("")) {
            optionsByType.put(SiteConfigEnum.PAC_FROM_HTC.getKey(), String.valueOf(BooleanEnum.TRUE.getKey()));
        }
        return optionsByType;
    }

    /**
     * @auth vvThanh
     * @param provinceID
     * @param key
     * @return
     */
    public String getProvinceConfig(String provinceID, String key) {
        HashMap<String, String> config = getProvinceConfig(provinceID);
        return config.getOrDefault(key, "");
    }

    /**
     * Quyền truy sử dụng chức năng do tỉnh cấu hình
     *
     * @auth vvThành
     * @param siteID
     * @return
     */
    public HashMap<String, String> getSiteRoleConfig(Long siteID) {
        HashMap<String, String> optionsByType = getOptionsByType(String.format("site_role_%s", siteID), !true);
        if (optionsByType == null) {
            optionsByType = new HashMap<>();
        }
        if (optionsByType.getOrDefault(ServiceEnum.HTC.getKey(), "").equals("")) {
            optionsByType.put(ServiceEnum.HTC.getKey(), String.valueOf(BooleanEnum.TRUE.getKey()));
        }
        if (optionsByType.getOrDefault(ServiceEnum.HTC_CONFIRM.getKey(), "").equals("")) {
            optionsByType.put(ServiceEnum.HTC_CONFIRM.getKey(), String.valueOf(BooleanEnum.TRUE.getKey()));
        }
        if (optionsByType.getOrDefault(ServiceEnum.LAYTEST.getKey(), "").equals("")) {
            optionsByType.put(ServiceEnum.LAYTEST.getKey(), String.valueOf(BooleanEnum.TRUE.getKey()));
        }
        if (optionsByType.getOrDefault(ServiceEnum.OPC.getKey(), "").equals("")) {
            optionsByType.put(ServiceEnum.OPC.getKey(), String.valueOf(BooleanEnum.TRUE.getKey()));
        }
        return optionsByType;
    }

    /**
     * @auth vvThành
     * @param siteID
     * @param key
     * @return
     */
    public String getSiteRoleConfig(Long siteID, String key) {
        HashMap<String, String> config = getSiteRoleConfig(siteID);
        return config.getOrDefault(key, "");
    }
}
