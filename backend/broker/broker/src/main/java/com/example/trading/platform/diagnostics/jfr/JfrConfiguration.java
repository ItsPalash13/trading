package com.example.trading.platform.diagnostics.jfr;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JfrProperties.class)
public class JfrConfiguration {
}
