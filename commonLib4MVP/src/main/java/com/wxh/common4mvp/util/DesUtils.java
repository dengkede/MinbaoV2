package com.wxh.common4mvp.util;

import android.util.Base64;

import java.io.IOException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * DES方式加密解密统一管理工具类
 */

public class DesUtils {
    /* 加密类 */
    private Cipher ecipher;
    /* 解密类 */
    private Cipher dcipher;

    private static DesUtils instance = null;

    public static DesUtils getInstance() {
        if (instance == null)
            throw new RuntimeException("you should call init method before getInstance");
        return instance;
    }

    public static void init(String passPhrase) {
        if (instance == null) {
            synchronized (DesUtils.class) {
                instance = new DesUtils(passPhrase);
            }
        }
    }

    private DesUtils(String passPhrase) {
        try {
            // Create the key，"tmpqwuew"为初始化密文
//            String passPhrase = "tmpqwuew";
            /* 生成秘钥 */
            KeySpec keySpec = new DESKeySpec(passPhrase.getBytes("utf-8"));
            SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
            // SecretKeySpec key = new
            // SecretKeySpec(passPhrase.getBytes(),"DES");
            /* 初始化加解密实例 */
            ecipher = Cipher.getInstance(key.getAlgorithm());
            dcipher = Cipher.getInstance(key.getAlgorithm());

            // Prepare the parameter to the ciphers
            // AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
            // iterationCount);
            // Create the ciphers
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 对字符串加密
     *
     * @param source String 要加密的字符串
     * @return byte[] 已加密的字节
     */
    public String encrypt(String source) {
        try {
            byte[] enc = ecipher.doFinal(source.getBytes("utf-8"));
            return new String(Base64.encode(enc, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 对字节数组解密
     *
     * @return String
     * @throws IOException
     */
    public String decrypt(String result) {
        try {
            byte[] buf = Base64.decode(result.getBytes("utf-8"), Base64.DEFAULT);
            byte[] utf8 = dcipher.doFinal(buf);
            return new String(utf8);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
