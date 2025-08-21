package com.zkrypto.signature;

public class Schnorr {
    public static native String[] generateKeys();
    public static native String generateGraduationCredential(String sk, String ci, String name, String univ, String univType, String maj, String degree, String registerNumber);
    public static native String generateEmploymentCredential(String sk, String ci, String name, String startDate, String expDate, String company, String department, String position);
    public static native String generateLicenseCredential(String sk, String ci, String name, String pid, String license, String expired);
}
