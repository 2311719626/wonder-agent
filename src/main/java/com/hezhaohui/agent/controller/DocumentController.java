package com.hezhaohui.agent.controller;

import cn.hutool.core.io.resource.ClassPathResource;
import com.hezhaohui.agent.rag.DocumentLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping("/document")
public class DocumentController {

    private final DocumentLoader documentLoader;
    private final Path uploadDir;
    public DocumentController(DocumentLoader documentLoader, Environment env) {
        this.documentLoader = documentLoader;
        String uploadPath = env.getProperty("upload.path", "tmp/document");
        this.uploadDir = Paths.get(uploadPath).toAbsolutePath();
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("无法创建上传目录", e);
        }
    }

    @PostMapping("/upload")
    public Result uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
                log.info("创建上传目录: {}", uploadDir);
            } else {
                log.info("上传目录已存在: {}", uploadDir);
            }

            // 保存文件到上传目录
            Path filePath = uploadDir.resolve(file.getOriginalFilename());
            file.transferTo(filePath.toFile());

            // 重新加载所有文档
            documentLoader.loadMarkdowns();

            return new Result(Code.SUCCESS, "文档上传成功: " + file.getName());
        } catch (IOException e) {
            return new Result(Code.ERROR, "文档上传失败: " + e.getMessage());
        }
    }
}
