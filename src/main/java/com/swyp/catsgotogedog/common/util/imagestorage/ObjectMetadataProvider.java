package com.swyp.catsgotogedog.common.util.imagestorage;

import io.awspring.cloud.s3.ObjectMetadata;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;

@Component
public class ObjectMetadataProvider {

    public ObjectMetadata createPublicReadMetadata(MultipartFile file) {
        return new ObjectMetadata.Builder()
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();
    }
}