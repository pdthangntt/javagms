package com.gms.service;

import com.gms.entity.db.HtcLaytestAgencyEntity;
import com.gms.entity.db.OpcChildEntity;
import com.gms.repository.OpcChildRepository;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.TransformerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 *
 * @author pdThang
 */
@Service
public class OpcChildService extends OpcBaseService {

    @Autowired
    private OpcChildRepository childRepository;

    /**
     * Danh sách lượt khám theo bệnh án/xoá
     *
     * @param arvID
     * @auth DSNAnh
     * @return
     */
    public List<OpcChildEntity> findByArvIDAndStageID(Long arvID, Long stageID) {
        return childRepository.findByArvIDAndStageID(arvID, stageID);
    }
    
    public List<OpcChildEntity> findByArvIDAndStageIDAndVisitID(Long arvID, Long stageID, Long visitID) {
        return childRepository.findByArvIDAndStageIDAndVisitID(arvID, stageID, visitID);
    }
    
    public List<OpcChildEntity> findByArvID(Long arvID) {
        return childRepository.findByArvID(arvID);
    }

    /**
     * Lưu thông tin con bên điều trị
     * 
     * @param arvID
     * @param stageID
     * @param childs
     * @return 
     */
    @Transactional(rollbackFor = Exception.class)
    public Iterable<OpcChildEntity> saveAllTreatment(Long arvID, Long stageID, List<OpcChildEntity> childs) {
        try {
            
            List<Long> childrenID = new ArrayList<>();        // Thông tin con hiện tại trong database
            List<Long> updateChildrenID = new ArrayList<>();  // Thông tin cập nhật lần mới
            List<Long> deleteChildrenID = new ArrayList<>();  // Thông tin xóa lần mới
            List<OpcChildEntity> children = childRepository.findByArvIDAndStageID(arvID, stageID);
            childrenID.addAll(CollectionUtils.collect(children, TransformerUtils.invokerTransformer("getID")));
            
            if ((childs == null || childs.isEmpty()) && (!childrenID.isEmpty()) ) {
                childRepository.deleteByIds(childrenID);
            } else if(childs != null && !childs.isEmpty()){
                updateChildrenID.addAll(CollectionUtils.collect(childs, TransformerUtils.invokerTransformer("getID")));
                for (Long child : childrenID) {
                    if (!updateChildrenID.contains(child)) {
                        deleteChildrenID.add(child);
                    }
                }

                // Xóa thông tin ds trẻ con
                if (deleteChildrenID != null && !deleteChildrenID.isEmpty()) {
                    childRepository.deleteByIds(deleteChildrenID);
                }
            }
            
            // Thêm mới lại thông tin con
            if (childs != null) {
               return childRepository.saveAll(childs);
            }
            return null;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }
    }
    
    /**
     * Lưu thông tin con bên lượt khám
     * 
     * @param stageID
     * @param visitID
     * @param childs
     * @return 
     */
    @Transactional(rollbackFor = Exception.class)
    public Iterable<OpcChildEntity> saveAll(Long arvID, Long stageID, Long visitID, List<OpcChildEntity> childs) {
        try {
            
            List<Long> childrenID = new ArrayList<>();        // Thông tin con hiện tại trong database
            List<Long> updateChildrenID = new ArrayList<>();  // Thông tin cập nhật lần mới
            List<Long> deleteChildrenID = new ArrayList<>();  // Thông tin cập nhật lần mới
            List<OpcChildEntity> children = childRepository.findByArvIDAndStageIDAndVisitID(arvID, stageID, visitID);
            childrenID.addAll(CollectionUtils.collect(children, TransformerUtils.invokerTransformer("getID")));
            
            if ((childs == null || childs.size() == 0) && (childrenID != null && childrenID.size() > 0)) {
                childRepository.deleteByIds(childrenID);
            } else {
                updateChildrenID.addAll(CollectionUtils.collect(childs, TransformerUtils.invokerTransformer("getID")));
                for (Long child : childrenID) {
                    if (!updateChildrenID.contains(child)) {
                        deleteChildrenID.add(child);
                    }
                }

                // Xóa thông tin ds trẻ con
                if (deleteChildrenID != null && deleteChildrenID.size() > 0) {
                    childRepository.deleteByIds(deleteChildrenID);
                }
            }

            // Thêm mới lại thông tin con
            if (childs != null) {
                return childRepository.saveAll(childs);
            }
            return null;
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }
    }

}
