package com.zkrypto.zkMatch.global.web3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class Web3Service {
    @Value("${omnione.privateKey}")
    private String privateKey;

    @Value("${omnione.contractAddress}")
    private String contractAddress;

    private final Web3j web3j;

    public ApplicationContract loadContract() {
        Credentials credentials = Credentials.create(privateKey);
        BigInteger gasPrice = BigInteger.valueOf(0L);
        BigInteger gasLimit = BigInteger.valueOf(3000000000L);
        StaticGasProvider gasProvider = new StaticGasProvider(gasPrice, gasLimit);
        return ApplicationContract.load(
                contractAddress,
                web3j,
                credentials,
                gasProvider
        );
    }

    public byte[] keccak256(String s) {
        byte[] bytes = s.getBytes();
        return Hash.sha3(bytes);
    }
}
