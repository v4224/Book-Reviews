package com.harinem.file_service.service;

import com.harinem.file_service.dto.response.FileData;
import com.harinem.file_service.dto.response.FileResponse;
import com.harinem.file_service.entity.FileMgmt;
import com.harinem.file_service.exception.AppException;
import com.harinem.file_service.exception.ErrorCode;
import com.harinem.file_service.mapper.FileMgmtMapper;
import com.harinem.file_service.repository.FileMgmtRepository;
import com.harinem.file_service.repository.FileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class FileService {

    FileRepository fileRepository;
    FileMgmtRepository fileMgmtRepository;

    FileMgmtMapper fileMgmtMapper;
    public FileResponse uploadFile(MultipartFile file) throws IOException {
        var fileInfo=fileRepository.store(file);

        var fileMgmt=fileMgmtMapper.toFileMgmt(fileInfo);

        String userId= SecurityContextHolder.getContext().getAuthentication().getName();
        fileMgmt.setOwnerId(userId);

        fileMgmt= fileMgmtRepository.save(fileMgmt);
        return FileResponse.builder()
                .url(fileInfo.getUrl())
                .originalFileName(file.getOriginalFilename())
                .build();
    }

    public FileData downloadFile(String fileName) throws IOException {
        FileMgmt fileMgmt = fileMgmtRepository.findById(fileName)
                .orElseThrow(() -> new AppException(ErrorCode.FILE_NOT_FOUND));

        log.info("Downloading file: {}", fileMgmt.getId());

        try {
            Resource resource= fileRepository.read(fileMgmt);
            return new FileData(fileMgmt.getContentType(),resource);
        } catch (IOException e) {
            log.error("Failed to read file: {}", fileMgmt.getId(), e);
            throw new AppException(ErrorCode.CAN_NOT_READ_FILE);
        }

    }
}
