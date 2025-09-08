package com.zkrypto.zkMatch.api.tas;

import com.zkrypto.zkMatch.api.tas.dto.request.RequestVcOfferReqDto;
import com.zkrypto.zkMatch.api.tas.dto.response.RequestVcOfferResDto;
import com.zkrypto.zkMatch.api.tas.dto.response.RequestVcPlanListResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "Tas", url = "${tas.url}")
public interface TasFeign {
    @RequestMapping(value = "/list/api/v1/vcplan/list/issuer", method = RequestMethod.GET)
    RequestVcPlanListResDto getRequestVcPlan();

    @RequestMapping(value = "/tas/api/v1/offer-issue-vc/qr", method = RequestMethod.POST)
    RequestVcOfferResDto requestVcOfferQR(@RequestBody RequestVcOfferReqDto requestVcOfferReqDto);
}
