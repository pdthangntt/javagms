package com.gms.controller.api.pqm;

import com.gms.components.TextUtils;
import com.gms.controller.api.qlnn.*;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.PqmHubEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.HtcVisitEntity;
import com.gms.entity.db.PqmVctEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.WardEntity;
import com.gms.service.DistrictService;
import com.gms.service.LocationsService;
import com.gms.service.PqmVctService;
import com.gms.service.ProvinceService;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "api_pqm_hub")
public class PqmController extends BaseController {

    @Autowired
    private PqmVctService vctService;
    @Autowired
    private LocationsService locationsService;

    @RequestMapping(value = "/v1/pqm-vct.api", method = RequestMethod.POST)
    public Response<?> actionSave(@RequestHeader("checksum") String checksum, @RequestBody String content) {
        try {
            String json = new String(Base64.getDecoder().decode(content.trim()));
            HtcVisitEntity visits = gson.fromJson(json, HtcVisitEntity.class);
            if (!checksum(checksum, String.valueOf(visits.getID()), String.valueOf(visits.getSiteID()))) {
                return new Response<>(false, "INCORRECT_CHECKSUM", "Mã xác thực không chính xác. vui lòng kiểm tra md5({ma_bi_mat}/{ma_confirm}/{ma_site})");
            }

            SiteEntity site = siteService.findOne(visits.getSiteID());
            if (site == null) {
                throw new Exception("Không tìm thấy cơ sở chuyển gửi");
            }
            List<HtcVisitEntity> items = new ArrayList<>();
            items.add(visits);

            HtcVisitEntity visit = getAddress(items).get(0);

            //Check ims
            if (!PqmHubEnum.FROM_IMS.getKey().equals(site.getHub())) {
                throw new Exception("Cơ sở không cho phép đổ dữ liệu từ IMS");
            }
            PqmVctEntity vct = vctService.findByCode(site.getID(), visit.getCode());
            String msg = "Cập nhật thành công";
            if (vct == null) {
                msg = "Thêm mới thành công";
                vct = new PqmVctEntity();
            }
            if (vct.getBlocked() != null && vct.getBlocked() == 1) {
                throw new Exception("Bản ghi bị khoá không cho phép sửa");
            }
            PqmVctEntity old = vct.clone();
            vct.set(visit);
            vct.setSiteID(site.getID());
            vct.setSource("IMS");

            vct.setIdentityCard(visit.getPatientID());
            vct.setResultsSiteTime(visit.getResultsSiteTime());
            vct.setResultsTime(visit.getResultsPatienTime());
            vct.setInsuranceNo(visit.getHealthInsuranceNo());
            vct.setPermanentAddress(visit.getPermanentAddressFull());
            vct.setCurrentAddress(visit.getCurrentAddressFull());

            vctService.save(vct);
            vctService.log(vct.getID(), "Đẩy dữ liệu từ IMS" + vct.changeToString(old, true));
            String body = Base64.getEncoder().encodeToString(gson.toJson(vct).getBytes());
            return new Response<>(true, msg, body);
        } catch (Exception e) {
            e.printStackTrace();
            return new Response<>(false, "EXCEPTION", e.getMessage());
        }
    }

    private List<HtcVisitEntity> getAddress(List<HtcVisitEntity> entities) {

        if (entities == null || entities.isEmpty()) {
            return null;
        }
        Set<String> pIDs = new HashSet<>();
        pIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getPermanentProvinceID")));
        pIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getCurrentProvinceID")));
        Map<String, ProvinceEntity> provinces = new HashMap<>();
        for (ProvinceEntity model : locationsService.findProvinceByIDs(pIDs)) {
            provinces.put(model.getID(), model);
        }

        Set<String> dIDs = new HashSet<>();
        dIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getPermanentDistrictID")));
        dIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getCurrentDistrictID")));
        Map<String, DistrictEntity> districts = new HashMap<>();
        for (DistrictEntity model : locationsService.findDistrictByIDs(dIDs)) {
            districts.put(model.getID(), model);
        }

        Set<String> wIDs = new HashSet<>();
        wIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getPermanentWardID")));
        wIDs.addAll(CollectionUtils.collect(entities, TransformerUtils.invokerTransformer("getCurrentWardID")));
        Map<String, WardEntity> wards = new HashMap<>();
        for (WardEntity model : locationsService.findWardByIDs(wIDs)) {
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

    private String buildAddress(String no, String street, String group, ProvinceEntity province, DistrictEntity district, WardEntity ward) {
        String address = StringUtils.isEmpty(no) ? "" : no;
        if (street != null && !"".equals(street)) {
            address += (StringUtils.isEmpty(address) ? "" : ", ") + street;
        }
        if (group != null && !"".equals(group)) {
            address += (StringUtils.isEmpty(address) ? "" : ", ") + group;
        }
        return buildAddress(address, province, district, ward);
    }

    private String buildAddress(String address, ProvinceEntity province, DistrictEntity district, WardEntity ward) {
        address = address == null ? "" : address;
        String key = TextUtils.removemarks(address);

        if (key.contains(TextUtils.removemarks("Việt Nam")) || key.contains(TextUtils.removemarks("ViệtNam"))) {
            return address;
        }

        if (ward != null && !key.contains("phuong")) {
//            String wardName = TextUtils.removemarks(ward.getName()).replaceAll("phuong-", "").replaceAll("phuong", "").trim();
//             && !key.contains(wardName)
            address += (StringUtils.isEmpty(address) ? "" : ", ") + ward.getName();
        }

        if (district != null && !key.contains(TextUtils.removemarks(district.getName()).replaceAll("huyen-", "").replaceAll("huyen", "").replaceAll("quan-", "").replaceAll("quan", "").replaceAll("thanh-pho-", "").replaceAll("thanh-pho", "").trim())) {
            address += (StringUtils.isEmpty(address) ? "" : ", ") + district.getName();
        }

        if (province != null && !key.contains(TextUtils.removemarks(province.getName()).replaceAll("tinh-", "").replaceAll("tinh", "").replaceAll("thanh-pho-", "").replaceAll("thanh-pho", "").trim())) {
            address += (StringUtils.isEmpty(address) ? "" : ", ") + province.getName();
        }
        return address;
    }

}
