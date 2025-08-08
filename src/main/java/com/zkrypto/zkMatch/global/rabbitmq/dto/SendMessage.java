package com.zkrypto.zkMatch.global.rabbitmq.dto;

import com.zkrypto.zkMatch.domain.corporation.domain.entity.Corporation;
import com.zkrypto.zkMatch.domain.member.domain.entity.Member;
import com.zkrypto.zkMatch.domain.recruit.domain.constant.Status;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import lombok.Getter;

@Getter
public class SendMessage {
    private String emailAddress;
    private String subject;
    private String content;

    public SendMessage(String emailAddress, String subject, String content) {
        this.emailAddress = emailAddress;
        this.subject = subject;
        this.content = content;
    }

    public static SendMessage from(Recruit recruit, Status status) {
        String subject = "[" + recruit.getPost().getTitle() + "]" + " 공지";
        String content = recruit.getPost().getTitle() + "에 ";
        if(status == Status.FAILED) {
            content += "탈락하셨습니다.";
        }
        else if(status == Status.PASS) {
            content += "합격하셨습니다.";
        }
        return new SendMessage(recruit.getMember().getEmail(), subject, content);
    }
}
