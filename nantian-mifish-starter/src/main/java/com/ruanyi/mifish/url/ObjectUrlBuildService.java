package com.ruanyi.mifish.url;

import com.ruanyi.mifish.kernel.model.uri.ObjectURI;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-22 22:38
 */
public interface ObjectUrlBuildService {

    /**
     * buildDownloadUrl
     * 
     * @param objectURI
     * @param expire
     * @return
     */
    String buildDownloadUrl(ObjectURI objectURI, long expire);

    /**
     * buildCdnUrl
     * 
     * @param objectURI
     * @param expire
     * @return
     */
    String buildCdnUrl(ObjectURI objectURI, long expire);
}
