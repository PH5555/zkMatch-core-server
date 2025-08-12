package com.zkrypto.zkMatch.domain.member.application.dto.response;

import com.zkrypto.zkMatch.domain.offer.domain.entity.Offer;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class MemberOfferResponse {
    private String corporationName;
    private String date;

    private MemberOfferResponse(String corporationName, String date) {
        this.corporationName = corporationName;
        this.date = date;
    }

    public static MemberOfferResponse from(Offer offer) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        String date = offer.getCreatedAt().format(formatter);
        return new MemberOfferResponse(offer.getCorporation().getCorporationName(), date);
    }
}
