package com.rea_lity.factory;

import com.rea_lity.AiService.ProjectDesignService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectDesignServiceFactoryTest {

    @Resource
    private ProjectDesignService projectDesignService;

    @Test
    void projectDesignService() {
        String design = projectDesignService.design(1L,"我需要一个博客项目来展示我的博客");
        System.out.println(design);
    }
}