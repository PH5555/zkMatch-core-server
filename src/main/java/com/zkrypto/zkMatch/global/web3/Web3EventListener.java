package com.zkrypto.zkMatch.global.web3;

import com.zkrypto.zkMatch.global.response.exception.CustomException;
import com.zkrypto.zkMatch.global.response.exception.ErrorCode;
import com.zkrypto.zkMatch.global.web3.dto.ApplicationEventDto;
import com.zkrypto.zkMatch.global.web3.dto.SubmitResumeEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Web3EventListener {
    private final Web3Service web3Service;

    @EventListener
    @Async
    public void process(ApplicationEventDto event) {
        ApplicationContract applicationContract = web3Service.loadContract();

        try {
            applicationContract.submitApplication(web3Service.keccak256(event.getUserId()), web3Service.keccak256(event.getPostId()), event.getProof()).send();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAILED_APPLY_BLOCKCHAIN);
        }
    }

    @EventListener
    @Async
    public void process(SubmitResumeEventDto event) {
        ApplicationContract applicationContract = web3Service.loadContract();

        try {
            applicationContract.submitResume(web3Service.keccak256(event.getUserId()), web3Service.keccak256(event.getResumeId()), web3Service.keccak256(event.getData())).send();
        } catch (Exception e) {
            throw new CustomException(ErrorCode.FAILED_APPLY_BLOCKCHAIN);
        }
    }
}
