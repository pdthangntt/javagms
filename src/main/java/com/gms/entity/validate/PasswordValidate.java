package com.gms.entity.validate;

import com.gms.entity.form.PasswordForm;
import com.gms.entity.form.ProfileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author TrangBN
 */

@Component
public class PasswordValidate implements Validator {
    
    

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(ProfileForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PasswordForm form = (PasswordForm) o;
        
        if (form.getPassword()!= null && !"".equals(form.getPassword())
                && form.getConfirmPwd() != null && !"".equals(form.getConfirmPwd())
                && !form.getPassword().equals(form.getConfirmPwd())) {
            errors.rejectValue("confirmPwd", "confirmPwd.error.message", "Mật khẩu nhập lại không chính xác.");
        }
    }
    
}
