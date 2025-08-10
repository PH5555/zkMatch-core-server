package com.zkrypto.zkMatch.domain.member.presentation;

import com.zkrypto.zkMatch.domain.member.application.dto.response.MemberPostResponse;
import com.zkrypto.zkMatch.domain.member.application.dto.response.MemberResponse;
import com.zkrypto.zkMatch.domain.member.application.service.MemberService;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
@Tag(name = "MemberController", description = "멤버 관련 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(
            summary = "로그아웃 API",
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
    @DeleteMapping()
    public ApiResponse<Void> signOut(@AuthenticationPrincipal UUID memberId){
        memberService.signOut(memberId);
        return ApiResponse.success();
    }

    @Operation(
            summary = "멤버 조회 API",
            description = "나의 정보를 조회합니다.",
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
                    content = {@Content(schema = @Schema(implementation = MemberResponse.class))}),
    })
    @GetMapping()
    public ApiResponse<MemberResponse> getMember(@AuthenticationPrincipal UUID memberId) {
        return ApiResponse.success(memberService.getMember(memberId));
    }

    @PostMapping("/portfolio")
    public ApiResponse<Void> uploadPortfolio(@AuthenticationPrincipal UUID memberId, @RequestPart("file") MultipartFile file) throws IOException {
        memberService.uploadPortfolio(memberId, file);
        return ApiResponse.success();
    }

    @Operation(
            summary = "지원 내역 조회 API",
            description = "내가 지원한 공고의 정보를 조회합니다.",
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
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = MemberPostResponse.class)))}),
    })
    @GetMapping("/post")
    public ApiResponse<List<MemberPostResponse>> getMemberPost(@AuthenticationPrincipal UUID memberId) {
        return ApiResponse.success(memberService.getPost(memberId));
    }
}
