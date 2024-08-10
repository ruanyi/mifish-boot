package com.ruanyi.mifish.upload.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ruanyi.mifish.common.ex.BusinessException;
import com.ruanyi.mifish.kernel.model.uri.UploadURI;
import com.ruanyi.mifish.upload.ObjectUploadComponent;
import com.ruanyi.mifish.upload.ObjectUploader;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-04 10:29
 */
@Component
public class SimpleObjectUploadComponent implements ObjectUploadComponent {

    /** uploaders */
    private Map<String, ObjectUploader> uploaders = new HashMap<>(12);

    /**
     * @see ObjectUploadComponent#upload(String, UploadURI)
     */
    @Override
    public boolean upload(String localPath, UploadURI uploadURI) throws BusinessException {
        return false;
    }
}
