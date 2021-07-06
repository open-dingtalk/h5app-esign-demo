package com.aliyun.dingtalk.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "dingtalk")
public class AppConfig {

    private String suiteKey;

    private String suiteSecret;

    private String aesKey;

    private String token;

}
