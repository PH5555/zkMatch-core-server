package com.zkrypto.zkMatch.domain.ca.application.service;

import com.zkrypto.zkMatch.domain.application.domain.repository.ApplicationRepository;
import com.zkrypto.zkMatch.domain.ca.application.dto.request.ApplyConfirmCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
@RequiredArgsConstructor
public class DefaultCaServiceImpl implements CaService{
    private final ApplicationRepository applicationRepository;

    @Override
    public void confirmApply(ApplyConfirmCommand command) {

    }
}
