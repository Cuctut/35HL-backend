package com.cuctut.hl.controller;

import com.cuctut.hl.common.RestResp;
import com.cuctut.hl.model.dto.resp.FileInfoRespDto;
import com.cuctut.hl.service.FilesService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FilesService filesService;

    @GetMapping("/list/{fileType}/{chapterId}")
    public RestResp<List<FileInfoRespDto>> getFileList(
            @PathVariable("fileType") String fileType,
            @PathVariable Long chapterId
    ) {
        return filesService.getFileList(fileType, chapterId);
    }

    @PostMapping("/upload/{fileType}/{chapterId}")
    public RestResp<FileInfoRespDto> uploadFile(
            @RequestParam("file") MultipartFile file,
            @PathVariable("fileType") String fileType,
            @PathVariable("chapterId") Long chapterId
    ) {
        return filesService.uploadFile(file, fileType, chapterId);
    }

    @GetMapping("/download/{fileId}")
    public void downloadFile(
            @PathVariable("fileId") Long fileId,
            HttpServletResponse response
    ) {
        filesService.downloadFile(fileId, response);
    }

    @DeleteMapping("/delete/{fileId}")
    public RestResp<Void> deleteExerciseFile(@PathVariable("fileId") Long fileId) {
        return filesService.deleteFile(fileId);
    }

    @GetMapping("/preview/{fileId}")
    public RestResp<String> previewFile(@PathVariable Long fileId) {
        return filesService.previewFile(fileId);
    }

    @GetMapping("/recent/{limit}")
    public RestResp<List<FileInfoRespDto>> getRecentFiles(@PathVariable("limit") int limit) {
        return filesService.getRecentFiles(limit);
    }
}
