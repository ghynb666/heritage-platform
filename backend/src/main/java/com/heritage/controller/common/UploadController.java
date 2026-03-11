package com.heritage.controller.common;

import com.heritage.common.Result;
import com.heritage.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Api(tags = "公共上传接口")
@RestController
@RequestMapping("/api/common")
public class UploadController {
    @Value("${file.upload-path}")
    private String uploadPath;

    private static final List<String> IMAGE_TYPES = Arrays.asList("jpg", "jpeg", "png", "gif");
    private static final List<String> VIDEO_TYPES = Arrays.asList("mp4");
    private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;
    private static final long MAX_VIDEO_SIZE = 100 * 1024 * 1024;

    @ApiOperation("通用文件上传")
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }
        String url = saveFile(file, "files");
        return Result.success(url);
    }

    @ApiOperation("头像上传")
    @PostMapping("/upload/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        validateImage(file, "头像");
        String url = saveFile(file, "avatar");
        return Result.success(url);
    }

    @ApiOperation("图片上传")
    @PostMapping("/upload/image")
    public Result<List<String>> uploadImages(@RequestParam("files") MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new BusinessException("上传文件不能为空");
        }
        List<String> urls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            validateImage(file, "图片");
            urls.add(saveFile(file, "heritage/images"));
        }
        return Result.success(urls);
    }

    @ApiOperation("视频上传")
    @PostMapping("/upload/video")
    public Result<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }
        if (file.getSize() > MAX_VIDEO_SIZE) {
            throw new BusinessException("视频大小不能超过100MB");
        }
        String extension = getFileExtension(file.getOriginalFilename());
        if (!VIDEO_TYPES.contains(extension.toLowerCase())) {
            throw new BusinessException("仅支持 mp4 格式视频");
        }
        return Result.success(saveFile(file, "heritage/videos"));
    }

    private void validateImage(MultipartFile file, String typeName) {
        if (file.isEmpty()) {
            throw new BusinessException(typeName + "不能为空");
        }
        if (file.getSize() > MAX_IMAGE_SIZE) {
            throw new BusinessException(typeName + "大小不能超过5MB");
        }
        String extension = getFileExtension(file.getOriginalFilename());
        if (!IMAGE_TYPES.contains(extension.toLowerCase())) {
            throw new BusinessException("仅支持 jpg/jpeg/png/gif 格式图片");
        }
    }

    private String saveFile(MultipartFile file, String subPath) {
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String fileName = UUID.randomUUID().toString().replace("-", "") + "." + extension;
        String relativePath = subPath + "/" + datePath + "/" + fileName;
        File dest = new File(uploadPath + relativePath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new BusinessException("文件上传失败");
        }
        return "/uploads/" + relativePath;
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
