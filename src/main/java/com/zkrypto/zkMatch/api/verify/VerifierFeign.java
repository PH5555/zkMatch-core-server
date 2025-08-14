package com.zkrypto.zkMatch.api.verify;

import com.zkrypto.zkMatch.api.verify.dto.RequestVpOfferReqDto;
import com.zkrypto.zkMatch.api.verify.dto.RequestVpOfferResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "Verifier", url = "${verifier.url}", path = "/verifier")
public interface VerifierFeign {
    @RequestMapping(value = "/api/v1/request-offer-qr", method = RequestMethod.POST)
    RequestVpOfferResDto requestVpOfferQR(@RequestBody RequestVpOfferReqDto requestVpOfferReqDto);
}
