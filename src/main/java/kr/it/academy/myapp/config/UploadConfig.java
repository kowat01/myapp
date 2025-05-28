package kr.it.academy.myapp.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "file")
public class UploadConfig {
    private String uploadDir;

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
