package com.zkrypto.zkMatch.api.tas.dto.response;

import com.zkrypto.zkMatch.api.tas.constant.VcPlan;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RequestVcPlanListResDto {
    private Integer count;
    private List<VcPlan> items;
}