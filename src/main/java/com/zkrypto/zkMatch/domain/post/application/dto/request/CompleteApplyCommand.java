package com.zkrypto.zkMatch.domain.post.application.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class CompleteApplyCommand {
    private List<Long> openedResumes;
}
