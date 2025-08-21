package com.zkrypto.zkMatch.domain.ca.presentation;

import com.zkrypto.zkMatch.domain.ca.application.dto.response.KeyPair;
import com.zkrypto.zkMatch.domain.ca.application.service.CaService;
import com.zkrypto.zkMatch.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ca")
@RequiredArgsConstructor
public class CaController {
    private final CaService caService;

    @GetMapping("/key")
    public ApiResponse<KeyPair> getKey(@RequestParam("keyId") String keyId) {
        return ApiResponse.success(caService.getKey(keyId));
    }
}
