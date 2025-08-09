package com.zkrypto.zkMatch.domain.corporation.presentation;

import com.zkrypto.zkMatch.domain.corporation.application.dto.request.*;
import com.zkrypto.zkMatch.domain.corporation.application.dto.response.CorporationResponse;
import com.zkrypto.zkMatch.domain.corporation.application.service.CorporationService;
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
                                               @RequestPart(value = "registerFile", required = false) MultipartFile file) throws IOException {
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
    //TODO : 검색 키워드 추가
    @GetMapping("/post")
    public ApiResponse<List<CorporationPostResponse>> getPost(@AuthenticationPrincipal UUID memberId){
        return ApiResponse.success(corporationService.getCorporationPost(memberId));
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
    @PatchMapping("/post")
    public ApiResponse<Void> updatePost(@RequestBody PostUpdateCommand postUpdateCommand) {
        corporationService.updatePost(postUpdateCommand);
        return ApiResponse.success();
    }

    @Operation(
            summary = "채용 공고 상세 API",
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

    // TODO: 지원자 상세 정보
    @GetMapping("/recruit")
    public ApiResponse<List<PostApplierResponse>> getPostApplier(@PathVariable(name = "postId") String postId,
                                                                 @PathVariable(name = "recruitId") String recruitId) {
        return ApiResponse.success(corporationService.getPostApplier(postId));
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
    @PutMapping("/recruit")
    public ApiResponse<Void> updateApplierStatus(@RequestBody UpdateApplierStatusCommand updateApplierStatusCommand){
        corporationService.updateApplierStatus(updateApplierStatusCommand);
        return ApiResponse.success();
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
    @PostMapping("/recruit/interview")
    public ApiResponse<Void> createInterview(@RequestBody InterviewCreationCommand interviewCreationCommand) {
        corporationService.createInterview(interviewCreationCommand);
        return ApiResponse.success();
    }

    //TODO
    @PatchMapping("/recruit/interview")
    public ApiResponse<Void> updateInterview(@RequestBody InterviewUpdateCommand interviewUpdateCommand) {
        corporationService.updateInterview(interviewUpdateCommand);
        return ApiResponse.success();
    }

    //TODO
    @PostMapping("/recruit/evaluate")
    public ApiResponse<Void> evaluateApplier(@RequestBody EvaluationCreationCommand interviewCreationCommand) {
        return ApiResponse.success();
    }

    //TODO
    @PostMapping("/candidate/search")
    public ApiResponse<Void> searchCandidate(@RequestBody CandidateSearchCommand candidateSearchCommand) {
        return ApiResponse.success();
    }

    //TODO
    @PostMapping("/candidate/offer")
    public ApiResponse<Void> offerCandidate(@RequestBody CandidateOfferCommand candidateSearchCommand) {
        return ApiResponse.success();
    }
}
