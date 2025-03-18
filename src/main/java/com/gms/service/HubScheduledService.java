package com.gms.service;

import com.gms.entity.db.HubScheduledEntity;
import com.gms.repository.HubScheduledRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author pdthang
 */
@Service
public class HubScheduledService extends BaseService implements IBaseService<HubScheduledEntity, Long> {

    @Autowired
    private HubScheduledRepository logRepository;

    @Override
    public List<HubScheduledEntity> findAll() {
        return logRepository.findAll();
    }

    @Override
    public HubScheduledEntity findOne(Long ID) {
        Optional<HubScheduledEntity> optional = logRepository.findById(ID);
        return optional.isPresent() ? optional.get() : null;
    }

    public List<HubScheduledEntity> findRun() {
        List<HubScheduledEntity> list = logRepository.findRun();
        return list.isEmpty() ? null : list;
    }

    @Override
    public HubScheduledEntity save(HubScheduledEntity model) {
        try {
            Date currentDate = new Date();
            model.setCreateAt(currentDate);
            logRepository.save(model);
            return model;
        } catch (Exception e) {
            throw e;
        }
    }

}
