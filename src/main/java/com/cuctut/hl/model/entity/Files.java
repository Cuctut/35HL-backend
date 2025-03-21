package com.cuctut.hl.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.cuctut.hl.model.dto.resp.FileInfoRespDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

/**
 * 
 * @TableName files
 */
@TableName(value ="files")
@Data
@Builder
public class Files implements Serializable {
    /**
     * 文件ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属章节ID
     */
    @TableField(value = "chapter_id")
    private Long chapterId;

    /**
     * 文件名
     */
    @TableField(value = "file_name")
    private String fileName;

    /**
     * minio文件名
     */
    @TableField(value = "minio_file_name")
    private String minioFileName;

    /**
     * minio bucket名
     */
    @TableField(value = "bucket_name")
    private String bucketName;

    /**
     * 文件类型（如PDF、PPT）
     */
    @TableField(value = "type")
    private String type;

    /**
     * 文件大小（字节）
     */
    @TableField(value = "size")
    private Long size;

    /**
     * 文件存储路径
     */
    @TableField(value = "path")
    private String path;

    /**
     * 上传时间
     */
    @TableField(value = "created_at")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm")
    private LocalDateTime createdAt;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    public FileInfoRespDto convertToFileInfoRespDto() {
        return FileInfoRespDto.builder()
                .id(id)
                .fileName(fileName)
                .type(type)
                .size(formatFileSize(size))
                .createdAt(createdAt)
                .build();
    }

    private static String formatFileSize(long size) {
        if (size < 1024) return size + " B";
        int exp = (int) (Math.log(size) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "i";
        return String.format("%.1f %sB", size / Math.pow(1024, exp), pre);
    }
}