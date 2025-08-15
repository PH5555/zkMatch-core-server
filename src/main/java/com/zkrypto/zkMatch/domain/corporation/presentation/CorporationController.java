package com.zkrypto.zkMatch.domain.corporation.presentation;

import com.zkrypto.zkMatch.domain.corporation.application.dto.request.*;
import com.zkrypto.zkMatch.domain.corporation.application.dto.response.*;
import com.zkrypto.zkMatch.domain.corporation.application.service.CorporationService;
import com.zkrypto.zkMatch.domain.member.application.dto.response.MemberResumeResponse;
import com.zkrypto.zkMatch.domain.post.application.dto.response.CorporationPostResponse;
import com.zkrypto.zkMatch.domain.post.application.dto.request.UpdateApplierStatusCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.request.PostCreationCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.response.PostApplierResponse;
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
@RequestMapping("/corporation")
@RequiredArgsConstructor
@Tag(name = "CorporationController", description = "기업 관련 API")
public class CorporationController {

    private final CorporationService corporationService;

    @Operation(
            summary = "기업 정보 조회 API",
            description = "기업 정보를 조회 합니다.",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            },
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            description = "Bearer 토큰(ADMIN)",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공",
                    content = {@Content(schema = @Schema(implementation = CorporationResponse.class))}),
    })
    @GetMapping()
    public ApiResponse<CorporationResponse> getCorporation(@AuthenticationPrincipal UUID memberId) {
        return ApiResponse.success(corporationService.getCorporation(memberId));
    }

    @Operation(
            summary = "기업 생성 API",
            description = "기업과 기업 인사 당담자 계정을 생성 합니다."
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공",
                    content = {@Content(schema = @Schema(implementation = Void.class))}),
    })
    @PostMapping()
    public ApiResponse<Void> createCorporation(@RequestPart("corporationCreationCommand") CorporationCreationCommand corporationCreationCommand,
                                               @RequestPart(value = "registerFile", required = false) MultipartFile file) {
        corporationService.createCorporation(corporationCreationCommand, file);
        return ApiResponse.success();
    }

    @Operation(
            summary = "기업 채용 공고 조회 API",
            description = "내 기업의 채용 공고를 모두 조회 합니다.",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            },
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            description = "Bearer 토큰(ADMIN)",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = CorporationPostResponse.class)))}),
    })
    @GetMapping("/post")
    public ApiResponse<List<CorporationPostResponse>> getPost(@AuthenticationPrincipal UUID memberId, @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword){
        return ApiResponse.success(corporationService.getCorporationPost(memberId, keyword));
    }

    @Operation(
            summary = "채용 공고 생성 API",
            description = "채용 공고를 생성합니다.",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            },
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            description = "Bearer 토큰(ADMIN)",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공",
                    content = {@Content(schema = @Schema(implementation = Void.class))}),
    })
    @PostMapping("/post")
    public ApiResponse<Void> createPost(@AuthenticationPrincipal UUID memberId, @RequestBody PostCreationCommand postCreationCommand){
        corporationService.createPost(memberId, postCreationCommand);
        return ApiResponse.success();
    }

    @Operation(
            summary = "채용 공고 수정 API",
            description = "채용 공고를 수정합니다.",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            },
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            description = "Bearer 토큰(ADMIN)",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공",
                    content = {@Content(schema = @Schema(implementation = Void.class))}),
    })
    @PatchMapping("/post/{postId}")
    public ApiResponse<Void> updatePost(@PathVariable(name = "postId") String postId, @RequestBody PostUpdateCommand postUpdateCommand) {
        corporationService.updatePost(postId, postUpdateCommand);
        return ApiResponse.success();
    }

    @Operation(
            summary = "지원자 조회 API",
            description = "해당 공고의 지원자들을 불러옵니다.",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            },
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            description = "Bearer 토큰(ADMIN)",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = PostApplierResponse.class)))}),
    })
    @GetMapping("/post/{postId}")
    public ApiResponse<List<PostApplierResponse>> getPostApplier(@PathVariable(name = "postId") String postId){
        return ApiResponse.success(corporationService.getPostApplier(postId));
    }

    @Operation(
            summary = "지원자 상세 정보 조회 API",
            description = "지원자의 상세 정보를 조회합니다. (지원자 기본 정보, 공개한 이력서)",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            },
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            description = "Bearer 토큰(ADMIN)",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공",
                    content = {@Content(schema = @Schema(implementation = ApplierDetailResponse.class))}),
    })
    @GetMapping("/recruit/{recruitId}")
    public ApiResponse<ApplierDetailResponse> getApplierDetail(@PathVariable(name = "recruitId") String recruitId) {
        return ApiResponse.success(corporationService.getApplierDetail(recruitId));
    }

    @Operation(
            summary = "지원자 상태 업데이트 API",
            description = "지원자의 상태를 업데이트합니다. (합격, 불합격)",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            },
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            description = "Bearer 토큰(ADMIN)",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공",
                    content = {@Content(schema = @Schema(implementation = Void.class))}),
    })
    @PatchMapping("/recruit/{recruitId}")
    public ApiResponse<Void> updateApplierStatus(@PathVariable(name = "recruitId") String recruitId, @RequestBody UpdateApplierStatusCommand updateApplierStatusCommand){
        corporationService.updateApplierStatus(recruitId, updateApplierStatusCommand);
        return ApiResponse.success();
    }

    @Operation(
            summary = "지원자 평가 API",
            description = "지원자를 평가합니다. 평가는 합격자를 대상으로만 할 수 있습니다.",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            },
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            description = "Bearer 토큰(ADMIN)",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공",
                    content = {@Content(schema = @Schema(implementation = Void.class))}),
    })
    @PostMapping("/recruit/{recruitId}/evaluation")
    public ApiResponse<Void> evaluateApplier(@PathVariable(name = "recruitId") String recruitId, @RequestBody EvaluationCreationCommand evaluationCreationCommand) {
        corporationService.evaluateApplier(recruitId, evaluationCreationCommand);
        return ApiResponse.success();
    }

    @Operation(
            summary = "지원자 평가 조회 API",
            description = "지원자의 평가 정보를 조회합니다.",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            },
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            description = "Bearer 토큰(ADMIN)",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = EvaluationResponse.class)))}),
    })
    @GetMapping("/recruit/{recruitId}/evaluation")
    public ApiResponse<List<EvaluationResponse>> getEvaluation(@PathVariable("recruitId") String recruitId) {
        return ApiResponse.success(corporationService.getEvaluation(recruitId));
    }

    @Operation(
            summary = "면접 일정 생성 API",
            description = "면접 일정을 생성합니다.",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            },
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            description = "Bearer 토큰(ADMIN)",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공",
                    content = {@Content(schema = @Schema(implementation = Void.class))}),
    })
    @PostMapping("/recruit/{recruitId}/interview")
    public ApiResponse<Void> createInterview(@PathVariable("recruitId") String recruitId, @RequestBody InterviewCreationCommand interviewCreationCommand) {
        corporationService.createInterview(recruitId, interviewCreationCommand);
        return ApiResponse.success();
    }

    @Operation(
            summary = "면접 일정 수정 API",
            description = "면접 일정을 수정합니다.",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            },
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            description = "Bearer 토큰(ADMIN)",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공",
                    content = {@Content(schema = @Schema(implementation = Void.class))}),
    })
    @PatchMapping("/recruit/{recruitId}/interview")
    public ApiResponse<Void> updateInterview(@PathVariable("recruitId") String recruitId, @RequestBody InterviewUpdateCommand interviewUpdateCommand) {
        corporationService.updateInterview(recruitId, interviewUpdateCommand);
        return ApiResponse.success();
    }

    @Operation(
            summary = "면접 일정 조회 API",
            description = "면접 일정을 조회합니다.",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            },
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            description = "Bearer 토큰(ADMIN)",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공",
                    content = {@Content(schema = @Schema(implementation = InterviewResponse.class))}),
    })
    @GetMapping("/recruit/{recruitId}/interview")
    public ApiResponse<InterviewResponse> getInterview(@PathVariable("recruitId") String recruitId) {
        return ApiResponse.success(corporationService.getInterview(recruitId));
    }

    @Operation(
            summary = "인재 검색 API",
            description = "필터링 조건에 맞는 인재를 검색합니다.",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            },
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            description = "Bearer 토큰(ADMIN)",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공",
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = CandidateResponse.class)))}),
    })
    @GetMapping("/candidate")
    public ApiResponse<List<CandidateResponse>> searchCandidate() {
        return ApiResponse.success(corporationService.searchCandidate());
    }

    @Operation(
            summary = "채용 제안 API",
            description = "인재 검색후 채용 제안을 보냅니다.",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            },
            parameters = {
                    @Parameter(
                            in = ParameterIn.HEADER,
                            name = "Authorization",
                            description = "Bearer 토큰(ADMIN)",
                            required = true
                    )
            }
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "요청 성공",
                    content = {@Content(schema = @Schema(implementation = Void.class))}),
    })
    @PostMapping("/candidate/offer")
    public ApiResponse<Void> offerCandidate(@AuthenticationPrincipal UUID memberId, @RequestBody CandidateOfferCommand candidateOfferCommand) {
        corporationService.offerCandidate(memberId, candidateOfferCommand);
        return ApiResponse.success();
    }
}
