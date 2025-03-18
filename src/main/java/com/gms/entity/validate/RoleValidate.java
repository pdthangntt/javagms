package com.gms.entity.validate;

import com.gms.entity.db.AuthRoleEntity;
import com.gms.entity.form.RoleForm;
import com.gms.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 *
 * @author vvthanh
 */
@Component
public class RoleValidate implements Validator {

    @Autowired
    private AuthService authService;

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(RoleForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        RoleForm form = (RoleForm) o;

        if (form.getName() != null && !"".equals(form.getName())) {
            AuthRoleEntity roleEntity = authService.findRoleByName(form.getName());
            if (form.getID() == null && roleEntity != null) {
                errors.rejectValue("name", "name.error.message", "Tên quyền đã được sử dụng.");
            } else if (form.getID() != null && roleEntity != null && !form.getID().equals(roleEntity.getID())) {
                errors.rejectValue("name", "name.error.message", "Tên quyền đã được sử dụng.");
            }
        }
    }

}
