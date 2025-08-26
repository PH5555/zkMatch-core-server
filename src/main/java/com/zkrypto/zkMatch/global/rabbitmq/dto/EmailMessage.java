package com.zkrypto.zkMatch.global.rabbitmq.dto;

import com.zkrypto.zkMatch.domain.recruit.domain.constant.Status;
import com.zkrypto.zkMatch.domain.recruit.domain.entity.Recruit;
import lombok.Getter;

@Getter
public class EmailMessage {
    private String to;
    private String subject;
    private String message;

    public EmailMessage(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
    }

    public static EmailMessage from(Recruit recruit, Status status) {
        String subject = "[" + recruit.getPost().getTitle() + "]" + " 공지";
        String message = recruit.getPost().getTitle() + "에 ";
        if(status == Status.FAILED) {
            message += "탈락하셨습니다.";
        }
        else if(status == Status.PASS) {
            message += "합격하셨습니다.";
        }
        return new EmailMessage(recruit.getMember().getEmail(), subject, message);
    }

    public static EmailMessage from(String email, String randomKey) {
        String subject = "[zkMatch] 이메일 인증";
        String message = randomKey + " 를 입력해주세요";
        return new EmailMessage(email, subject, message);
    }
}
