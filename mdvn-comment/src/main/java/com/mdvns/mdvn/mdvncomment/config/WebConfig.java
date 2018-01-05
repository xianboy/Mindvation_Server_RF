package com.mdvns.mdvn.mdvncomment.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "url")
@Data
@NoArgsConstructor
public class WebConfig {
    private String retrieveByIdUrl;
}
