package com.gms.entity.validate;

import com.gms.entity.db.SiteEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.form.SiteForm;
import com.gms.entity.form.StaffForm;
import com.gms.service.SiteService;
import com.gms.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author vvthanh
 */
@Component
public class StaffValidate implements Validator {

    @Autowired
    private StaffService staffService;

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(StaffForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        StaffForm form = (StaffForm) o;

        if (form.getUsername() != null && !form.getUsername().isEmpty()) {
            StaffEntity entity = staffService.findByUsername(form.getUsername());
            if (form.getID() == null && entity != null) {
                errors.rejectValue("username", "username.error.message", "Tên đăng nhập đã được sử dụng.");
            } else if (form.getID() != null && entity != null && !form.getID().equals(entity.getID())) {
                errors.rejectValue("username", "username.error.message", "Tên đăng nhập đã được sử dụng.");
            }
        }

        if (form.getPwd() != null && !"".equals(form.getPwd())
                && form.getConfirmPwd() != null && !"".equals(form.getConfirmPwd())
                && !form.getPwd().equals(form.getConfirmPwd())) {
            errors.rejectValue("confirmPwd", "confirmPwd.error.message", "Mật khẩu nhập lại không chính xác.");
        }
    }

}
