package com.gms.components.rabbit;

import com.gms.config.RabbitConfig;
import com.gms.entity.rabbit.EmailMessage;
import com.gms.entity.rabbit.IndicatorMessage;
import com.gms.entity.rabbit.OnesignalMessage;
import com.gms.entity.rabbit.PacPatientMessage;
import com.gms.entity.rabbit.RabbitMessage;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author vvthanh
 */
@Service
public class RabbitMQSender {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQSender.class);

    @Autowired
    private AmqpTemplate rabbitTemplate;

    /**
     * @auth vvThành
     * @param entity
     */
    public void send(RabbitMQEntity entity) {
        logger.debug("Sending Exchange {}, Router {}, Message {}", entity.getExchangeName(), entity.getRoutingkey(), entity.getMessage());
        try {
            rabbitTemplate.convertAndSend(entity.getExchangeName(), entity.getRoutingkey(), entity.getMessage());
        } catch (AmqpException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * @auth vvThanh
     * @param message
     */
    public void sendVisit(RabbitMessage message) {
        send(new RabbitMQEntity(RabbitConfig.EXCHANGE_NAME, RabbitConfig.QUEUE_UPDATE_STATUS_CONFIRM_TO_HTC, message));
    }

    public void sendPacPatient(PacPatientMessage message) {
        send(new RabbitMQEntity(RabbitConfig.EXCHANGE_NAME, RabbitConfig.QUEUE_HIVINFO_PATIENT, message));
    }

    public void sendHivInfo(PacPatientMessage message) {
        send(new RabbitMQEntity(RabbitConfig.EXCHANGE_NAME, RabbitConfig.QUEUE_HIVINFO_UPDATE, message));
    }

    public void sendEmail(EmailMessage message) {
        send(new RabbitMQEntity(RabbitConfig.EXCHANGE_NAME, RabbitConfig.QUEUE_EMAIL, message));
    }

}
