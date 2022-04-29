package com.sample.springrabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfig {

    public static final String QUEUE = "message_queue";
    public static final String EXCHANGE = "message_exchange";
    public static final String ROUTING_KEY = "message_routingKey";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(messageConverter());
        //template.setUseDirectReplyToContainer(false);
        template.setConfirmCallback((correlationData, ack, cause) -> {
            System.out.println("================");
            System.out.println("correlationData = " + correlationData);
            System.out.println("ack = " + ack);
            System.out.println("cause = " + cause);
            System.out.println("================");
        });

        template.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println("================");
            System.out.println("message = " + message);
            System.out.println("replyCode = " + replyCode);
            System.out.println("replyText = " + replyText);
            System.out.println("exchange = " + exchange);
            System.out.println("routingKey = " + routingKey);
            System.out.println("================");
        });
        return template;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setHost("localhost");
        //factory.setVirtualHost("test");
        factory.setUsername("kee");
        factory.setPassword("kee");
        factory.setPublisherReturns(true);
        factory.setPublisherConfirms(true);
        factory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        return factory;
    }

    @Bean
    Queue marketingQueue() {
        return new Queue("FanoutQueue", false);
    }

    @Bean
    Queue marketing2Queue() {
        return new Queue("Fanout2Queue", false);
    }

    @Bean
    FanoutExchange exchangeFan() {
        return new FanoutExchange("fanout-exchange");
    }

    @Bean
    Binding marketingBinding(Queue marketingQueue, FanoutExchange exchange) {
        return BindingBuilder.bind(marketingQueue).to(exchange);
    }

    @Bean
    Binding marketing2Binding(Queue marketing2Queue, FanoutExchange exchange) {
        return BindingBuilder.bind(marketing2Queue).to(exchange);
    }


    @Bean
    Queue financeQueue() {
        return new Queue("financeQueue", false);
    }

    @Bean
    Queue adminQueue() {
        return new Queue("adminQueue", false);
    }

    @Bean
    DirectExchange dexchange() {
        return new DirectExchange("direct-exchange");
    }

    @Bean
    Binding financeBinding(Queue financeQueue, DirectExchange dexchange) {
        return BindingBuilder.bind(financeQueue).to(dexchange).with("finance");
    }

    @Bean
    Binding adminBinding(Queue adminQueue, DirectExchange dexchange) {
        return BindingBuilder.bind(adminQueue).to(dexchange).with("admin");
    }
}
