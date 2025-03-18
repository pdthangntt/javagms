package com.gms.controller.service;

import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.repository.impl.PacPatientRepositoryImpl;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PacDashboardController extends BaseController {

    @Autowired
    private PacPatientRepositoryImpl patientRepositoryImpl;

    @RequestMapping(value = "/pac-dashboard/chartgeo.json", method = RequestMethod.GET)
    public Response<?> actionChartGeo() {
        LoggedUser currentUser = getCurrentUser();
        Map<String, Object> data = new HashMap<>();
        data.put("provinceName", !(isPAC() || isTTYT() || isTYT()) || currentUser.getSiteProvince() == null ? null : currentUser.getSiteProvince().getName());
        data.put("districtName", !(isTTYT() || isTYT()) || currentUser.getSiteDistrict() == null ? null : currentUser.getSiteDistrict().getName());
        data.put("wardName", !isTYT() || currentUser.getSiteWard() == null ? null : currentUser.getSiteWard().getName());
        data.put("chartgeo", patientRepositoryImpl.getChartGeo(
                (isPAC() || isTTYT() || isTYT() ? currentUser.getSite().getProvinceID() : null),
                (isTTYT() || isTYT() ? currentUser.getSite().getDistrictID() : null),
                (isTYT() ? currentUser.getSite().getWardID() : null)));
        Map<String, String> map = patientRepositoryImpl.getDataDasboard(
                (isPAC() || isTTYT() || isTYT() ? currentUser.getSite().getProvinceID() : null),
                (isTTYT() || isTYT() ? currentUser.getSite().getDistrictID() : null),
                (isTYT() ? currentUser.getSite().getWardID() : null));
        data.put("newTotal", map.get("total") == null ? "0" : map.get("total")); //Số phát hiện
        data.put("patientTotal", map.get("numAlive") == null ? "0" : map.get("numAlive")); //Số người nhiễm
        data.put("allTotal", map.get("numDead") == null ? "0" : map.get("numDead")); //Số luỹ tích
        data.put("opcTotal", map.get("numTreatment") == null ? "0" : map.get("numTreatment")); //Số đang điều trị
        return new Response<>(true, data);
    }

}
