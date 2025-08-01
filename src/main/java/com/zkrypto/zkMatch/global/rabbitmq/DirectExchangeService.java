package com.zkrypto.zkMatch.global.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirectExchangeService {
    private final RabbitTemplate rabbitTemplate;
    private final String DIRECT_EXCHANGE_NAME = "email_exchange";
    private final String DIRECT_QUEUE_ROUTING_KEY = "direct_routing_key";

    public void send() {

    }
}
