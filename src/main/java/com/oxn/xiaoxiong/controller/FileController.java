package com.oxn.xiaoxiong.controller;

import com.oxn.xiaoxiong.common.BaseResponse;
import com.oxn.xiaoxiong.common.ResultUtils;
import com.oxn.xiaoxiong.enums.StatusCode;
import com.oxn.xiaoxiong.exception.ThrowUtils;
import com.oxn.xiaoxiong.util.TencentCOSUtil;
import com.qcloud.cos.model.UploadResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Tag(name = "文件管理")
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private TencentCOSUtil tencentCOSUtil;

    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(@RequestParam MultipartFile file) {
        ThrowUtils.throwIf(file.isEmpty(), StatusCode.PARAMS_ERROR, "文件不能为空");
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String key = UUID.randomUUID() + extension;

            try (InputStream in = file.getInputStream()) {
                String resultKey = tencentCOSUtil.uploadStream(key, in, file.getSize(),
                        file.getContentType());
                return ResultUtils.success(resultKey);
            }

        } catch (Exception e) {
            ThrowUtils.throwIf(true, StatusCode.SYSTEM_ERROR, "文件上传失败: " + e.getMessage());
        }
        return null;
    }

    @Operation(summary = "下载文件")
    @GetMapping("/download")
    public ResponseEntity<org.springframework.core.io.Resource> downloadFile(@RequestParam String key)
            throws IOException {
        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("cos_download_", "");
        tencentCOSUtil.downloadFile(key, tempFile.toFile());

        org.springframework.core.io.Resource resource = new FileSystemResource(tempFile.toFile());
        String filename = key.substring(key.lastIndexOf("/") + 1);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}