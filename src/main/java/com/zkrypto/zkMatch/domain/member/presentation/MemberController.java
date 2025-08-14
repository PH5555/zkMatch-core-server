package com.zkrypto.zkMatch.domain.member.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zkrypto.zkMatch.domain.member.application.dto.request.ResumeCreationCommand;
import com.zkrypto.zkMatch.domain.member.application.dto.response.*;
import com.zkrypto.zkMatch.domain.member.application.service.MemberService;
import com.zkrypto.zkMatch.domain.resume.domain.constant.ResumeType;
import com.zkrypto.zkMatch.domain.scrab.application.dto.response.ScrabResponse;
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

    @Operation(
            summary = "포트폴리오 업로드 API",
            description = "포트폴리오를 업로드 합니다.",
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
    @PostMapping("/portfolio")
    public ApiResponse<Void> uploadPortfolio(@AuthenticationPrincipal UUID memberId, @RequestPart("file") MultipartFile file) {
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

    @Operation(
            summary = "스크랩 공고 조회 API",
            description = "내가 스크랩한 공고를 조회합니다.",
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
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = ScrabResponse.class)))}),
    })
    @GetMapping("/scrab")
    public ApiResponse<List<ScrabResponse>> getMemberScrab(@AuthenticationPrincipal UUID memberId) {;
        return ApiResponse.success(memberService.getScrab(memberId));
    }

    @Operation(
            summary = "이력 불러오기 QR 요청 API",
            description = "이력 불러오기를 위한 QR을 가져옵니다.",
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
                    content = {@Content(schema = @Schema(implementation = ResumeQrResponse.class))}),
    })
    @GetMapping("/resume/did")
    public ApiResponse<ResumeQrResponse> getResumeDidQr(@AuthenticationPrincipal UUID memberId, @RequestParam("type") ResumeType type) {
        return ApiResponse.success(memberService.getResumeDidQr(memberId, type));
    }

    @Operation(
            summary = "이력 불러오기 완료 요청 API",
            description = "QR을 스캔하여 CA앱에서 제출 완료 후 호출합니다.",
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
    @PostMapping("/resume/did")
    public ApiResponse<Void> completeResumeDidQr(@AuthenticationPrincipal UUID memberId) throws JsonProcessingException {
        memberService.completeResumeDidQr(memberId);
        return ApiResponse.success();
    }

    @Operation(
            summary = "이력 생성 API",
            description = "이력을 생성합니다.",
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
    @PostMapping("/resume")
    public ApiResponse<Void> createMemberResume(@AuthenticationPrincipal UUID memberId, ResumeCreationCommand resumeCreationCommand) {
        memberService.createMemberResume(memberId, resumeCreationCommand);
        return ApiResponse.success();
    }

    @Operation(
            summary = "멤버 이력 조회 API",
            description = "나의 이력을 조회합니다.",
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
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = MemberResumeResponse.class)))}),
    })
    @GetMapping("/resume")
    public ApiResponse<List<MemberResumeResponse>> getMemberResume(@AuthenticationPrincipal UUID memberId) {
        return ApiResponse.success(memberService.getMemberResume(memberId));
    }

    @Operation(
            summary = "멤버 이력 삭제 API",
            description = "나의 이력을 삭제합니다.",
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
    @DeleteMapping("/resume/{resumeId}")
    public ApiResponse<Void> deleteMemberResume(@AuthenticationPrincipal UUID memberId, @PathVariable("resumeId") String resumeId) {
        memberService.deleteMemberResume(memberId, resumeId);
        return ApiResponse.success();
    }

    @Operation(
            summary = "채용 제안 조회 API",
            description = "나의 채용 제안 이력을 조회합니다.",
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
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = MemberOfferResponse.class)))}),
    })
    @GetMapping("/offer")
    public ApiResponse<List<MemberOfferResponse>> getMemberOffer(@AuthenticationPrincipal UUID memberId) {
        return ApiResponse.success(memberService.getMemberOffer(memberId));
    }
}
