package com.ruanyi.mifish.model.video;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2023-09-03 14:08
 */
public enum VideoTcAlgorithm {

    /**
     * 微帧264算法，
     */
    WZ_264("wz_264", "微帧科技"),

    /**
     * 微帧265算法，
     */
    WZ_265("wz_265", "微帧科技"),

    /**
     * mpc 265算法，
     */
    MPC_265("mpc_265", "华为"),

    /**
     * mpc 264算法，
     */
    MPC_264("mpc_265", "华为"),

    /**
     * 视俊 265算法，
     */
    SJ_265("sj_265", "视俊"),

    /**
     * 开源 x264算法，
     */
    X264("x264", "社区开源"),
    /**
     * 开源 x265算法，
     */
    X265("x265", "社区开源");

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
    VideoTcAlgorithm(String code, String firm) {
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
    public static VideoTcAlgorithm of(String code) {
        for (VideoTcAlgorithm tcAlgorithm : values()) {
            if (tcAlgorithm.code.equals(code)) {
                return tcAlgorithm;
            }
        }
        return null;
    }
}
