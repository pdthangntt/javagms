package com.gms.controller.backend;

import com.gms.components.UrlUtils;
import com.gms.entity.db.AuthActionEntity;
import com.gms.entity.db.AuthRoleActionEntity;
import com.gms.entity.db.AuthRoleEntity;
import com.gms.entity.db.AuthRoleServiceEntity;
import com.gms.entity.db.AuthServiceAssignmentEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.entity.form.RoleForm;
import com.gms.entity.form.ServiceForm;
import com.gms.entity.validate.RoleValidate;
import com.gms.service.AuthService;
import com.gms.service.ParameterService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author vvthanh
 */
@Controller(value = "backend_role")
public class RoleController extends BaseController {

    @Autowired
    private AuthService authService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private RoleValidate validate;

    @RequestMapping(value = {"/role/index.html"}, method = RequestMethod.GET)
    public String actionIndex(Model model) {
        List<ParameterEntity> services = parameterService.getServices();
        HashMap<String, String> serviceOptions = new HashMap<>();
        for (ParameterEntity service : services) {
            serviceOptions.put(service.getCode(), service.getValue());
        }

        model.addAttribute("title", "Quyền");
        model.addAttribute("parent_title", "Bảo mật");
        model.addAttribute("roles", authService.findAllRole());
        model.addAttribute("services", serviceOptions);
        return "backend/role/index";
    }

    private Map<String, List<Long>> getAssignmentServices() {
        Map<String, List<Long>> options = new HashMap<>();
        for (Map.Entry<String, List<AuthServiceAssignmentEntity>> entry : authService.findServices().entrySet()) {
            options.put(entry.getKey(), new ArrayList<Long>());
            for (AuthServiceAssignmentEntity item : entry.getValue()) {
                options.get(entry.getKey()).add(item.getActionID());
            }
        }
        return options;
    }

    @RequestMapping(value = {"/role/new.html"}, method = RequestMethod.GET)
    public String actionNew(Model model, RoleForm form) {
        model.addAttribute("title", "Thêm quyền mới");
        model.addAttribute("parent_title", "Bảo mật");
        model.addAttribute("form", form);
        model.addAttribute("roles", buildRoleTree(authService.findAllAction()));
        model.addAttribute("services", parameterService.getServices());
        model.addAttribute("assignmentServices", getAssignmentServices());
        return "backend/role/new";
    }

    @RequestMapping(value = {"/role/new.html"}, method = RequestMethod.POST)
    public String doActionNew(Model model,
            @ModelAttribute("form") @Valid RoleForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        List<AuthActionEntity> actionEntitys = authService.findAllAction();

        model.addAttribute("title", "Thêm quyền mới");
        model.addAttribute("parent_title", "Bảo mật");
        model.addAttribute("form", form);
        model.addAttribute("roles", buildRoleTree(actionEntitys));
        model.addAttribute("services", parameterService.getServices());
        model.addAttribute("assignmentServices", getAssignmentServices());

        validate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "backend/role/new";
        }

        try {
            AuthRoleEntity roleEntity = new AuthRoleEntity();
            roleEntity.setName(form.getName());
            roleEntity = authService.saveRole(roleEntity, form.getAction(), form.getService());
            if (roleEntity == null) {
                throw new Exception("Thêm quyền thất bại!");
            }
            redirectAttributes.addFlashAttribute("success", "Thêm quyền thành công.");
            redirectAttributes.addFlashAttribute("success_id", roleEntity.getID());
            return redirect(UrlUtils.roleIndex());
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "backend/role/new";
        }
    }

    @RequestMapping(value = {"/role/define.html"}, method = RequestMethod.GET)
    public String actionDefine(Model model) {
        model.addAttribute("title", "Định nghĩa hành động");
        model.addAttribute("parent_title", "Bảo mật");

        ArrayList<String> scanRoles = authService.scanRoles();
        List<AuthActionEntity> roles = authService.findAllAction();

        for (String scanRole : scanRoles) {
            boolean isExits = false;
            for (AuthActionEntity role : roles) {
                if (scanRole.equals(role.getName())) {
                    isExits = true;
                    break;
                }
            }
            if (!isExits) {
                roles.add(new AuthActionEntity(scanRole));
            }
        }

        model.addAttribute("roles", buildRoleTree(roles));
        return "backend/role/define";
    }

    @RequestMapping(value = {"/role/update.html"}, method = RequestMethod.GET)
    public String actionUpdate(Model model,
            @RequestParam(name = "oid") Long roleID,
            RoleForm form,
            RedirectAttributes redirectAttributes) {

        AuthRoleEntity role = authService.findOneRole(roleID);
        if (role == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy quyền có mã %s", roleID));
            return redirect(UrlUtils.roleIndex());
        }

        form.setID(role.getID());
        form.setName(role.getName());
        form.setAction(new ArrayList<Long>());
        form.setService(new ArrayList<String>());

        if (role.getRoleActions() != null) {
            for (AuthRoleActionEntity roleAction : role.getRoleActions()) {
                form.getAction().add(roleAction.getAction().getID());
            }
        }

        if (role.getRoleServices() != null) {
            for (AuthRoleServiceEntity roleService : role.getRoleServices()) {
                form.getService().add(roleService.getServiceID());
            }
        }

        model.addAttribute("title", "Cập nhật quyền");
        model.addAttribute("parent_title", "Bảo mật");
        model.addAttribute("form", form);
        model.addAttribute("roles", buildRoleTree(authService.findAllAction()));
        model.addAttribute("services", parameterService.getServices());
        model.addAttribute("assignmentServices", getAssignmentServices());
        return "backend/role/update";
    }

    @RequestMapping(value = {"/role/update.html"}, method = RequestMethod.POST)
    public String doActionUpdate(Model model,
            @RequestParam(name = "oid") Long roleID,
            @ModelAttribute("form") @Valid RoleForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        AuthRoleEntity role = authService.findOneRole(roleID);
        if (role == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy quyền có mã %s", roleID));
            return redirect(UrlUtils.roleIndex());
        }

        model.addAttribute("title", "Cập nhật quyền");
        model.addAttribute("parent_title", "Bảo mật");
        model.addAttribute("form", form);
        model.addAttribute("roles", buildRoleTree(authService.findAllAction())); 
        model.addAttribute("services", parameterService.getServices());
        model.addAttribute("assignmentServices", getAssignmentServices());   

        validate.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            return "backend/role/update";
        }

        try {
            role.setName(form.getName());
            role = authService.saveRole(role, form.getAction(), form.getService());
            if (role == null) {
                throw new Exception("Cập nhật quyền thất bại!");
            }
            redirectAttributes.addFlashAttribute("success", "Cập nhật quyền thành công.");
            redirectAttributes.addFlashAttribute("success_id", role.getID());
            return redirect(UrlUtils.roleIndex());
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "backend/role/update";
        }
    }

    @RequestMapping(value = {"/role/remove.html"}, method = RequestMethod.GET)
    public String actionRemove(Model model,
            @RequestParam(name = "oid") Long roleID,
            RedirectAttributes redirectAttributes) {

        AuthRoleEntity role = authService.findOneRole(roleID);
        if (role == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy quyền có mã %s", roleID));
            return redirect(UrlUtils.roleIndex());
        }

        boolean removeRole = authService.removeRole(role);
        if (removeRole) {
            redirectAttributes.addFlashAttribute("success", String.format("Xoá quyền có mã %s thành công", roleID));
        } else {
            redirectAttributes.addFlashAttribute("error", String.format("Xoá quyền có mã %s không thành công", roleID));
        }
        return redirect(UrlUtils.roleIndex());
    }

    @RequestMapping(value = {"/role/service.html"}, method = RequestMethod.GET)
    public String actionServiceUpdate(Model model,
            RedirectAttributes redirectAttributes) {
        model.addAttribute("title", "Quản lý dịch vụ");
        model.addAttribute("parent_title", "Bảo mật");
        model.addAttribute("services", parameterService.getServices());
        model.addAttribute("assignment", authService.findServices());
        return "backend/role/service_index";
    }

    @RequestMapping(value = {"/role/service/update.html"}, method = RequestMethod.GET)
    public String actionServiceUpdate(Model model,
            @RequestParam(name = "oid") String serviceID,
            ServiceForm form,
            RedirectAttributes redirectAttributes) {
        ParameterEntity service = parameterService.getServiceByID(serviceID);
        if (service == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy dịch vụ có mã %s", serviceID));
            return redirect(UrlUtils.roleService());
        }

        List<AuthServiceAssignmentEntity> serviceAssignments = authService.findServiceAssginment(service.getCode());
        form.setServiceID(service.getCode());
        form.setAction(new ArrayList<Long>());

        if (serviceAssignments != null && !serviceAssignments.isEmpty()) {
            for (AuthServiceAssignmentEntity serviceAssignment : serviceAssignments) {
                if (serviceAssignment.getAction() == null) {
                    continue;
                }
                form.getAction().add(serviceAssignment.getAction().getID());
            }
        }

        model.addAttribute("title", "Định nghĩa quyền theo dịch vụ");
        model.addAttribute("parent_title", "Bảo mật");
        model.addAttribute("service", service);
        model.addAttribute("form", form);
        model.addAttribute("roles", buildRoleTree(authService.findAllAction()));
        return "backend/role/service_update";
    }

    @RequestMapping(value = {"/role/service/update.html"}, method = RequestMethod.POST)
    public String doActionServiceUpdate(Model model,
            @RequestParam(name = "oid") String serviceID,
            @ModelAttribute("form") @Valid ServiceForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        ParameterEntity service = parameterService.getServiceByID(serviceID);
        if (service == null) {
            redirectAttributes.addFlashAttribute("error", String.format("Không tìm thấy dịch vụ có mã %s", serviceID));
            return redirect(UrlUtils.roleService());
        }

        model.addAttribute("title", "Định nghĩa quyền theo dịch vụ");
        model.addAttribute("parent_title", "Bảo mật");
        model.addAttribute("service", service);
        model.addAttribute("form", form);
        model.addAttribute("roles", buildRoleTree(authService.findAllAction()));

        if (bindingResult.hasErrors()) {
            return "backend/role/service_update";
        }

        try {
            service = authService.saveService(service, form.getAction());
            if (service == null) {
                throw new Exception("Định nghĩa quyền theo dịch vụ thất bại!");
            }
            redirectAttributes.addFlashAttribute("success", "Định nghĩa quyền theo dịch vụ thành công.");
            redirectAttributes.addFlashAttribute("success_id", service.getCode());
            return redirect(UrlUtils.roleService());
        } catch (Exception ex) {
            model.addAttribute("error", ex.getMessage());
            return "backend/role/service_update";
        }
    }

}
