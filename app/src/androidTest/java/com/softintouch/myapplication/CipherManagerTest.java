package com.softintouch.myapplication;

import android.support.test.runner.AndroidJUnit4;
import android.util.Base64;

import com.softintouch.myapplication.util.CipherManager;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CipherManagerTest {

    /**
     * Generate a random key for AES 256 and Encrypt a random text with
     * AES 256 and encrypt the key of AES 256 with RSA 2048.
     * Then use RSA 2048 decryption to retrieve the key of AES and use
     * it to decrypt the random text. its good to show both encrypted
     * and decrypted message somewhere in simple GUI
     */
    @Test
    public void testAESwithRSA() throws Exception {
        String originalMessage = "Hello world";
        System.out.println("----Message start-----");
        System.out.println(originalMessage);
        System.out.println("----Message end-----");

        // get aes key
        String aesKey = CipherManager.getKey();
        System.out.println("---- AES key start-----");
        System.out.println(aesKey);
        System.out.println("---- AES key end-----");

        // encrypt string
        String encryptedMessage = CipherManager.encrypt(aesKey,originalMessage);
        System.out.println("---- encrypted string start -----");
        System.out.println(encryptedMessage);
        System.out.println("---- encrypted string end-----");

        // get RSA public key
        String rsaPublicKey = CipherManager.getRSAPublicKeyStr();
        System.out.println("----Public RSA key start-----");
        System.out.println(rsaPublicKey);
        System.out.println("----Public RSA key end-----");

        // encrypted key with help aes
        String encryptedAESKey = CipherManager.encryptRSA(CipherManager.getRSAPublicKey(rsaPublicKey), aesKey);
        System.out.println("----Encrypted aes key start-----");
        System.out.println(encryptedAESKey);
        System.out.println("----Encrypted aes key end-----");

        // private rsa key
        String rsaPrivetKey = CipherManager.getRSAPrivateKey();
        System.out.println("----Private RSA key start-----");
        System.out.println(rsaPrivetKey);
        System.out.println("----Private RSA KEY end-----");

        // private decrypt aes key
        String decryptedAESKey = CipherManager.decryptRSA(CipherManager.getRSAPrivateKey(rsaPrivetKey), encryptedAESKey);
        System.out.println("----Decrypted aes key start-----");
        System.out.println(decryptedAESKey);
        System.out.println("----Decrypted aes key end-----");

        // decrypt message
        String decryptedMessage = CipherManager.decrypt(decryptedAESKey, encryptedMessage);
        System.out.println("----Decrypted message start-----");
        System.out.println(decryptedMessage);
        System.out.println("----Decrypted message end-----");

        Assert.assertEquals("Original string equal encrypted", originalMessage, decryptedMessage);
    }


    @Test
    public void testAES() throws Exception {
        String str = "Hello world";
        String encryptedString = CipherManager.encrypt(CipherManager.getKey(), str);
        String decryptedString = CipherManager.decrypt(CipherManager.getKey(), encryptedString);
        assertEquals("Original string equal encrypted", str, decryptedString);
    }

    @Test
    public void testRSA() throws Exception {
        String message = "Hello world";
        String publicKey = CipherManager.getRSAPublicKeyStr();
        String privateKey = CipherManager.getRSAPrivateKey();

        String encryptedString = CipherManager.encryptRSA(CipherManager.getRSAPublicKey(publicKey), message);
        String decryptedString = CipherManager.decryptRSA(CipherManager.getRSAPrivateKey(privateKey), encryptedString);
        assertEquals("Original string equal encrypted", message, decryptedString);
    }
}
