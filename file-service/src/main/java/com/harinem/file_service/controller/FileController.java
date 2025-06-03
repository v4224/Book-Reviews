package com.harinem.file_service.controller;

import com.harinem.file_service.dto.ApiResponse;
import com.harinem.file_service.dto.response.FileResponse;
import com.harinem.file_service.service.FileService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class FileController {

    FileService fileService;

    @PostMapping("/media/upload")
    ApiResponse<FileResponse> uploadMedia(@RequestParam("file") MultipartFile file) throws IOException {
        return ApiResponse.<FileResponse>builder()
                .result(fileService.uploadFile(file))
                .build();

    }

    @GetMapping("/media/download/{fileName}")
    ResponseEntity<Resource> downloadMedia(@PathVariable String fileName) throws IOException {
        var data=fileService.downloadFile(fileName);
        return ResponseEntity.<Resource>ok()
                .header(HttpHeaders.CONTENT_TYPE, data.contentType())
                .header(HttpHeaders.CACHE_CONTROL, "max-age=1800, must-revalidate, no-transform")
                .body(data.resource());

    }
}
