package com.mayo.server.chef.domain.component;

import com.mayo.server.chef.adapter.in.web.MyPageRequest;
import com.mayo.server.chef.app.port.in.S3InputPort;
import com.mayo.server.chef.app.port.in.TransformedImageDto;
import com.mayo.server.chef.infra.S3Client;
import com.mayo.server.common.annotation.Adapter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Adapter
@RequiredArgsConstructor
public class S3Adapter implements S3InputPort {

    private final S3Client s3Client;

    @Value("${spring.aws.credentials.cf}")
    private String AWS_CF_DISTRIBUTION;

    @Override
    public TransformedImageDto getPreSignedUrl(String category, Long id, Integer imageId, String imageKey) {
        return new TransformedImageDto(
                imageId,
                s3Client.getPreSignedUrl(getImagePrefix(category, id, imageId, imageKey))
        );
    }

    @Override
    public TransformedImageDto getCloudFrontSignedUrl(
            Integer id,
            String imageKey
    ) {

        return new TransformedImageDto(
                id,
                AWS_CF_DISTRIBUTION + "/" + imageKey
        );
    }

    @Override
    public String getImagePrefix(String category, Long id, Integer imageId, String imageKey) {

        return id + "/" + category + "/" + imageId + "_" + imageKey;
    }

    @Override
    public void deleteS3Objects(Long id, String category) {

        List<String> keyList = s3Client.listObjectKeys(id + "/" + category);
        keyList.forEach(s3Client::deleteObjectS3);
    }

    ;
}
