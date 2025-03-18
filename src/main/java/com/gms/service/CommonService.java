package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.BooleanEnum;
import com.gms.entity.constant.HtcConfirmStatusEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.AuthActionEntity;
import com.gms.entity.db.AuthAssignmentEntity;
import com.gms.entity.db.AuthRoleActionEntity;
import com.gms.entity.db.AuthRoleEntity;
import com.gms.entity.db.HtcConfirmEntity;
import com.gms.entity.db.HtcLaytestEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.HtcVisitLaytestEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.repository.StaffRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 *
 * @author vvthanh
 */
@Service
public class CommonService extends BaseService implements UserDetailsService {

    @Autowired
    private HtcVisitService visitService;
    @Autowired
    private HtcConfirmService confirmService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private HtcLaytestService laytestService;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private HtcLaytestService htcLaytestService;
    @Autowired
    private HtcVisitService htcVisitService;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private PacPatientService pacPatientService;
    @Autowired
    private HtcConfirmService htcConfirmService;
    @Autowired
    private OpcArvService opcArvService;

    /**
     * Send confirm test
     *
     * @param visit
     * @return
     * @throws java.lang.Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public HtcVisitEntity sendConfirm(HtcVisitEntity visit) throws Exception {
        try {
            SiteEntity confirmSite = siteService.findOne(Long.valueOf(visit.getSiteConfirmTest()));
            if (confirmSite == null) {
                throw new Exception("Không tìm thấy cơ sở xét nghiệm khẳng định");
            }
            if (confirmService.findBySouce(Long.valueOf(visit.getSiteConfirmTest()), visit.getCode(), visit.getSiteID()) != null) {
                throw new Exception("Không thể gửi thông tin. Khách hàng đã tồn tại trên cơ sở khẳng định");
            }
            HtcConfirmEntity confirm = new HtcConfirmEntity();
            confirm.setSiteID(confirmSite.getID());
            confirm.setFullname(visit.getPatientName());
            confirm.setYear(visit.getYearOfBirth() == null ? 0 : Integer.valueOf(visit.getYearOfBirth()));
            confirm.setWardID(visit.getPermanentWardID());
            confirm.setDistrictID(visit.getPermanentDistrictID());
            confirm.setProvinceID(visit.getPermanentProvinceID());
            confirm.setAddress(visit.getPermanentAddress());
            confirm.setPermanentAddressGroup(visit.getPermanentAddressGroup());
            confirm.setPermanentAddressStreet(visit.getPermanentAddressStreet());
            confirm.setObjectGroupID(visit.getObjectGroupID());
            confirm.setRemove(Boolean.FALSE);
            confirm.setSourceID(visit.getCode());
            confirm.setSourceSiteID(visit.getSiteID());
            confirm.setSourceServiceID(ServiceEnum.HTC.getKey());
            confirm.setSourceSampleDate(visit.getSampleSentDate() == null ? new Date() : visit.getSampleSentDate());
            confirm.setGenderID(visit.getGenderID());
            confirm.setPatientID(visit.getPatientID());
            confirm.setTestResultsID(visit.getTestResultsID());
            confirm.setSourceReceiveSampleTime(visit.getPreTestTime());
            confirm.setServiceID(visit.getServiceID());

            // cập nhật 12-02-2020
            confirm.setModeOfTransmission(visit.getModeOfTransmission());
            confirm.setCurrentAddress(visit.getCurrentAddress());
            confirm.setCurrentAddressGroup(visit.getCurrentAddressGroup());
            confirm.setCurrentAddressStreet(visit.getCurrentAddressStreet());
            confirm.setCurrentProvinceID(visit.getCurrentProvinceID());
            confirm.setCurrentDistrictID(visit.getCurrentDistrictID());
            confirm.setCurrentWardID(visit.getCurrentWardID());
            confirm.setInsurance(visit.getHasHealthInsurance());
            confirm.setInsuranceNo(visit.getHealthInsuranceNo());

            //them ngay 07/04/2021
            confirm.setRaceID(visit.getRaceID());
            confirm.setPatientPhone(visit.getPatientPhone());
            confirm.setJobID(visit.getJobID());
            confirm.setRiskBehaviorID(visit.getRiskBehaviorID());

            visit.setConfirmTestStatus(HtcConfirmStatusEnum.PENDING.getKey());
            visit.setSampleSentDate(new Date());
            visitService.save(visit);
            confirm = confirmService.save(confirm);
            visitService.log(visit.getID(), "Chuyển thông tin xét nghiệm sang cơ sở KĐ " + confirmSite.getName());
            return visit;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public HtcVisitEntity confirmReceive(HtcVisitEntity visit, HtcConfirmEntity confirmModel, boolean confirm) throws Exception {
        try {
            visitService.save(visit);
            confirmService.save(confirmModel);
            SiteEntity confirmSite = siteService.findOne(Long.valueOf(visit.getSiteConfirmTest()));
            if (confirmSite == null) {
                throw new Exception("Không tìm thấy cơ sở xét nghiệm khẳng định");
            }
            if (confirm == true) {
                visitService.log(visit.getID(), "Đã thực hiện đối chiếu kết quả khẳng định vào ngày " + TextUtils.formatDate(visit.getFeedbackTime(), "dd/MM/yyyy"));
            } else {
                visitService.log(visit.getID(), "Gửi phản hồi thông tin sai tới cơ sở khẳng định " + confirmSite.getName() + " với lý do như sau: " + visit.getFeedbackMessage());
            }
            return visit;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public HtcLaytestEntity Transferred(HtcLaytestEntity laytest) throws Exception {
        try {
            laytestService.save(laytest);
            SiteEntity visitSite = siteService.findOne(laytest.getSiteID());
            if (visitSite == null) {
                throw new Exception("Không tìm thấy cơ sở xét nghiệm sàng lọc");
            }
            visitService.log(laytest.getID(), "Gửi thông tin khách hàng sang cơ sở xét nghiệm " + visitSite.getName());
            return laytest;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new Exception(e.getMessage());
        }
    }

    /**
     * @auth vvThành
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        StaffEntity user = staffRepository.getByUsername(username);
        if (user == null || user.getSiteID() == null || user.getSiteID().equals(0)) {
            throw new DisabledException(String.format("Tài khoản '%s' không tồn tại trên hệ thống!", username));
        }
        if (!user.isIsActive()) {
            throw new DisabledException("Tài khoản đang bị tạm khoá!");
        }

        SiteEntity site = siteService.findOne(user.getSiteID(), true, true);
        if (site == null) {
            throw new DisabledException("Cơ sở không tồn tại!");
        }
        if (!site.isIsActive()) {
            throw new DisabledException(String.format("Cơ sở %s tạm dừng hoạt động!", site.getName()));
        }
        //update last login 
        user.setLastLoginTime(new Date());
        user = staffRepository.save(user);

        //Lấy danh sách action by service
        List<String> actionNameByServices = new ArrayList<>();
        if (site.getServiceIDs() != null && !site.getServiceIDs().isEmpty()) {
            List<AuthActionEntity> actionByServices = authService.findActionByServices(site.getServiceIDs());
            if (actionByServices != null) {
                for (AuthActionEntity actionNameByService : actionByServices) {
                    actionNameByServices.add(actionNameByService.getName());
                }
            }
        }
        AuthRoleEntity role;
        Set<Long> actionID = new HashSet<>();
        List<String> dbRoles = new ArrayList<>();
        for (AuthAssignmentEntity userRole : user.getUserRoles()) {
            role = userRole.getRole();
            if (role == null) {
                continue;
            }

            for (AuthRoleActionEntity roleAction : role.getRoleActions()) {
                actionID.add(roleAction.getActionID());
            }
        }

        List<AuthActionEntity> actions = authService.findActionByIDs(actionID);
        if (actions != null) {
            for (AuthActionEntity action : actions) {
                if (!actionNameByServices.contains(action.getName())) {
                    continue;
                }
                dbRoles.add(action.getName());
            }
        }

        LoggedUser loggedUser = new LoggedUser(user, site, dbRoles, parameterService.getSiteConfig(site.getID()));
        loggedUser.setProvinceConfig(parameterService.getProvinceConfig(site.getProvinceID()));
        loggedUser.setSiteProvince(locationsService.findProvince(loggedUser.getSite().getProvinceID()));
        loggedUser.setSiteDistrict(locationsService.findDistrict(loggedUser.getSite().getDistrictID()));
        loggedUser.setSiteWard(locationsService.findWard(loggedUser.getSite().getWardID()));
        return loggedUser;
    }

    /**
     * Lưu thông tin tạo mới khách hàng sang lọc từ dịch vụ không chuyên Lưu
     * thông tin vào bảng, HtcVisit, HtcLaytest và HtcVisitLaytest
     *
     * @param entity
     * @param laytestEntity
     * @throws java.lang.Exception
     * @auth TrangBN
     * @param condition
     * @return
     */
    public HtcVisitEntity saveHtcAndLaytest(HtcVisitEntity condition, HtcVisitLaytestEntity entity, HtcLaytestEntity laytestEntity) throws Exception {

        try {
            condition = htcVisitService.save(condition, entity, siteService.findOne(laytestEntity.getSiteID()).getName());

            if (condition != null) {
                laytestEntity.setAcceptDate(new Date());
                HtcLaytestEntity save = htcLaytestService.save(laytestEntity);
                if (save != null) {
                    htcLaytestService.log(save.getID(), String.format("Đã được tiếp nhận bởi: %s", siteService.findOne(condition.getSiteID()).getName()));
                }

            }
        } catch (Exception e) {
        }
        return condition;
    }

    /**
     * Chuyển gửi giám sát phát hiện
     *
     * @auth TrangBN
     * @param visit
     * @param pac
     * @return
     * @throws Exception
     */
//    @Transactional(rollbackFor = Exception.class)
//    public HtcVisitEntity sendToPacPatient(HtcVisitEntity visit, PacPatientInfoEntity pac) throws Exception {
//
//        HtcVisitEntity htcSave = new HtcVisitEntity();
//
//        try {
//            PacPatientInfoEntity pacSave = pacPatientService.save(pac);
//            pacPatientService.log(pacSave.getID(), "Thêm mới thành công từ chuyển gửi GSPH HTC1");
//
//            visit.setPacSentDate(new Date());
//            visit.setPacPatientID(pacSave.getID());
//            htcSave = htcVisitService.save(visit);
//            htcVisitService.log(htcSave.getID(), "Cập nhật trạng thái đã chuyển gửi GSPH thành công");
//        } catch (Exception e) {
//            getLogger().error(e.getMessage());
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            throw new Exception(e.getMessage());
//        }
//        return htcSave;
//    }
    /**
     * Chuyển gửi giám sát phát hiện
     *
     * @auth TrangBN
     * @param confirm
     * @param pac
     * @return
     * @throws Exception
     */
//    @Transactional(rollbackFor = Exception.class)
//    public HtcConfirmEntity sendToPacPatientConfirm(HtcConfirmEntity confirm, PacPatientInfoEntity pac) throws Exception {
//
//        HtcConfirmEntity confirmResult = new HtcConfirmEntity();
//        try {
//            PacPatientInfoEntity pacSave = pacPatientService.save(pac);
//            pacPatientService.log(pacSave.getID(), "Thêm mới thành công từ chuyển gửi GSPH khẳng định");
//
//            confirm.setPacSentDate(new Date());
//            confirm.setPacPatientID(pacSave.getID());
//            confirmResult = htcConfirmService.save(confirm);
//            htcConfirmService.log(confirmResult.getID(), "Cập nhật trạng thái đã chuyển gửi GSPH thành công");
//        } catch (Exception e) {
//            getLogger().error(e.getMessage());
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            throw new Exception(e.getMessage());
//        }
//        return confirmResult;
//    }
    /**
     * Chuyển gửi giám sát phát hiện từ OPC
     *
     * @auth TrangBN
     * @param arv
     * @param pac
     * @return
     * @throws Exception
     */
//    @Transactional(rollbackFor = Exception.class)
//    public OpcArvEntity sendToPacPatientOpc(OpcArvEntity arv, PacPatientInfoEntity pac) throws Exception {
//
//        OpcArvEntity savedArv = new OpcArvEntity();
//        
//        try {
//            PacPatientInfoEntity pacSave = pacPatientService.save(pac);
//            pacPatientService.log(pacSave.getID(), "Thêm mới thành công từ chuyển gửi GSPH điều trị");
//
//            arv.setTransferTimeGSPH(new Date());
//            arv.setUpdateTimeGSPH(new Date());
//            arv.setGsphID(pacSave.getID());
//            savedArv = opcArvService.update(arv);
//            opcArvService.logArv(savedArv.getID(), savedArv.getPatientID(), "Cập nhật trạng thái đã chuyển gửi GSPH thành công");
//        } catch (Exception e) {
//            getLogger().error(e.getMessage());
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            throw new Exception(e.getMessage());
//        }
//        return savedArv;
//    }
    /**
     * Thêm cấu hình cơ sở
     *
     * @param type
     * @param site
     * @return
     */
    public ParameterEntity addConfig(String type, SiteEntity site) {
        HashMap<String, String> buildTypeParameter = buildFinalParameter().get(type);

        if (buildTypeParameter == null || buildTypeParameter.get("title").isEmpty()) {
            return null;
        }

        try {
            ParameterEntity parameterEntity = new ParameterEntity();
            parameterEntity.setType(type);
            parameterEntity.setStatus(BooleanEnum.TRUE.getKey());
            parameterEntity.setCode(String.format("s-%s", site.getID()));
            parameterEntity.setPosition(1);
            parameterEntity.setParentID(0L);
            parameterEntity.setSiteID(site.getID());
            parameterEntity.setValue(site.getName());

            parameterEntity = parameterService.save(parameterEntity);
            if (parameterEntity == null) {
                throw new Exception("Thêm dữ liệu tham số thất bại!");
            }
            return parameterEntity;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Lấy param đang có
     *
     * @return
     */
    private HashMap<String, HashMap<String, String>> buildFinalParameter() {
        HashMap<String, HashMap<String, String>> dataModels = new LinkedHashMap<>();
        List<ParameterEntity> models = parameterService.findByType(ParameterEntity.SYSTEMS_PARAMETER);

        Map<String, String> finalParameter = new LinkedHashMap<>();
        finalParameter = new LinkedHashMap<>();

        finalParameter.put(ParameterEntity.BLOOD_BASE, "Nơi lấy máu");
        finalParameter.put(ParameterEntity.TREATMENT_FACILITY, "Cơ sở điều trị");
        finalParameter.put(ParameterEntity.PLACE_TEST, "Nơi xét nghiệm");

        for (Map.Entry<String, String> entry : finalParameter.entrySet()) {
            String key = entry.getKey();
            if (dataModels.get(key) == null) {
                dataModels.put(key, new HashMap<String, String>());
                dataModels.get(key).put("title", "");
                dataModels.get(key).put("icon", "");
                dataModels.get(key).put("description", "");
                dataModels.get(key).put("hivinfo", "");
                dataModels.get(key).put("elog", "");
                dataModels.get(key).put("sitemap", "");
                dataModels.get(key).put("useparent", "");
            }
        }

        for (ParameterEntity parameter : models) {
            String[] splitCode = parameter.getCode().split("_");
            if (splitCode.length < 2) {
                continue;
            }
            if (dataModels.get(splitCode[0]) == null) {
                continue;
            }
            dataModels.get(splitCode[0]).put(splitCode[1], parameter.getValue());
        }
        return dataModels;
    }
}
