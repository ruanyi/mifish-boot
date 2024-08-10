package com.ruanyi.mifish.upload.impl;

import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.ruanyi.mifish.common.ex.BusinessException;
import com.ruanyi.mifish.kernel.model.storage.Credential;
import com.ruanyi.mifish.kernel.model.uri.UploadURI;
import com.ruanyi.mifish.upload.ObjectUploader;

/**
 * Description:
 *
 * @author: ruanyi
 * @Date: 2024-05-23 22:50
 */
public class S3ObjectUploader implements ObjectUploader {

    /**
     * @see ObjectUploader#upload(String, UploadURI)
     */
    @Override
    public boolean upload(String localPath, UploadURI uploadURI) throws BusinessException {
        Credential credential = uploadURI.getCredential();
        AWSStaticCredentialsProvider awsCredential =
            new AWSStaticCredentialsProvider(new BasicAWSCredentials(credential.getAk(), credential.getSk()));
        AwsClientBuilder.EndpointConfiguration endpointConfiguration =
            new AwsClientBuilder.EndpointConfiguration(credential.getEndpoint(), null);
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(awsCredential)
            .withEndpointConfiguration(endpointConfiguration).build();
        try {
            s3.putObject(new PutObjectRequest(uploadURI.getBucket(), uploadURI.getKey(), new File(localPath))
                .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonServiceException ase) {
            // todo
        } catch (AmazonClientException ace) {
            // todo
        } finally {
            s3.shutdown();
        }
        return true;
    }
}
