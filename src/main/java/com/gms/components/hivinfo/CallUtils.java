package com.gms.components.hivinfo;

import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.json.hivinfo.common.Item;
import com.gms.entity.json.hivinfo.common.ResponseCommon;
import com.gms.entity.json.hivinfo.cosodieutri.CoSoDieuTri;
import com.gms.entity.json.hivinfo.cosodieutri.ResponseCoSoDieuTri;
import com.gms.entity.json.hivinfo.doituong.Doituong;
import com.gms.entity.json.hivinfo.doituong.ResponseDoituong;
import com.gms.entity.json.hivinfo.gender.Gender;
import com.gms.entity.json.hivinfo.gender.ResponseGender;
import com.gms.entity.json.hivinfo.location.District;
import com.gms.entity.json.hivinfo.location.ResponseDistrict;
import com.gms.entity.json.hivinfo.location.ResponseWard;
import com.gms.entity.json.hivinfo.location.Ward;
import com.gms.entity.json.hivinfo.noilaymau.NoiLayMau;
import com.gms.entity.json.hivinfo.noilaymau.ResponseNoiLayMau;
import com.gms.entity.json.hivinfo.noixetnghiem.NoiXetNghiem;
import com.gms.entity.json.hivinfo.noixetnghiem.ResponseNoiXetNghiem;
import com.gms.entity.json.hivinfo.patient.Data;
import com.gms.entity.json.hivinfo.patient.ResponsePatient;
import com.gms.entity.json.hivinfo.vitrilaymau.ResponseViTriLayMau;
import com.gms.entity.json.hivinfo.vitrilaymau.ViTriLayMau;
import com.google.gson.Gson;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author vvthanh
 */
@Component
public class CallUtils {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private String baseUrl;
    private String secret;
    private String username;
    private String pwd;

    private HttpHeaders createHeaders(final String username, final String password) {
        return new HttpHeaders() {
            {
                String auth = username + ":" + password;
                byte[] encodedAuth = Base64.encodeBase64(
                        auth.getBytes(Charset.forName("US-ASCII")));
                String authHeader = "Basic " + new String(encodedAuth);
                set("Authorization", authHeader);
            }
        };
    }

    private String decode(String str) {
        try {
            return SercurityUtils.decrypt(str, secret, true);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return null;
        }
    }

    private String encode(String str) {
        try {
            return SercurityUtils.encrypt(str, secret, true);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return null;
        }
    }

    public CallUtils(String url, String secret, String username, String pwd) {
        this.baseUrl = url;
        this.secret = secret;
        this.username = username;
        this.pwd = pwd;
    }

    /**
     * Đanh sách đường lây
     *
     * @return
     * @throws Exception
     */
    public List<ParameterEntity> getDuongLay() throws Exception {
        String url = baseUrl + "/duonglay.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ResponseCommon> exchange = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(createHeaders(username, pwd)), ResponseCommon.class);
        ResponseCommon response = exchange.getBody();
        if (response == null || !response.isSuccess()) {
            throw new Exception(response == null ? "Empty" : response.getMessage());
        }
        List<ParameterEntity> entitys = new ArrayList<>();
        ParameterEntity entity = null;
        for (Item duongLay : response.getData()) {
            entity = buildEntity(duongLay, ParameterEntity.MODE_OF_TRANSMISSION);
            String[] name = duongLay.getName().split(":");
            entity.setValue(StringUtils.trim(name.length >= 2 ? name[1] : name[0]));
            entitys.add(entity);
        }

        return entitys;
    }

    /**
     * Danh sách công việc
     *
     * @auth vvThành
     * @return
     * @throws Exception
     */
    public List<ParameterEntity> getCongviec() throws Exception {
        String url = baseUrl + "/job.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseCommon response = restTemplate.getForObject(url, ResponseCommon.class);
        if (!response.isSuccess()) {
            throw new Exception(response.getMessage());
        }
        List<ParameterEntity> entitys = new ArrayList<>();
        ParameterEntity entity = null;
        for (Item item : response.getData()) {
            entity = buildEntity(item, ParameterEntity.JOB);
            String[] name = item.getName().split(":");
            entity.setValue(StringUtils.trim(name.length >= 2 ? name[1] : name[0]));

            entitys.add(entity);
        }

        return entitys;
    }

    /**
     * Danh sách nơi xét nghiệm. Attribute 01 lưu thông tin id tỉnh hiv info,
     * attribute 02 lưu mã tỉnh local si
     *
     * @auth vvThành
     * @param provinces
     * @throws java.lang.Exception
     * @return
     */
    public List<ParameterEntity> getNoixetnghiem(List<ProvinceEntity> provinces) throws Exception {
        HashMap<String, ProvinceEntity> provinceOptions = new HashMap<>();
        for (ProvinceEntity province : provinces) {
            provinceOptions.put(province.getHivInfoCode(), province);
        }

        String url = baseUrl + "/noixetnghiem.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseNoiXetNghiem response = restTemplate.getForObject(url, ResponseNoiXetNghiem.class);
        if (!response.isSuccess()) {
            throw new Exception(response.getMessage());
        }
        List<ParameterEntity> entitys = new ArrayList<>();
        ParameterEntity entity = null;
        for (NoiXetNghiem item : response.getData()) {
            entity = new ParameterEntity();
            entity.setStatus(1);
            entity.setElogCode(null);
            entity.setPosition(item.getId());
            entity.setCode(String.valueOf(item.getId()));
            entity.setType(ParameterEntity.PLACE_TEST);
            entity.setHivInfoCode(String.valueOf(item.getId()));
            entity.setValue(item.getName());
            entity.setAttribute01(String.valueOf(item.getIdtinh()));
            entity.setAttribute02(provinceOptions.get(entity.getAttribute01()) == null ? null : provinceOptions.get(entity.getAttribute01()).getID());
            entity.setNote(provinceOptions.get(entity.getAttribute01()) == null ? null : provinceOptions.get(entity.getAttribute01()).getName());
            entitys.add(entity);
        }

        return entitys;
    }

    /**
     * Danh sách tỉnh thành
     *
     * @auth vvThành
     * @return
     * @throws Exception
     */
    public List<Item> getTinhThanh() throws Exception {
        String url = baseUrl + "/locations/province.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseCommon response = restTemplate.getForObject(url, ResponseCommon.class);
        if (!response.isSuccess()) {
            throw new Exception(response.getMessage());
        }
        return response.getData();
    }

    /**
     * Danh sách quận huyện
     *
     * @return
     * @throws Exception
     */
    public List<District> getQuanHuyen() throws Exception {
        String url = baseUrl + "/locations/district.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseDistrict response = restTemplate.getForObject(url, ResponseDistrict.class);
        if (!response.isSuccess()) {
            throw new Exception(response.getMessage());
        }
        return response.getData();
    }

    /**
     * Danh sách phường xã
     *
     * @return
     * @throws Exception
     */
    public List<Ward> getPhuongXa() throws Exception {
        String url = baseUrl + "/locations/ward.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ResponseWard> exchange = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(createHeaders(username, pwd)), ResponseWard.class);
        ResponseWard response = exchange.getBody();
        if (!response.isSuccess()) {
            throw new Exception(response.getMessage());
        }
        return response.getData();
    }

    /**
     * Cơ sở điều trị. Attribute 01 lưu thông tin id tỉnh hiv info, attribute 02
     * lưu mã tỉnh local si
     *
     * @param provinces
     * @return
     * @throws Exception
     */
    public List<ParameterEntity> getCoSoDieuTri(List<ProvinceEntity> provinces) throws Exception {
        HashMap<String, ProvinceEntity> provinceOptions = new HashMap<>();
        for (ProvinceEntity province : provinces) {
            provinceOptions.put(province.getHivInfoCode(), province);
        }

        String url = baseUrl + "/cosodieutri.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ResponseCoSoDieuTri> exchange = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(createHeaders(username, pwd)), ResponseCoSoDieuTri.class);
        ResponseCoSoDieuTri response = exchange.getBody();
        if (response == null || !response.isSuccess()) {
            throw new Exception(response == null ? "Null Ressult" : response.getMessage());
        }

        List<ParameterEntity> entitys = new ArrayList<>();
        ParameterEntity entity = null;
        for (CoSoDieuTri item : response.getData()) {
            entity = new ParameterEntity();
            entity.setStatus(1);
            entity.setElogCode(null);
            entity.setPosition(item.getId());
            entity.setCode(String.valueOf(item.getId()));
            entity.setType(ParameterEntity.TREATMENT_FACILITY);
            entity.setHivInfoCode(String.valueOf(item.getId()));
            entity.setValue(item.getName());
            entity.setAttribute01(String.valueOf(item.getIdtinh()));
            entity.setAttribute02(provinceOptions.get(entity.getAttribute01()) == null ? null : provinceOptions.get(entity.getAttribute01()).getID());
            entity.setNote(provinceOptions.get(entity.getAttribute01()) == null ? null : provinceOptions.get(entity.getAttribute01()).getName());
            entitys.add(entity);
        }
        return entitys;
    }

    /**
     * Danh sách triệu trứng
     *
     * @return
     * @throws Exception
     */
    public List<ParameterEntity> getTrieuTrung() throws Exception {
        String url = baseUrl + "/trieuchung.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseCommon response = restTemplate.getForObject(url, ResponseCommon.class);
        if (!response.isSuccess()) {
            throw new Exception(response.getMessage());
        }
        List<ParameterEntity> entitys = new ArrayList<>();
        ParameterEntity entity = null;
        for (Item item : response.getData()) {
            entity = buildEntity(item, ParameterEntity.SYSMPTOM);
            String[] name = item.getName().split(":");
            entity.setValue(StringUtils.trim(name.length >= 2 ? name[1] : name[0]));

            entitys.add(entity);
        }

        return entitys;
    }

    /**
     * Phác đồ điều trị
     *
     * @return
     * @throws Exception
     */
    public List<ParameterEntity> getPhacDoDieuTri() throws Exception {
        String url = baseUrl + "/phacdodieutri.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseCommon response = restTemplate.getForObject(url, ResponseCommon.class);
        if (!response.isSuccess()) {
            throw new Exception(response.getMessage());
        }
        List<ParameterEntity> entitys = new ArrayList<>();
        ParameterEntity entity = null;
        for (Item item : response.getData()) {
            entity = new ParameterEntity();
            entity.setStatus(1);
            entity.setElogCode(null);
            entity.setPosition(item.getId());
            entity.setCode(String.valueOf(item.getId()));
            entity.setType(ParameterEntity.TREATMENT_REGIMEN);
            entity.setHivInfoCode(String.valueOf(item.getId()));
            entity.setValue(item.getName());
            entitys.add(entity);
        }
        return entitys;
    }

    /**
     * Nguy cơ lây nhiễm
     *
     * @auth vvThành
     * @return
     * @throws Exception
     */
    public List<ParameterEntity> getNguyCoLayNhiem() throws Exception {
        String url = baseUrl + "/nguycolaynhiem.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ResponseCommon> exchange = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(createHeaders(username, pwd)), ResponseCommon.class);
        ResponseCommon response = exchange.getBody();
        if (response == null || !response.isSuccess()) {
            throw new Exception(response == null ? "Empty" : response.getMessage());
        }
        List<ParameterEntity> entitys = new ArrayList<>();
        for (Item item : response.getData()) {
            entitys.add(buildEntity(item, ParameterEntity.RISK_BEHAVIOR));
        }
        return entitys;
    }

    /**
     * Nguyên nhân tử vong
     *
     * @auth vvThành
     * @return
     * @throws Exception
     */
    public List<ParameterEntity> getNguyenNhanTuVong() throws Exception {
        String url = baseUrl + "/nguyennhantuvong.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseCommon response = restTemplate.getForObject(url, ResponseCommon.class);
        if (!response.isSuccess()) {
            throw new Exception(response.getMessage());
        }
        List<ParameterEntity> entitys = new ArrayList<>();
        ParameterEntity entity = null;
        for (Item item : response.getData()) {
            entity = new ParameterEntity();
            entity.setStatus(1);
            entity.setElogCode(null);
            entity.setPosition(item.getId());
            entity.setCode(String.valueOf(item.getId()));
            entity.setType(ParameterEntity.CAUSE_OF_DEATH);
            entity.setHivInfoCode(String.valueOf(item.getId()));
            entity.setNote(item.getName());

            String[] name = item.getName().split(":");
            entity.setValue(StringUtils.trim(name.length >= 2 ? name[1] : name[0]));

            entitys.add(entity);
        }

        return entitys;
    }

    /**
     * Nơi lấy máu
     *
     * @param provinces
     * @param models
     * @return
     * @throws Exception
     */
    public List<ParameterEntity> getNoiLayMau(List<ProvinceEntity> provinces, List<ParameterEntity> models) throws Exception {
        HashMap<String, ProvinceEntity> provinceOptions = new HashMap<>();
        HashMap<String, ParameterEntity> modelOptions = new HashMap<>();

        for (ProvinceEntity province : provinces) {
            provinceOptions.put(province.getHivInfoCode(), province);
        }

        for (ParameterEntity item : models) {
            modelOptions.put(item.getHivInfoCode(), item);
        }

        String url = baseUrl + "/noilaymau.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseNoiLayMau response = restTemplate.getForObject(url, ResponseNoiLayMau.class);
        if (!response.isSuccess()) {
            throw new Exception(response.getMessage());
        }
        List<ParameterEntity> entitys = new ArrayList<>();
        ParameterEntity entity = null;
        for (NoiLayMau item : response.getData()) {
            entity = new ParameterEntity();
            entity.setStatus(1);
            entity.setElogCode(null);
            entity.setPosition(item.getId());
            entity.setCode(String.valueOf(item.getId()));
            entity.setType(ParameterEntity.BLOOD_BASE);
            entity.setHivInfoCode(String.valueOf(item.getId()));
            entity.setValue(item.getName());
            entity.setAttribute01(String.valueOf(item.getIdprovince()));
            entity.setAttribute02(provinceOptions.get(entity.getAttribute01()) == null ? null : provinceOptions.get(entity.getAttribute01()).getID());
            entity.setNote(provinceOptions.get(entity.getAttribute01()) == null ? null : provinceOptions.get(entity.getAttribute01()).getName());

            if (models != null && item.getParentsID() != null || !"".equals(item.getParentsID())) {
                ParameterEntity pe = modelOptions.get(item.getParentsID());
                entity.setParentID(pe == null ? 0 : pe.getID());
            }
            entitys.add(entity);
        }
        return entitys;
    }

    /**
     * Địa điểm giảm sat
     *
     * @auth vvThành
     * @return
     * @throws Exception
     */
    public List<ParameterEntity> getDiaDiemGiamSat() throws Exception {
        String url = baseUrl + "/diadiemgiamsat.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseCommon response = restTemplate.getForObject(url, ResponseCommon.class);
        if (!response.isSuccess()) {
            throw new Exception(response.getMessage());
        }
        List<ParameterEntity> entitys = new ArrayList<>();
        ParameterEntity entity = null;
        for (Item item : response.getData()) {
            entity = new ParameterEntity();
            entity.setStatus(1);
            entity.setElogCode(null);
            entity.setPosition(item.getId());
            entity.setCode(String.valueOf(item.getId()));
            entity.setType(ParameterEntity.LOCATION_MONITORING);
            entity.setHivInfoCode(String.valueOf(item.getId()));
            entity.setValue(item.getName());
            entitys.add(entity);
        }
        return entitys;
    }

    /**
     * Sinh phẩm xét nghiệm
     *
     * @auth vvThành
     * @return
     * @throws Exception
     */
    public List<ParameterEntity> getSinhPhamXetNghiem() throws Exception {
        String url = baseUrl + "/sinhphamxetnghiem.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseCommon response = restTemplate.getForObject(url, ResponseCommon.class);
        if (!response.isSuccess()) {
            throw new Exception(response.getMessage());
        }
        List<ParameterEntity> entitys = new ArrayList<>();
        for (Item item : response.getData()) {
            entitys.add(buildEntity(item, ParameterEntity.BIOLOGY_PRODUCT_TEST));
        }
        return entitys;
    }

    /**
     * Bệnh lây truyền
     *
     * @auth vvThành
     * @return
     * @throws Exception
     */
    public List<ParameterEntity> getBenhLayTruyen() throws Exception {
        String url = baseUrl + "/benhlaytruyen.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseCommon response = restTemplate.getForObject(url, ResponseCommon.class);
        if (!response.isSuccess()) {
            throw new Exception(response.getMessage());
        }
        List<ParameterEntity> entitys = new ArrayList<>();
        for (Item item : response.getData()) {
            entitys.add(buildEntity(item, ParameterEntity.COMMUNICABLE_DISEAS));
        }
        return entitys;
    }

    /**
     * Vị trí lấy mẫu
     *
     * @return
     * @throws Exception
     */
    public List<ParameterEntity> getViTriLayMau() throws Exception {
        String url = baseUrl + "/vitrilaymau.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseViTriLayMau response = restTemplate.getForObject(url, ResponseViTriLayMau.class);
        if (!response.isSuccess()) {
            throw new Exception(response.getMessage());
        }
        List<ParameterEntity> entitys = new ArrayList<>();
        ParameterEntity entity = null;
        for (ViTriLayMau item : response.getData()) {
            entity = new ParameterEntity();
            entity.setStatus(1);
            entity.setElogCode(null);
            entity.setPosition(item.getId());
            entity.setCode(String.valueOf(item.getId()));
            entity.setType(ParameterEntity.LOCATION_OF_BLOOD);
            entity.setHivInfoCode(String.valueOf(item.getId()));
            entity.setValue(item.getName());
            entity.setNote(item.getShortName());
            entity.setAttribute01(String.valueOf(item.getFormSize()));
            entity.setAttribute02(String.valueOf(item.getRegisterAddressId()));
            entitys.add(entity);
        }
        return entitys;
    }

    /**
     * Danh sách giới tính
     *
     * @throws java.lang.Exception
     * @auth vvThành
     * @return
     */
    public List<ParameterEntity> getGioiTinh() throws Exception {
        String url = baseUrl + "/gender.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ResponseGender> exchange = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(createHeaders(username, pwd)), ResponseGender.class);
        ResponseGender response = exchange.getBody();
        if (response == null || !response.isSuccess()) {
            throw new Exception(response == null ? "Empty" : response.getMessage());
        }
        List<ParameterEntity> entitys = new ArrayList<>();
        ParameterEntity entity = null;
        for (Gender item : response.getData()) {
            entity = new ParameterEntity();
            entity.setStatus(1);
            entity.setElogCode(null);
            entity.setPosition(item.getId());
            entity.setCode(String.valueOf(item.getId()));
            entity.setType(ParameterEntity.GENDER);
            entity.setHivInfoCode(String.valueOf(item.getId()));
            entity.setValue(item.getName());
            entitys.add(entity);
        }
        return entitys;
    }

    /**
     * Chuyển đổi tên cơ sở về một loại tên thống nhất
     *
     * @param str
     * @param province
     * @return
     */
    public String getSiteName(String str, ProvinceEntity province) {
        String name = str;
        Map<String, String> map = new HashMap<>();
        map.put("Trung tâm Phòng, chống HIV/AIDS", "Trung tâm phòng chống HIV/AIDS");
        map.put("Thanh Hóa PKNT", "Phòng khám ngoại trú");
        map.put("Nghệ An BV", "Bệnh viện");
        map.put("Nghệ An TTYT", "Trung tâm Y tế");
        map.put("TTYTDP", "Trung tâm Y tế dự phòng");
        map.put("TTYT", "Trung tâm Y tế");
        map.put("TT YT", "Trung tâm Y tế");
        map.put("TP", "Thành phố");
        map.put("TP.", "Thành phố");
        map.put("Nghệ An BVĐK", "Bệnh viện Đa khoa");
        map.put("BVĐK", "Bệnh viện Đa khoa");
        map.put("Nghệ An PKNT", "Phòng khám ngoại trú");
        map.put("PKNT", "Phòng khám ngoại trú");
        map.put("TX", "Thị xã");
        map.put("HN - OPC", "");
        map.put("OPC huyện", "Huyện");
        map.put("huyện", "Huyện");
        map.put("OPC", "");
        map.put("HCM", "");
        map.put("Bến Lức, Long An", "Bến Lức");
        try {
            String k = "Trung tâm Phòng, chống HIV/AIDS";
            if (str.contains(k)) {
                return String.format("%s %s", map.get(k), province.getName());
            }

            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (str.contains(key)) {
                    name = name.replaceAll(key, value);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return name.trim();
    }

    /**
     * Tình trạng hiện tại, trạng thái cư trú
     *
     * @auth vvThành
     * @return
     * @throws Exception
     */
    public List<ParameterEntity> getTinhTrangHienTai() throws Exception {
        String url = baseUrl + "/tinhtranghientai.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseCommon response = restTemplate.getForObject(url, ResponseCommon.class);
        if (!response.isSuccess()) {
            throw new Exception(response.getMessage());
        }
        List<ParameterEntity> entitys = new ArrayList<>();
        for (Item item : response.getData()) {
            entitys.add(buildEntity(item, ParameterEntity.STATUS_OF_RESIDENT));
        }
        return entitys;
    }

    /**
     * Loại bệnh nhân
     *
     * @auth vvThành
     * @return
     * @throws Exception
     */
    public List<ParameterEntity> getLoaiBenhNhan() throws Exception {
        String url = baseUrl + "/loaibenhnhan.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseCommon response = restTemplate.getForObject(url, ResponseCommon.class);
        if (!response.isSuccess()) {
            throw new Exception(response.getMessage());
        }
        List<ParameterEntity> entitys = new ArrayList<>();
        for (Item item : response.getData()) {
            entitys.add(buildEntity(item, ParameterEntity.TYPE_OF_PATIENT));
        }
        return entitys;
    }

    /**
     * Trạng thái điều trị
     *
     * @auth vvThành
     * @return
     * @throws Exception
     */
    public List<ParameterEntity> getTrangThaiDieuTri() throws Exception {
        String url = baseUrl + "/trangthaidieutri.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseCommon response = restTemplate.getForObject(url, ResponseCommon.class);
        if (!response.isSuccess()) {
            throw new Exception(response.getMessage());
        }
        List<ParameterEntity> entitys = new ArrayList<>();
        for (Item item : response.getData()) {
            entitys.add(buildEntity(item, ParameterEntity.STATUS_OF_TREATMENT));
        }
        return entitys;
    }

    /**
     * Chuyển đổi từ common sang entity param
     *
     * @param item
     * @param type
     * @return
     */
    private ParameterEntity buildEntity(Item item, String type) {
        ParameterEntity entity = new ParameterEntity();
        entity.setStatus(1);
        entity.setElogCode(null);
        entity.setPosition(item.getId());
        entity.setCode(String.valueOf(item.getId()));
        entity.setType(type);
        entity.setHivInfoCode(String.valueOf(item.getId()));
        entity.setValue(item.getName().trim());
        return entity;
    }

    /**
     * Danh sách giám sát phát hiện quản lý
     *
     * @param size
     * @param province
     * @auth vvThành
     * @return
     * @throws Exception
     */
    public Map<String, Data> getPatient(int size, String province) throws Exception {
        String url = baseUrl + "/patient/get.json?size=" + size + "&province_id=" + province;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ResponsePatient> exchange = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(createHeaders(username, pwd)), ResponsePatient.class);
        ResponsePatient response = exchange.getBody();
        if (response == null || !response.isSuccess()) {
            throw new Exception(response == null ? "Call Error" : response.getMessage());
        }
        Map<String, Data> data = response.getData();
        if (data != null && !data.isEmpty()) {
            for (Map.Entry<String, Data> entry : data.entrySet()) {
                Data item = entry.getValue();
                data.get(entry.getKey()).getGsph().setFullname(decode(item.getGsph().getFullname()));
                data.get(entry.getKey()).getGsph().setPatientID(decode(item.getGsph().getPatientID()));
                data.get(entry.getKey()).getGsph().setPermanentStreet(decode(item.getGsph().getPermanentStreet()));
                data.get(entry.getKey()).getGsph().setPermanentGroup(decode(item.getGsph().getPermanentGroup()));
                data.get(entry.getKey()).getGsph().setPermanentHomeNo(decode(item.getGsph().getPermanentHomeNo()));
                data.get(entry.getKey()).getGsph().setCurrentStreet(decode(item.getGsph().getCurrentStreet()));
                data.get(entry.getKey()).getGsph().setCurrentGroup(decode(item.getGsph().getCurrentGroup()));
                data.get(entry.getKey()).getGsph().setCurrentHomeNo(decode(item.getGsph().getCurrentHomeNo()));
            }
        }
        return data;
    }

    public List<ParameterEntity> getDantoc() throws Exception {
        String url = baseUrl + "/dantoc.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ResponseCommon> exchange = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(createHeaders(username, pwd)), ResponseCommon.class);
        ResponseCommon response = exchange.getBody();
        if (response == null || !response.isSuccess()) {
            throw new Exception(response == null ? "Null Ressult" : response.getMessage());
        }
        List<ParameterEntity> entitys = new ArrayList<>();
        for (Item item : response.getData()) {
            item.setName(item.getName().trim().equals("H mông") || item.getName().trim().equals("M nông")
                    || item.getName().trim().equals("H rê") || item.getName().trim().equals("K tu")
                    || item.getName().trim().equals("X tiêng") ? item.getName().trim().replace(" ", "") : item.getName().trim());
            item.setName(item.getName().trim().equals("Rag lai") ? "Ra-glai" : item.getName().trim());
            item.setName(item.getName().trim().equals("Rơ man") ? "Rơ-măm" : item.getName().trim());
            item.setName(item.getName().trim().equals("Vân kiều") ? "Bru-Vân Kiều" : item.getName().trim());
            item.setName(item.getName().trim().equals("Ktu") ? "Cơ-tu" : item.getName().trim());
            item.setName(item.getName().trim().equals("Khác") ? "Người nước ngoài" : item.getName().trim());
            entitys.add(buildEntity(item, ParameterEntity.RACE));
        }
        return entitys;
    }

    /**
     * Nhóm đối tượng
     *
     * @return
     * @throws Exception
     */
    public List<ParameterEntity> getDoituong() throws Exception {
        String url = baseUrl + "/doituong.json";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ResponseDoituong> exchange = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(createHeaders(username, pwd)), ResponseDoituong.class);
        ResponseDoituong response = exchange.getBody();
        if (response == null || !response.isSuccess()) {
            throw new Exception(response == null ? "Null Ressult" : response.getMessage());
        }
        List<ParameterEntity> entitys = new ArrayList<>();
        ParameterEntity entity;
        for (Doituong item : response.getData()) {
            if (!item.isMauGSPH() || item.getParentID() != null) {
                continue;
            }
            entity = new ParameterEntity();
            entity.setStatus(1);
            entity.setElogCode(null);
            entity.setPosition(item.getId());
            entity.setCode(item.getShortName());
            entity.setType(ParameterEntity.TEST_OBJECT_GROUP);
            entity.setHivInfoCode(String.valueOf(item.getId()));
            entity.setValue(item.getName());

            String[] name = item.getName().split(":");
            entity.setValue(StringUtils.trim(name.length >= 2 ? name[1] : name[0]));
            entitys.add(entity);
        }
        return entitys;
    }

    /**
     * Đẩy thông tin lên hiv info : quản lý người nhiễm
     *
     * @param data
     * @throws Exception
     */
    public void savePatientHiv(Data data) throws Exception {
//        data.getGsph().setFullname(encode(data.getGsph().getFullname() == null ? "" : data.getGsph().getFullname()));
//        data.getGsph().setPatientID(encode(data.getGsph().getPatientID() == null ? "" : data.getGsph().getPatientID()));
//        data.getGsph().setPermanentStreet(encode(data.getGsph().getPatientID() == null ? "" : data.getGsph().getPatientID()));
//        data.getGsph().setPermanentGroup(encode(data.getGsph().getPatientID() == null ? "" : data.getGsph().getPatientID()));
//        data.getGsph().setPermanentHomeNo(encode(data.getGsph().getPatientID() == null ? "" : data.getGsph().getPatientID()));
//        data.getGsph().setCurrentStreet(encode(data.getGsph().getPatientID() == null ? "" : data.getGsph().getPatientID()));
//        data.getGsph().setCurrentGroup(encode(data.getGsph().getPatientID() == null ? "" : data.getGsph().getPatientID()));
//        data.getGsph().setCurrentHomeNo(encode(data.getGsph().getPatientID() == null ? "" : data.getGsph().getPatientID()));
//        Gson gson = new Gson();
//        System.out.println("json: " + gson.toJson(data));

//        String url = baseUrl + "/patient/save.json";
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<ResponseDoituong> exchange = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(createHeaders(username, pwd)), ResponseDoituong.class);
//        ResponseDoituong response = exchange.getBody();
//        if (response == null || !response.isSuccess()) {
//            throw new Exception(response == null ? "Null Ressult" : response.getMessage());
//        }
    }
}
