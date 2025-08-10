package com.zkrypto.zkMatch.domain.portfolio.presentation;

import com.zkrypto.zkMatch.domain.portfolio.application.dto.request.DidVerifyCommand;
import com.zkrypto.zkMatch.domain.portfolio.application.dto.response.PortfolioResponse;
import com.zkrypto.zkMatch.domain.portfolio.application.service.PortfolioService;
import com.zkrypto.zkMatch.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/portfolio")
@RequiredArgsConstructor
@Tag(name = "PortfolioController", description = "포트폴리오 관련 API")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @Operation(
            summary = "포트폴리오 조회 API",
            description = "나의 포트폴리오 정보를 조회합니다.",
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
                    content = {@Content(schema = @Schema(implementation = PortfolioResponse.class))}),
    })
    @GetMapping()
    public ApiResponse<PortfolioResponse> getPortfolio(@AuthenticationPrincipal UUID memberId) {
        return ApiResponse.success(null);
    }

    @Operation(
            summary = "DID 인증 요청 API",
            description = "특정 DID 인증 요청을 합니다.",
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
    @PostMapping("/did")
    public void requestDidVerify (@RequestBody DidVerifyCommand didVerifyCommand) {

    }

//    @Operation(
//            summary = "자기소개서 업로드 API",
//            description = "자기소개서를 업로드 합니다.",
//            security = {
//                    @SecurityRequirement(name = "bearerAuth")
//            },
//            parameters = {
//                    @Parameter(
//                            in = ParameterIn.HEADER,
//                            name = "Authorization",
//                            description = "Bearer 토큰",
//                            required = true
//                    )
//            }
//    )
//    @ApiResponses(value = {
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공",
//                    content = {@Content(schema = @Schema(implementation = Void.class))}),
//    })
//    @PostMapping("/statement")
//    public ApiResponse<Void> uploadPersonalStatement(@AuthenticationPrincipal UUID memberId, @RequestPart("file") MultipartFile file) throws IOException {
//        portfolioService.uploadPersonalStatement(memberId, file);
//        return ApiResponse.success();
//    }

}
