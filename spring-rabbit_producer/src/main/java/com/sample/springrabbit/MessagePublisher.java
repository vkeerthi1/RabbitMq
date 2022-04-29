package com.sample.springrabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.UUID;

@RestController
public class MessagePublisher {

    @Autowired
    private RabbitTemplate template;

    @PostMapping("/publish")
    public String publishMessage(@RequestBody CustomMessage message) {
        message.setMessageId(UUID.randomUUID().toString());
        message.setMessageDate(new Date());
        template.convertAndSend(MQConfig.EXCHANGE,
                MQConfig.ROUTING_KEY, message);

        return "Message Published";
    }

    @GetMapping(value = "/producer")
    public String producer(@RequestBody CustomMessage message) {

        template.convertAndSend("fanout-exchange", "", message);

        return "Message sent to the RabbitMQ Fanout Exchange Successfully";
    }

    @GetMapping(value = "/producerDirect")
    public String producer(@RequestParam("exchangeName") String exchange, @RequestParam("routingKey") String routingKey,
                           @RequestBody CustomMessage messageData) {
        //producerDirect?exchangeName=direct-exchange&routingKey=admin&messageData=HelloWorldJavaInUse
        template.convertAndSend(exchange, routingKey, messageData);

        return "Message sent to the RabbitMQ Successfully";
    }
}