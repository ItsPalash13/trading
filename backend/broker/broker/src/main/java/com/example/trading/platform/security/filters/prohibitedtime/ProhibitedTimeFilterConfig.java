package com.example.trading.platform.security.filters.prohibitedtime;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ProhibitedTimeProperties.class)
public class ProhibitedTimeFilterConfig {
}
