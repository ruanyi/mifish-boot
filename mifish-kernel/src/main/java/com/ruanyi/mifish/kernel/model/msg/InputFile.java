package com.ruanyi.mifish.kernel.model.msg;

import com.ruanyi.mifish.common.annotation.OpenApi;
import com.ruanyi.mifish.kernel.model.storage.Credential;

import lombok.Getter;
import lombok.Setter;

/**
 * Description:
 *
 * 切勿随便挪位置，指令引擎中有使用
 *
 * @author: ruanyi
 * @Date: 2019-08-23 10:57
 */
@Getter
@Setter
@OpenApi(scene = "输入文件")
public class InputFile {

    /** 原始文件地址url */
    private String url;

    /** 构建下载的url地址 */
    private String downloadUrl;

    /** type */
    private String type;

    /** cloud */
    private String cloud;

    /** bucket */
    private String bucket;

    /** 输入文件标识 */
    private String key = "video";

    /** credential */
    private Credential credential;

    /** preDownload */
    private boolean preDownload;

    /**
     * of
     *
     * @param bucket
     * @param key
     * @return
     */
    public static final InputFile of(String bucket, String key) {
        String url = bucket + ":" + key;
        InputFile inputFile = new InputFile();
        inputFile.url = url;
        return inputFile;
    }

    /**
     * video
     *
     * @param bucket
     * @param key
     * @return
     */
    public static final InputFile video(String bucket, String key) {
        return name(bucket, key, "video");
    }

    /**
     * audio
     *
     * @param bucket
     * @param key
     * @return
     */
    public static final InputFile audio(String bucket, String key) {
        return name(bucket, key, "audio");
    }

    /**
     * name
     *
     * @param bucket
     * @param key
     * @param name
     * @return
     */
    public static final InputFile name(String bucket, String key, String name) {
        String url = bucket + ":" + key;
        InputFile inputFile = new InputFile();
        inputFile.url = url;
        inputFile.key = name;
        return inputFile;
    }
}
