package com.sample.springrabbit;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@Component
public class MessageListener {

    @RabbitListener(queues = MQConfig.QUEUE)
    public void listener(CustomMessage message) {
        System.out.println(message);
    }


    @RabbitListener(queues = "FanoutQueue")
    public void listenera(CustomMessage message) {
        System.out.println("Fanout");
        System.out.println(message);
    }
    @RabbitListener(queues = "Fanout2Queue")
    public void list(CustomMessage message) {
        System.out.println("Fanout2Queue");
        System.out.println(message);
    }
    @RabbitListener(queues = "financeQueue")
    public void listeneraa(CustomMessage message) {
        System.out.println("financeQueue Direct");
        System.out.println(message);
    }

    @RabbitListener(queues = "adminQueue")
    public void listen(CustomMessage message) {
        System.out.println("adminQueue Direct");
        System.out.println(message);
    }
}