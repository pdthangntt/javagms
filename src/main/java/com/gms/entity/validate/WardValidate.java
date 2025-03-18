
package com.gms.entity.validate;

import com.gms.entity.form.WardForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author NamAnh_HaUI
 */
@Component
public class WardValidate implements Validator {

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(WardForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        WardForm form = (WardForm) o;
        if (!form.getType().isEmpty()) {
            if (form.getType().equals("Phường") || form.getType().equals("Xã")) {

            } else {
                errors.rejectValue("type", "type.error.message", "Loại phường xã không đúng định dạng");
            }
        } else {
            errors.rejectValue("type", "type.error.message", "Loại phương xã không được để trống");
        }
    }

}
