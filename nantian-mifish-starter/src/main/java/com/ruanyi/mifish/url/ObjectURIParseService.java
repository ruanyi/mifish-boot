package com.ruanyi.mifish.url;

import java.util.Optional;

import com.ruanyi.mifish.kernel.model.uri.ObjectURI;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-03 11:06
 */
public interface ObjectURIParseService {

    /**
     * parseWithoutException
     * 
     * @param url
     * @return
     */
    Optional<ObjectURI> parseWithoutException(String url);
}
