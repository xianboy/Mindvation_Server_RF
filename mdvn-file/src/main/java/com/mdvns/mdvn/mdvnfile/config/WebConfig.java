package com.mdvns.mdvn.mdvnfile.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Papi 调用SAPI的相关URL配置
 */

@ConfigurationProperties(prefix = "url")
@Component
public class WebConfig {

}
