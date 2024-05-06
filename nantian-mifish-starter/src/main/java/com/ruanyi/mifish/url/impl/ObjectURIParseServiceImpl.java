package com.ruanyi.mifish.url.impl;

import java.net.URL;
import java.util.Optional;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.kernel.model.uri.ObjectURI;
import com.ruanyi.mifish.repository.StorageMetaRepository;
import com.ruanyi.mifish.url.ObjectURIParseService;
import com.ruanyi.mifish.url.ObjectUrlHelper;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-03 11:07
 */
@Service
public class ObjectURIParseServiceImpl implements ObjectURIParseService {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.media;

    /** storageMetaRepository */
    @Resource(name = "storageMetaRepository")
    private StorageMetaRepository storageMetaRepository;

    /**
     * @see ObjectURIParseService#parseWithoutException(String)
     */
    @Override
    public Optional<ObjectURI> parseWithoutException(String url) {
        try {
            if (StringUtils.isBlank(url)) {
                LOG.info(Pair.of("clazz", "ObjectURIParseService"), Pair.of("method", "parse"),
                    Pair.of("msg", "url is null"));
                return Optional.empty();
            }
            if (!url.startsWith("http://") && !url.startsWith("https://") && !url.startsWith("//")) {
                url = "http://" + url;
            }
            URL uri = new URL(url);
            // 1、解析出：key信息
            String path = uri.getPath();
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            // 坑、巨坑，url 中带有：%2B，将这个符号处理成：+
            // 例如：https://xxtool-release.obs.cn-north-4.myhuaweicloud.com:443/pc/user_custom/bvc4tp096mqup0ogub10%2B1467594298.jpeg
            if (StringUtils.contains(path, "%2B")) {
                path = ObjectUrlHelper.decode(path);
            }
            // 2、解析出：bucket信息
            String host = uri.getHost();
            if (StringUtils.isBlank(host)) {
                LOG.warn(Pair.of("clazz", "ObjectURIParseService"), Pair.of("method", "parse"),
                    Pair.of("msg", "url without host"), Pair.of("url", url));
                return Optional.empty();
            }
            // 3、解析出：cloud信息
            // 1.1、优先从mtyun获取
            String bucket = this.storageMetaRepository.obtainBucketByHost(host);
            String cloud = this.storageMetaRepository.obtainCloudByHost(host);
            ObjectURI objectURI = new ObjectURI();
            objectURI.setCloud(cloud);
            objectURI.setBucket(bucket);
            objectURI.setKey(path);
            objectURI.setUrl(url);
            return Optional.of(objectURI);
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "ObjectURIParseService"), Pair.of("method", "parse"),
                Pair.of("msg", "can not recognize the bucket and the key from the url"), Pair.of("url", url));
            return Optional.empty();
        }
    }
}
