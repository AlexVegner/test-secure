package com.softintouch.myapplication.util;

import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CipherManager {

    private static final String TAG = CipherManager.class.getSimpleName();
    private static final int KEY_OFFSET = 4;
    private static final int KEY_LEN = 32; // 256 bits
    public static final byte[] IV = { 65, 1, 2, 23, 4, 5, 6, 7, 32, 21, 10, 11, 12, 13, 84, 45 };

    private static String mSignatureHash;
    private static KeyPair mRsaKey;

    public static String getKey(){
        if (mSignatureHash == null) {
            try {
                byte[] keyByte = UUID.randomUUID().toString().getBytes();
                SecretKeySpec secretKey = new SecretKeySpec(keyByte, KEY_OFFSET, KEY_LEN, "AES");
                mSignatureHash = Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);
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
    public static String encrypt(String key, String value) {
        if (value == null)
            return null;
        String encryptedValue = null;
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKey = new SecretKeySpec(Base64.decode(key, Base64.DEFAULT), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(IV));
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
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return encryptedValue;
    }

    /**
     * Decrypt string
     * @param value
     * @return encrypt String or null.
     */
    public static String decrypt(String key, String value) {
        if (value == null)
            return null;
        String decryptedValue = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKey = new SecretKeySpec(Base64.decode(key, Base64.DEFAULT), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(IV));
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
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return decryptedValue;
    }

    public static KeyPair getKeyPairRSA() {
        if (mRsaKey == null) {
            KeyPairGenerator generator = null;
            try {
                generator = KeyPairGenerator.getInstance("RSA");
                generator.initialize(2048, new SecureRandom());
                mRsaKey = generator.generateKeyPair();
                return mRsaKey;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return mRsaKey;
        }
    }

    public static String getRSAPublicKeyStr() {
        KeyPair keyPair = getKeyPairRSA();
        return Base64.encodeToString(keyPair.getPublic().getEncoded(), Base64.DEFAULT);
    }

    public static String getRSAPrivateKey() {
        KeyPair keyPair = getKeyPairRSA();
        return Base64.encodeToString(keyPair.getPrivate().getEncoded(), Base64.DEFAULT);
    }

    public static PublicKey getRSAPublicKey(String publicKeyString) {
        try {
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(publicKeyString, Base64.DEFAULT)));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PrivateKey getRSAPrivateKey(String privateKeyString) {
        try {
            return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(privateKeyString, Base64.DEFAULT)));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Encrypt string
     * @param value
     * @return encrypt String or null.
     */
    public static String encryptRSA(PublicKey publicKey, String value) {
        if (value == null)
            return null;
        String encryptedValue = null;
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            encryptedValue = Base64.encodeToString(cipher.doFinal(value.getBytes("UTF-8")), Base64.DEFAULT);
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
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encryptedValue;
    }

    /**
     * Decrypt string
     * @param value
     * @return encrypt String or null.
     */
    public static String decryptRSA(PrivateKey privateKey, String value) {
        if (value == null)
            return null;
        String decryptedValue = null;
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            decryptedValue = new String(cipher.doFinal(Base64.decode(value, Base64.DEFAULT)));
        } catch (InvalidKeyException e) {
            Log.d(TAG, "InvalidKeyException: ", e);
        } catch (IllegalBlockSizeException e) {
            Log.d(TAG, "IllegalBlockSizeException: ", e);
        } catch (BadPaddingException e) {
            Log.d(TAG, "BadPaddingException: ", e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return decryptedValue;
    }

}