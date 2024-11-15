package com.mayo.server.chef.app.port.in;

public interface S3InputPort {

    public TransformedImageDto getPreSignedUrl(
            String category,
            Long id,
            Integer imageId,
            String imageKey
    );

    public TransformedImageDto getCloudFrontSignedUrl(
            Integer id,
            String imageKey
    );

    public String getImagePrefix(String category, Long id, Integer imageId, String imageKey);

    public void deleteS3Objects(Long id, String category);

}
