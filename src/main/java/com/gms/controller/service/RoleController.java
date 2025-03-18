package com.gms.controller.service;

import com.gms.entity.bean.Response;
import com.gms.entity.db.AuthActionEntity;
import com.gms.entity.form.ActionForm;
import com.gms.service.AuthService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController extends BaseController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "/role/save-action-define.json", method = RequestMethod.POST)
    public Response<?> actionSaveActionDefine(@Valid @RequestBody ActionForm form,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new Response<>(false, bindingResult.getAllErrors());
        }

        AuthActionEntity roleEntity = new AuthActionEntity();
        roleEntity.setName(form.getName());
        roleEntity.setTitle(form.getTitle());
        roleEntity = authService.saveAction(roleEntity);

        if (roleEntity == null) {
            return new Response<>(false, "Không cập nhật được thông tin! Vui lòng thử lại sau.");
        }
        return new Response<>(true, "Cập nhật thông tin thành công.");
    }
}
