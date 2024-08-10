package com.ruanyi.mifish.repository.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.ruanyi.mifish.kernel.model.storage.BucketHost;
import com.ruanyi.mifish.kernel.model.storage.BucketMeta;
import com.ruanyi.mifish.repository.StorageMetaLoader;
import com.ruanyi.mifish.repository.StorageMetaRepository;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-03 15:58
 */
@Component
public class StorageMetaRepositoryImpl implements StorageMetaRepository {

    /**
     * storageMetas
     * 
     * HOST -> Bucket
     */
    private Map<String, BucketMeta> hostMetas = new ConcurrentHashMap<>();

    /**
     * storageMetas
     * 
     * cloud,bucket -> List<BucketMeta/>
     */
    private Table<String, String, BucketMeta> storageMetas = HashBasedTable.create();

    @Resource
    private StorageMetaLoader storageMetaLoader;

    /**
     * @see StorageMetaRepository#obtainBucketByHost(String)
     */
    @Override
    public String obtainBucketByHost(String host) {
        BucketMeta bucketMeta = this.hostMetas.get(host);
        if (bucketMeta != null) {
            return bucketMeta.getBucket();
        }
        return null;
    }

    /**
     * @see StorageMetaRepository#obtainCloudByHost(String)
     */
    @Override
    public String obtainCloudByHost(String host) {
        BucketMeta bucketMeta = this.hostMetas.get(host);
        if (bucketMeta != null) {
            return bucketMeta.getCloud();
        }
        return null;
    }

    /**
     * @see StorageMetaRepository#obtainHostByBucketTag(String,String, String)
     */
    @Override
    public String obtainHostByBucketTag(String cloud, String bucket, String tag) {
        BucketMeta bucketMeta = this.storageMetas.get(cloud, bucket);
        if (bucketMeta != null) {
            List<BucketHost> hosts = bucketMeta.getHosts();
            if (hosts != null) {
                for (BucketHost bucketHost : hosts) {
                    List<String> tags = bucketHost.getTags();
                    if (tags != null && tags.contains(tag)) {
                        return bucketHost.getHost();
                    }
                }
            }
        }
        return null;
    }
}
