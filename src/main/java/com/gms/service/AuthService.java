package com.gms.service;

import com.gms.entity.db.AuthActionEntity;
import com.gms.entity.db.AuthAssignmentEntity;
import com.gms.entity.db.AuthRoleActionEntity;
import com.gms.entity.db.AuthRoleEntity;
import com.gms.entity.db.AuthRoleServiceEntity;
import com.gms.entity.db.AuthServiceAssignmentEntity;
import com.gms.entity.db.ParameterEntity;
import com.gms.repository.AuthActionRepository;
import com.gms.repository.AuthAssignmentRepository;
import com.gms.repository.AuthRoleActionRepository;
import com.gms.repository.AuthRoleRepository;
import com.gms.repository.AuthRoleServiceRepository;
import com.gms.repository.AuthServiceAssignmentRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 *
 * @author vvthanh
 */
@Service
public class AuthService extends BaseService {

    @Autowired
    private AuthRoleRepository roleRepository;
    @Autowired
    private AuthActionRepository actionRepository;
    @Autowired
    private AuthRoleActionRepository roleActionRepository;
    @Autowired
    private AuthRoleServiceRepository roleServiceRepository;
    @Autowired
    private AuthAssignmentRepository assignmentRepository;
    @Autowired
    private AuthServiceAssignmentRepository serviceAssignmentRepository;
    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    /**
     * Danh URL scan trên hệ thống
     *
     * @auth vvThành
     * @return
     */
    public ArrayList<String> scanRoles() {
        ArrayList<String> ignore = new ArrayList<>();
        ignore.add("/signin.html");
        ignore.add("/logout.html");
        ignore.add("/ping.json");
        ignore.add("/error.html");
        ignore.add("/error");
        ignore.add("/403.html");
        ignore.add("/");
        ignore.add("/backend/index.html");
        ignore.add("/index.html");
        ignore.add("/auth/forgot-password.html");
        ignore.add("/auth/reset-password.html");
        ignore.add("/test.html");
        ignore.add("/robots.txt");

        ArrayList<String> roles = new ArrayList<>();
        Set<RequestMappingInfo> handlerMethods = requestMappingHandlerMapping.getHandlerMethods().keySet();
        for (RequestMappingInfo handlerMethod : handlerMethods) {
            for (String pattern : handlerMethod.getPatternsCondition().getPatterns()) {
                if (ignore.contains(pattern)) {
                    continue;
                }
                roles.add(pattern);
            }
        }
        return roles;
    }

    /**
     * Lấy toàn bộ quyền trên hệ thống
     *
     * @auth vvThành
     * @return
     */
    public List<AuthActionEntity> findAllAction() {
        return actionRepository.findAll(Sort.by(Sort.Direction.ASC, new String[]{"name"}));
    }

    /**
     * Lưu action row in table action
     *
     * @auth vvThanh
     * @param entity
     * @return
     */
    public AuthActionEntity saveAction(AuthActionEntity entity) {
        try {
            AuthActionEntity model = actionRepository.findByName(entity.getName());
            if (model == null) {
                model = new AuthActionEntity();
                model.setCreateAT(new Date());
            }
            model.setUpdateAt(new Date());
            model.setName(entity.getName());
            model.setTitle(entity.getTitle() == null || entity.getTitle().isEmpty() ? entity.getName() : entity.getTitle());
            return actionRepository.save(model);
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            return null;
        }
    }

    /**
     * Lưu role
     *
     * @auth vvThành
     * @param entity
     * @param action
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AuthRoleEntity saveRole(AuthRoleEntity entity, List<Long> action, List<String> services) {
        try {
            Date currentDate = new Date();
            AuthRoleEntity model;
            if (entity.getID() == null || entity.getID() == 0) {
                model = new AuthRoleEntity();
                model.setCreateAT(currentDate);
            } else {
                model = findOneRole(entity.getID());
            }
            model.setUpdateAt(currentDate);
            model.setName(entity.getName());
            model = roleRepository.save(model);

            roleActionRepository.removeByRoleID(model.getID());

            AuthRoleActionEntity roleAction;
            List<AuthRoleActionEntity> entitys = new ArrayList<>();
            for (Long actionID : action) {
                roleAction = new AuthRoleActionEntity();
                roleAction.setCreateAT(currentDate);
                roleAction.setRole(model);
                AuthActionEntity actionEntity = findOneAction(actionID);
                if (actionEntity != null) {
                    roleAction.setAction(actionEntity);
                    entitys.add(roleAction);
                }
            }
            roleActionRepository.saveAll(entitys);

            roleServiceRepository.removeByRoleID(model.getID());
            if (services != null && !services.isEmpty()) {
                AuthRoleServiceEntity serviceEntity = null;
                List<AuthRoleServiceEntity> serviceEntitys = new ArrayList<>();
                for (String service : services) {
                    serviceEntity = new AuthRoleServiceEntity();
                    serviceEntity.setCreateAT(currentDate);
                    serviceEntity.setRole(model);
                    serviceEntity.setServiceID(service);
                    serviceEntitys.add(serviceEntity);
                }
                roleServiceRepository.saveAll(serviceEntitys);
            }

            return model;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }
    }

    /**
     * Chi tiết action by ID
     *
     * @auth vvThành
     * @param ID
     * @return
     */
    public AuthActionEntity findOneAction(Long ID) {
        Optional<AuthActionEntity> optional = actionRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     * Chi tiết role by ID
     *
     * @auth vvThành
     * @param ID
     * @return
     */
    public AuthRoleEntity findOneRole(Long ID) {
        Optional<AuthRoleEntity> optional = roleRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     * Lấy role theo name
     *
     * @auth vvThành
     * @param name
     * @return
     */
    public AuthRoleEntity findRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    /**
     * Danh sách quyền trên hệ thống
     *
     * @auth vvThành
     * @return
     */
    public List<AuthRoleEntity> findAllRole() {
        List<AuthRoleEntity> entitys = roleRepository.findAll(Sort.by(Sort.Direction.ASC, new String[]{"name"}));
        return entitys == null || entitys.isEmpty() ? null : entitys;
    }

    /**
     * @auth vvThành
     * @param userID
     */
    public void removeAssignmentByUserID(Long userID) {
        try {
            assignmentRepository.removeByUserID(userID);
        } catch (Exception e) {
            getLogger().error(e.getMessage());
        }
    }

    /**
     * Lưu cấp quyền cho quản trị viên
     *
     * @param assignment
     * @return
     */
    public AuthAssignmentEntity saveAssignment(AuthAssignmentEntity assignment) {
        try {
            Date currentDate = new Date();
            AuthAssignmentEntity model = assignmentRepository.findByUserIDAndRoleID(assignment.getUser().getID(), assignment.getRole().getID());
            if (model == null) {
                model = new AuthAssignmentEntity();
                model.setCreateAT(currentDate);
                model.setUser(assignment.getUser());
            }
            model.setRole(assignment.getRole());
            return assignmentRepository.save(model);
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            return null;
        }
    }

    /**
     * Chi tiết quyền theo quản trị viên
     *
     * @auth vvThành
     * @param userID
     * @return
     */
    public List<AuthAssignmentEntity> findOneAssignmentByUserID(Long userID) {
        return assignmentRepository.findByUserID(userID);
    }

    /**
     * Xoá quyền, xoá liên kết với action, xoá những user có quyền
     *
     * @auth vvThành
     * @param role
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean removeRole(AuthRoleEntity role) {
        try {
            assignmentRepository.removeByRoleID(role.getID());
            roleActionRepository.removeByRoleID(role.getID());
            roleRepository.delete(role);
            return true;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    /**
     * Danh sách quyền theo service
     *
     * @auth vvThành
     * @param serviceID
     * @return
     */
    public List<AuthServiceAssignmentEntity> findServiceAssginment(String serviceID) {
        return serviceAssignmentRepository.findByServiceID(serviceID);
    }

    /**
     * Cấp quyền cho dịch vụ
     *
     * @auth vvThành
     * @param service
     * @param action
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ParameterEntity saveService(ParameterEntity service, List<Long> action) {
        try {
            if (action == null) {
                action = new ArrayList<>();
            }
            Date currentDate = new Date();
            serviceAssignmentRepository.removeByServiceID(service.getCode());

            AuthServiceAssignmentEntity assignmentEntity;
            List<AuthServiceAssignmentEntity> entitys = new ArrayList<>();
            for (Long actionID : action) {
                assignmentEntity = new AuthServiceAssignmentEntity();
                assignmentEntity.setCreateAT(currentDate);
                assignmentEntity.setServiceID(service.getCode());
                AuthActionEntity actionEntity = findOneAction(actionID);
                if (actionEntity != null) {
                    assignmentEntity.setAction(actionEntity);
                    entitys.add(assignmentEntity);
                }
            }
            serviceAssignmentRepository.saveAll(entitys);
            return service;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }

    }

    /**
     * @auth vvThành
     * @return
     */
    public HashMap<String, List<AuthServiceAssignmentEntity>> findServices() {
        HashMap<String, List<AuthServiceAssignmentEntity>> _all_services = new HashMap<>();
        List<AuthServiceAssignmentEntity> entitys = serviceAssignmentRepository.findAll();
        for (AuthServiceAssignmentEntity entity : entitys) {
            if (_all_services.get(entity.getServiceID()) == null) {
                _all_services.put(entity.getServiceID(), new ArrayList<AuthServiceAssignmentEntity>());
            }
            _all_services.get(entity.getServiceID()).add(entity);
        }
        return _all_services.isEmpty() ? null : _all_services;
    }

    /**
     * Danh sách action theo dịch vụ
     *
     * @auth vvThành
     * @param serviceIDs
     * @return
     */
    public List<AuthActionEntity> findActionByServices(List<String> serviceIDs) {
        List<AuthActionEntity> actions = new ArrayList<>();
        if (serviceIDs == null || serviceIDs.isEmpty()) {
            return actions;
        }
        Set<Long> ids = new HashSet<>();
        for (Map.Entry<String, List<AuthServiceAssignmentEntity>> entry : findServices().entrySet()) {
            if (!serviceIDs.contains(entry.getKey())) {
                continue;
            }
            for (AuthServiceAssignmentEntity asae : entry.getValue()) {
                ids.add(asae.getActionID());
            }
        }
        return ids.isEmpty() ? null : actionRepository.findByIds(ids);
    }

    /**
     * Danh sách hành động theo ids
     *
     * @param actionID
     * @return
     */
    public List<AuthActionEntity> findActionByIDs(Set<Long> actionID) {
        if (actionID == null || actionID.isEmpty()) {
            return null;
        }
        return actionRepository.findByIds(actionID);
    }

    /**
     * Xoá hành động
     *
     * @auth vvThành
     * @param role
     */
    public void removeAction(AuthActionEntity role) {
//        roleRepository.removeBy
        actionRepository.delete(role);
    }

}
