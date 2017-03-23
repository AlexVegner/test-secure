package com.softintouch.myapplication;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.softintouch.myapplication.util.CipherManager;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CipherManagerTest {
    @Test
    public void useAppContext() throws Exception {
        String str = "Hello world";
        String encryptedString = CipherManager.encrypt(str);
        String decryptedString = CipherManager.decrypt(encryptedString);
        assertEquals("Original string equal encrypted", str, decryptedString);
    }
}
