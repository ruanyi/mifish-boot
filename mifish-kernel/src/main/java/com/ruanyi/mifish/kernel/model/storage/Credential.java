package com.ruanyi.mifish.kernel.model.storage;

import org.apache.commons.lang3.StringUtils;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2019-02-15 20:20
 */
public class Credential {

    /** ak */
    private String ak;

    /** sk */
    private String sk;

    /** endpoint */
    private String endpoint;

    /** region */
    private String region;

    /**
     * Credentials
     *
     * @param ak
     * @param sk
     */
    public Credential(String ak, String sk) {
        this.ak = ak;
        this.sk = sk;
    }

    /**
     * Credential
     * 
     * @param ak
     * @param sk
     * @param endpoint
     */
    public Credential(String ak, String sk, String endpoint) {
        this.ak = ak;
        this.sk = sk;
        this.endpoint = endpoint;
    }

    /**
     * getAk
     *
     * @return
     */
    public String getAk() {
        return ak;
    }

    /**
     * getSk
     *
     * @return
     */
    public String getSk() {
        return sk;
    }

    /**
     * getEndPoint
     *
     * @return
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * setEndpoint
     * 
     * @param endpoint
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * getRegion
     * 
     * @return
     */
    public String getRegion() {
        return region;
    }

    /**
     * setRegion
     * 
     * @param region
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * of
     *
     * @param ak
     * @param sk
     * @return
     */
    public static Credential of(String ak, String sk) {
        if (StringUtils.isNotBlank(ak) && StringUtils.isNotBlank(sk)) {
            return new Credential(ak, sk);
        }
        return null;
    }
}
