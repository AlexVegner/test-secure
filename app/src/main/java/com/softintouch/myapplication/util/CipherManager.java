package com.softintouch.myapplication.util;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class CipherManager {

    private static final String TAG = CipherManager.class.getSimpleName();

    private static byte[] mSignatureHash;

    private static byte[] getKey(){
        if (mSignatureHash == null) {
            try {
                mSignatureHash = md5(UUID.randomUUID().toString().getBytes());
            } catch (Exception e) {
                Log.d(TAG, "Can't het hash: ", e);
            }
        }
        return mSignatureHash;
    }

    /**
     * Encrypt string
     * @param value
     * @return encrypt String or null.
     */
    public static String encrypt(String value) {
        if (value == null)
            return null;
        String encryptedValue = null;
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKey = new SecretKeySpec(getKey(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedValue = Base64.encodeToString(cipher.doFinal(value.getBytes()), Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, "NoSuchAlgorithmException: ", e);
        } catch (InvalidKeyException e) {
            Log.d(TAG, "InvalidKeyException: ", e);
        } catch (NoSuchPaddingException e) {
            Log.d(TAG, "NoSuchPaddingException: ", e);
        } catch (IllegalBlockSizeException e) {
            Log.d(TAG, "IllegalBlockSizeException: ", e);
        } catch (BadPaddingException e) {
            Log.d(TAG, "BadPaddingException: ", e);
        }
        return encryptedValue;
    }


    /**
     * Get md5
     * @param bytesOfMessage
     * @return
     */
    private static byte[] md5(byte[] bytesOfMessage){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
            return md.digest(bytesOfMessage);
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, "NoSuchAlgorithmException: ", e);
        }
        return null;
    }

    /**
     * Decrypt string
     * @param value
     * @return encrypt String or null.
     */
    public static String decrypt(String value) {
        if (value == null)
            return null;
        String decryptedValue = null;
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKeySpec secretKey = new SecretKeySpec(getKey(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decryptedValue = new String(cipher.doFinal(Base64.decode(value.getBytes(), Base64.DEFAULT)));
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, "NoSuchAlgorithmException: ", e);
        } catch (InvalidKeyException e) {
            Log.d(TAG, "InvalidKeyException: ", e);
        } catch (NoSuchPaddingException e) {
            Log.d(TAG, "NoSuchPaddingException: ", e);
        } catch (IllegalBlockSizeException e) {
            Log.d(TAG, "IllegalBlockSizeException: ", e);
        } catch (BadPaddingException e) {
            Log.d(TAG, "BadPaddingException: ", e);
        }
        return decryptedValue;
    }
}