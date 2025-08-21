package com.zkrypto.snark;

public class SNARK {
    public static native String[] generateCircuit();
    public static native String generateProof(String pkJson, String gradCredJson, String empCredJson, String licenseCredJson, String gradSchnorrPkJson, String empSchnorrPkJson, String licenseSchnorrPkJson, String gradMajor1, String gradMajor2, String gradMajor3, String gradUnivType1, String gradUnivType2, String currTime, String empPeriod, String licenseType);
    public static native boolean verify(String vkJson, String proofJson, String gradSchnorrPkJson, String empSchnorrPkJson, String licenseSchnorrPkJson, String gradMajor1, String gradMajor2, String gradMajor3, String gradUnivType1, String gradUnivType2, String currTime, String empPeriod, String licenseType);
}
