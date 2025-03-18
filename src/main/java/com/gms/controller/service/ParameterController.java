package com.gms.controller.service;

import com.gms.entity.bean.Response;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.ParameterDefineForm;
import com.gms.service.ParameterService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ParameterController extends BaseController {

    @Autowired
    private ParameterService parameterService;

    /**
     * Lưu tham số khi định nghĩa
     *
     * @param form
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/parameter/save-fast.json", method = RequestMethod.POST)
    public Response<?> actionSaveFast(@Valid @RequestBody ParameterDefineForm form,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new Response<>(false, bindingResult.getAllErrors());
        }

        String code = String.format("%s_%s", form.getType(), form.getKey());
        ParameterEntity parameterEntity = parameterService.findOne(ParameterEntity.SYSTEMS_PARAMETER, code);
        if (parameterEntity == null) {
            parameterEntity = new ParameterEntity();
            parameterEntity.setType(ParameterEntity.SYSTEMS_PARAMETER);
            parameterEntity.setCode(code);
        }
        parameterEntity.setValue(form.getValue());
        ParameterEntity entity = parameterService.save(parameterEntity);
        if (entity == null) {
            return new Response<>(false, "Không cập nhật được thông tin! Vui lòng thử lại sau.");
        }
        return new Response<>(true, "Cập nhật thông tin thành công.");
    }
}
