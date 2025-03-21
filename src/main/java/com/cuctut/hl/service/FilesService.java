package com.cuctut.hl.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuctut.hl.auth.UserHolder;
import com.cuctut.hl.common.BucketNameEnum;
import com.cuctut.hl.common.ErrorCodeEnum;
import com.cuctut.hl.common.RestResp;
import com.cuctut.hl.exception.BusinessException;
import com.cuctut.hl.mapper.FilesMapper;
import com.cuctut.hl.model.dto.resp.FileInfoRespDto;
import com.cuctut.hl.model.entity.Files;
import io.minio.*;
import io.minio.http.Method;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
* @author 86134
* @description 针对表【files】的数据库操作Service
* @createDate 2024-12-25 22:22:48
*/
@Slf4j
@AllArgsConstructor
@Service
public class FilesService extends ServiceImpl<FilesMapper, Files> {

    private final MinioClient minioClient;


    public RestResp<List<FileInfoRespDto>> getFileList(String fileType, Long chapterId) {
        LambdaQueryWrapper<Files> query = new LambdaQueryWrapper<>();
        query.eq(Files::getChapterId, chapterId).eq(Files::getBucketName, fileType).orderByDesc(Files::getCreatedAt);
        List<Files> list = list(query);
        List<FileInfoRespDto> result = list.stream().map(Files::convertToFileInfoRespDto).toList();
        return RestResp.ok(result);
    }

    public RestResp<FileInfoRespDto> uploadFile(MultipartFile file, String bucketName, Long chapterId) {
        if (!UserHolder.getUserRole().equals("ADMIN")) throw new BusinessException(ErrorCodeEnum.USER_UN_AUTH);
        if (!BucketNameEnum.mapBucketName(bucketName)) throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_TYPE_NOT_MATCH);
        try {
            boolean isBucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isBucketExists) minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());

            String minioFileName = getMinioFileName(file);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(minioFileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());

            String fileName = file.getOriginalFilename();
            String fileType = Objects.requireNonNull(fileName)
                    .substring(fileName.lastIndexOf(".") + 1)
                    .toUpperCase();
            Files fileInfo = Files.builder()
                    .chapterId(chapterId)
                    .fileName(fileName)
                    .minioFileName(minioFileName)
                    .bucketName(bucketName)
                    .type(fileType)
                    .size(file.getSize())
                    .path("")
                    .build();

            boolean success = save(fileInfo);
            if (!success) throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_ERROR);
            fileInfo.setCreatedAt(getById(fileInfo.getId()).getCreatedAt());
            return RestResp.ok(fileInfo.convertToFileInfoRespDto());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException(ErrorCodeEnum.USER_UPLOAD_FILE_ERROR);
        }
    }

    public void downloadFile(Long fileId, HttpServletResponse response) {
        // 查询文件信息
        Files file = getById(fileId);
        if (Objects.isNull(file)) throw new BusinessException(ErrorCodeEnum.USER_DOWNLOAD_FILE_ERROR);

        InputStream inputStream = getDownloadFileInputStream(file.getMinioFileName(), file.getBucketName());

        // 设置响应头
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(file.getFileName(), StandardCharsets.UTF_8));
        response.setContentLengthLong(file.getSize());

        // 将文件流写入响应
        try {
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public RestResp<Void> deleteFile(Long fileId) {
        if (!UserHolder.getUserRole().equals("ADMIN")) throw new BusinessException(ErrorCodeEnum.USER_UN_AUTH);
        Files fileInfo = getById(fileId);
        deleteFile(fileInfo.getMinioFileName(), fileInfo.getBucketName());
        removeById(fileId);
        return RestResp.ok();
    }

    public RestResp<String> previewFile(Long fileId) {
        Files fileInfo = getById(fileId);
        String fileUrl = getFileUrl(fileInfo.getMinioFileName(), fileInfo.getBucketName());
        return RestResp.ok(fileUrl);
    }

    public RestResp<List<FileInfoRespDto>> getRecentFiles(int limit) {
        LambdaQueryWrapper<Files> query = new LambdaQueryWrapper<>();
        query.orderByDesc(Files::getCreatedAt).last("LIMIT " + limit);
        List<Files> list = list(query);
        List<FileInfoRespDto> result = list.stream().map(Files::convertToFileInfoRespDto).toList();
        return RestResp.ok(result);
    }



    private InputStream getDownloadFileInputStream(String minioFileName, String bucketName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(minioFileName)
                    .build());
        } catch (Exception e) {
            throw new BusinessException(ErrorCodeEnum.USER_DOWNLOAD_FILE_ERROR);
        }
    }

    private void deleteFile(String minioFileName, String bucketName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(minioFileName)
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException(ErrorCodeEnum.THIRD_SERVICE_ERROR);
        }
    }

    private String getFileUrl(String minioFileName, String bucketName)  {
        try {
            return minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(minioFileName)
                    .method(Method.GET)
                    .expiry(1, TimeUnit.HOURS) // 设置有效期为 1 小时
                    .build());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException(ErrorCodeEnum.THIRD_SERVICE_ERROR);
        }
    }

    private String getMinioFileName(MultipartFile file) {
        return System.currentTimeMillis() + "_" + file.getOriginalFilename();
    }

}
