package com.swyp.catsgotogedog.common.util.imagestorage;

import com.swyp.catsgotogedog.global.exception.ImageUploadException;
import io.awspring.cloud.s3.S3Template;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectAclRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class ImageStorageService {

    private final S3Template s3Template;
    private final S3Client s3Client;
    private final String endpoint;
    private final String bucketName;

    private static final int MAX_FILE_COUNT = 10;

    public ImageStorageService(S3Template s3Template,
                               S3Client s3Client,
                               @Value("${spring.cloud.aws.s3.endpoint}") String endpoint,
                               @Value("${spring.cloud.aws.s3.bucket}") String bucketName) {

        this.s3Template = s3Template;
        this.s3Client = s3Client;
        this.endpoint = endpoint;
        this.bucketName = bucketName;
    }

    // 다중 이미지 업로드 -> upload 메서드(path 포함)로 오버로딩
    public List<String> upload(List<MultipartFile> files) {
        return upload(files, "");
    }

    // 다중 이미지 업로드 -> 각 파일을 doUpload 메서드로 처리. 각 반환 값을 리스트로 수집 후 반환
    public List<String> upload(List<MultipartFile> files, String path) {
        validateFiles(files);
        return files.stream()
                .map(file -> doUpload(file, path))
                .collect(Collectors.toList());
    }

    // 단일 이미지 업로드 -> upload 메서드(path 포함)로 오버로딩
    public List<String> upload(MultipartFile file) {
        return upload(file, "");
    }

    // 단일 이미지 업로드 -> doUpload 호출 후 리스트로 래핑하여 반환
    public List<String> upload(MultipartFile file, String path) {
        validateFile(file);
        return Collections.singletonList(doUpload(file, path));
    }

    // 단일 이미지 삭제 -> 리스트화 하여 처리
    public void delete(String key) {
        validateKey(key);
        doDelete(key);
    }

    // 다중 이미지 삭제 각 이미지 키를 검증 후 삭제
    public void delete(List<String> keys) {
        validateKeyList(keys);
        keys.forEach(this::doDelete);
    }

    // 키를 기반으로 URL 생성 -> 단일 이미지
    public List<String> generateUrls(String key) {
        validateKey(key);
        return Collections.singletonList(doGenerateUrl(key));
    }

    // 다중 이미지 URL 생성
    public List<String> generateUrls(List<String> keys) {
        validateKeyList(keys);
        return keys.stream()
                .map(this::doGenerateUrl)
                .collect(Collectors.toList());
    }

    private String doUpload(MultipartFile file, String path) {
        String key = genKey(file, path);

        try (InputStream stream = file.getInputStream()) {
            s3Template.upload(bucketName, key, stream);
            setAclPublicRead(key);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to upload", e);
        } catch (Exception e) {
            // ACL 설정 실패 시 업로드된 객체 삭제
            s3Template.deleteObject(bucketName, key);
            throw new ImageUploadException("Failed to set ACL for " + key, e);
        }
        return key;
    }

    private void setAclPublicRead(String key) {
        PutObjectAclRequest aclRequest = PutObjectAclRequest.builder()
                .bucket(bucketName)
                .key(key)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .build();
        s3Client.putObjectAcl(aclRequest);
    }

    private void doDelete(String key) {
        s3Template.deleteObject(bucketName, key);
    }

    private String doGenerateUrl(String key) {
        return String.format("%s/%s/%s", endpoint, bucketName, key);
    }

    // MIME 타입 검사 등 Tika를 사용한 바이너리 검사 기능 별도로 개발 필요
    private void validateFiles(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }
        if (files.size() > MAX_FILE_COUNT) {
            throw new IllegalArgumentException("파일은 최대 " + MAX_FILE_COUNT + "개까지만 업로드할 수 있습니다.");
        }
        files.forEach(this::validateFile);
    }

    // MIME 타입 검사 등 Tika를 사용한 바이너리 검사 기능 별도로 개발 필요
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }
    }

    private static void validateKeyList(List<String> keys) {
        if (keys == null || keys.isEmpty()) {
            throw new IllegalArgumentException("키 리스트는 null 또는 비어있을 수 없습니다.");
        }
        // 전체 키 리스트의 유효성을 먼저 검사
        if (keys.stream().anyMatch(key -> key == null || key.isBlank())) {
            throw new IllegalArgumentException("키 리스트에 null 또는 빈 문자열이 포함될 수 없습니다.");
        }
    }

    private static void validateKey(String key) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("키는 null 또는 빈 문자열이 될 수 없습니다.");
        }
    }

    private static String genKey(MultipartFile file, String path) {
        String originalFilename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "";
        return path + UUID.randomUUID() + originalFilename;
    }

}
