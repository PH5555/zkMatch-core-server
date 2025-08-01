package com.zkrypto.zkMatch.global.rabbitmq.dto;

import com.zkrypto.zkMatch.domain.corporation.domain.entity.Corporation;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import lombok.Getter;

@Getter
public class SendMessage {
    private String emailAddress;
    private String receiverName;
    private String corporationName;

    public SendMessage(String emailAddress, String receiverName, String corporationName) {
        this.emailAddress = emailAddress;
        this.receiverName = receiverName;
        this.corporationName = corporationName;
    }

    public static SendMessage from(Corporation corporation, Member member) {
        return new SendMessage(member.getEmail(), member.getName(), corporation.getCorporationName());
    }
}
