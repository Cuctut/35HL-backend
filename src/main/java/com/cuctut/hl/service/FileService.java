package com.cuctut.hl.service;

import org.springframework.stereotype.Service;

@Service
public class FileService {

    // private final FileMapper fileMapper;
    //
    //
    //

    // @GetMapping("/{fileName}")
    // public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String fileName) {
    //     try {
    //         InputStream stream = minioClient.getObject(GetObjectArgs.builder()
    //                 .bucket(MATERIALS_BUCKET_NAME)
    //                 .object(fileName)
    //                 .build());
    //         return ResponseEntity.ok()
    //                 .body(new InputStreamResource(stream));
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return ResponseEntity.status(500).build();
    //     }
    // }
}
