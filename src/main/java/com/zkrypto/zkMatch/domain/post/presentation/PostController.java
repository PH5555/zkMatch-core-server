package com.zkrypto.zkMatch.domain.post.presentation;

import com.zkrypto.zkMatch.domain.post.application.dto.request.PostApplyCommand;
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
            summary = "즉시 지원 API",
            description = "해당 공고에 지원합니다.",
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
    @PutMapping()
    public ApiResponse<Void> applyPost(@AuthenticationPrincipal UUID memberId, @RequestBody PostApplyCommand postApplyCommand) {
        postService.applyPost(memberId, postApplyCommand);
        return ApiResponse.success();
    }

    //TODO: 관심 분야 공고
}
