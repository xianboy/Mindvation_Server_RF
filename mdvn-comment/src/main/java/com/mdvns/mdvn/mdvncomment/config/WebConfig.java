package com.mdvns.mdvn.mdvncomment.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "url")
@Data
@NoArgsConstructor
@Component
public class WebConfig {
    private String retrieveByIdUrl;
}
