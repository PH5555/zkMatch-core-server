package com.zkrypto.zkMatch.api.cas;

import com.zkrypto.zkMatch.api.cas.dto.request.SaveUserInfoResDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "Cas", url = "${cas.url}", path = "/cas/api/v1")
public interface CasFeign {
    @RequestMapping(value = "/save-user-info", method = RequestMethod.POST)
    void saveUserInfo(@RequestBody SaveUserInfoResDto saveUserInfoResDto);
}