package com.zkrypto.zkMatch.global.web3;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicStruct;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple5;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/LFDT-web3j/web3j/tree/main/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.7.0.
 */
@SuppressWarnings("rawtypes")
public class ApplicationContract extends Contract {
    public static final String BINARY = "Bin file was not provided";

    public static final String FUNC_APPLICATIONCOUNT = "applicationCount";

    public static final String FUNC_APPLICATIONS = "applications";

    public static final String FUNC_GETAPPLICATIONBYID = "getApplicationById";

    public static final String FUNC_HASAPPLIED = "hasApplied";

    public static final String FUNC_SUBMITAPPLICATION = "submitApplication";

    @Deprecated
    protected ApplicationContract(String contractAddress, Web3j web3j, Credentials credentials,
            BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected ApplicationContract(String contractAddress, Web3j web3j, Credentials credentials,
            ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected ApplicationContract(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected ApplicationContract(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<BigInteger> applicationCount() {
        final Function function = new Function(FUNC_APPLICATIONCOUNT, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple5<BigInteger, byte[], byte[], String, BigInteger>> applications(
            BigInteger param0) {
        final Function function = new Function(FUNC_APPLICATIONS, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple5<BigInteger, byte[], byte[], String, BigInteger>>(function,
                new Callable<Tuple5<BigInteger, byte[], byte[], String, BigInteger>>() {
                    @Override
                    public Tuple5<BigInteger, byte[], byte[], String, BigInteger> call() throws
                            Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<BigInteger, byte[], byte[], String, BigInteger>(
                                (BigInteger) results.get(0).getValue(), 
                                (byte[]) results.get(1).getValue(), 
                                (byte[]) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Application> getApplicationById(BigInteger _id) {
        final Function function = new Function(FUNC_GETAPPLICATIONBYID, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_id)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Application>() {}));
        return executeRemoteCallSingleValueReturn(function, Application.class);
    }

    public RemoteFunctionCall<Boolean> hasApplied(byte[] param0, byte[] param1) {
        final Function function = new Function(FUNC_HASAPPLIED, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(param0), 
                new org.web3j.abi.datatypes.generated.Bytes32(param1)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> submitApplication(byte[] _userId, byte[] _postId,
            String _proof) {
        final Function function = new Function(
                FUNC_SUBMITAPPLICATION, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(_userId), 
                new org.web3j.abi.datatypes.generated.Bytes32(_postId), 
                new org.web3j.abi.datatypes.Utf8String(_proof)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static ApplicationContract load(String contractAddress, Web3j web3j,
            Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ApplicationContract(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static ApplicationContract load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ApplicationContract(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static ApplicationContract load(String contractAddress, Web3j web3j,
            Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ApplicationContract(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static ApplicationContract load(String contractAddress, Web3j web3j,
            TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ApplicationContract(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static class Application extends DynamicStruct {
        public BigInteger id;

        public byte[] userId;

        public byte[] postId;

        public String proof;

        public BigInteger timestamp;

        public Application(BigInteger id, byte[] userId, byte[] postId, String proof,
                BigInteger timestamp) {
            super(new org.web3j.abi.datatypes.generated.Uint256(id), 
                    new org.web3j.abi.datatypes.generated.Bytes32(userId), 
                    new org.web3j.abi.datatypes.generated.Bytes32(postId), 
                    new org.web3j.abi.datatypes.Utf8String(proof), 
                    new org.web3j.abi.datatypes.generated.Uint256(timestamp));
            this.id = id;
            this.userId = userId;
            this.postId = postId;
            this.proof = proof;
            this.timestamp = timestamp;
        }

        public Application(Uint256 id, Bytes32 userId, Bytes32 postId, Utf8String proof,
                Uint256 timestamp) {
            super(id, userId, postId, proof, timestamp);
            this.id = id.getValue();
            this.userId = userId.getValue();
            this.postId = postId.getValue();
            this.proof = proof.getValue();
            this.timestamp = timestamp.getValue();
        }
    }
}
