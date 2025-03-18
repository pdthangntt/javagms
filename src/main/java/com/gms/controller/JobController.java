package com.gms.controller;

import com.gms.components.ElogUtils;
import com.gms.components.ExcelUtils;
import com.gms.components.PasswordUtils;
import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.bean.LoggedUser;
import com.gms.entity.constant.ConnectStatusEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.AuthAssignmentEntity;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.OpcArvEntity;
import com.gms.entity.db.PacPatientInfoEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.form.pac.PacPatientForm;
import com.gms.entity.input.HtcSearch;
import com.gms.entity.input.PacPatientNewSearch;
import com.gms.entity.json.Locations;
import com.gms.entity.json.country.Item;
import com.gms.entity.json.country.Result;
import com.gms.repository.SiteRepository;
import com.gms.repository.WardRepository;
import com.gms.repository.impl.PacPatientRepositoryImpl;
import com.gms.service.AuthService;
import com.gms.service.HtcVisitService;
import com.gms.service.LocationsService;
import com.gms.service.OpcArvService;
import com.gms.service.PacPatientHistoryService;
import com.gms.service.PacPatientService;
import com.gms.service.ParameterService;
import com.gms.service.PqmService;
import com.gms.service.QrCodeService;
import com.gms.service.SiteService;
import com.gms.service.StaffService;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Văn Thành
 */
@Controller(value = "job")
@RequestMapping(value = "job")
public class JobController extends WebController {

    @Autowired
    private ParameterService parameterService;
    @Autowired
    private LocationsService locationsService;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private HtcVisitService htcVisitService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private AuthService authService;
    @Autowired
    private PacPatientService patientService;
    @Autowired
    PacPatientService pacPatientService;
    @Autowired
    OpcArvService opcArvService;
    @Autowired
    private PacPatientRepositoryImpl pacPatientImpl;
    @Autowired
    private PacPatientHistoryService patientHistoryService;

    private ElogUtils elogUtils;

//    @RequestMapping(value = {"/country.job"}, method = RequestMethod.GET)
    public String actionCountry() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMATDATETIME);
        getLogger().info("The time is now {}", dateFormat.format(new Date()));
        String url = "http://api.printful.com/countries";
        RestTemplate restTemplate = new RestTemplate();
        Result result = restTemplate.getForObject(url, Result.class);
        if (result == null || !"200".equals(result.getCode())) {
            getLogger().info("Service {} empty", url);
        } else {
            ParameterEntity parameterEntity;
            for (Item item : result.getResult()) {
                parameterEntity = new ParameterEntity();
                parameterEntity.setType(ParameterEntity.COUNTRY);
                parameterEntity.setCode(item.getCode());
                parameterEntity.setValue(item.getName());
                parameterEntity.setStatus(1);
                ParameterEntity entity = parameterService.save(parameterEntity);
                getLogger().info("Save country {}-{} status {}", parameterEntity.getCode(), parameterEntity.getValue(), entity == null);
            }
        }
        return "backend/test";
    }

//    @RequestMapping(value = {"/elog_lib.job"}, method = RequestMethod.GET)
    public String actionElog() throws Exception {
        InputStream file = TextUtils.getFile("data/elog/HTC-HX_mk_1234.xlsm");
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet sheet = workbook.getSheet("Options");

        elogUtils = new ElogUtils(sheet);
        buildGender();
//        buildTestResult();
        buildServiceAfterTest();
        buildServiceTest();
//        buildJob();
        buildRace();
        buildModesOfTransmision();
        buildRiskBehavior();
        buildTestObjectGroup();
        return "backend/test";
    }

    private void build(List<ParameterEntity> data, List<ParameterEntity> models) {
        if (models == null) {
            models = new ArrayList<>();
        }
        for (ParameterEntity gElog : data) {
            boolean exits = false;
            for (ParameterEntity gender : models) {
                if (TextUtils.removemarks(gender.getValue()).equals(TextUtils.removemarks(gElog.getValue()))) {
                    exits = true;
                    gender.setElogCode(gElog.getElogCode());
                    gender.setPosition(gElog.getPosition());
                    gender.setStatus(1);
                    break;
                }
            }
            if (!exits) {
                models.add(gElog);
            }
        }
        parameterService.save(models);
    }

    private void buildTestObjectGroup() {
        List<ParameterEntity> dataElog = new ArrayList<>();
        List<ParameterEntity> entitys = parameterService.getTestObjectGroup();
        try {
            InputStream file = TextUtils.getFile("data/parameter/Tham số hệ thống.xls");
            Workbook workbook = ExcelUtils.getWorkbook(file, "Tham số hệ thống.xls");
            Sheet sheet = workbook.getSheetAt(0);
            ParameterEntity model;
            for (Row row : sheet) {
                if (row.getRowNum() <= 4 || row.getRowNum() >= 15 || String.valueOf(row.getCell(0)).equals("")) {
                    continue;
                }
                model = new ParameterEntity();
                model.setPosition(row.getRowNum());
                model.setCode(String.valueOf(row.getCell(0)).replaceAll(".0", ""));
                model.setType(ParameterEntity.TEST_OBJECT_GROUP);
                model.setElogCode(String.valueOf(row.getCell(0)));
                model.setValue(row.getCell(1).getStringCellValue().trim());
                model.setStatus(1);
                dataElog.add(model);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        build(dataElog, entitys);
    }

    private void buildGender() {
        List<ParameterEntity> genderElog = elogUtils.getGenders();
        List<ParameterEntity> genders = parameterService.getGenders();
        build(genderElog, genders);
    }

    private void buildTestResult() {
        List<ParameterEntity> data = elogUtils.getTestResult();
        List<ParameterEntity> models = parameterService.getTestResult();
        build(data, models);
    }

    private void buildServiceAfterTest() {
        List<ParameterEntity> data = elogUtils.getServiceAfterTest();
        List<ParameterEntity> models = parameterService.getServiceAfterTest();
        build(data, models);
    }

    private void buildServiceTest() {
        List<ParameterEntity> data = elogUtils.getServiceTest();
        List<ParameterEntity> models = parameterService.getServiceTest();
        build(data, models);
    }

    private void buildJob() {
        List<ParameterEntity> data = elogUtils.getJobs();
        List<ParameterEntity> models = parameterService.getJob();
        build(data, models);
    }

    private void buildRace() {
        List<ParameterEntity> data = elogUtils.getRaces();
        List<ParameterEntity> models = parameterService.getRaces();
        build(data, models);
    }

    private void buildModesOfTransmision() {
        List<ParameterEntity> data = elogUtils.getModesOfTransmision();
        List<ParameterEntity> models = parameterService.getModesOfTransmision();
        build(data, models);
    }

    /**
     * Nguy cơ lây nhiễm
     */
    private void buildRiskBehavior() {
        List<ParameterEntity> data = elogUtils.getRiskBehaviort();
        List<ParameterEntity> models = parameterService.getRiskBehavior();
        build(data, models);
    }

//    @RequestMapping(value = {"/location.job"}, method = RequestMethod.GET)
    public String actionLocationElog() throws Exception {
        InputStream file = TextUtils.getFile("data/elog/locations.xlsx");
        Workbook workbook = ExcelUtils.getWorkbook(file, "locations.xlsx");
        Sheet sheet = workbook.getSheetAt(0);
        ProvinceEntity province;
        Map<String, ProvinceEntity> provinces = new HashMap<>();
        Map<String, String> provinceOptions = new HashMap<>();
        for (ProvinceEntity item : locationsService.findAllProvince()) {
            provinceOptions.put(item.getID(), item.getElogCode());
            provinces.put(TextUtils.removeDiacritical(item.getName().replaceAll("Tỉnh", "").replaceAll("Thành phố", "").trim()).toLowerCase(), item);
        }
        Map<String, DistrictEntity> districts = new HashMap<>();
        DistrictEntity district;
        for (DistrictEntity item : locationsService.findAllDistrict()) {
            String key = provinceOptions.get(item.getProvinceID()) + "-" + TextUtils.removeDiacritical(item.getName().toLowerCase().replaceAll("thị xã", "").replaceAll("thành phố", "").replaceAll("huyện", "").replaceAll("quận", "").trim());
//            if (item.getID().equals("058")) {
//                System.out.println("----" + item.getName());
//            }
            if (districts.getOrDefault(key, null) != null) {
                getLogger().error("Đã tồn tại: {}/{} - {}", key, item.getName(), item.getProvinceID());
            }
            districts.put(key.toLowerCase(), item);
        }
        String key;
        String code;
        String p;
        for (Row row : sheet) {
            if (row.getRowNum() <= 1) {
                continue;
            }
//            if (!String.valueOf(row.getCell(0)).equals("")) {
//                key = TextUtils.removeDiacritical(String.valueOf(row.getCell(1)).replaceAll("Tỉnh", "").replaceAll("Thành phố", "").trim());
//                code = String.format("%s", new Double(String.valueOf(row.getCell(0))).intValue());
//                if (key.equals("TP HCM")) {
//                    key = "Ho Chi Minh";
//                }
//                getLogger().info("Tỉnh thành {} -- {}: {}", key, code, row.getCell(1));
//                province = provinces.getOrDefault(key.toLowerCase(), null);
//                if (province == null) {
//                    getLogger().error("{} không tìm thấy", key);
//                }
//                province.setElogCode(code);
//                province = locationsService.addProvince(province);
//            }

            if (!String.valueOf(row.getCell(4)).equals("")) {
                p = String.format("%s", new Double(String.valueOf(row.getCell(5))).intValue());
                key = p + "-" + TextUtils.removeDiacritical(String.valueOf(row.getCell(6)).toLowerCase().replaceAll("thị xã", "").replaceAll("thành phố", "").replaceAll("huyện", "").replaceAll("quận", "").trim().trim());
                code = String.format("%s", new Double(String.valueOf(row.getCell(4))).intValue());

//                getLogger().info("Quận huyện: {} -- {}: {}", key, code, row.getCell(6));
                district = districts.getOrDefault(key.toLowerCase(), null);
                if (district == null) {
                    getLogger().warn("Không tìm thấy quận/huyện: {}", key);
                    continue;
                }
                if (district.getElogCode() != null && !"".equals(district.getElogCode())) {
                    continue;
                }
                district.setElogCode(code);
                locationsService.addDistrict(district);
            }
        }
        return "backend/test";
    }
    
    
    
//    @RequestMapping(value = {"/hub/location-province.job"}, method = RequestMethod.GET)
    public String actionLocationPqm() throws Exception {
        InputStream file = TextUtils.getFile("data/hub/locations.xlsx");
        Workbook workbook = ExcelUtils.getWorkbook(file, "locations.xlsx");
        Sheet sheet = workbook.getSheetAt(2);
        ProvinceEntity province;
        Map<String, ProvinceEntity> provinces = new HashMap<>();
        Map<String, String> provinceOptions = new HashMap<>();
        for (ProvinceEntity item : locationsService.findAllProvince()) {
            provinceOptions.put(item.getID(), item.getElogCode());
            provinces.put(TextUtils.removeDiacritical(item.getName().trim()).toLowerCase(), item);
        }
        
        String key;
        String code;
        for (Row row : sheet) {
            if (row.getRowNum() <= 1) {
                continue;
            }
            if (row.getCell(3) != null && !String.valueOf(row.getCell(3)).equals("")) {
                key = TextUtils.removeDiacritical(String.valueOf(row.getCell(3)).trim());
                code = String.format("%s", new Double(String.valueOf(row.getCell(4))).intValue());
                
                getLogger().info("Tỉnh thành {} -- {}: {}", key, code, row.getCell(1));
                province = provinces.getOrDefault(key.toLowerCase(), null);
                if (province == null) {
                    getLogger().error("{} không tìm thấy", key);
                    continue;
                }
                province.setPqmCode(code);
                province = locationsService.addProvince(province);
            }

        }
        return "backend/test";
    }

//    @RequestMapping(value = {"/location.job"}, method = RequestMethod.GET)
    public String actionLocation() throws Exception {
        buildProvince();
        buildDistrict();
        buildWard();
        return "backend/test";
    }

    private void buildProvince() throws Exception {
        InputStream file = TextUtils.getFile("data/locations/cities.xls");
        Workbook workbook = ExcelUtils.getWorkbook(file, "cities.xls");
        Sheet sheet = workbook.getSheetAt(0);
//        HSSFWorkbook workbook = new HSSFWorkbook(excelFile);
//        HSSFSheet sheet = workbook.getSheetAt(0);
        ProvinceEntity province;
        for (Row row : sheet) {
            if (row.getRowNum() == 0 || String.valueOf(row.getCell(0)).equals("")) {
                continue;
            }
            province = new ProvinceEntity();
            province.setID(String.valueOf(row.getCell(0)));
            province.setName(String.valueOf(row.getCell(1)));
            province.setType(String.valueOf(row.getCell(3)));
            province.setPosition(Integer.valueOf(row.getCell(0).toString()));
            locationsService.addProvince(province);
        }
    }

    private void buildDistrict() throws Exception {
        InputStream file = TextUtils.getFile("data/locations/districts.xls");
        Workbook workbook = ExcelUtils.getWorkbook(file, "districts.xls");
        Sheet sheet = workbook.getSheetAt(0);
        DistrictEntity entity;
        for (Row row : sheet) {
            if (row.getRowNum() == 0 || String.valueOf(row.getCell(0)).equals("")) {
                continue;
            }
            entity = new DistrictEntity();
            entity.setID(String.valueOf(row.getCell(0)));
            entity.setProvinceID(String.valueOf(row.getCell(4)));
            entity.setName(String.valueOf(row.getCell(1)));
            entity.setType(String.valueOf(row.getCell(3)));
            entity.setPosition(Integer.valueOf(row.getCell(0).toString()));
            locationsService.addDistrict(entity);
        }
    }

    private void buildWard() throws Exception {
        InputStream file = TextUtils.getFile("data/locations/wards.xls");
        Workbook workbook = ExcelUtils.getWorkbook(file, "wards.xls");
        Sheet sheet = workbook.getSheetAt(0);
        WardEntity entity;
        List<WardEntity> entitys = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0 || String.valueOf(row.getCell(0)).equals("")) {
                continue;
            }

            if (entitys.size() == 300) {
                wardRepository.saveAll(entitys);
                entitys = new ArrayList<>();
            }

            entity = new WardEntity();
            entity.setID(String.valueOf(row.getCell(0)));
            entity.setDistrictID(String.valueOf(row.getCell(4)));
            entity.setName(String.valueOf(row.getCell(1)));
            entity.setType(String.valueOf(row.getCell(3)));
            entity.setPosition(Integer.valueOf(row.getCell(0).toString()));
            entitys.add(entity);
        }

        if (entitys.size() > 0) {
            wardRepository.saveAll(entitys);
        }
    }

//    @RequestMapping(value = {"/site.job"}, method = RequestMethod.GET)
    public String actionSite() throws Exception {
        Iterable<SiteEntity> entitys = siteRepository.findAll();
        Iterator<SiteEntity> iterator = entitys.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            SiteEntity entity = iterator.next();
            i += 1;
            System.out.println("----" + i);
            if (entity.getParentID().equals(Long.parseLong("0")) || siteRepository.findById(entity.getParentID()).isPresent()) {
                if (entity.isIsActive()) {
                    continue;
                }
            }

            getLogger().info("remove " + entity.getID());
            siteService.remove(entity);
        }
        return "backend/test";
    }

    @RequestMapping(value = {"/site-staff.job"}, method = RequestMethod.GET)
    public String actionSiteStaff(
            @RequestParam(name = "pre", required = true, defaultValue = "tg") String pre,
            @RequestParam(name = "p", required = true, defaultValue = "") String p,
            @RequestParam(name = "cp", required = true, defaultValue = "0") Long cp,
            @RequestParam(name = "staff", required = true, defaultValue = "") Long s) throws Exception {
        List<AuthAssignmentEntity> roleStaff = authService.findOneAssignmentByUserID(s);
        List<Long> roles = new ArrayList<>();
        if (roleStaff != null) {
            for (AuthAssignmentEntity authAssignmentEntity : roleStaff) {
                roles.add(authAssignmentEntity.getRole().getID());
            }
        }

        PacPatientInfoEntity pac = null;
        if (cp != null && !cp.equals(Long.valueOf("0"))) {
            pac = patientService.findOne(cp);
        }

        Map<String, ParameterEntity> services = new HashMap<>();
        for (ParameterEntity item : parameterService.getServices()) {
            services.put(item.getCode(), item);
        }

        Map<String, String> wards = new HashMap<>();
        for (WardEntity item : locationsService.findAllWard()) {
            wards.put(item.getID(), item.getName());
        }

        List<SiteEntity> sites = siteRepository.findByProvinceID(p);
        for (SiteEntity item : sites) {
            if (item.getType() != SiteEntity.TYPE_WARD) {
                continue;
            }
            SiteEntity site = siteService.findOne(item.getID(), true, true);
            if (site.getType() == SiteEntity.TYPE_WARD) {
                site.setServices(new ArrayList<ParameterEntity>());
                site.getServices().add(services.get(ServiceEnum.TYT.getKey()));
                siteService.save(site);

                //Tạo tài khoản
                StaffEntity staff = new StaffEntity();
                staff.setSiteID(site.getID());
                staff.setUsername(TextUtils.removeDiacritical(pre + wards.get(site.getWardID()).replaceAll(" ", "").replaceAll("-", "").toLowerCase()));
                staff.setEmail("hotro.hiv.ims@gmail.com");
                staff.setIsActive(true);
                staff.setName(site.getShortName());
                staff.setPhone("0123456789");
                staff.setPwd(PasswordUtils.encrytePassword("123456"));

                if (staffService.findByUsername(staff.getUsername()) == null) {
                    staffService.save(staff, roles);
                }

                if (pac != null) {
                    PacPatientInfoEntity pacNew = (PacPatientInfoEntity) pac.clone();
                    pacNew.setID(null);
                    pacNew.setProvinceID(site.getProvinceID());
                    pacNew.setDistrictID(site.getDistrictID());
                    pacNew.setWardID(site.getWardID());
                    pacNew.setPermanentProvinceID(site.getProvinceID());
                    pacNew.setPermanentDistrictID(site.getDistrictID());
                    pacNew.setPermanentWardID(site.getWardID());
                    patientService.save(pacNew);
                    PacPatientInfoEntity pacNew2 = (PacPatientInfoEntity) pacNew.clone();
                    pacNew2.setFullname("Nguyễn Văn A");
                    pacNew2.setID(null);
                    patientService.save(pacNew2);
                    PacPatientInfoEntity pacNew3 = (PacPatientInfoEntity) pacNew.clone();
                    pacNew3.setFullname("Nguyễn Văn B");
                    pacNew3.setID(null);
                    patientService.save(pacNew3);
                }
            }
        }
        return "backend/test";
    }

    @RequestMapping(value = {"/pac-opc/connect.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model, RedirectAttributes redirectAttributes,
            @RequestParam(name = "confirm", required = false, defaultValue = "confirm") String confirm,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) throws Exception {

        int success = 0;
        int error = 0;
        int total = 0;

        LoggedUser currentUser = getCurrentUser();
        PacPatientNewSearch search = new PacPatientNewSearch();
        search.setPageIndex(page);
        search.setPageSize(size);
        search.setRemove(false);
        if (isPAC()) {
            search.setProvinceID(currentUser.getSite().getProvinceID());
        } else {
            search.setProvinceID("-9999");
        }
//            case "not_connected":
        search.setConnectStatus(ConnectStatusEnum.NOT_CONNECT.getKey());

        search.setSortable(Sort.by(Sort.Direction.DESC, new String[]{"updateAt"}));
        DataPage<PacPatientInfoEntity> dataPage = pacPatientService.findChange(search);
        Map<String, String> errors = new HashMap<>();
        if (confirm.equals("1") && dataPage.getData() != null && !dataPage.getData().isEmpty()) {

            search.setPageSize(99999999);
            DataPage<PacPatientInfoEntity> dataPage1 = pacPatientService.findChange(search);
            List<Long> ids = new ArrayList<>();

            ids.addAll(CollectionUtils.collect(dataPage1.getData(), TransformerUtils.invokerTransformer("getID")));
            PacPatientInfoEntity e;
            PacPatientInfoEntity e1;
            total = ids.size();
            for (Long id : ids) {
                e = pacPatientService.findOne(id);
                if (e == null || StringUtils.isEmpty(e.getIdentityCard())) {
                    errors.put(e.getID().toString(), e == null ? "Không tìm thấy người nhiễm biến động" : "Không tìm thấy chứng minh nhân dân");
                    error++;
                } else {
                    List<PacPatientInfoEntity> entitys = pacPatientService.findPatientConnectOPC(currentUser.getSite().getProvinceID(), e.getIdentityCard());
                    if (entitys == null || entitys.isEmpty()) {
                        errors.put(e.getID().toString(), "Không tìm thấy bệnh nhân cần kết nối trong danh sách quản lý");
                        error++;
                    } else if (entitys.size() > 1) {
                        errors.put(e.getID().toString(), "Tìm thấy " + entitys.size() + " bệnh nhân có cùng chứng minh nhân dân trong danh sách quản lý");
                        error++;
                    } else {
                        e1 = pacPatientService.findOne(entitys.get(0).getID());

                        e1.setStatusOfTreatmentID(e.getStatusOfTreatmentID());
                        e1.setStatusOfChangeTreatmentID(e.getStatusOfChangeTreatmentID());
                        e1.setStartTreatmentTime(e.getStartTreatmentTime());
                        e1.setSiteTreatmentFacilityID(e.getSiteTreatmentFacilityID());
                        e1.setTreatmentRegimenID(e.getTreatmentRegimenID());
                        e1.setCdFourResult(e.getCdFourResult());
                        e1.setCdFourResultDate(e.getCdFourResultDate());
                        e1.setCdFourResultCurrent(e.getCdFourResultCurrent());
                        e1.setCdFourResultLastestDate(e.getCdFourResultLastestDate());
                        e1.setVirusLoadArv(e.getVirusLoadArv());
                        e1.setVirusLoadArvDate(e.getVirusLoadArvDate());
                        e1.setVirusLoadArvCurrent(e.getVirusLoadArvCurrent());
                        e1.setVirusLoadArvLastestDate(e.getVirusLoadArvLastestDate());
                        e1.setClinicalStage(e.getClinicalStage());
                        e1.setClinicalStageDate(e.getClinicalStageDate());
                        e1.setAidsStatus(e.getAidsStatus());
                        e1.setAidsStatusDate(e.getAidsStatusDate());
                        e1.setSymptomID(e.getSymptomID());
                        e1.setDeathTime(e.getDeathTime());
                        e1.setRequestDeathTime(e.getRequestDeathTime());
                        e1.setCauseOfDeath(e.getCauseOfDeath());
                        e1.setChangeTreatmentDate(e.getChangeTreatmentDate());

                        e.setParentID(e1.getID());

                        PacPatientInfoEntity saveModel = pacPatientService.save(e1);
                        pacPatientService.log(saveModel.getID(), "Cập nhật thông tin điều trị sau khi kết nối ca phát hiện thành công.");

                        PacPatientInfoEntity saveTarget = pacPatientService.save(e);
                        pacPatientService.log(saveTarget.getID(), "Cập nhật thông tin sau khi kết nối ca điều trị thành công.");
                        success++;
                    }
                }

            }
        }

        model.addAttribute("error", String.format("Tổng số %s bệnh nhân, thành công %s, lỗi %s", total, success, error));
        model.addAttribute("title", "Kết nối biến động điều trị");
        model.addAttribute("errors", errors);
        model.addAttribute("total", total);
        model.addAttribute("dataPage", dataPage);
        return "backend/pac/opc_connect";
    }

    @RequestMapping(value = {"/opc-from-htc/connect.html"}, method = RequestMethod.GET)
    public String actionReceiveIndex(Model model,
            @RequestParam(name = "confirm", required = false, defaultValue = "confirm") String confirm,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "50") int size) throws ParseException {

        int success = 0;
        int error = 0;
        int total = 0;
        Set<Long> siteIDs = new HashSet<>();
        siteIDs.add(getCurrentUser().getUser().getSiteID());

        HtcSearch search = new HtcSearch();
        search.setRemove(false);
        search.setReceiveStatus(null);
        search.setPageIndex(page);
        search.setPageSize(size);

        DataPage<HtcVisitEntity> dataPage = htcVisitService.findReceiveHtcConnect(search);
        int i = 0;
        Map<String, String> errors = new HashMap<>();
        if (confirm.equals("1") && dataPage.getData() != null && !dataPage.getData().isEmpty()) {
            search.setPageSize(99999999);
            DataPage<HtcVisitEntity> dataPage1 = htcVisitService.findReceiveHtcConnect(search);
            OpcArvEntity arv;
            List<String> patientIDs = new ArrayList<>();

            for (HtcVisitEntity e : dataPage1.getData()) {
                if (StringUtils.isEmpty(e.getPatientID())) {
                    errors.put("Bệnh nhân mã " + e.getID(), "Không có chứng minh nhân dân");
                    continue;
                }
                if (patientIDs.contains(e.getPatientID())) {
                    errors.put("Bệnh nhân mã " + e.getID(), "Tồn tại nhiều bệnh nhân có trùng cmnd");
                    continue;
                }
                patientIDs.add(e.getPatientID());

                List<OpcArvEntity> arvs = opcArvService.findConnectHtcVisit(siteIDs, e.getPatientID());
                if (arvs == null || arvs.isEmpty()) {
                    errors.put("Bệnh nhân mã " + e.getID(), "Không tìm thấy tại cơ sở OPC");
                } else if (arvs.size() > 1) {
                    errors.put("Bệnh nhân mã " + e.getID(), "Có " + arvs.size() + " bệnh nhân cùng chứng minh nhân dân: " + e.getPatientID());
                } else {
                    arv = arvs.get(0);
                    e.setArrivalTime(arv.getTranferToTime() == null ? new Date() : arv.getTranferToTime());//ngày tiếp nhận
                    e.setFeedbackReceiveTime(arv.getFeedbackResultsReceivedTime() == null ? new Date() : arv.getFeedbackResultsReceivedTime());//ngày phản hồi
                    e.setRegisterTherapyTime(arv.getRegistrationTime());
                    //cơ sở điều trị
                    SiteEntity sitep = siteService.findOne(arv.getSiteID());
                    e.setArrivalSite(sitep.getName());
                    e.setRegisteredTherapySite(sitep.getName());
                    e.setTherapyRegistProvinceID(sitep.getProvinceID());
                    e.setTherapyRegistDistrictID(sitep.getDistrictID());
                    //
                    e.setTherapyNo(arv.getCode());
                    e.setTherapyExchangeStatus("2");

                    htcVisitService.save(e);
                    htcVisitService.log(e.getID(), "Khách hàng được chuyển tới cơ sở " + getCurrentUser().getSite().getName() + " được phản hồi thành công");

                    arv.setTranferToTime(arv.getTranferToTime() == null ? new Date() : arv.getTranferToTime());
                    arv.setFeedbackResultsReceivedTime(arv.getFeedbackResultsReceivedTime() == null ? new Date() : arv.getFeedbackResultsReceivedTime());//ngày phản hồi
                    arv.setSourceID(e.getID());
                    arv.setSourceServiceID(ServiceEnum.HTC.getKey());

                    opcArvService.save(arv);
                    opcArvService.logArv(arv.getID(), arv.getPatientID(), "Gửi phản hồi tiếp nhận khách hàng được chuyển gửi từ cơ sở " + siteService.findOne(e.getSiteID()).getName());
                    i++;
                }

            }
        }

        model.addAttribute("title", "Tiếp nhận từ cơ sở tư vấn xét nghiệm");
        model.addAttribute("smallTitle", "Tiếp nhận từ TVXN");
        model.addAttribute("errors", errors);
        model.addAttribute("i", i);
        model.addAttribute("parentTitle", "Điều trị ngoại trú");
        model.addAttribute("dataPage", dataPage);

        return "backend/opc_arv/htc_receive_connect";
    }

    @Autowired
    private QrCodeService qrCodeService;
    @Autowired
    private PqmService pqmService;

    @RequestMapping(value = {"/qr-test.job"}, method = RequestMethod.GET)
    public String actionReceiveIndex(Model model) throws ParseException {
        //Chỉ số HIV dương tính
        pqmService.getHTS_TST_POS(12, 2020, "82");
        pqmService.getHTS_TST_POS(12, 2020, "72");

        pqmService.getHTS_TST_POS(11, 2020, "82");
        pqmService.getHTS_TST_POS(11, 2020, "72");

        pqmService.getHTS_TST_POS(10, 2020, "82");
        pqmService.getHTS_TST_POS(10, 2020, "72");

        pqmService.getHTS_TST_POS(9, 2020, "82");
        pqmService.getHTS_TST_POS(9, 2020, "72");

        pqmService.getHTS_TST_POS(8, 2020, "82");
        pqmService.getHTS_TST_POS(8, 2020, "72");

        pqmService.getHTS_TST_POS(7, 2020, "82");
        pqmService.getHTS_TST_POS(7, 2020, "72");

        pqmService.getHTS_TST_POS(6, 2020, "82");
        pqmService.getHTS_TST_POS(6, 2020, "72");

        pqmService.getHTS_TST_POS(5, 2020, "82");
        pqmService.getHTS_TST_POS(5, 2020, "72");
        return "backend/test";
    }

}
