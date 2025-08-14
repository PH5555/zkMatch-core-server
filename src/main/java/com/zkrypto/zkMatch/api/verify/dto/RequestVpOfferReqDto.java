package com.zkrypto.zkMatch.api.verify.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RequestVpOfferReqDto {
    private String id;
    @Schema(example = "a1b2c3d4-e5f6-7890-1234-56789abcdef0")
    @NotNull(message = "policyId cannot be null")
    private String policyId;
}