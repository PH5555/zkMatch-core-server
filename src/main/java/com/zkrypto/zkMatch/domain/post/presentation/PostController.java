package com.zkrypto.zkMatch.domain.post.presentation;

import com.zkrypto.zkMatch.domain.post.application.dto.request.PostApplyCommand;
import com.zkrypto.zkMatch.domain.post.application.dto.response.ApplyQrResponse;
import com.zkrypto.zkMatch.domain.post.application.dto.response.PostResponse;
import com.zkrypto.zkMatch.domain.post.application.service.PostService;
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
@RequiredArgsConstructor
@RequestMapping("/post")
@Tag(name = "PostController", description = "공고 관련 API")
public class PostController {

    private final PostService postService;

    @Operation(
            summary = "채용 공고 조회 API",
            description = "채용 공고를 모두 조회 합니다.",
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
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = PostResponse.class)))}),
    })
    @GetMapping()
    public ApiResponse<List<PostResponse>> getPost(){
        return ApiResponse.success(postService.getPost());
    }

    @Operation(
            summary = "지원 QR 생성 API",
            description = "해당 공고에 지원하기 위한 QR을 생성합니다.",
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
                    content = {@Content(schema = @Schema(implementation = ApplyQrResponse.class))}),
    })
    @GetMapping("/{postId}")
    public ApiResponse<ApplyQrResponse> createApplyQr(@AuthenticationPrincipal UUID memberId, @PathVariable("postId") String postId) {
        return ApiResponse.success(postService.createApplyQr(memberId, postId));
    }

    @Operation(
            summary = "관심 분야 채용 공고 조회 API",
            description = "관심 분야 채용 공고를 조회 합니다.",
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
                    content = {@Content(array = @ArraySchema(schema = @Schema(implementation = PostResponse.class)))}),
    })
    @GetMapping("/interest")
    public ApiResponse<List<PostResponse>> getInterestPost(@AuthenticationPrincipal UUID memberId){
        return ApiResponse.success(postService.getInterestPost(memberId));
    }
}
