package com.gms.entity.validate;

import com.gms.entity.form.ProvinceForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author NamAnh_HaUI
 */
@Component
public class ProvinceValidate implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(ProvinceForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ProvinceForm form = (ProvinceForm) o;
        if (!form.getType().isEmpty()) {
            if (form.getType().equals("Tỉnh") || form.getType().equals("Thành phố trung ương")) {

            } else {
                errors.rejectValue("type", "type.error.message", "Loại tỉnh thành không đúng định dạng");
            }
        } else {
            errors.rejectValue("type", "type.error.message", "Loại tỉnh thành không được để trống");
        }
    }
}
