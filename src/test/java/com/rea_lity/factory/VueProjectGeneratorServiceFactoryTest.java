package com.rea_lity.factory;

import com.rea_lity.AiService.VueProjectGeneratorService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class VueProjectGeneratorServiceFactoryTest {

    @Resource
    VueProjectGeneratorService vueProjectGeneratorService;

    @Test
    void vueProjectGeneratorService() {
        vueProjectGeneratorService.generateVueProject(1L, "构建一个博客项目");
    }
}