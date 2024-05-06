package com.ruanyi.mifish.repository;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-03 15:17
 */
public interface StorageMetaRepository {

    /**
     * obtainBucketByHost
     * 
     * @param host
     * @return
     */
    String obtainBucketByHost(String host);

    /**
     * obtainCloudByHost
     * 
     * @param host
     * @return
     */
    String obtainCloudByHost(String host);

    /**
     * obtainHostByBucketTag
     * 
     * @param cloud
     * @param bucket
     * @param tag
     * @return
     */
    String obtainHostByBucketTag(String cloud, String bucket, String tag);
}
