package com.zkrypto.zkMatch.global.rabbitmq;

import com.zkrypto.zkMatch.domain.corporation.domain.entity.Corporation;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.global.rabbitmq.dto.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DirectExchangeService {
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String routingKey;

    public void send(Corporation corporation, Member member) {
        SendMessage message = SendMessage.from(corporation, member);
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}