package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.entity.bean.DataPage;
import com.gms.entity.constant.PqmIndicatorEnum;
import com.gms.entity.db.PqmDataEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.repository.PqmDataRepository;
import com.gms.repository.impl.PqmDataImpl;
import com.gms.repository.impl.PqmImpl;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
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
public class PqmDataService extends BaseService implements IBaseService<PqmDataEntity, Long> {

    @Autowired
    private SiteService siteService;
    @Autowired
    private PqmDataRepository pqmRepository;
    @Autowired
    private LocationsService locationsService;

    @Autowired
    private PqmDataImpl pqmDataImpl;

    public DataPage<PqmDataEntity> findData(int month, int year, Set<Long> siteID,
            int pageIndex, int pageSize) {
        DataPage<PqmDataEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(pageIndex);
        dataPage.setMaxResult(pageSize);
        Sort sortable = Sort.by(Sort.Direction.ASC, new String[]{"siteID"});
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, sortable);

        Page<PqmDataEntity> pages = pqmRepository.findData(month,
                year,
                siteID,
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());

        return dataPage;
    }

    public PqmDataEntity findOne(int month,
            int year,
            String indicatorCode,
            Long siteID,
            String sexID,
            String ageGroup,
            String objectGroupID) {
        PqmDataEntity entity = pqmRepository.findOne(month,
                year,
                indicatorCode,
                siteID,
                sexID,
                ageGroup,
                objectGroupID);
        return entity;
    }

    public List<PqmDataEntity> findByIndicator(
            int month,
            int year,
            String indicatorCode,
            Long siteID) {
        List<PqmDataEntity> entitys = pqmRepository.findByIndicator(month,
                year,
                indicatorCode,
                siteID);
        return entitys.isEmpty() ? null : entitys;

    }

    public List<PqmDataEntity> findBySiteID(
            int month,
            int year,
            Long siteID) {
        List<PqmDataEntity> entitys = pqmRepository.findBySiteID(month,
                year,
                siteID);
        return entitys.isEmpty() ? null : entitys;

    }

    public List<PqmDataEntity> findBySiteIDs(
            int month,
            int year,
            Set<Long> siteID) {
        List<PqmDataEntity> entitys = pqmRepository.findBySiteIDs(month,
                year,
                siteID);
        return entitys.isEmpty() ? null : entitys;

    }

    public List<PqmDataEntity> findBySiteIDsOrderBySiteID(
            int month,
            int year,
            Set<Long> siteID) {
        List<PqmDataEntity> entitys = pqmRepository.findBySiteIDsOrderBySiteID(month,
                year,
                siteID);
        return entitys.isEmpty() ? null : entitys;

    }

    public List<PqmDataEntity> findByIndicator(
            int month,
            int year,
            Set<Long> siteID) {
        List<PqmDataEntity> entitys = pqmRepository.findByIndicator(month,
                year,
                siteID);
        return entitys.isEmpty() ? null : entitys;

    }

    public List<PqmDataEntity> findByIndicator(
            int month,
            int year,
            Set<Long> siteID,
            Set<Long> siteSendID
    ) {
        List<PqmDataEntity> entitys = pqmRepository.findByIndicator(month,
                year,
                siteID.isEmpty() ? null : siteID,
                siteSendID.isEmpty() ? null : siteSendID);
        return entitys.isEmpty() ? null : entitys;

    }

    public List<PqmDataEntity> findByIndicatorConfirm(
            int month,
            int year,
            Set<Long> siteID,
            Set<Long> siteSendID
    ) {
        List<PqmDataEntity> entitys = pqmRepository.findByIndicatorConfirm(month,
                year,
                siteID.isEmpty() ? null : siteID,
                siteSendID.isEmpty() ? null : siteSendID);
        return entitys.isEmpty() ? null : entitys;

    }

    public List<PqmDataEntity> findByIndicatorNotConfirm(
            int month,
            int year,
            Set<Long> siteID,
            Set<Long> siteSendID
    ) {
        List<PqmDataEntity> entitys = pqmRepository.findByIndicatorNotConfirm(month,
                year,
                siteID.isEmpty() ? null : siteID,
                siteSendID.isEmpty() ? null : siteSendID);
        return entitys.isEmpty() ? null : entitys;

    }

    @Override
    public List<PqmDataEntity> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public PqmDataEntity findOne(Long ID) {
        PqmDataEntity entity = pqmRepository.findOne(ID);
        return entity;
    }

    @Override
    public PqmDataEntity save(PqmDataEntity entity) {
        Date currentDate = new Date();
        PqmDataEntity model;
        if (entity.getID() == null || entity.getID() == 0) {
            model = new PqmDataEntity();
            model.setCreateAT(currentDate);
        } else {
            model = findOne(entity.getID());
        }
        try {
            model.set(entity);
        } catch (IllegalAccessException | NoSuchFieldException ex) {
//            Logger.getLogger(HtcTargetService.class.getName()).log(Level.SEVERE, null, ex);
        }
        model.setUpdateAt(currentDate);

        SiteEntity site = siteService.findOne(model.getSiteID());
        if (site != null) {
            model.setProvinceID(site.getProvinceID());
            model.setDistrictID(site.getDistrictID());
        }

        model = pqmRepository.save(model);
        return model;
    }

    public List<PqmDataEntity> saveAlls(List<PqmDataEntity> entitys) {
        Date currentDate = new Date();
        Map<Long, String> provinces = new HashMap<>();
        Map<Long, String> districts = new HashMap<>();
        for (SiteEntity siteEntity : siteService.findAll()) {
            provinces.put(siteEntity.getID(), siteEntity.getProvinceID());
            districts.put(siteEntity.getID(), siteEntity.getDistrictID());
        }
        List<PqmDataEntity> list = new ArrayList<>();
        int i = 1;
        for (PqmDataEntity entity : entitys) {
            i++;
            if (entity.getID() == null || entity.getID() == 0) {
                entity.setCreateAT(currentDate);
            }
            entity.setUpdateAt(currentDate);
            entity.setProvinceID(provinces.get(entity.getSiteID()));
            entity.setDistrictID(districts.get(entity.getSiteID()));
            if (StringUtils.isNotEmpty(entity.getIndicatorCode())) {
                list.add(entity);
            }
        }
        return saveAllz(list);
    }

    public List<PqmDataEntity> saveAllz(List<PqmDataEntity> entities) {
        Iterable<PqmDataEntity> all = pqmRepository.saveAll(entities);
        List<PqmDataEntity> copy = ImmutableList.copyOf(all);
        return copy;
    }

    public void saveAll(List<PqmDataEntity> entitys) {
        for (PqmDataEntity e : entitys) {
            save(e);
        }

    }

    public PqmDataEntity findOne(Long siteID, String indicator, int month, int year, String gender, String object, String ageGroup) {
        PqmDataEntity entity = pqmRepository.findOne(siteID, indicator, month, year, gender, object, ageGroup);
        return entity;
    }

    public void getHTS_TST_POS(int month, int year, Long siteID) {

        Date startTime = TextUtils.getFirstDayOfMonth(month, year);
        Date endTime = TextUtils.getLastDayOfMonth(month, year);
        Set<Long> siteIDs = new HashSet<>();

        siteIDs.add(siteID);
        List<Object[]> items = pqmDataImpl.getHTS_TST_POS(siteIDs, startTime, endTime);
        List<PqmDataEntity> list = new ArrayList<>();
        for (Object[] item : items) {

            String gender = item[0] == null ? "" : item[0].toString();
            String object = item[1] == null ? "" : item[1].toString();
            String ageGroup = item[2].toString();
            Long quantity = item[3] == null ? Long.valueOf("0") : Long.valueOf(item[3].toString());
            if (quantity.equals(Long.valueOf("0"))) {
                continue;
            }
            PqmDataEntity model = new PqmDataEntity();
            model.setSiteID(siteID);
            model.setIndicatorCode(PqmIndicatorEnum.HTS_TST_POS.getKey());
            model.setMonth(month);
            model.setYear(year);
            model.setSexID(gender.equals("1") ? "nam" : gender.equals("2") ? "nu" : "");
            model.setObjectGroupID(object);
            model.setAgeGroup(ageGroup);
            model.setMultiMonth(0);
            model.setStatus(3);
            model.setQuantity(quantity);

            list.add(model);
        }
        saveAlls(list);
    }

    public void getHTS_TST_Recency(int month, int year, Long siteID) {

        Date startTime = TextUtils.getFirstDayOfMonth(month, year);
        Date endTime = TextUtils.getLastDayOfMonth(month, year);
        Set<Long> siteIDs = new HashSet<>();

        siteIDs.add(siteID);
        List<Object[]> items = pqmDataImpl.getHTS_TST_Recency(siteIDs, startTime, endTime);
        List<PqmDataEntity> list = new ArrayList<>();
        for (Object[] item : items) {

            String gender = item[0] == null ? "" : item[0].toString();
            String object = item[1] == null ? "" : item[1].toString();
            String ageGroup = item[2].toString();
            Long quantity = item[3] == null ? Long.valueOf("0") : Long.valueOf(item[3].toString());
            if (quantity.equals(Long.valueOf("0"))) {
                continue;
            }

            PqmDataEntity model = new PqmDataEntity();

            model.setSiteID(siteID);
            model.setIndicatorCode(PqmIndicatorEnum.HTS_TST_Recency.getKey());
            model.setMonth(month);
            model.setYear(year);
            model.setSexID(gender.equals("1") ? "nam" : gender.equals("2") ? "nu" : "");
            model.setObjectGroupID(object);
            model.setAgeGroup(ageGroup);
            model.setMultiMonth(0);
            model.setStatus(3);
            model.setQuantity(quantity);
            model.setKeyEarly("true");

            list.add(model);
        }
        saveAlls(list);
    }

    public void getPOS_TO_ART(int month, int year, Long siteID) {

        Date startTime = TextUtils.getFirstDayOfMonth(month, year);
        Date endTime = TextUtils.getLastDayOfMonth(month, year);
        Set<Long> siteIDs = new HashSet<>();

        siteIDs.add(siteID);
        List<Object[]> items = pqmDataImpl.getPOS_TO_ART(siteIDs, startTime, endTime);
        List<PqmDataEntity> list = new ArrayList<>();
        for (Object[] item : items) {

            String gender = item[0] == null ? "" : item[0].toString();
            String object = item[1] == null ? "" : item[1].toString();
            String ageGroup = item[2].toString();
            Long quantity = item[3] == null ? Long.valueOf("0") : Long.valueOf(item[3].toString());
            if (quantity.equals(Long.valueOf("0"))) {
                continue;
            }

            PqmDataEntity model = new PqmDataEntity();

            model.setSiteID(siteID);
            model.setIndicatorCode(PqmIndicatorEnum.POS_TO_ART.getKey());
            model.setMonth(month);
            model.setYear(year);
            model.setSexID(gender.equals("1") ? "nam" : gender.equals("2") ? "nu" : "");
            model.setObjectGroupID(object);
            model.setAgeGroup(ageGroup);
            model.setMultiMonth(0);
            model.setStatus(3);
            model.setQuantity(quantity);

            list.add(model);
        }
        saveAlls(list);
    }

    public void getTX_New(int month, int year, Long siteID) {

        Date startTime = TextUtils.getFirstDayOfMonth(month, year);
        Date endTime = TextUtils.getLastDayOfMonth(month, year);
        Set<Long> siteIDs = new HashSet<>();

        siteIDs.add(siteID);
        List<Object[]> items = pqmDataImpl.getTX_New(siteIDs, startTime, endTime);
        List<PqmDataEntity> list = new ArrayList<>();
        for (Object[] item : items) {

            String gender = item[0] == null ? "" : item[0].toString();
            String object = item[1] == null ? "" : item[1].toString();
            String ageGroup = item[2].toString();
            Long quantity = item[3] == null ? Long.valueOf("0") : Long.valueOf(item[3].toString());
            if (quantity.equals(Long.valueOf("0"))) {
                continue;
            }

            PqmDataEntity model = new PqmDataEntity();

            model.setSiteID(siteID);
            model.setIndicatorCode(PqmIndicatorEnum.TX_New.getKey());
            model.setMonth(month);
            model.setYear(year);
            model.setSexID(gender.equals("1") ? "nam" : gender.equals("2") ? "nu" : "");
            model.setObjectGroupID(object);
            model.setAgeGroup(ageGroup);
            model.setMultiMonth(0);
            model.setStatus(3);
            model.setQuantity(quantity);

            list.add(model);
        }
        saveAlls(list);
    }

    public void getTX_CURR(int month, int year, Long siteID) {

        Date startTime = TextUtils.getFirstDayOfMonth(month, year);
        Date endTime = TextUtils.getLastDayOfMonth(month, year);
        Set<Long> siteIDs = new HashSet<>();

        siteIDs.add(siteID);
        List<Object[]> items = pqmDataImpl.getTX_CURR(siteIDs, startTime, endTime);
        List<PqmDataEntity> list = new ArrayList<>();
        for (Object[] item : items) {

            String gender = item[0] == null ? "" : item[0].toString();
            String object = item[1] == null ? "" : item[1].toString();
            String ageGroup = item[2].toString();
            Long quantity = item[3] == null ? Long.valueOf("0") : Long.valueOf(item[3].toString());
            if (quantity.equals(Long.valueOf("0"))) {
                continue;
            }

            PqmDataEntity model = new PqmDataEntity();

            model.setSiteID(siteID);
            model.setIndicatorCode(PqmIndicatorEnum.TX_CURR.getKey());
            model.setMonth(month);
            model.setYear(year);
            model.setSexID(gender.equals("1") ? "nam" : gender.equals("2") ? "nu" : "");
            model.setObjectGroupID(object);
            model.setAgeGroup(ageGroup);
            model.setMultiMonth(0);
            model.setStatus(3);
            model.setQuantity(quantity);

            list.add(model);
        }
        saveAlls(list);
    }

    public void getMMD(int month, int year, Long siteID) {

        Date startTime = TextUtils.getFirstDayOfMonth(month, year);
        Date endTime = TextUtils.getLastDayOfMonth(month, year);
        Set<Long> siteIDs = new HashSet<>();

        siteIDs.add(siteID);
        List<Object[]> items = pqmDataImpl.getMMD(siteIDs, startTime, endTime);
        List<PqmDataEntity> list = new ArrayList<>();
        for (Object[] item : items) {

            String gender = item[0] == null ? "" : item[0].toString();
            String object = item[1] == null ? "" : item[1].toString();
            String ageGroup = item[2].toString();
            Long quantity = item[3] == null ? Long.valueOf("0") : Long.valueOf(item[3].toString());
            if (quantity.equals(Long.valueOf("0"))) {
                continue;
            }

            PqmDataEntity model = new PqmDataEntity();

            model.setSiteID(siteID);
            model.setIndicatorCode(PqmIndicatorEnum.MMD.getKey());
            model.setMonth(month);
            model.setYear(year);
            model.setSexID(gender.equals("1") ? "nam" : gender.equals("2") ? "nu" : "");
            model.setObjectGroupID(object);
            model.setAgeGroup(ageGroup);
            model.setMultiMonth(0);
            model.setStatus(3);
            model.setQuantity(quantity);

            list.add(model);
        }
        saveAlls(list);
    }

    public void getIIT(int month, int year, Long siteID) {

        Date startTime = TextUtils.getFirstDayOfMonth(month, year);
        Date endTime = TextUtils.getLastDayOfMonth(month, year);
        Set<Long> siteIDs = new HashSet<>();

        siteIDs.add(siteID);
        List<Object[]> items = pqmDataImpl.getIIT(siteIDs, startTime, endTime);
        List<PqmDataEntity> list = new ArrayList<>();
        for (Object[] item : items) {

            String gender = item[0] == null ? "" : item[0].toString();
            String object = item[1] == null ? "" : item[1].toString();
            String ageGroup = item[2].toString();
            Long quantity = item[3] == null ? Long.valueOf("0") : Long.valueOf(item[3].toString());
            if (quantity.equals(Long.valueOf("0"))) {
                continue;
            }

            PqmDataEntity model = new PqmDataEntity();

            model.setSiteID(siteID);
            model.setIndicatorCode(PqmIndicatorEnum.IIT.getKey());
            model.setMonth(month);
            model.setYear(year);
            model.setSexID(gender.equals("1") ? "nam" : gender.equals("2") ? "nu" : "");
            model.setObjectGroupID(object);
            model.setAgeGroup(ageGroup);
            model.setMultiMonth(0);
            model.setStatus(3);
            model.setQuantity(quantity);

            list.add(model);
        }
        saveAlls(list);
    }

    public void getVL_detectable(int month, int year, Long siteID) {

        Date startTime = TextUtils.getFirstDayOfMonth(month, year);
        Date endTime = TextUtils.getLastDayOfMonth(month, year);
        Set<Long> siteIDs = new HashSet<>();

        siteIDs.add(siteID);
        List<Object[]> items = pqmDataImpl.getVL_detectable(siteIDs, startTime, endTime);
        List<PqmDataEntity> list = new ArrayList<>();
        for (Object[] item : items) {

            String gender = item[0] == null ? "" : item[0].toString();
            String object = item[1] == null ? "" : item[1].toString();
            String ageGroup = item[2].toString();
            Long quantity = item[3] == null ? Long.valueOf("0") : Long.valueOf(item[3].toString());
            if (quantity.equals(Long.valueOf("0"))) {
                continue;
            }

            PqmDataEntity model = new PqmDataEntity();

            model.setSiteID(siteID);
            model.setIndicatorCode(PqmIndicatorEnum.VL_detectable.getKey());
            model.setMonth(month);
            model.setYear(year);
            model.setSexID(gender.equals("1") ? "nam" : gender.equals("2") ? "nu" : "");
            model.setObjectGroupID(object);
            model.setAgeGroup(ageGroup);
            model.setMultiMonth(0);
            model.setStatus(3);
            model.setQuantity(quantity);

            list.add(model);
        }
        saveAlls(list);
    }

    public void getTB_PREV(int month, int year, Long siteID) {

        Date startTime = TextUtils.getFirstDayOfMonth(month, year);
        Date endTime = TextUtils.getLastDayOfMonth(month, year);
        Set<Long> siteIDs = new HashSet<>();

        siteIDs.add(siteID);
        List<Object[]> items = pqmDataImpl.getTB_PREV(siteIDs, startTime, endTime);
        List<PqmDataEntity> list = new ArrayList<>();
        for (Object[] item : items) {

            String gender = item[0] == null ? "" : item[0].toString();
            String object = item[1] == null ? "" : item[1].toString();
            String ageGroup = item[2].toString();
            Long quantity = item[3] == null ? Long.valueOf("0") : Long.valueOf(item[3].toString());
            if (quantity.equals(Long.valueOf("0"))) {
                continue;
            }

            PqmDataEntity model = new PqmDataEntity();

            model.setSiteID(siteID);
            model.setIndicatorCode(PqmIndicatorEnum.TB_PREV.getKey());
            model.setMonth(month);
            model.setYear(year);
            model.setSexID(gender.equals("1") ? "nam" : gender.equals("2") ? "nu" : "");
            model.setObjectGroupID(object);
            model.setAgeGroup(ageGroup);
            model.setMultiMonth(0);
            model.setStatus(3);
            model.setQuantity(quantity);

            list.add(model);
        }
        saveAlls(list);
    }

    public void getPrEP_New(int month, int year, Long siteID) {

        Date startTime = TextUtils.getFirstDayOfMonth(month, year);
        Date endTime = TextUtils.getLastDayOfMonth(month, year);
        Set<Long> siteIDs = new HashSet<>();

        siteIDs.add(siteID);
        List<Object[]> items = pqmDataImpl.getPrEP_New(siteIDs, startTime, endTime);
        List<PqmDataEntity> list = new ArrayList<>();
        for (Object[] item : items) {

            String gender = item[0] == null ? "" : item[0].toString();
            String object = item[1] == null ? "" : item[1].toString();
            String ageGroup = item[2].toString();
            Long quantity = item[3] == null ? Long.valueOf("0") : Long.valueOf(item[3].toString());
            if (quantity.equals(Long.valueOf("0"))) {
                continue;
            }

            PqmDataEntity model = new PqmDataEntity();

            model.setSiteID(siteID);
            model.setIndicatorCode(PqmIndicatorEnum.PrEP_New.getKey());
            model.setMonth(month);
            model.setYear(year);
            model.setSexID(gender.equals("1") ? "nam" : gender.equals("2") ? "nu" : "");
            model.setObjectGroupID(object);
            model.setAgeGroup(ageGroup);
            model.setMultiMonth(0);
            model.setStatus(3);
            model.setQuantity(quantity);

            list.add(model);
        }
        saveAlls(list);
    }

    public void getPrEP_CURR(int month, int year, Long siteID) {

        Date startTime = TextUtils.getFirstDayOfMonth(month, year);
        Date endTime = TextUtils.getLastDayOfMonth(month, year);
        Set<Long> siteIDs = new HashSet<>();

        siteIDs.add(siteID);
        List<Object[]> items = pqmDataImpl.getPrEP_CURR(siteIDs, startTime, endTime);
        List<PqmDataEntity> list = new ArrayList<>();
        for (Object[] item : items) {

            String gender = item[0] == null ? "" : item[0].toString();
            String object = item[1] == null ? "" : item[1].toString();
            String ageGroup = item[2].toString();
            Long quantity = item[3] == null ? Long.valueOf("0") : Long.valueOf(item[3].toString());
            if (quantity.equals(Long.valueOf("0"))) {
                continue;
            }

            PqmDataEntity model = new PqmDataEntity();

            model.setSiteID(siteID);
            model.setIndicatorCode(PqmIndicatorEnum.PrEP_CURR.getKey());
            model.setMonth(month);
            model.setYear(year);
            model.setSexID(gender.equals("1") ? "nam" : gender.equals("2") ? "nu" : "");
            model.setObjectGroupID(object);
            model.setAgeGroup(ageGroup);
            model.setMultiMonth(0);
            model.setStatus(3);
            model.setQuantity(quantity);

            list.add(model);
        }
        saveAlls(list);
    }

    public void getPrEP_3M(int month, int year, Long siteID) {

        Date startTime = TextUtils.getFirstDayOfMonth(month, year);
        Date endTime = TextUtils.getLastDayOfMonth(month, year);
        Set<Long> siteIDs = new HashSet<>();

        siteIDs.add(siteID);
        List<Object[]> items = pqmDataImpl.getPrEP_3M(siteIDs, startTime, endTime);
        List<PqmDataEntity> list = new ArrayList<>();
        for (Object[] item : items) {

            String gender = item[0] == null ? "" : item[0].toString();
            String object = item[1] == null ? "" : item[1].toString();
            String ageGroup = item[2].toString();
            Long quantity = item[3] == null ? Long.valueOf("0") : Long.valueOf(item[3].toString());
            if (quantity.equals(Long.valueOf("0"))) {
                continue;
            }

            PqmDataEntity model = new PqmDataEntity();

            model.setSiteID(siteID);
            model.setIndicatorCode(PqmIndicatorEnum.PrEP_3M.getKey());
            model.setMonth(month);
            model.setYear(year);
            model.setSexID(gender.equals("1") ? "nam" : gender.equals("2") ? "nu" : "");
            model.setObjectGroupID(object);
            model.setAgeGroup(ageGroup);
            model.setMultiMonth(0);
            model.setStatus(3);
            model.setQuantity(quantity);

            list.add(model);
        }
        saveAlls(list);
    }

    public void getPrEP_Over_90(int month, int year, Long siteID) {

        Date startTime = TextUtils.getFirstDayOfMonth(month, year);
        Date endTime = TextUtils.getLastDayOfMonth(month, year);
        Set<Long> siteIDs = new HashSet<>();

        siteIDs.add(siteID);
        List<Object[]> items = pqmDataImpl.getPrEP_Over_90(siteIDs, startTime, endTime);
        List<PqmDataEntity> list = new ArrayList<>();
        for (Object[] item : items) {

            String gender = item[0] == null ? "" : item[0].toString();
            String object = item[1] == null ? "" : item[1].toString();
            String ageGroup = item[2].toString();
            Long quantity = item[3] == null ? Long.valueOf("0") : Long.valueOf(item[3].toString());
            if (quantity.equals(Long.valueOf("0"))) {
                continue;
            }

            PqmDataEntity model = new PqmDataEntity();

            model.setSiteID(siteID);
            model.setIndicatorCode(PqmIndicatorEnum.PrEP_Over_90.getKey());
            model.setMonth(month);
            model.setYear(year);
            model.setSexID(gender.equals("1") ? "nam" : gender.equals("2") ? "nu" : "");
            model.setObjectGroupID(object);
            model.setAgeGroup(ageGroup);
            model.setMultiMonth(0);
            model.setStatus(3);
            model.setQuantity(quantity);

            list.add(model);
        }
        saveAlls(list);
    }

//resst banwgf cachs xoa
    public void resetDataSites(List<Long> siteID, int month, int year) {
        pqmRepository.deleteBySiteIDAndMonthAndYear(siteID, month, year);
    }
    public void resetDataBySites(List<Long> siteID ) {
        pqmRepository.deleteBySites(siteID );
    }
    public void deleteByProvinceID(String siteID ) {
        pqmRepository.deleteByProvinceID(siteID );
    }

    public void resetDataSiteConfirm(Long siteID, int month, int year) {
        pqmRepository.deleteBySiteIDAndMonthAndYearConfirmSite(siteID, month, year);
    }

    public void resetDataSite(Long siteID, int month, int year) {
        List<PqmDataEntity> list = pqmRepository.findByIndicator(siteID, month, year);
        for (PqmDataEntity pqmDataEntity : list) {
            pqmDataEntity.setStatus(3);
            pqmDataEntity.setQuantity(Long.valueOf("0"));
            save(pqmDataEntity);
        }
    }
}
