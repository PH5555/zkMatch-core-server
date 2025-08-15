package com.zkrypto.zkMatch.domain.post.application.dto.request;

import com.zkrypto.zkMatch.domain.recruit.domain.constant.Status;
import lombok.Getter;

@Getter
public class UpdateApplierStatusCommand {
    private Status status;
}
