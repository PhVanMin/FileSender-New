package com.example.xender.Utils;

import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionUtils {

    // This method encrypts the input data using AES algorithm
    public static byte[] encrypt(String input, String keyString) throws Exception {
        SecretKey key = generateKey(keyString);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
    }

    // This method decrypts the input data using AES algorithm
    public static byte[] decrypt(String input, String keyString) throws Exception {
        SecretKey key = generateKey(keyString);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedBytes = Base64.decode(input, Base64.DEFAULT);
        return cipher.doFinal(encryptedBytes);
    }

    // This method generates a SecretKey from the given keyString
    private static SecretKey generateKey(String keyString) throws Exception {
        byte[] keyData = keyString.getBytes(StandardCharsets.UTF_8);
        return new SecretKeySpec(keyData, "AES");
    }
//
//    public void Test() {
//        try {
//            String originalData = "This is some data to encrypt.";
//            String secretKey = "YourSecretKeyHere";
//
//            // Encrypt data
//            byte[] encryptedBytes = EncryptionUtils.encrypt(originalData, secretKey);
//            String encryptedData = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
//            Log.d("Encrypted Data:", encryptedData);
////            System.out.println(encryptedData);
//            // Decrypt data
//            byte[] decryptedBytes = EncryptionUtils.decrypt(encryptedData, secretKey);
//            String decryptedData = Base64.encodeToString(decryptedBytes, Base64.DEFAULT);
//            Log.d("Decrypted Data:", decryptedData);
////            System.out.println(decryptedData);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
