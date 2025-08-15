package com.zkrypto.zkMatch.domain.member.application.dto.response;

import com.zkrypto.zkMatch.domain.offer.domain.entity.Offer;
import com.zkrypto.zkMatch.global.utils.DateFormatter;
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
        return new MemberOfferResponse(offer.getCorporation().getCorporationName(), DateFormatter.format(offer.getCreatedAt()));
    }
}
