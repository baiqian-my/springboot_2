package com.liuluyi.springbootweek03.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/hutool")
public class HutoolController {

    @GetMapping("/id")
    public Map<String, String> generateId() {
        Map<String, String> response = new HashMap<>();
        String uuid = IdUtil.fastSimpleUUID();
        response.put("uuid", uuid);
        return response;
    }

    @GetMapping("/md5")
    public Map<String, Object> md5(@RequestParam String text) {
        Map<String, Object> response = new HashMap<>();
        String md5 = SecureUtil.md5(text);
        response.put("text", text);
        response.put("md5", md5);
        return response;
    }

    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        try {
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String uniqueFileName = IdUtil.fastSimpleUUID() + extension;
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            FileUtil.mkdir(uploadDir);
            String savePath = uploadDir + uniqueFileName;

            File destFile = new File(savePath);
            file.transferTo(destFile);

            response.put("success", true);
            response.put("originalFileName", originalFilename);
            response.put("uniqueFileName", uniqueFileName);
            response.put("savePath", savePath);

        } catch (IOException e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }

        return response;
    }
}