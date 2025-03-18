package com.gms.service;

import com.gms.components.TextUtils;
import com.gms.components.rabbit.RabbitMQSender;
import com.gms.entity.bean.DataPage;
import com.gms.entity.db.EmailoutboxEntity;
import com.gms.entity.input.EmailSearch;
import com.gms.entity.rabbit.EmailMessage;
import com.gms.repository.EmailoutboxRepository;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 *
 * @author vvthanh
 */
@Service
//@PropertySource("classpath:application.properties")
public class EmailService extends BaseService implements IBaseService<EmailoutboxEntity, Long> {

    @Autowired
    private EmailoutboxRepository emailoutboxRepository;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private RabbitMQSender rabbitMQSender;

    @Value("${spring.mail.from}")
    private String fromEmail;

    @Override
    public List<EmailoutboxEntity> findAll() {
        return emailoutboxRepository.findAll();
    }

    @Override
    public EmailoutboxEntity findOne(Long ID) {
        Optional<EmailoutboxEntity> model = emailoutboxRepository.findById(ID);
        return model.isPresent() ? model.get() : null;
    }

    /**
     * Tìm row email chưa run. có lock
     *
     * @auth vvThành
     * @return
     */
    @Transactional
    public EmailoutboxEntity findByNotRun() {
        return emailoutboxRepository.findByNotRunForUpdate();
    }

    @Override
    public EmailoutboxEntity save(EmailoutboxEntity entity) {
        try {
            if (entity.getID() == null) {
                entity.setCreateAT(new Date());
                entity.setIsRun(false);
            }
            return emailoutboxRepository.save(entity);
        } catch (Exception e) {
            getLogger().error(e.getMessage());
            return null;
        }
    }

    /**
     * Send email for Controller
     *
     * @auth vvThành
     * @param to
     * @param subject
     * @param type
     * @param variables
     * @return
     */
    public EmailoutboxEntity send(String to, String subject, String type, Map<String, Object> variables) {
        return send(to, subject, type, variables, null, null, null);
    }

    /**
     * Send email có đính kèm
     *
     * @param to
     * @param subject
     * @param type
     * @param variables
     * @param fileName
     * @param fileType
     * @param fileContent
     * @return
     */
    public EmailoutboxEntity send(String to, String subject, String type, Map<String, Object> variables, String fileName, String fileType, byte[] fileContent) {
        EmailoutboxEntity entity = new EmailoutboxEntity();
        Context ctx = new Context(new Locale("vi", "VN"));
        ctx.setVariables(variables);
        ctx.setVariable("contents", variables.get("contents"));
        String htmlContent = templateEngine.process(String.format("mail/%s.html", type), ctx);
        entity.setFrom(fromEmail);
        entity.setTo(to);
        entity.setSubject(subject);
        entity.setContent(htmlContent);
        entity.setAttachmentFileName(fileName == null || fileName.isEmpty() ? null : fileName);
        entity.setAttachmentType(fileType == null || fileType.isEmpty() ? null : fileType);
        entity.setAttachment(fileContent);
        save(entity);
        rabbitMQSender.sendEmail(new EmailMessage(entity));
        return entity;
    }

    /**
     * Tìm kiếm khách hàng
     *
     * @author PDTHANG
     * @param search
     * @return
     */
    public DataPage<EmailoutboxEntity> find(EmailSearch search) {
        DataPage<EmailoutboxEntity> dataPage = new DataPage<>();
        dataPage.setCurrentPage(search.getPageIndex());
        dataPage.setMaxResult(search.getPageSize());
        Sort sortable = Sort.by(Sort.Direction.DESC, new String[]{"sendAt"});
        if (search.getSortable() != null) {
            sortable = search.getSortable();
        }
        Pageable pageable = PageRequest.of(search.getPageIndex() - 1, search.getPageSize(), sortable);

        Page<EmailoutboxEntity> pages = emailoutboxRepository.find(
                search.getSubject(),
                search.getFrom(),
                search.getTo(),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getSendAtFrom()),
                TextUtils.formatDate("dd/MM/yyyy", FORMATDATE, search.getSendAtTo()),
                search.getTab(),
                pageable);

        dataPage.setTotalPages(pages.getTotalPages());
        dataPage.setTotalRecords((int) pages.getTotalElements());
        dataPage.setData(pages.getContent());
        return dataPage;
    }
}
