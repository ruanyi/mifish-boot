package com.ruanyi.mifish.kernel.model.info;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.common.utils.JacksonUtils;
import com.ruanyi.mifish.kernel.model.MediaType;

/**
 * Description:
 * <p>
 * 描述视频的avinfo信息
 *
 * @author: ruanyi
 * @Date: 2019-09-24 14:41
 */
public final class AvInfo extends MetaInfo {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.framework;

    public static final String STREAMS_KEY = "streams";

    public static final String FORMAT_KEY = "format";

    /**
     * 不允许自主实例化
     *
     * 两个构造函数，只能调用其中一个 <br>
     *
     * 计算失败时，调用这个构造函数
     *
     * @param code
     * @param message
     */
    private AvInfo(String code, String message) {
        super(code, message);
    }

    /**
     * 不允许自主实例化
     *
     * 两个构造函数，只能调用其中一个 <br>
     * 
     * 计算失败时，调用这个构造函数
     * 
     */
    private AvInfo(String avInfo) {
        super(avInfo);
    }

    /**
     * getAvInfo
     *
     * @return
     */
    public String getAvInfo() {
        return super.getMediaInfo();
    }

    /**
     * getFormatName
     * 
     * @return
     */
    public String getFormatName() {
        try {
            return getFormatInfo("format_name") + "";
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "AvInfo"), Pair.of("method", "getFormatName"),
                Pair.of("format_name", "exception"), Pair.of("avInfo", getAvInfo()));
        }
        return null;
    }

    /**
     * getVideoCodecName
     *
     * @return
     */
    public String getVideoCodecName() {
        try {
            Map<String, Object> videoS = getVideoStream();
            if (!videoS.isEmpty()) {
                return videoS.get("codec_name") + "";
            }
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "AvInfo"), Pair.of("method", "getVideoCodecName"),
                Pair.of("codec_name", "exception"), Pair.of("avInfo", getAvInfo()));
        }
        return null;
    }

    /**
     * getPixFmt
     * 
     * @return
     */
    public String getPixFmt() {
        try {
            Map<String, Object> videoS = getVideoStream();
            if (!videoS.isEmpty()) {
                return videoS.get("pix_fmt") + "";
            }
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "AvInfo"), Pair.of("method", "getPixFmt"),
                Pair.of("codec_name", "exception"), Pair.of("avInfo", getAvInfo()));
        }
        return null;
    }

    /**
     * getShortEdge
     *
     * @return
     */
    public double getShortEdge() {
        try {
            Map<String, Object> videoS = getVideoStream();
            if (videoS != null && !videoS.isEmpty()) {
                Double w = Double.parseDouble(videoS.get("width") + "");
                Double h = Double.parseDouble(videoS.get("height") + "");
                if (w != null && h != null) {
                    return Math.min(w, h);
                }
            }
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "AvInfo"), Pair.of("method", "getShortEdge"),
                Pair.of("short_edge_status", "exception"), Pair.of("avInfo", getAvInfo()));
        }
        return -1d;
    }

    /**
     * getVideoWidth
     *
     * @return
     */
    public Integer getVideoWidth() {
        try {
            Map<String, Object> videoS = getVideoStream();
            if (videoS != null && !videoS.isEmpty()) {
                Integer w = Integer.parseInt(videoS.get("width") + "");
                return w;
            }
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "AvInfo"), Pair.of("method", "getVideoWidth"),
                Pair.of("video_width_status", "exception"), Pair.of("avInfo", getAvInfo()));
        }
        return -1;
    }

    /**
     * getVideoHeight
     *
     * @return
     */
    public Integer getVideoHeight() {
        try {
            Map<String, Object> videoS = getVideoStream();
            if (videoS != null && !videoS.isEmpty()) {
                Integer h = Integer.parseInt(videoS.get("height") + "");
                return h;
            }
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "AvInfo"), Pair.of("method", "getVideoHeight"),
                Pair.of("video_height_status", "exception"), Pair.of("avInfo", getAvInfo()));
        }
        return -1;
    }

    /**
     * getPrimaries
     * 
     * @return
     */
    public String getPrimaries() {
        try {
            Map<String, Object> videoS = getVideoStream();
            if (videoS != null && !videoS.isEmpty()) {
                return videoS.get("color_primaries") + "";
            }
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "AvInfo"), Pair.of("method", "getPrimaries"),
                Pair.of("video_primaries_status", "exception"), Pair.of("avInfo", getAvInfo()));
        }
        return null;
    }

    /**
     * getNbFrames
     * 
     * @return
     */
    public int getNbFrames() {
        try {
            Map<String, Object> videoS = getVideoStream();
            if (videoS != null && !videoS.isEmpty()) {
                String framesStr = videoS.get("nb_frames") + "";
                return Integer.parseInt(framesStr);
            }
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "AvInfo"), Pair.of("getNbFrames", "getPrimaries"),
                Pair.of("video_nb_frames_status", "exception"), Pair.of("avInfo", getAvInfo()));
        }
        return -1;
    }

    /**
     * getFileSize
     *
     * @return
     */
    public String getFileSize() {
        try {
            Map<String, Object> format = this.getFormat();
            if (format != null && !format.isEmpty()) {
                Object v = format.get("size");
                return v + "";
            }
            return null;
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "AvInfo"), Pair.of("method", "getFileSize"),
                Pair.of("file_size_status", "exception"), Pair.of("avInfo", getAvInfo()));
            return null;
        }
    }

    /**
     * getDuration
     *
     * @return
     */
    public String getDuration() {
        try {
            Map<String, Object> format = this.getFormat();
            if (format != null && !format.isEmpty()) {
                Object v = format.get("duration");
                return v + "";
            }
            return null;
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "AvInfo"), Pair.of("method", "getDuration"),
                Pair.of("duration_status", "exception"), Pair.of("avInfo", getAvInfo()));
            return null;
        }
    }

    /**
     * getVideoDuration
     *
     * @return
     */
    public double getVideoDuration() {
        try {
            Map<String, Object> videoS = getVideoStream();
            if (videoS != null && !videoS.isEmpty()) {
                Object obj = videoS.get("duration");
                if (obj == null) {
                    obj = getFormatInfo("duration");
                }
                if (obj instanceof Number) {
                    return ((Number)obj).doubleValue();
                } else {
                    double duration = Double.parseDouble(obj + "");
                    return duration;
                }
            }
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "AvInfo"), Pair.of("method", "getVideoDuration"),
                Pair.of("video_duration_status", "exception"), Pair.of("avinfo_str", getAvInfo()));
        }
        return -1.0;
    }

    /**
     * getAudioDuration
     * 
     * @return
     */
    public double getAudioDuration() {
        try {
            Map<String, Object> audioStream = getAudioStream();
            if (audioStream != null && !audioStream.isEmpty()) {
                Object obj = audioStream.get("duration");
                if (obj instanceof Number) {
                    return ((Number)obj).doubleValue();
                } else {
                    return Double.parseDouble(obj + "");
                }
            }
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "AvInfo"), Pair.of("method", "getAudioDuration"),
                Pair.of("audio_duration_status", "exception"), Pair.of("avinfo_str", getAvInfo()));
        }
        return 0;
    }

    /**
     * getFormatInfo
     *
     * @param key
     * @return
     */
    public Object getFormatInfo(String key) {
        Map<String, Object> formatInfo = (Map<String, Object>)this.metas.get(FORMAT_KEY);
        if (formatInfo == null || formatInfo.isEmpty()) {
            return null;
        }
        return formatInfo.get(key);
    }

    /**
     * getVideoBitrate
     *
     * @return
     */
    public int getVideoBitrate() {
        try {
            Map<String, Object> videoS = getVideoStream();
            if (videoS != null && !videoS.isEmpty()) {
                int b = Integer.parseInt(videoS.get("bit_rate") + "") / 1000;
                return b;
            }
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "AvInfo"), Pair.of("method", "getVideoBitrate"),
                Pair.of("video_bitrate_status", "exception"), Pair.of("avInfo", getAvInfo()));
        }
        return -1;
    }

    /**
     * getAudioBitrate
     * 
     * @return
     */
    public int getAudioBitrate() {
        try {
            Map<String, Object> audioStream = getAudioStream();
            if (audioStream != null && !audioStream.isEmpty()) {
                int b = Integer.parseInt(audioStream.get("bit_rate") + "") / 1000;
                return b;
            }
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "AvInfo"), Pair.of("method", "getAudioBitrate"),
                Pair.of("audio_bitrate_status", "exception"), Pair.of("avInfo", getAvInfo()));
        }
        return -1;
    }

    /**
     * getOriginVideoBitrate
     *
     * @return
     */
    public int getOriginVideoBitrate() {
        try {
            Map<String, Object> videoS = getVideoStream();
            if (videoS != null && !videoS.isEmpty() && videoS.containsKey("bit_rate")) {
                Object obj = videoS.get("bit_rate");
                if (obj instanceof Number) {
                    int b = ((Number)obj).intValue();
                    return b;
                } else if (obj instanceof String) {
                    Integer b = Integer.parseInt(obj + "");
                    return b;
                }
            }
            Object br = this.getFormatInfo("bit_rate");
            if (br instanceof Number) {
                int b = ((Number)br).intValue();
                return b;
            } else if (br instanceof String) {
                Integer b = Integer.parseInt(br + "");
                return b;
            }
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "AvInfo"), Pair.of("method", "getOriginVideoBitrate"),
                Pair.of("video_bitrate_status", "exception"), Pair.of("avInfo", getAvInfo()));
        }
        return -1;
    }

    /**
     * getVideoRotate
     *
     * @return
     */
    public int getVideoRotate() {
        try {
            Map<String, Object> videoS = getVideoStream();
            if (videoS != null && !videoS.isEmpty() && videoS.containsKey("side_data_list")) {
                List<Map<String, Object>> sideDataList = (List<Map<String, Object>>)videoS.get("side_data_list");
                if (sideDataList != null && !sideDataList.isEmpty()) {
                    Map<String, Object> sideData = sideDataList.get(0);
                    Object obj = sideData.get("rotation");
                    if (Objects.isNull(obj)) {
                        return 0;
                    }
                    if (obj instanceof Number) {
                        return ((Number)obj).intValue();
                    } else {
                        String str = obj + "";
                        return Integer.parseInt(str);
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "AvInfo"), Pair.of("method", "getVideoBitrate"),
                Pair.of("video_bitrate_status", "exception"), Pair.of("avInfo", getAvInfo()));
        }
        return 0;
    }

    /**
     * getVideoFps
     *
     * @return
     */
    public int getVideoFps() {
        try {
            Map<String, Object> videoS = getVideoStream();
            if (videoS != null && !videoS.isEmpty() && videoS.containsKey("r_frame_rate")) {
                String fpsStr = videoS.get("r_frame_rate") + "";
                String[] fpsArray = StringUtils.split(fpsStr, "/");
                if (fpsArray != null && fpsArray.length == 1) {
                    return Integer.parseInt(fpsArray[0]);
                } else if (fpsArray != null && fpsArray.length > 1) {
                    Integer a = Integer.parseInt(fpsArray[0]);
                    Integer b = Integer.parseInt(fpsArray[1]);
                    if (b == 0) {
                        return a;
                    }
                    return Math.round(a / b);
                }
            }
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "AvInfo"), Pair.of("method", "getVideoFps"),
                Pair.of("video_fps_status", "exception"), Pair.of("avInfo", getAvInfo()));
        }
        return 0;
    }

    /**
     * getVideoStream
     *
     * @return
     */
    public Map<String, Object> getVideoStream() {
        return getStreamInfo("video");
    }

    /**
     * getStreamInfo
     *
     * @param codecType
     * @return
     */
    public Map<String, Object> getStreamInfo(String codecType) {
        List<Map<String, Object>> streams = (List<Map<String, Object>>)this.metas.get(STREAMS_KEY);
        if (streams != null) {
            for (Map<String, Object> map : streams) {
                if (StringUtils.equals(map.get("codec_type") + "", codecType)) {
                    return map;
                }
            }
        }
        return new HashMap<>(0);
    }

    /**
     * getFormat
     *
     * @return
     */
    public Map<String, Object> getFormat() {
        return (Map<String, Object>)this.metas.get(FORMAT_KEY);
    }

    /**
     * getAudioStream
     *
     * @return
     */
    public Map<String, Object> getAudioStream() {
        return getStreamInfo("audio");
    }

    /**
     * 判断条件：视频流为空，音频流不为空，当做音频来处理
     *
     * 其他情况，均当做：视频来处理
     *
     * @return
     */
    @Override
    public MediaType getMediaType() {
        Map<String, Object> videoStream = getVideoStream();
        Map<String, Object> audioStream = getAudioStream();
        // 视频流为空，音频流不为空，当做音频来处理
        if (videoStream.isEmpty() && !audioStream.isEmpty()) {
            return MediaType.AUDIO;
        }
        // 其他情况，均当做：视频来处理
        return MediaType.VIDEO;
    }

    /**
     * failure
     *
     * @param errInfo
     * @return
     */
    public static AvInfo failure(String errInfo) {
        Map<String, Object> metas = JacksonUtils.json2Map(errInfo, Object.class);
        if (metas == null || metas.isEmpty()) {
            return empty();
        }
        String code = metas.get("code") + "";
        String message = metas.get("message") + "";
        return new AvInfo(code, message);
    }

    /**
     * empty
     *
     * @return
     */
    public static AvInfo empty() {
        return new AvInfo("999", "unknow exception");
    }

    /**
     * from
     *
     * @param avinfo
     * @return
     */
    public static AvInfo from(String avinfo) {
        Map<String, Object> metas = JacksonUtils.json2Map(avinfo, Object.class);
        if (metas != null && !metas.isEmpty()) {
            AvInfo avInfo = new AvInfo(avinfo);
            avInfo.metas.putAll(metas);
            return avInfo;
        }
        return new AvInfo("199", "av info解析出来为空");
    }
}
