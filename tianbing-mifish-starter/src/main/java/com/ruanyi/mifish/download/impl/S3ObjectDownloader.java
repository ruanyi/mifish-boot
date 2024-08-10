package com.ruanyi.mifish.download.impl;

import java.io.FileOutputStream;

import org.apache.commons.lang3.tuple.Pair;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.ruanyi.mifish.common.ex.BusinessException;
import com.ruanyi.mifish.common.logs.MifishLogs;
import com.ruanyi.mifish.download.ObjectDownloader;
import com.ruanyi.mifish.kernel.model.storage.Credential;
import com.ruanyi.mifish.kernel.model.uri.DownloadURI;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-23 21:35
 */
public class S3ObjectDownloader implements ObjectDownloader {

    /** LOG */
    private static final MifishLogs LOG = MifishLogs.media;

    /**
     * @see ObjectDownloader#download(DownloadURI, String)
     */
    @Override
    public boolean download(DownloadURI downloadURI, String savesPath) throws BusinessException {
        try {
            Credential credential = downloadURI.getCredential();
            AWSStaticCredentialsProvider awsCredential =
                new AWSStaticCredentialsProvider(new BasicAWSCredentials(credential.getAk(), credential.getSk()));
            AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(awsCredential)
                .withRegion(credential.getRegion()).build();
            GetObjectRequest getObjectRequest = new GetObjectRequest(downloadURI.getBucket(), downloadURI.getKey());
            S3Object object = s3.getObject(getObjectRequest);
            S3ObjectInputStream inputStream = object.getObjectContent();
            FileOutputStream fos = new FileOutputStream(savesPath);
            IOUtils.copy(inputStream, fos);
            return true;
        } catch (Exception ex) {
            LOG.error(ex, Pair.of("clazz", "S3ObjectDownloader"), Pair.of("", ""));
        }
        return false;
    }

    /**
     * @see ObjectDownloader#getCloud()
     */
    @Override
    public String getCloud() {
        return "S3";
    }
}
