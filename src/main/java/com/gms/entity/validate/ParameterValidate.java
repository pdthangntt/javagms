package com.gms.entity.validate;

import com.gms.components.TextUtils;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.form.ParameterForm;
import com.gms.entity.form.StaffForm;
import com.gms.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author vvthanh
 */
@Component
public class ParameterValidate implements Validator {

    @Autowired
    private ParameterService parameterService;

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(ParameterForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ParameterForm form = (ParameterForm) o;

        if (form.getCode() != null && !form.getCode().isEmpty()) {
            String code = TextUtils.removemarks(form.getCode());

            ParameterEntity entity = parameterService.findOne(form.getType(), code);
            if (form.getID() == null && entity != null) {
                errors.rejectValue("code", "code.error.message", "Giá trị tham số đã được sử dụng.");
            } else if (form.getID() != null && entity != null && !form.getID().equals(entity.getID())) {
                errors.rejectValue("code", "code.error.message", "Giá trị tham số đã được sử dụng.");
            }
        }

        if (form.getParentID() > 0 && form.getParentID() == form.getID()) {
            errors.rejectValue("parentID", "code.error.message", "Cấp cha không chính xác.");
        }

    }

}
