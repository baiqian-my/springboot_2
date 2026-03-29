package org.example.springbootweek04.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.example.springbootweek04.exception.BusinessException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Slf4j
public class FileUtils{

    private static final String UPLOAD_DIR = getUploadDir();

    static {
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new RuntimeException("创建上传目录失败：" + UPLOAD_DIR);
            }
        }
        log.info("上传目录初始化成功：{}", UPLOAD_DIR);
    }

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp",
            ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx",
            ".txt", ".md", ".csv",
            ".zip", ".rar", ".7z",
            ".json", ".xml"
    );

    private static String getUploadDir() {
        String osName = System.getProperty("os.name").toLowerCase();
        String baseDir;
        
        if (osName.contains("win")) {
            baseDir = System.getenv("TEMP");
        } else {
            baseDir = "/tmp";
        }
        
        Path uploadPath = Paths.get(baseDir, "springboot-uploads/");
        
        try {
            Files.createDirectories(uploadPath);
            String uploadDir = uploadPath.toAbsolutePath().toString();
            log.info("上传目录：{}", uploadDir);
            return uploadDir + "/";
        } catch (IOException e) {
            throw new RuntimeException("创建上传目录失败", e);
        }
    }

    public static String upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new BusinessException(400, "文件名不能为空");
        }

        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(suffix)) {
            throw new BusinessException(400, "不支持的文件类型: " + suffix);
        }

        String fileName = UUID.randomUUID() + suffix;
        File dest = new File(UPLOAD_DIR + fileName);
        file.transferTo(dest);
        return fileName;
    }
}