package com.zkrypto.zkMatch.api.issuer;

import com.zkrypto.zkMatch.domain.corporation.domain.constant.SaveUserInfoReqDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "IssuerAdmin", url = "${issuer.url}", path = "/issuer/admin/v1")
public interface IssuerFeign {
    @RequestMapping(value = "/users/demo", method = RequestMethod.POST)
    void saveUserInfo(@RequestBody SaveUserInfoReqDto saveUserInfoReqDto);

}