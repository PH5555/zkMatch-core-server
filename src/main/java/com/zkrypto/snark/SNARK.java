package com.zkrypto.snark;

public class SNARK {
    public static native String generateGraduationCredential(String ci, String name, String univ, String univType, String maj, String degree, String registerNumber);
    public static native String generateEmploymentCredential(String ci, String name, String startDate, String expDate, String company, String department, String position);
    public static native String generateLicenseCredential(String ci, String name, String pid, String license, String expired);
    public static native String[] generateCircuit();
    public static native String generateProof(String pkJson, String gradCredJson, String empCredJson, String licenseCredJson, String gradMajor1, String gradMajor2, String gradMajor3, String gradUnivType1, String gradUnivType2, String currTime, String empPeriod, String licenseType);
    public static native boolean verify(String vkJson, String proofJson, String gradMajor1, String gradMajor2, String gradMajor3, String gradUnivType1, String gradUnivType2, String currTime, String empPeriod, String licenseType);
}