package com.gms.service;

import com.gms.components.ClassUtils;
import com.gms.components.TextUtils;
import com.gms.components.annotation.interf.FieldValueExists;
import com.gms.entity.constant.ServiceEnum;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.SitePathEntity;
import com.gms.entity.db.SiteServiceEntity;
import com.gms.entity.db.SiteVnptFkEntity;
import com.gms.entity.db.WardEntity;
import com.gms.repository.DistrictRepository;
import com.gms.repository.ParameterRepository;
import com.gms.repository.ProvinceRepository;
import com.gms.repository.SitePathRepository;
import com.gms.repository.SiteRepository;
import com.gms.repository.SiteServiceRepository;
import com.gms.repository.SiteVnptFkRepository;
import com.gms.repository.WardRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SiteService extends BaseService implements IBaseService<SiteEntity, Long>, FieldValueExists {

    @Autowired
    private SiteVnptFkRepository vnptFkRepository;
    @Autowired
    private SitePathRepository sitePathRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private SiteServiceRepository siteServiceRepository;
    @Autowired
    private ParameterRepository parameterRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private WardRepository wardRepository;

    @Override
    public List<SiteEntity> findAll() {
        return siteRepository.findAll(Sort.by(Sort.Direction.ASC, new String[]{"ID", "name"}));
    }

    @Override
    public SiteEntity findOne(Long ID) {
        Optional<SiteEntity> optional = siteRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     *
     * @auth vvThành
     * @param ID
     * @return
     */
    public SiteEntity findOne(Integer ID) {
        return findOne(Long.parseLong(ID.toString()));
    }

    /**
     * @auth vvThành
     * @param ID
     * @param isPath
     * @return
     */
    public SiteEntity findOne(Long ID, boolean isPath) {
        return findOne(ID, isPath, false);
    }

    /**
     * Lấy thêm cấp cha
     *
     * @param isService
     * @auth vvThanh
     * @param ID
     * @param isPath
     * @return
     */
    public SiteEntity findOne(Long ID, boolean isPath, boolean isService) {
        SiteEntity model = findOne(ID);
        if (model != null && isPath) {
            model.setPath(new ArrayList<SiteEntity>());
            try {
                model.setPathID(sitePathRepository.findBySiteID(model.getID()));
                if (model.getPathID() == null) {
                    model.setPathID(new ArrayList<Long>());
                }

                for (Long siteID : model.getPathID()) {
                    for (SiteEntity siteEntity : siteRepository.findByIDs(new HashSet<>(model.getPathID()))) {
                        if (siteEntity.getID().equals(siteID)) {
                            model.getPath().add(siteEntity);
                        }
                    }
                }
                Collections.sort(model.getPath(), new Comparator<SiteEntity>() {
                    @Override
                    public int compare(SiteEntity o1, SiteEntity o2) {
                        return o1.getID().compareTo(o2.getID());
                    }
                });
            } catch (Exception e) {
                getLogger().error(e.getMessage());
            }
        }

        if (model != null && isService) {
            List<SiteServiceEntity> siteServiceEntitys = siteServiceRepository.findBySiteID(model.getID());
            HashSet<String> serviceIds = new HashSet<>();
            for (SiteServiceEntity siteServiceEntity : siteServiceEntitys) {
                serviceIds.add(siteServiceEntity.getServiceID());
            }
            if (!serviceIds.isEmpty()) {
                model.setServiceIDs(new ArrayList<>(serviceIds));
                model.setServices(parameterRepository.findByTypeAndCode(ParameterEntity.SERVICE, serviceIds));
            }
        }
        return model;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SiteEntity save(SiteEntity model) {
        try {
            Date currentDate = new Date();
            List<ParameterEntity> services = model.getServices();
            if (model.getID() == null || model.getID() == 0) {
                model.setCreateAT(currentDate);
                model.setCreatedBY(getCurrentUserID());
            }
            model.setCode(TextUtils.removemarks(model.getCode()));
            model.setUpdateAt(currentDate);
            model.setUpdatedBY(getCurrentUserID());
            model = siteRepository.save(model);

            sitePathRepository.removeBySiteID(model.getID());
            List<SitePathEntity> sitePaths = new ArrayList<>();
            SiteEntity site = model.clone();
            SitePathEntity sitePath = new SitePathEntity();
            sitePath.setSiteID(model.getID());
            sitePath.setAncestorId(site.getID());
            sitePath.setCreateAT(currentDate);
            sitePaths.add(sitePath);

            SiteEntity parent = null;
            while (!site.getParentID().equals(0)) {
                parent = findOne(site.getParentID());
                if (parent == null) {
                    break;
                }
                site = parent.clone();
                sitePath = new SitePathEntity();
                sitePath.setSiteID(model.getID());
                sitePath.setAncestorId(site.getID());
                sitePath.setCreateAT(currentDate);
                sitePaths.add(sitePath);
            }
            sitePathRepository.saveAll(sitePaths);

            siteServiceRepository.removeBySiteID(model.getID());
            if (services != null && !services.isEmpty()) {
                List<SiteServiceEntity> serviceEntitys = new ArrayList<>();
                SiteServiceEntity serviceEntity = null;
                for (ParameterEntity service : services) {
                    serviceEntity = new SiteServiceEntity();
                    serviceEntity.setCreateAT(currentDate);
                    serviceEntity.setSiteID(model.getID());
                    serviceEntity.setServiceID(service.getCode());
                    serviceEntitys.add(serviceEntity);
                }
                siteServiceRepository.saveAll(serviceEntitys);
            }
            return model;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }
    }

    /**
     * Lấy tất cả cấp con
     *
     * @auth vvThành
     * @param parentID
     * @return
     */
    public List<SiteEntity> getChild(Long parentID) {
        return siteRepository.findByparent(parentID);
    }

    /**
     * @auth vvThành
     * @param code
     * @return
     */
    public boolean exitsByCode(String code) {
        return code == null || "".equals(code) ? false : siteRepository.existsByCode(TextUtils.removemarks(code));
    }

    /**
     * @auth vvThành
     * @param code
     * @param ID
     * @return
     */
    public boolean exitsByCode(String code, Long ID) {
        return code == null || "".equals(code) ? false : siteRepository.existsByCode(TextUtils.removemarks(code), ID);
    }

    /**
     * @auth vvThành
     * @param code
     * @return
     */
    public SiteEntity findByCode(String code) {
        return siteRepository.findByCode(TextUtils.removemarks(code));
    }

    public SiteEntity findByElogSiteCode(String code) {
        return siteRepository.findByElogSiteCode(TextUtils.removemarks(code));
    }

    public SiteEntity findByArvSiteCode(String code) {
        return siteRepository.findByArvSiteCode(TextUtils.removemarks(code));
    }

    public SiteEntity findByHmedSiteCode(String code) {
        return siteRepository.findByHmedSiteCode(TextUtils.removemarks(code));
    }

    public SiteEntity findByPrepSiteCode(String code) {
        return siteRepository.findByPrepSiteCode(TextUtils.removemarks(code));
    }

    /**
     * Validate FK
     *
     * @auth vvThành
     * @param value
     * @param fieldName
     * @return
     * @throws UnsupportedOperationException
     */
    @Override
    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {
        if (fieldName.equals("parentID") && value != null && !"".equals(value.toString()) && !value.toString().equals("0")) {
            return siteRepository.existsByID(Long.parseLong(String.valueOf(value)));
        }

        if (fieldName.equals("type") && value != null && !"".equals(value.toString()) && !value.toString().equals("0")) {
            return SiteEntity.typeLabel.containsKey(Integer.parseInt(value.toString()));
        }
        return true;
    }

    /**
     * Danh sách ID các cấp thuộc quản lý của site theo ID
     *
     * @auth vvThành
     * @param siteID
     * @return
     */
    public List<Long> getLeafID(Long siteID) {
        return sitePathRepository.findBySiteID(siteID);
    }

    /**
     * Toàn bộ ID có liên quan đến siteID
     *
     * @param siteID
     * @return
     */
    public List<Long> getProgenyID(Long siteID) {
        return sitePathRepository.findProgenyID(siteID);
    }

    /**
     * Tìm cơ sở theo service và province
     *
     * @param serviceID
     * @param provinceID
     * @return
     */
    public List<SiteEntity> findByServiceAndProvince(String serviceID, String provinceID) {
        Set<String> services = new HashSet();
        services.add(serviceID);
        Set<String> provinces = new HashSet();
        provinces.add(provinceID);
        return siteRepository.findByServiceAndProvince(services, provinces);
    }

    /**
     * Tìm cơ sở theo service và province
     *
     * @param serviceID
     * @param provinceID
     * @return
     */
    public List<SiteEntity> findByServiceAndProvince(Set<String> serviceID, Set<String> provinceID) {
        return siteRepository.findByServiceAndProvince(serviceID, provinceID);
    }

    /**
     *
     * @param services
     * @param ids
     * @return
     */
    public List<SiteEntity> findByServiceAndSiteID(Set<String> services, Set<Long> ids) {
        return siteRepository.findByServiceAndSiteID(services, ids);
    }

    /**
     * @return @auth vvThanh
     * @param siteIDs
     */
    public List<SiteEntity> findByIDs(Set<Long> siteIDs) {
        return siteRepository.findByIDs(siteIDs);
    }

    /**
     * Xoá cơ sở
     *
     * @auth vvThành
     * @param entity
     */
    @Transactional(rollbackFor = Exception.class)
    public void remove(SiteEntity entity) {
        try {
            sitePathRepository.removeBySiteID(entity.getID());
            siteServiceRepository.removeBySiteID(entity.getID());
            siteRepository.delete(entity);
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    /**
     * Tìm kiếm tất cả cơ sở cấp con theo LIKE tên
     *
     * @auth vvThành
     * @param siteID
     * @param k
     * @param pageable
     * @return
     */
    public List<SiteEntity> findChildByName(String k, Long siteID, Pageable pageable) {
        return siteRepository.findChildByName(k, siteID, pageable);
    }

    public List<SiteEntity> findPath(List<SiteEntity> sites) {
        if (sites != null && sites.isEmpty()) {
            return null;
        }
        List<ParameterEntity> services = parameterRepository.findByType(ParameterEntity.SERVICE);
        Map<String, ParameterEntity> serviceOptions = new HashMap<>();
        for (ParameterEntity service : services) {
            serviceOptions.put(service.getCode(), service);
        }

        Set<Long> sIDs = new HashSet<>();
        sIDs.addAll(ClassUtils.getAttribute(sites, "getID"));
        List<SiteServiceEntity> siteServiceEntitys = siteServiceRepository.findBySiteID(sIDs);

        for (SiteEntity site : sites) {
            site.setServiceIDs(new ArrayList<String>());
            site.setServices(new ArrayList<ParameterEntity>());
            for (SiteServiceEntity service : siteServiceEntitys) {
                if (service.getSiteID().equals(site.getID())) {
                    site.getServiceIDs().add(service.getServiceID());
                    site.getServices().add(serviceOptions.getOrDefault(service.getServiceID(), null));
                }
            }
        }
        return sites;
    }

    /**
     * Danh sách cở sở XN khẳng định
     *
     * @auth vvThành
     * @param serviceID
     * @return
     */
    public List<SiteEntity> findByService(String serviceID) {
        return siteRepository.findByService(serviceID);
    }

    /**
     * Danh sách cơ sở xét nghiệm theo tỉnh
     *
     * @auth vvThành
     * @param provinceID
     * @return
     */
    public List<SiteEntity> getSiteConfirm(String provinceID) {
        return findByServiceAndProvince(ServiceEnum.HTC_CONFIRM.getKey(), provinceID);
    }

    /**
     * Danh sách tất cả cơ sở xét nghiệm khẳng định
     *
     * @auth vvThành
     * @return
     */
    public List<SiteEntity> getSiteConfirm() {
        return findByService(ServiceEnum.HTC_CONFIRM.getKey());
    }

    /**
     * Danh sách tất cả cơ sở xét nghiệm sàng lọc
     *
     * @auth vvThành
     * @return
     */
    public List<SiteEntity> getSiteHtc() {
        return findByService(ServiceEnum.HTC.getKey());
    }

    /**
     * Cơ sở xét nghiệm theo tỉnh
     *
     * @auth vvThành
     * @param provinceID
     * @return
     */
    public List<SiteEntity> getSiteHtc(String provinceID) {
        return findByServiceAndProvince(ServiceEnum.HTC.getKey(), provinceID);
    }

    /**
     * Cơ sở điều trị theo tỉnh
     *
     * @param provinceID
     * @return
     */
    public List<SiteEntity> getSiteOpc(String provinceID) {
        return findByServiceAndProvince(ServiceEnum.OPC.getKey(), provinceID);
    }

    public List<SiteEntity> getSitePrEP(String provinceID) {
        return findByServiceAndProvince(ServiceEnum.PREP.getKey(), provinceID);
    }

    /**
     * @auth vvThành
     * @param siteParentID
     * @param provinceID
     * @param districtID
     * @param wardID
     * @return
     */
    public List<SiteEntity> findByLocation(Long siteParentID, String provinceID, String districtID, String wardID) {
        Set<Long> siteID = new HashSet<>(getLeafID(siteParentID));
        return siteRepository.findByLocation(siteID, provinceID, districtID, wardID);
    }

    /**
     * @author DSNAnh
     * @param provinceID
     * @return
     */
    public List<SiteEntity> findByProvinceID(String provinceID) {
        return siteRepository.findByProvinceID(provinceID);
    }

    /**
     * TTYT
     *
     * @param provinceID
     * @param districtID
     * @return
     */
    public SiteEntity findByTTYT(String provinceID, String districtID) {
        List<SiteEntity> sites = siteRepository.findByProvinceIDAndDistrictID(provinceID, districtID);
        if (sites != null) {
            for (SiteEntity site : sites) {
                site = findOne(site.getID(), false, true);
                if (site.getServiceIDs() != null && !site.getServiceIDs().isEmpty() && site.getServiceIDs().contains(ServiceEnum.TTYT.getKey())) {
                    return site;
                }
            }
        }
        return null;
    }

    /**
     * PAC/CDC tỉnh
     *
     * @param provinceID
     * @return
     */
    public SiteEntity findByPAC(String provinceID) {
        List<SiteEntity> sites = siteRepository.findByProvinceID(provinceID);
        if (sites != null) {
            for (SiteEntity site : sites) {
                site = findOne(site.getID(), false, true);
                if (site.getServiceIDs() != null && !site.getServiceIDs().isEmpty() && site.getServiceIDs().contains(ServiceEnum.PAC.getKey())) {
                    return site;
                }
            }
        }
        return null;
    }

    /**
     * TYT
     *
     * @param provinceID
     * @param districtID
     * @param wardID
     * @return
     */
    public SiteEntity findByTYT(String provinceID, String districtID, String wardID) {
        List<SiteEntity> sites = siteRepository.findByProvinceIDAndDistrictIDAndWardID(provinceID, districtID, wardID);
        if (sites != null) {
            for (SiteEntity site : sites) {
                site = findOne(site.getID(), false, true);
                if (site.getServiceIDs() != null && !site.getServiceIDs().isEmpty() && site.getServiceIDs().contains(ServiceEnum.TYT.getKey())) {
                    return site;
                }
            }
        }
        return null;
    }

    /**
     * Lấy full địa chỉ cho cơ sở
     *
     * @auth TrangBN
     * @param site
     * @return
     */
    public String getAddress(SiteEntity site) {
        ProvinceEntity provinceEntity = new ProvinceEntity();
        DistrictEntity districtEntity = new DistrictEntity();
        WardEntity wardEntity = new WardEntity();

        Optional<ProvinceEntity> province = provinceRepository.findById(site.getProvinceID());
        Optional<DistrictEntity> district = districtRepository.findById(site.getDistrictID());
        Optional<WardEntity> ward = wardRepository.findById(site.getWardID());
        if (province.isPresent()) {
            provinceEntity = province.get();
        }
        if (district.isPresent()) {
            districtEntity = district.get();
        }

        if (ward.isPresent()) {
            wardEntity = ward.get();
        }

        return buildAddress(site.getAddress(), provinceEntity, districtEntity, wardEntity);
    }

    /**
     *
     * @auth vvThành
     * @param vnptSiteID
     * @return
     */
    public SiteVnptFkEntity findByVnptSiteID(String vnptSiteID) {
        return vnptFkRepository.findByVnptSiteID(vnptSiteID);
    }
}
