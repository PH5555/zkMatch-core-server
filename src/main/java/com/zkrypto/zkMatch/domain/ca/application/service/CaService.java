package com.zkrypto.zkMatch.domain.ca.application.service;

import com.zkrypto.zkMatch.domain.ca.application.dto.request.ApplyConfirmCommand;

public interface CaService {
    void confirmApply(ApplyConfirmCommand command);
}
