package com.gms.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author vvthanh
 */
@Configuration
@EnableRabbit
public class RabbitConfig {

    public final static String EXCHANGE_NAME = "gms.hi";
    public final static String QUEUE_EMAIL = "gms.email.send";
    public final static String QUEUE_UPDATE_STATUS_CONFIRM_TO_HTC = "gms.confirm.update.htc";
    public final static String QUEUE_HIVINFO_PATIENT = "gms.hivinfo.patient";
    public final static String QUEUE_HIVINFO_UPDATE = "gms.hivinfo.update";

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue queueEmail() {
        return new Queue(QUEUE_EMAIL, false);
    }

    @Bean
    public Binding bindingEmail(@Qualifier("queueEmail") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(QUEUE_EMAIL);
    }

    @Bean
    public Queue queueVisit() {
        return new Queue(QUEUE_UPDATE_STATUS_CONFIRM_TO_HTC, false);
    }

    @Bean
    public Binding bindingVisit(@Qualifier("queueVisit") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(QUEUE_UPDATE_STATUS_CONFIRM_TO_HTC);
    }

    @Bean
    public Queue queueHivinfoPatient() {
        return new Queue(QUEUE_HIVINFO_PATIENT, false);
    }

    @Bean
    public Binding bindingHivinfoPatient(@Qualifier("queueHivinfoPatient") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(QUEUE_HIVINFO_PATIENT);
    }

    @Bean
    public Queue queueUpdateToHivinfo() {
        return new Queue(QUEUE_HIVINFO_UPDATE, false);
    }

    @Bean
    public Binding bindingUpdateToHivinfo(@Qualifier("queueUpdateToHivinfo") Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(QUEUE_HIVINFO_UPDATE);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(name = "rabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            SimpleRabbitListenerContainerFactoryConfigurer configurer,
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

}
