package com.zkrypto.zkMatch.domain.scrab.presentation;

import com.zkrypto.zkMatch.domain.scrab.application.dto.request.ScrabCommand;
import com.zkrypto.zkMatch.domain.scrab.application.dto.response.ScrabResponse;
import com.zkrypto.zkMatch.domain.scrab.application.service.ScrabService;
import com.zkrypto.zkMatch.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/scrab")
@RequiredArgsConstructor
@Tag(name = "ScrabController", description = "스크랩 공고 관련 API")
public class ScrabController {

    private final ScrabService scrabService;

    @Operation(
            summary = "공고 스크랩 API",
            description = "해당 공고를 스크랩한 공고로 등록합니다.",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            },
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            description = "Bearer 토큰",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공",
                    content = {@Content(schema = @Schema(implementation = Void.class))}),
    })
    @PostMapping()
    public ApiResponse<Void> scrabPost(@AuthenticationPrincipal UUID memberId, @RequestBody ScrabCommand scrabCommand) {
        scrabService.scrabPost(memberId, scrabCommand);
        return ApiResponse.success();
    }
}
