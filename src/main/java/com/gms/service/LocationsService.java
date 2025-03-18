package com.gms.service;

import com.gms.components.annotation.interf.FieldValueExists;
import com.gms.entity.db.DistrictEntity;
import com.gms.entity.db.ProvinceEntity;
import com.gms.entity.db.WardEntity;
import com.gms.entity.json.Locations;
import com.gms.repository.DistrictRepository;
import com.gms.repository.ProvinceRepository;
import com.gms.repository.WardRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 *
 * @author vvthanh
 */
@Service
public class LocationsService extends BaseService implements FieldValueExists {

    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private WardRepository wardRepository;

    /**
     * Lấy tất cả danh sách tỉnh/thành, quận/huyện, xã/phường
     *
     * @auth vvThành
     * @return
     */
    public Locations findAll() {
        Locations data = new Locations();
        data.setProvince(provinceRepository.findAll(Sort.by(Sort.Direction.ASC, new String[]{"position"})));
        data.setDistrict(districtRepository.findAll(Sort.by(Sort.Direction.ASC, new String[]{"position"})));
//        data.setWard(wardRepository.findAll(Sort.by(Sort.Direction.ASC, new String[]{"position"})));
        return data;
    }

    /**
     * Lấy tất cả danh sách tỉnh/thành
     *
     * @auth NamAnh_HaUI
     * @return
     */
    public List<ProvinceEntity> findAllProvince() {
        return provinceRepository.findAll(Sort.by(Sort.Direction.ASC, new String[]{"position"}));
    }

    /**
     * Xóa 1 tỉnh/thành
     *
     * @param province
     * @auth NamAnh_HaUI
     */
    public void deleteProvince(ProvinceEntity province) {
        provinceRepository.delete(province);
    }

    /**
     * Thêm 1 tỉnh/thành
     *
     * @param provinceEntity
     * @auth NamAnh_HaUI
     * @return
     */
    public ProvinceEntity addProvince(ProvinceEntity provinceEntity) {
        return provinceRepository.save(provinceEntity);
    }

    /**
     * Validate FK
     *
     * @auth vvThanh
     * @param value
     * @param fieldName
     * @return
     * @throws UnsupportedOperationException
     */
    @Override
    public boolean fieldValueExists(Object value, String fieldName) throws UnsupportedOperationException {
        if (value != null && !"".equals(value.toString())
                && (fieldName.equals("provinceID") || fieldName.equals("permanentProvinceID")
                || fieldName.equals("currentProvinceID") || fieldName.equals("therapyRegistProvinceID")
                || fieldName.equals("exchangeProvinceID"))) {
            return provinceRepository.existsByID(value.toString());
        }
        if (value != null && !"".equals(value.toString())
                && (fieldName.equals("districtID") || fieldName.equals("permanentDistrictID")
                || fieldName.equals("currentDistrictID") || fieldName.equals("exchangeDistrictID")
                || fieldName.equals("therapyRegistDistrictID"))) {
            return districtRepository.existsByID(value.toString());
        }
        if (value != null && !"".equals(value.toString())
                && (fieldName.equals("wardID") || fieldName.equals("currentWardID") || fieldName.equals("permanentWardID"))) {
            return wardRepository.existsByID(value.toString());
        }
        return true;
    }

    /**
     * @auth vvThành
     * @param provinceID
     * @return
     */
    public ProvinceEntity findProvince(String provinceID) {
        Optional<ProvinceEntity> optional = provinceRepository.findById(provinceID);
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     * @auth vvThành
     * @param districtID
     * @return
     */
    public DistrictEntity findDistrict(String districtID) {
        Optional<DistrictEntity> optional = districtRepository.findById(districtID);
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     * @auth vvThành
     * @param wardID
     * @return
     */
    public WardEntity findWard(String wardID) {
        Optional<WardEntity> optional = wardRepository.findById(wardID);
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     * Danh sách xã phường theo quận huyện
     *
     * @auth vvThành
     * @param districtID
     * @return
     */
    public List<WardEntity> findWardByDistrictID(String districtID) {
        return wardRepository.findByDistrictID(districtID, Sort.by(Sort.Direction.ASC, new String[]{"position"}));
    }

    /**
     * Danh sách xã phường theo quận huyện
     *
     * @auth vvThành
     * @param districtID
     * @return
     */
    public List<WardEntity> findWardByDistrictIDAndIsActive(String districtID) {
        return wardRepository.findByDistrictIDAndIsActive(districtID, Sort.by(Sort.Direction.ASC, new String[]{"position"}));
    }

    /**
     * Danh sách huyện theo tỉnh
     *
     * @auth NamAnh_HaUI
     * @param provinceID
     * @return
     */
    public List<DistrictEntity> findDistrictByProvinceID(String provinceID) {
        List<DistrictEntity> entities = districtRepository.findByProvinceID(provinceID, Sort.by(Sort.Direction.ASC, new String[]{"position"}));
        return entities;
    }

    /**
     * Xác nhận xem quận huyện có tồn tại không
     *
     * @param provinceID
     * @param districtID
     * @return
     * @auth NamAnh_HaUI
     */
    public boolean findDistrict(String provinceID, String districtID) {
        DistrictEntity districtEntity = districtRepository.findDistrictByProvinceId(provinceID, districtID);
        if (districtEntity == null) {
            return true;
        }
        return false;
    }

    /**
     * Thêm huyện
     *
     * @auth NamAnh_HaUI
     * @param districtEntity
     */
    public void addDistrict(DistrictEntity districtEntity) {
        districtRepository.save(districtEntity);
    }

    /**
     * Xóa huyện
     *
     * @param districtID
     * @auth NamAnh_HaUI
     */
    public void deleteDistrict(String districtID) {
        districtRepository.deleteById(districtID);
    }

    /**
     * Thêm xã/phường
     *
     * @param wardEntity
     * @auth NamAnh_HaUI
     */
    public void addWard(WardEntity wardEntity) {
        wardRepository.save(wardEntity);
    }

    /**
     * Xác nhận xem xã phường có tồn tại không
     *
     * @param provinceID
     * @param districtID
     * @param wardID
     * @return
     * @auth NamAnh_HaUI
     */
    public boolean findWard(String provinceID, String districtID, String wardID) {
        WardEntity wardEntity = wardRepository.findWardByProvinceIdAndDistrictId(provinceID, districtID, wardID);
        if (wardEntity == null) {
            return true;
        }
        return false;
    }

    /**
     * Xóa xã/phường
     *
     * @param wardID
     * @auth NamAnh_HaUI
     */
    public void deleteWard(String wardID) {
        wardRepository.deleteById(wardID);
    }

    /**
     * Lưu tỉnh thành theo danh sách
     *
     * @auth vvThành
     * @param entitys
     */
    public void saveProvince(List<ProvinceEntity> entitys) {
        provinceRepository.saveAll(entitys);
    }

    /**
     * @auth vvThành
     * @param entitys
     */
    public void saveDistrict(List<DistrictEntity> entitys) {
        districtRepository.saveAll(entitys);
    }

    /**
     * @auth vvThành
     * @param entitys
     */
    public void saveWard(List<WardEntity> entitys) {
        wardRepository.saveAll(entitys);
    }

    /**
     * @auth vvThành
     * @return
     */
    public List<DistrictEntity> findAllDistrict() {
        return districtRepository.findAll(Sort.by(Sort.Direction.ASC, new String[]{"position"}));
    }

    /**
     * @auth vvThành
     * @return
     */
    public List<WardEntity> findAllWard() {
        return wardRepository.findAll();
    }

    /**
     * @param districtID
     * @auth vvThanh
     * @return
     */
    public List<WardEntity> findByDistrictID(Set<String> districtID) {
        return wardRepository.findByDistrictID(districtID);
    }

    /**
     * @return @auth DSNAnh
     * @param provinceIDs
     */
    public List<ProvinceEntity> findProvinceByIDs(Set<String> provinceIDs) {
        return provinceRepository.findByIDs(provinceIDs);
    }
    
    /**
     * @return @auth DSNAnh
     * @param districtIDs
     */
    public List<DistrictEntity> findDistrictByIDs(Set<String> districtIDs) {
        return districtRepository.findByIDs(districtIDs);
    }

    /**
     * @auth vvthanh
     * @param wID
     * @return
     */
    public List<WardEntity> findWardByIDs(Set<String> wID) {
        return wardRepository.findByIDs(wID);
    }
}
