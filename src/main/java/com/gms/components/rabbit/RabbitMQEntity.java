package com.gms.components.rabbit;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 *
 * @author vvthanh
 */
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id", scope = RabbitMQEntity.class)
public class RabbitMQEntity {

    private String exchangeName;
    private String routingkey;
    private Object message;

    public RabbitMQEntity(String exchangeName, String routingkey, Object message) {
        this.exchangeName = exchangeName;
        this.routingkey = routingkey;
        this.message = message;
    }

    public RabbitMQEntity() {
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getRoutingkey() {
        return routingkey;
    }

    public void setRoutingkey(String routingkey) {
        this.routingkey = routingkey;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

}
