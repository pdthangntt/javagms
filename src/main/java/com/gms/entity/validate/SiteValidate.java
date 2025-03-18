package com.gms.entity.validate;

import com.gms.entity.db.SiteEntity;
import com.gms.entity.form.SiteForm;
import com.gms.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author vvthanh
 */
@Component
public class SiteValidate implements Validator {

    @Autowired
    private SiteService siteService;

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(SiteForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        SiteForm form = (SiteForm) o;

        if (form.getCode() != null && !form.getCode().isEmpty()) {
            if (form.getID() != null) {
                SiteEntity siteCode = siteService.findByCode(form.getCode());
                if (siteCode != null && !siteCode.getID().equals(form.getID())) {
                    errors.rejectValue("code", "code.error.message", "Mã cơ sở đã được sử dụng");
                }
            } else if (siteService.exitsByCode(form.getCode())) {
                errors.rejectValue("code", "code.error.message", "Mã cơ sở đã được sử dụng");
            }
        }

        if (form.getParentID() != null && form.getID() != null
                && !form.getParentID().equals(0)
                && !form.getID().equals(0)
                && form.getParentID().equals(form.getID())) {
            errors.rejectValue("parentID", "parent.error.message", String.format("Cơ sở cấp quản lý không chính xác", SiteEntity.typeLabel.get(form.getType())));
        }

        if (form.getParentID() != null && !form.getParentID().equals(0)) {
            SiteEntity parent = siteService.findOne(form.getParentID());
            if (parent != null) {
                if (form.getType() == SiteEntity.TYPE_DISTRICT && form.getProvinceID() != null && !form.getProvinceID().equals(parent.getProvinceID())) {
                    errors.rejectValue("provinceID", "province.error.message",
                            String.format("%s phải cùng tỉnh thành với cơ sở quản lý trực tiếp", SiteEntity.typeLabel.get(form.getType())));
                }

                if (form.getType() == SiteEntity.TYPE_WARD) {
                    if (form.getProvinceID() != null && !form.getProvinceID().equals(parent.getProvinceID())) {
                        errors.rejectValue("provinceID", "province.error.message",
                                String.format("Cở sở cấp %s phải có cùng tỉnh thành với cơ sở quản lý trực tiếp", SiteEntity.typeLabel.get(form.getType())));
                    }

                    if (form.getDistrictID() != null && !form.getDistrictID().equals(parent.getDistrictID())) {
                        errors.rejectValue("districtID", "district.error.message",
                                String.format("Cở sở cấp %s phải có cùng quận huyện với cơ sở quản lý trực tiếp", SiteEntity.typeLabel.get(form.getType())));
                    }
                }
            }
        }
    }

}
