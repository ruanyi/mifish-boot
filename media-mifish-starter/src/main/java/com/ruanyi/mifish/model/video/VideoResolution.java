package com.ruanyi.mifish.model.video;

/**
 * Description:
 *
 * @author: rls
 * @Date: 2023-09-02 22:12
 */
public enum VideoResolution {

    /** 视频分辨率为：unknow */
    unknow("unknow", "*x*", 0, -1),

    /** 视频分辨率为：480p */
    _480p("480p", "400x711", 480, 2500 * 1000),

    /** _540p x */
    _540p("540p", "540x960", 540, 2500 * 1000),

    _720p("720p", "720x1280", 720, 3000 * 1000),

    _1080p("1080p", "1080x1920", 1080, 5000 * 1000),

    _2k("2k", "1440x2560", 1440, 10000 * 1000),

    _4k("4k", "2160x4096", 2160, 20000 * 1000),

    /** 比4K还要大，例如：8k等等 */
    bigger_4k(">4k", "", 4096, 20000 * 1000);

    /** name */
    private String name;

    /** wh */
    private String wh;

    /** maxShortEdge */
    private int maxShortEdge;

    /** 推荐码率 */
    private int bitrate;

    /**
     * VideoResolution
     *
     * @param name
     * @param wh
     * @param maxShortEdge
     * @param bitrate
     */
    VideoResolution(String name, String wh, int maxShortEdge, int bitrate) {
        this.name = name;
        this.wh = wh;
        this.maxShortEdge = maxShortEdge;
        this.bitrate = bitrate;
    }

    /**
     * getName
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * getWh
     * 
     * @return
     */
    public String getWh() {
        return wh;
    }

    /**
     * getMaxShortEdge
     * 
     * @return
     */
    public int getMaxShortEdge() {
        return maxShortEdge;
    }

    /**
     * getBitrate
     * 
     * @return
     */
    public int getBitrate() {
        return bitrate;
    }
}
