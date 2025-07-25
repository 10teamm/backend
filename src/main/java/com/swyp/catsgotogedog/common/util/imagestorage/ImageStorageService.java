package com.swyp.catsgotogedog.common.util.imagestorage;

import com.swyp.catsgotogedog.common.util.imagestorage.dto.ImageInfo;
import com.swyp.catsgotogedog.global.exception.*;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectAclRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class ImageStorageService {

    private final S3Template s3Template;
    private final S3Client s3Client;
    private final String bucketName;

    private final int MAX_FILE_COUNT = 10;

    public ImageStorageService(S3Template s3Template,
                               S3Client s3Client,
                               @Value("${spring.cloud.aws.s3.bucket}") String bucketName) {

        this.s3Template = s3Template;
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    /**
     * 다중 이미지 업로드
     * @param files MultipartFile list
     * @return List&lt;ImageInfo&gt;
     */
    public List<ImageInfo> upload(List<MultipartFile> files) {
        return upload(files, "");
    }

    /**
     * 다중 이미지 업로드
     * @param files MultipartFile list
     * @param path 업로드 경로
     * @return List&lt;ImageInfo&gt;
     */
    public List<ImageInfo> upload(List<MultipartFile> files, String path) {
        validateFiles(files);
        return files.stream()
                .map(file -> doUpload(file, path))
                .collect(Collectors.toList());
    }

    /**
     * 단일 이미지 업로드
     * @param file MultipartFile
     * @return List&lt;ImageInfo&gt;
     */
    public List<ImageInfo> upload(MultipartFile file) {
        return upload(file, "");
    }

    /**
     * 단일 이미지 업로드
     * @param file MultipartFile
     * @param path 업로드 경로
     * @return List&lt;ImageInfo&gt;
     */
    public List<ImageInfo> upload(MultipartFile file, String path) {
        validateFile(file);
        return Collections.singletonList(doUpload(file, path));
    }

    /**
     * 이미지 삭제
     * @param key image key
     */
    public void delete(String key) {
        validateKey(key);
        doDelete(key);
    }

    /**
     * 다중 이미지 삭제
     * @param keys list of image keys
     */
    public void delete(List<String> keys) {
        validateKeyList(keys);
        keys.forEach(this::doDelete);
    }

    private ImageInfo doUpload(MultipartFile file, String path) {
        String key = genKey(file, path);

        try (InputStream stream = file.getInputStream()) {
            S3Resource resource = s3Template.upload(bucketName, key, stream);
            setAclPublicRead(resource.getFilename());
            return new ImageInfo(resource.getFilename(), resource.getURL().toString());
        } catch (IOException e) {
            throw new ImageNotFoundException(ErrorCode.IMAGE_NOT_FOUND);
        } catch (Exception e) {
            // ACL 설정 실패 시 업로드된 객체 삭제
            s3Template.deleteObject(bucketName, key);
            throw new ImageUploadException(ErrorCode.IMAGE_UPLOAD_FAILED);
        }
    }

    // S3 객체의 ACL을 PUBLIC_READ로 설정
    private void setAclPublicRead(String key) {
        PutObjectAclRequest aclRequest = PutObjectAclRequest.builder()
                .bucket(bucketName)
                .key(key)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();
        s3Client.putObjectAcl(aclRequest);
    }

    // S3에서 객체 삭제
    private void doDelete(String key) {
        s3Template.deleteObject(bucketName, key);
    }

    // MIME 타입 검사 등 Tika를 사용한 바이너리 검사 기능 별도로 개발 필요
    private void validateFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new ImageNotFoundException(ErrorCode.IMAGE_NOT_FOUND);
        }
        if (files.size() > MAX_FILE_COUNT) {
            throw new TooManyImagesException(ErrorCode.TOO_MANY_IMAGES);
        }
        files.forEach(this::validateFile);
    }

    // MIME 타입 검사 등 Tika를 사용한 바이너리 검사 기능 별도로 개발 필요
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ImageNotFoundException(ErrorCode.IMAGE_NOT_FOUND);
        }
    }

    private void validateKeyList(List<String> keys) {
        if (keys == null || keys.isEmpty()) {
            throw new ImageNotFoundException(ErrorCode.IMAGE_NOT_FOUND);
        }
        // 전체 키 리스트의 유효성을 먼저 검사
        if (keys.stream().anyMatch(key -> key == null || key.isBlank())) {
            throw new ImageKeyNotFoundException(ErrorCode.IMAGE_KEY_NOT_FOUND);
        }
    }

    private void validateKey(String key) {
        if (key == null || key.isBlank()) {
            throw new ImageKeyNotFoundException(ErrorCode.IMAGE_KEY_NOT_FOUND);
        }
    }

    // 파일 이름과 UUID를 조합하여 고유한 키 생성
    private String genKey(MultipartFile file, String path) {
        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "";
        return path + UUID.randomUUID() + originalFilename;
    }

}
