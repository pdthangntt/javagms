package com.gms.entity.validate;

import com.gms.entity.form.DistrictForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author NamAnh_HaUI
 */
@Component
public class DistrictValidate implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(DistrictForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        DistrictForm form = (DistrictForm) o;
        if (!form.getType().isEmpty()) {
            if (form.getType().equals("Quận")
                    || form.getType().equals("Huyện")
                    || form.getType().equals("Thị xã")) {

            } else {
                errors.rejectValue("type", "type.error.message", "Loại quận huyện không đúng định dạng");
            }
        } else {
            errors.rejectValue("type", "type.error.message", "Loại quận huyện không được để trống");
        }
    }

}
