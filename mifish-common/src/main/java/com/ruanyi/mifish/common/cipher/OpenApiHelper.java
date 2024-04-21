package com.ruanyi.mifish.common.cipher;

import java.util.Date;
import java.util.Random;

import com.ruanyi.mifish.common.utils.UUIDUtil;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-04-21 16:43
 */
public final class OpenApiHelper {

    private static final String DIGIT = "0123456789";

    private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";

    private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * obtainRandomCode
     *
     * 获取指定长度的随机字符串
     *
     * 0：数字 <br/>
     * 
     * 1：小写字母
     * 
     * 2：大写字母
     * 
     * 3：小写字母+ 大写字母 + 数字混合
     * 
     * 4：32位的UUID
     * 
     * @param type
     * @param len
     * @return
     */
    private static String obtainRandomCode(int type, int len) {
        StringBuilder builder = null;
        StringBuilder sb = new StringBuilder();
        switch (type) {
            case 0:
                builder = new StringBuilder(DIGIT);
                break;
            case 1:
                builder = new StringBuilder(LOWERCASE_LETTERS);
                break;
            case 2:
                builder = new StringBuilder(UPPERCASE_LETTERS);
                break;
            case 3:
                builder = new StringBuilder(DIGIT);
                builder.append(LOWERCASE_LETTERS);
                builder.append(UPPERCASE_LETTERS);
                break;
            case 4:
            default:
                sb.append(UUIDUtil.obtainUUID());
                break;
        }
        Random random = new Random();
        random.setSeed(new Date().getTime());
        if (type == 0 || type == 1 || type == 2 || type == 3) {
            int size = builder.length();
            for (int i = 0; i < len; i++) {
                sb.append(builder.charAt(random.nextInt(size)));
            }
        }
        return sb.toString();
    }

    /**
     * 生成指定长度的api key
     * 
     * @param len
     * @return
     */
    public static final String generateApiKey(int len) {
        return obtainRandomCode(3, len);
    }

    /**
     * generateApiSecret
     * 
     * @param len
     * @return
     */
    public static final String generateApiSecret(int len) {
        return obtainRandomCode(3, len);
    }

    /**
     * generate32ApiKey
     * 
     * @return
     */
    public static final String generate32ApiKey() {
        return obtainRandomCode(4, 32);
    }

    /**
     * generate32ApiSecret
     * 
     * @return
     */
    public static final String generate32ApiSecret() {
        return obtainRandomCode(4, 32);
    }
}
