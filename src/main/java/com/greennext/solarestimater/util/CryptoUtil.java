package com.greennext.solarestimater.util;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.Key;
import java.security.MessageDigest;

@Component
public class CryptoUtil {

    /**
     * SHA-1 Abstract algorithm.
     * @param by byte array to hash
     * @return lowercase hexadecimal string of SHA-1 hash
     */
    public static String sha1ToLowerCase(byte[] by) {
        try {
            byte dat[] = MessageDigest.getInstance("SHA-1").digest(by);
            return byte2HexStr(dat, 0, dat.length).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * RC4 encryption.
     * @param key encryption key
     * @param org original data
     * @param ofst offset in original data
     * @param len length of data to encrypt
     * @return encrypted byte array
     */
    public static byte[] rc4enc(byte key[], byte[] org, int ofst, int len) {
        try {
            Key k = new SecretKeySpec(key, "RC4");
            Cipher cip = Cipher.getInstance("RC4");
            cip.init(Cipher.ENCRYPT_MODE, k);
            return cip.doFinal(org, ofst, len);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Change byte flow to hexadecimal string (compact format, no spaces).
     */
    private static String byte2HexStr(byte by[], int ofst, int len) {
        if (len < 1)
            return "";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bos);
        for (int i = ofst; i < ofst + len; i++)
            ps.printf("%02X", by[i]);
        return bos.toString();
    }
}
