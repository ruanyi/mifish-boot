package com.ruanyi.mifish.model.audio;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-09-03 14:08
 */
public enum AudioTcAlgorithm {

    /**
     * aac
     */
    AAC("aac", "aac"),

    /**
     * libfdk_aac
     */
    LIBFDK_AAC("libfdk_aac", "libfdk_aac");

    /** code */
    private String code;

    /** 公司，厂商 */
    private String firm;

    /**
     * TcAlgorithm
     *
     * @param code
     * @param firm
     */
    AudioTcAlgorithm(String code, String firm) {
        this.code = code;
        this.firm = firm;
    }

    /**
     * getCode
     *
     * @return
     */
    public String getCode() {
        return code;
    }

    /**
     * getFirm
     *
     * @return
     */
    public String getFirm() {
        return firm;
    }

    /**
     * of
     *
     * @param code
     * @return
     */
    public static AudioTcAlgorithm of(String code) {
        for (AudioTcAlgorithm tcAlgorithm : values()) {
            if (tcAlgorithm.code.equals(code)) {
                return tcAlgorithm;
            }
        }
        return null;
    }
}
