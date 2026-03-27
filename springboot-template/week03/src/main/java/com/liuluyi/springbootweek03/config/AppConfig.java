package com.liuluyi.springbootweek03.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "app")
@Component
@Validated  // 添加@Validated注解以启用配置属性验证
public class AppConfig {
    @NotBlank(message = "app.name is required and cannot be blank")  // 为name属性添加@NotBlank校验
    private String name;
    private String version;
    private String description;
    private Boolean published;
    private List<String> features;
    private Author author;

    @Data
    public static class Author {
        private String name;
        private String email;
        private String website;
    }
}
