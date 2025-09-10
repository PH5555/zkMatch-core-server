package com.zkrypto.zkMatch.global.web3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;

@Service
@RequiredArgsConstructor
public class Web3Service {
    @Value("${omnione.privateKey}")
    private String privateKey;

    @Value("${omnione.contractAddress}")
    private String contractAddress;

    private final Web3j web3j;
    private final ContractGasProvider contractGasProvider;

    public ApplicationContract loadContract() {
        Credentials credentials = Credentials.create(privateKey);
        return ApplicationContract.load(
                contractAddress,
                web3j,
                credentials,
                contractGasProvider
        );
    }
}
