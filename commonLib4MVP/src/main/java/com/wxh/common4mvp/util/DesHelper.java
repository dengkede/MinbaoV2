package com.wxh.common4mvp.util;

import android.util.Base64;

import com.wxh.common4mvp.base.BaseConfig;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class DesHelper {

    private final static String DES = "DES";
    private final static String ENCODE = "UTF-8";
    private final static String defaultKey = BaseConfig.DES_PHRASE_KEY;

    /**
     * 使用 默认key 加密
     *
     * @return String
     */
    public static String encrypt(String data) {
        byte[] bt = new byte[0];
        try {
            bt = decrypt(data.getBytes(ENCODE), defaultKey.getBytes(ENCODE), Cipher.ENCRYPT_MODE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Base64.encodeToString(bt, Base64.DEFAULT);
    }

    /**
     * 使用 默认key 解密
     *
     * @return String
     */
    public static String decrypt(String data) {
        if (StringUtils.isEmpty(data)) {
            return null;
        }
        byte[] bt = new byte[0];
        try {
            byte[] buf = Base64.decode(data, Base64.DEFAULT);
            bt = decrypt(buf, defaultKey.getBytes(ENCODE), Cipher.DECRYPT_MODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(bt);
    }


    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key        加密键byte数组
     * @param cipherMode 解密  Cipher.DECRYPT_MODE  加密 Cipher.ENCRYPT_MODE
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key, int cipherMode) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);

        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey secretkey = keyFactory.generateSecret(dks);

        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);

        // 用密钥初始化Cipher对象
        cipher.init(cipherMode, secretkey, sr);

        return cipher.doFinal(data);
    }
}