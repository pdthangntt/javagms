package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.constant.PqmIndicatorEnum;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.PqmReportEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.repository.PqmReportRepository;
import com.gms.repository.impl.PqmImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author pdThang
 */
@Service
public class PqmReportService extends BaseService {

    @Autowired
    private SiteService siteService;
    @Autowired
    private PqmImpl pqmImpl;
    @Autowired
    private PqmReportRepository pqmRepository;
    @Autowired
    private LocationsService locationsService;

    public List<PqmReportEntity> findAll() {
        return (List<PqmReportEntity>) pqmRepository.findAll();
    }

    public void getHTS_TST_POS(int month, int year, String province) {
        Map<String, ProvinceEntity> provinces = new HashMap<>();
        Map<String, DistrictEntity> districts = new HashMap<>();
        for (ProvinceEntity item : locationsService.findAllProvince()) {
            provinces.put(item.getID(), item);
        }
        for (DistrictEntity item : locationsService.findAllDistrict()) {
            districts.put(item.getID(), item);
        }
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), province);

        Date startTime = TextUtils.getFirstDayOfMonth(month, year);
        Date endTime = TextUtils.getLastDayOfMonth(month, year);
        Map<Long, SiteEntity> siteIDs = new HashMap<>();
        for (SiteEntity site : sites) {
            siteIDs.put(site.getID(), site);
        }
        List<Object[]> items = pqmImpl.getHTS_TST_POS(siteIDs.keySet(), startTime, endTime);
        for (Object[] item : items) {
            Long siteID = Long.valueOf(item[0].toString());
            String provinceID = item[1].toString();
            String districtID = item[2].toString();
            String sexID = item[3].toString();
            String keyPopulationID = item[4].toString();
            String ageGroup = item[5].toString();
            Long quantity = Long.valueOf(item[6].toString());

            PqmReportEntity model = pqmRepository.findOne(month, year, PqmIndicatorEnum.HTS_TST_POS.getKey(), siteID, sexID, ageGroup, keyPopulationID);
            if (model == null) {
                ProvinceEntity provinceModel = provinces.getOrDefault(provinceID, null);
                DistrictEntity districtModel = districts.getOrDefault(districtID, null);
                SiteEntity siteModel = siteIDs.getOrDefault(siteID, null);
                model = new PqmReportEntity();
                model.setCreateAT(new Date());
                model.setProvinceCode(provinceID); //Mapping
                model.setDistrictCode(districtID); //Mapping
                model.setSiteCode(String.valueOf(siteID)); //Mapping
                model.setSex(sexID.equals("1") ? "MALE" : "FEMALE");
                model.setKeyPopulation(keyPopulationID); //Mapping
                model.setMultiMonth(0);

                model.setMonth(month);
                model.setYear(year);
                model.setIndicatorCode(PqmIndicatorEnum.HTS_TST_POS.getKey());
                model.setProvinceID(provinceID);
                model.setProvinceName(provinceModel == null ? null : provinceModel.getName());
                model.setDistrictID(provinceID);
                model.setDistrictName(districtModel == null ? null : districtModel.getName());
                model.setSiteID(siteID);
                model.setSiteName(siteModel == null ? null : siteModel.getName());
                model.setSexID(sexID);
                model.setAgeGroup(ageGroup);
                model.setKeyPopulationID(keyPopulationID);
            }
            model.setUpdateAt(new Date());
            model.setQuantity(quantity);
            pqmRepository.save(model);
        }
    }

    public void getHTS_TST_RECENCY(int month, int year, String province) {
        Map<String, ProvinceEntity> provinces = new HashMap<>();
        Map<String, DistrictEntity> districts = new HashMap<>();
        for (ProvinceEntity item : locationsService.findAllProvince()) {
            provinces.put(item.getID(), item);
        }
        for (DistrictEntity item : locationsService.findAllDistrict()) {
            districts.put(item.getID(), item);
        }
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), province);

        Date startTime = TextUtils.getFirstDayOfMonth(month, year);
        Date endTime = TextUtils.getLastDayOfMonth(month, year);
        Map<Long, SiteEntity> siteIDs = new HashMap<>();
        for (SiteEntity site : sites) {
            siteIDs.put(site.getID(), site);
        }
        List<Object[]> items = pqmImpl.getHTS_TST_RECENCY(siteIDs.keySet(), startTime, endTime);
        for (Object[] item : items) {
            Long siteID = Long.valueOf(item[0].toString());
            String provinceID = item[1].toString();
            String districtID = item[2].toString();
            String sexID = item[3].toString();
            String keyPopulationID = item[4].toString();
            String ageGroup = item[5].toString();
            Long quantity = Long.valueOf(item[6].toString());

            PqmReportEntity model = pqmRepository.findOne(month, year, PqmIndicatorEnum.HTS_TST_RECENCY.getKey(), siteID, sexID, ageGroup, keyPopulationID);
            if (model == null) {
                ProvinceEntity provinceModel = provinces.getOrDefault(provinceID, null);
                DistrictEntity districtModel = districts.getOrDefault(districtID, null);
                SiteEntity siteModel = siteIDs.getOrDefault(siteID, null);
                model = new PqmReportEntity();
                model.setCreateAT(new Date());
                model.setProvinceCode(provinceID); //Mapping
                model.setDistrictCode(districtID); //Mapping
                model.setSiteCode(String.valueOf(siteID)); //Mapping
                model.setSex(sexID.equals("1") ? "MALE" : "FEMALE");
                model.setKeyPopulation(keyPopulationID); //Mapping
                model.setMultiMonth(0);

                model.setMonth(month);
                model.setYear(year);
                model.setIndicatorCode(PqmIndicatorEnum.HTS_TST_RECENCY.getKey());
                model.setProvinceID(provinceID);
                model.setProvinceName(provinceModel == null ? null : provinceModel.getName());
                model.setDistrictID(provinceID);
                model.setDistrictName(districtModel == null ? null : districtModel.getName());
                model.setSiteID(siteID);
                model.setSiteName(siteModel == null ? null : siteModel.getName());
                model.setSexID(sexID);
                model.setAgeGroup(ageGroup);
                model.setKeyPopulationID(keyPopulationID);
            }
            model.setUpdateAt(new Date());
            model.setQuantity(quantity);
            pqmRepository.save(model);
        }
    }

    public void getPOS_TO_ART(int month, int year, String province) {
        Map<String, ProvinceEntity> provinces = new HashMap<>();
        Map<String, DistrictEntity> districts = new HashMap<>();
        for (ProvinceEntity item : locationsService.findAllProvince()) {
            provinces.put(item.getID(), item);
        }
        for (DistrictEntity item : locationsService.findAllDistrict()) {
            districts.put(item.getID(), item);
        }
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), province);

        Date startTime = TextUtils.getFirstDayOfMonth(month, year);
        Date endTime = TextUtils.getLastDayOfMonth(month, year);
        Map<Long, SiteEntity> siteIDs = new HashMap<>();
        for (SiteEntity site : sites) {
            siteIDs.put(site.getID(), site);
        }
        List<Object[]> items = pqmImpl.getPOS_TO_ART(siteIDs.keySet(), startTime, endTime);
        for (Object[] item : items) {
            Long siteID = Long.valueOf(item[0].toString());
            String provinceID = item[1].toString();
            String districtID = item[2].toString();
            String sexID = item[3].toString();
            String keyPopulationID = item[4].toString();
            String ageGroup = item[5].toString();
            Long quantity = Long.valueOf(item[6].toString());

            PqmReportEntity model = pqmRepository.findOne(month, year, PqmIndicatorEnum.POS_TO_ART.getKey(), siteID, sexID, ageGroup, keyPopulationID);
            if (model == null) {
                ProvinceEntity provinceModel = provinces.getOrDefault(provinceID, null);
                DistrictEntity districtModel = districts.getOrDefault(districtID, null);
                SiteEntity siteModel = siteIDs.getOrDefault(siteID, null);
                model = new PqmReportEntity();
                model.setCreateAT(new Date());
                model.setProvinceCode(provinceID); //Mapping
                model.setDistrictCode(districtID); //Mapping
                model.setSiteCode(String.valueOf(siteID)); //Mapping
                model.setSex(sexID.equals("1") ? "MALE" : "FEMALE");
                model.setKeyPopulation(keyPopulationID); //Mapping
                model.setMultiMonth(0);

                model.setMonth(month);
                model.setYear(year);
                model.setIndicatorCode(PqmIndicatorEnum.POS_TO_ART.getKey());
                model.setProvinceID(provinceID);
                model.setProvinceName(provinceModel == null ? null : provinceModel.getName());
                model.setDistrictID(provinceID);
                model.setDistrictName(districtModel == null ? null : districtModel.getName());
                model.setSiteID(siteID);
                model.setSiteName(siteModel == null ? null : siteModel.getName());
                model.setSexID(sexID);
                model.setAgeGroup(ageGroup);
                model.setKeyPopulationID(keyPopulationID);
            }
            model.setUpdateAt(new Date());
            model.setQuantity(quantity);
            pqmRepository.save(model);
        }
    }

    public void getIMS_TST_POS(int month, int year, String province) {
        Map<String, ProvinceEntity> provinces = new HashMap<>();
        Map<String, DistrictEntity> districts = new HashMap<>();
        for (ProvinceEntity item : locationsService.findAllProvince()) {
            provinces.put(item.getID(), item);
        }
        for (DistrictEntity item : locationsService.findAllDistrict()) {
            districts.put(item.getID(), item);
        }
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), province);

        Date startTime = TextUtils.getFirstDayOfMonth(month, year);
        Date endTime = TextUtils.getLastDayOfMonth(month, year);
        Map<Long, SiteEntity> siteIDs = new HashMap<>();
        for (SiteEntity site : sites) {
            siteIDs.put(site.getID(), site);
        }
        List<Object[]> items = pqmImpl.getIMS_TST_POS(siteIDs.keySet(), startTime, endTime);
        for (Object[] item : items) {
            Long siteID = Long.valueOf(item[0].toString());
            String provinceID = item[1].toString();
            String districtID = item[2].toString();
            String sexID = item[3].toString();
            String keyPopulationID = item[4].toString();
            String ageGroup = item[5].toString();
            Long quantity = Long.valueOf(item[6].toString());

            PqmReportEntity model = pqmRepository.findOne(month, year, PqmIndicatorEnum.IMS_TST_POS.getKey(), siteID, sexID, ageGroup, keyPopulationID);
            if (model == null) {
                ProvinceEntity provinceModel = provinces.getOrDefault(provinceID, null);
                DistrictEntity districtModel = districts.getOrDefault(districtID, null);
                SiteEntity siteModel = siteIDs.getOrDefault(siteID, null);
                model = new PqmReportEntity();
                model.setCreateAT(new Date());
                model.setProvinceCode(provinceID); //Mapping
                model.setDistrictCode(districtID); //Mapping
                model.setSiteCode(String.valueOf(siteID)); //Mapping
                model.setSex(sexID.equals("1") ? "MALE" : "FEMALE");
                model.setKeyPopulation(keyPopulationID); //Mapping
                model.setMultiMonth(0);

                model.setMonth(month);
                model.setYear(year);
                model.setIndicatorCode(PqmIndicatorEnum.IMS_TST_POS.getKey());
                model.setProvinceID(provinceID);
                model.setProvinceName(provinceModel == null ? null : provinceModel.getName());
                model.setDistrictID(provinceID);
                model.setDistrictName(districtModel == null ? null : districtModel.getName());
                model.setSiteID(siteID);
                model.setSiteName(siteModel == null ? null : siteModel.getName());
                model.setSexID(sexID);
                model.setAgeGroup(ageGroup);
                model.setKeyPopulationID(keyPopulationID);
            }
            model.setUpdateAt(new Date());
            model.setQuantity(quantity);
            pqmRepository.save(model);
        }
    }

    public void getIMS_POS_ART(int month, int year, String province) {
        Map<String, ProvinceEntity> provinces = new HashMap<>();
        Map<String, DistrictEntity> districts = new HashMap<>();
        for (ProvinceEntity item : locationsService.findAllProvince()) {
            provinces.put(item.getID(), item);
        }
        for (DistrictEntity item : locationsService.findAllDistrict()) {
            districts.put(item.getID(), item);
        }
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), province);

        Date startTime = TextUtils.getFirstDayOfMonth(month, year);
        Date endTime = TextUtils.getLastDayOfMonth(month, year);
        Map<Long, SiteEntity> siteIDs = new HashMap<>();
        for (SiteEntity site : sites) {
            siteIDs.put(site.getID(), site);
        }
        List<Object[]> items = pqmImpl.getIMS_POS_ART(siteIDs.keySet(), startTime, endTime);
        for (Object[] item : items) {
            Long siteID = Long.valueOf(item[0].toString());
            String provinceID = item[1].toString();
            String districtID = item[2].toString();
            String sexID = item[3].toString();
            String keyPopulationID = item[4].toString();
            String ageGroup = item[5].toString();
            Long quantity = Long.valueOf(item[6].toString());

            PqmReportEntity model = pqmRepository.findOne(month, year, PqmIndicatorEnum.IMS_POS_ART.getKey(), siteID, sexID, ageGroup, keyPopulationID);
            if (model == null) {
                ProvinceEntity provinceModel = provinces.getOrDefault(provinceID, null);
                DistrictEntity districtModel = districts.getOrDefault(districtID, null);
                SiteEntity siteModel = siteIDs.getOrDefault(siteID, null);
                model = new PqmReportEntity();
                model.setCreateAT(new Date());
                model.setProvinceCode(provinceID); //Mapping
                model.setDistrictCode(districtID); //Mapping
                model.setSiteCode(String.valueOf(siteID)); //Mapping
                model.setSex(sexID.equals("1") ? "MALE" : "FEMALE");
                model.setKeyPopulation(keyPopulationID); //Mapping
                model.setMultiMonth(0);

                model.setMonth(month);
                model.setYear(year);
                model.setIndicatorCode(PqmIndicatorEnum.IMS_POS_ART.getKey());
                model.setProvinceID(provinceID);
                model.setProvinceName(provinceModel == null ? null : provinceModel.getName());
                model.setDistrictID(provinceID);
                model.setDistrictName(districtModel == null ? null : districtModel.getName());
                model.setSiteID(siteID);
                model.setSiteName(siteModel == null ? null : siteModel.getName());
                model.setSexID(sexID);
                model.setAgeGroup(ageGroup);
                model.setKeyPopulationID(keyPopulationID);
            }
            model.setUpdateAt(new Date());
            model.setQuantity(quantity);
            pqmRepository.save(model);
        }
    }

    public void getIMS_TST_RECENCY(int month, int year, String province) {
        Map<String, ProvinceEntity> provinces = new HashMap<>();
        Map<String, DistrictEntity> districts = new HashMap<>();
        for (ProvinceEntity item : locationsService.findAllProvince()) {
            provinces.put(item.getID(), item);
        }
        for (DistrictEntity item : locationsService.findAllDistrict()) {
            districts.put(item.getID(), item);
        }
        List<SiteEntity> sites = siteService.findByServiceAndProvince(ServiceEnum.HTC.getKey(), province);

        Date startTime = TextUtils.getFirstDayOfMonth(month, year);
        Date endTime = TextUtils.getLastDayOfMonth(month, year);
        Map<Long, SiteEntity> siteIDs = new HashMap<>();
        for (SiteEntity site : sites) {
            siteIDs.put(site.getID(), site);
        }
        List<Object[]> items = pqmImpl.getIMS_TST_RECENCY(siteIDs.keySet(), startTime, endTime);
        for (Object[] item : items) {
            Long siteID = Long.valueOf(item[0].toString());
            String provinceID = item[1].toString();
            String districtID = item[2].toString();
            String sexID = item[3].toString();
            String keyPopulationID = item[4].toString();
            String ageGroup = item[5].toString();
            Long quantity = Long.valueOf(item[6].toString());

            PqmReportEntity model = pqmRepository.findOne(month, year, PqmIndicatorEnum.IMS_TST_RECENCY.getKey(), siteID, sexID, ageGroup, keyPopulationID);
            if (model == null) {
                ProvinceEntity provinceModel = provinces.getOrDefault(provinceID, null);
                DistrictEntity districtModel = districts.getOrDefault(districtID, null);
                SiteEntity siteModel = siteIDs.getOrDefault(siteID, null);
                model = new PqmReportEntity();
                model.setCreateAT(new Date());
                model.setProvinceCode(provinceID); //Mapping
                model.setDistrictCode(districtID); //Mapping
                model.setSiteCode(String.valueOf(siteID)); //Mapping
                model.setSex(sexID.equals("1") ? "MALE" : "FEMALE");
                model.setKeyPopulation(keyPopulationID); //Mapping
                model.setMultiMonth(0);

                model.setMonth(month);
                model.setYear(year);
                model.setIndicatorCode(PqmIndicatorEnum.IMS_TST_RECENCY.getKey());
                model.setProvinceID(provinceID);
                model.setProvinceName(provinceModel == null ? null : provinceModel.getName());
                model.setDistrictID(provinceID);
                model.setDistrictName(districtModel == null ? null : districtModel.getName());
                model.setSiteID(siteID);
                model.setSiteName(siteModel == null ? null : siteModel.getName());
                model.setSexID(sexID);
                model.setAgeGroup(ageGroup);
                model.setKeyPopulationID(keyPopulationID);
            }
            model.setUpdateAt(new Date());
            model.setQuantity(quantity);
            pqmRepository.save(model);
        }
    }

}
