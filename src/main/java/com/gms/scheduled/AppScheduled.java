package com.gms.scheduled;

import com.gms.components.EmailUtils;
import com.gms.components.TextUtils;
import com.gms.components.UrlUtils;
import com.gms.config.RabbitConfig;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.db.StaffActivityEntity;
import com.gms.entity.db.StaffNotificationEntity;
import com.gms.entity.rabbit.EmailMessage;
import com.gms.repository.StaffNotificationRepository;
import com.gms.service.EmailService;
import com.gms.service.StaffActivityService;
import com.gms.service.StaffService;
import java.util.Date;
import java.util.List;
import javax.mail.util.ByteArrayDataSource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author vvthanh
 */
@Component
public class AppScheduled extends BaseScheduled {

    @Autowired
    private StaffActivityService activityService;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private EmailUtils emailUtils;
    @Autowired
    private EmailService emailService;
    @Autowired
    private StaffService staffService;

    @RabbitListener(queues = RabbitConfig.QUEUE_EMAIL)
    public void queueSendEmail(final EmailMessage message) {
        EmailoutboxEntity entity = message.getEmailoutbox();
        if (entity == null) {
            return;
        }

        getLogger().info("[Rabbit] Send email {}", message.getEmailoutbox().getTo());
        entity.setSendAt(new Date());
        entity.setIsRun(true);
        emailService.save(entity);

        try {
            getLogger().info("Scheduled send email {}", entity.getTo());
            ByteArrayDataSource dataSource = null;
            if (entity.getAttachmentFileName() != null && entity.getAttachment() != null) {
                dataSource = new ByteArrayDataSource(entity.getAttachment(), entity.getAttachmentType());
            }
            emailUtils.send(entity.getTo(), entity.getSubject(), entity.getContent(), entity.getAttachmentFileName() == null ? null : TextUtils.removeDiacritical(entity.getAttachmentFileName()), dataSource);

            if (entity.getAttachmentFileName() != null && !"".equals(entity.getAttachmentFileName())) {
                staffService.sendMessage(entity.getTo(), entity.getSubject(), "Vui lòng kiểm tra email", UrlUtils.backendHome());
            }
        } catch (Exception e) {
            entity.setErrorMessage(e.getMessage());
            emailService.save(entity);
        }
    }

    /**
     * Job chạy vào 2h sáng hàng ngày
     *
     * @auth vvThành
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void appRun() {
        //Xoá cache
        for (String name : cacheManager.getCacheNames()) {
            Cache cache = cacheManager.getCache(name);
            if (cache == null) {
                continue;
            }
            cache.clear();
        }

        //xoá những active của staff
        List<StaffActivityEntity> entitys = activityService.findActivitiesOlderThanAMonth();
        if (entitys != null && !entitys.isEmpty()) {
            getLogger().info("Scheduled remove log staff {} in {}", entitys.size(), getDate().format(new Date()));
            activityService.deleteAll(entitys);
        }
        List<StaffNotificationEntity> notis = staffService.findNotificationsOlderThanAMonth();
        if (notis != null && !notis.isEmpty()) {
            getLogger().info("Scheduled remove notify {} in {}", notis.size(), getDate().format(new Date()));
            staffService.deleteAllNotification(notis);
        }
    }
}
