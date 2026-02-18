package com.rea_lity.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class CustomConfig {
    @Value("${dashscope.api-key}")
    private String DASHSCOPE_API_KEY;

    @Value("${pexels.api-key}")
    private String PEXELS_API_KEY;
}
