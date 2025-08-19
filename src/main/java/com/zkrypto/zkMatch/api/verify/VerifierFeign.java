package com.zkrypto.zkMatch.api.verify;

import com.zkrypto.zkMatch.api.verify.dto.request.ConfirmVerifyReqDto;
import com.zkrypto.zkMatch.api.verify.dto.request.RequestVpOfferReqDto;
import com.zkrypto.zkMatch.api.verify.dto.response.ConfirmVerifyResDto;
import com.zkrypto.zkMatch.api.verify.dto.response.RequestVpOfferResDto;
import com.zkrypto.zkMatch.api.verify.dto.response.VpPolicyResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "Verifier", url = "${verifier.url}", path = "/verifier")
public interface VerifierFeign {
    @RequestMapping(value = "/api/v1/request-offer-qr", method = RequestMethod.POST)
    RequestVpOfferResDto requestVpOfferQR(@RequestBody RequestVpOfferReqDto requestVpOfferReqDto);

    @RequestMapping(value = "/api/v1/confirm-verify", method = RequestMethod.POST)
    ConfirmVerifyResDto confirmVerify(@RequestBody ConfirmVerifyReqDto confirmVerifyReqDto);

    @RequestMapping(value = "/admin/v1/policies/all", method = RequestMethod.GET)
    List<VpPolicyResponseDto> getAllPolicies();
}
