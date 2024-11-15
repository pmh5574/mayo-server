package com.mayo.server.chef.infra;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.mayo.server.common.utility.DateUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class S3Client {

    private final AmazonS3Client amazonS3Client;
    public final String S3_BUCKET_NAME = "mayo-ap-northeast-2-s3";

    public String getPreSignedUrl(String fileName){

        return amazonS3Client.generatePresignedUrl(
                S3_BUCKET_NAME,
                fileName,
                DateUtility.getPresignedExpireDate(),
                HttpMethod.valueOf("PUT")
        ).toString();
    }

    public void deleteObjectS3(String objectName){

        amazonS3Client.deleteObject(S3_BUCKET_NAME, objectName);
    }

    public List<String> listObjectKeys(String prefix) {

        ObjectListing objectListing = amazonS3Client.listObjects(S3_BUCKET_NAME, prefix);
        return objectListing.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .collect(Collectors.toList());
    }

}
