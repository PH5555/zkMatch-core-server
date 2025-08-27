package com.zkrypto.zkMatch.domain.ca.application.service;

import com.zkrypto.zkMatch.domain.application.domain.entity.Application;
import com.zkrypto.zkMatch.domain.application.domain.repository.ApplicationRepository;
import com.zkrypto.zkMatch.domain.ca.application.dto.request.ApplyConfirmCommand;
import com.zkrypto.zkMatch.domain.post.application.service.PostService;
import com.zkrypto.zkMatch.domain.resume.domain.entity.Resume;
import com.zkrypto.zkMatch.domain.resume.domain.repository.ResumeRepository;
import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Primary
@Service
@RequiredArgsConstructor
public class DefaultCaServiceImpl implements CaService{
    private final ApplicationRepository applicationRepository;
    private final PostService postService;
    private final ResumeRepository resumeRepository;

    @Override
    @Transactional
    public void confirmApply(ApplyConfirmCommand command) {
        // 지원 정보 조회
        Application application = applicationRepository.findById(Long.parseLong(command.getApplicationId()))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_APPLICATION));

        // 이력서 조회
        List<Resume> encResumes = resumeRepository.getResumesByMember(application.getMember());

        // 지원조건 확인
        Boolean applyCondition = postService.checkApplyCondition(encResumes, application.getMember(), application.getPost());

        if(applyCondition) {
            application.success();
        }
    }
}
