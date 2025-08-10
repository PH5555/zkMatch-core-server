package com.zkrypto.zkMatch.global.crypto;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

@Component
public class AesService {
    @Value("${encryption.aes.key}")
    private String aesKey;
    private static String masterPassword;

    private static final int KEY_LENGTH = 256;
    private static final int ITERATIONS = 65536;
    private static final int GCM_TAG_LENGTH = 128;

    @PostConstruct
    public void init() {
        masterPassword = aesKey;
    }

    public static String encrypt(String plaintext, String userSalt) throws Exception {
        SecretKey key = deriveKey(masterPassword, userSalt);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        byte[] iv = new byte[12];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

        byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        byte[] encryptedIvAndText = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, encryptedIvAndText, 0, iv.length);
        System.arraycopy(encrypted, 0, encryptedIvAndText, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(encryptedIvAndText);
    }

    public static String decrypt(String ciphertext, String userSalt) throws Exception {
        SecretKey key = deriveKey(masterPassword, userSalt);
        byte[] decoded = Base64.getDecoder().decode(ciphertext);

        byte[] iv = new byte[12];
        byte[] encrypted = new byte[decoded.length - 12];
        System.arraycopy(decoded, 0, iv, 0, 12);
        System.arraycopy(decoded, 12, encrypted, 0, encrypted.length);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private static SecretKey deriveKey(String password, String salt) throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), Base64.getDecoder().decode(salt), ITERATIONS, KEY_LENGTH);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }
}
