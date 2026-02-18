package com.rea_lity.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LogoGeneratorTest {

    @Test
    void generateLogo() throws MalformedURLException {
        LogoGenerator logoGenerator = new LogoGenerator();
        logoGenerator.generateLogo("生成一张包含字母ROJ的logo图片");
        System.out.println("测试完成");
    }
}