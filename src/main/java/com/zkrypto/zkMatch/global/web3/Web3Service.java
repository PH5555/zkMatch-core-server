package com.zkrypto.zkMatch.global.web3;

import org.web3j.crypto.Hash;

public class Web3Service {
    public String keccak256(String s) {
        return Hash.sha3String(s);
    }
}
