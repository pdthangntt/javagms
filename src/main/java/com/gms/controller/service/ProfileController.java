package com.gms.controller.service;

import com.gms.entity.bean.LoggedUser;
import com.gms.entity.bean.Response;
import com.gms.entity.constant.StaffConfigEnum;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.db.StaffEntity;
import com.gms.entity.form.ParameterDefineForm;
import com.gms.service.ParameterService;
import com.gms.service.SiteService;
import com.gms.service.StaffService;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController extends BaseController {

    @Autowired
    private StaffService staffService;
    @Autowired
    private ParameterService parameterService;

    /**
     * Lưu tham số khi định nghĩa
     *
     * @param form
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/profile/save-config.json", method = RequestMethod.POST)
    public Response<?> actionSaveConfig(@Valid @RequestBody ParameterDefineForm form,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new Response<>(false, bindingResult.getAllErrors());
        }
        form.setType(String.format("staff_%s", getCurrentUser().getUser().getID()));
        String code = form.getKey();
        ParameterEntity parameterEntity = parameterService.findOne(form.getType(), code);
        if (parameterEntity == null) {
            parameterEntity = new ParameterEntity();
            parameterEntity.setType(form.getType());
            parameterEntity.setCode(code);
        }
        if (code.equals(StaffConfigEnum.LAYTEST_STAFF_CODE.getKey())) {
            form.setValue(form.getValue().trim().toLowerCase());
            List<ParameterEntity> codes = parameterService.findStaffCode(form.getValue());
            for (ParameterEntity item : codes) {
                if (!item.getID().equals(parameterEntity.getID())) {
                    return new Response<>(false, String.format("Mã %s đã được sử dụng", item.getValue()));
                }
            }
        }

        parameterEntity.setValue(form.getValue());
        ParameterEntity entity = parameterService.save(parameterEntity);
        if (entity == null) {
            return new Response<>(false, "Không cập nhật được thông tin! Vui lòng thử lại sau.");
        }
        return new Response<>(true, "Cập nhật thông tin thành công.", entity);
    }

    @RequestMapping(value = "/profile/save-device.json", method = RequestMethod.GET)
    public Response<?> actionSaveDevice(@RequestParam(name = "device", required = false, defaultValue = "") String device) {
        StaffEntity staff = staffService.findOne(getCurrentUser().getUser().getID());
//        if (staff.getDevice() == null) {
        staff.setDevice(new ArrayList<String>());
//        }
        staff.getDevice().add(device);
        staffService.save(staff);
        //Update current user
        LoggedUser currentUser = getCurrentUser();
        currentUser.setUser(staff);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(currentUser, getCurrentUser().getPassword(), getCurrentUser().getAuthorities()));
        return new Response<>(true, "cập nhật thành công");
    }
}
