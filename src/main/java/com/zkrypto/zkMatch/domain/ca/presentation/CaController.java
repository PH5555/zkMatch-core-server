package com.zkrypto.zkMatch.domain.ca.presentation;

import com.zkrypto.zkMatch.domain.ca.application.dto.request.ApplyConfirmCommand;
import com.zkrypto.zkMatch.domain.ca.application.dto.response.PkResponse;
import com.zkrypto.zkMatch.domain.ca.application.service.CaService;
import com.zkrypto.zkMatch.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ca")
@RequiredArgsConstructor
public class CaController {
    private final CaService caService;

    @GetMapping("/key")
    public ApiResponse<PkResponse> getKey() {
        return ApiResponse.success(caService.getPk());
    }

    @PostMapping("/offer/verify")
    public ApiResponse<Void> confirmApply(@RequestBody ApplyConfirmCommand applyConfirmCommand) {
        caService.confirmApply(applyConfirmCommand);
        return ApiResponse.success();
    }
}
