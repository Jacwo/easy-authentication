package org.apereo.cas.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Random;

/**
 * @Author yangyuanliang
 * @Date 2024/4/8 10:47
 * @Version 2.0
 **/
@Slf4j
public class AESUtil {
    private static final String[] strings = {
            "iPHtqot014ZhiQn6oP5xcw==",
            "eHlDOI0VRCLcNoLXfgJOyQ==",
            "hz/gCMdUIwkABYwSC1fvLQ==",
            "MoeJ5/QDeqBaXHGMR9A1dg==",
            "huNJDl5PwJoIEuo2ds8y9g==",
            "ZSIeBjRGhPdJWqvamhupFg==",
            "aYqrK+l4EM4cqJ+7IbTe4Q==",
            "pnOSNdKDnADjwY+Sc9KVXw==",
            "QwjGJXuyxqUJi0ZxZlqqgA==",
            "1mcPSH/ZqOj0jk2wqciiwg==",
            "Qvy2L55c2aTCHUc6uX/Llw==",
            "YnHmFS0kCcjy4oAtEtyxvg==",
            "zWC9gZm+MKdxkZP6Sitlvg==",
            "4ZLNw6MlEiLAc0Kc7NnRdA==",
            "LQS0IvRc8DPvGU+/wgZ8/g==",
            "06JaVL/NIU69dRAwGFeDqw==",
            "d9XYfl9siLfxBU+Ze1LxSQ==",
            "MfgtG9wGJ27Vn/U7ibDTUg==",
            "fsKdt8Vpq8BtvoaKi2/5aw==",
            "Kq+Qa+P0ntM9kuU/52V6DQ=="
    };

    // 创建随机数生成器
    private static final Random random = new Random();

    // 定义方法来随机返回一个字符串
    public static String getRandomString() {
        // 生成一个 0 到数组长度之间的随机索引
        int randomIndex = random.nextInt(strings.length);
        // 返回对应随机索引的字符串
        return strings[randomIndex];
    }

    public static synchronized byte[] generatorKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static synchronized String generatorKeyString() throws NoSuchAlgorithmException {
        // byte[] key = generatorKey();
        return getRandomString();
    }

    public static synchronized String encrypt(String strToEncrypt, String secret) {
        try {
            byte[] key = Base64.getDecoder().decode(secret);
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            log.error("Error while encrypting: " + e.toString());
        }
        return null;
    }


    public static synchronized String decrypt(String strToDecrypt, String secret) {
        try {
            byte[] key = Base64.getDecoder().decode(secret);
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] dectyptByte = Base64.getDecoder().decode(strToDecrypt.getBytes("UTF-8"));
            String decStr = new String(cipher.doFinal(dectyptByte), "UTF-8");
            return decStr;
        } catch (Exception e) {
            log.error("Error while decrypting: " + e.toString());
        }
        return null;
    }
}
