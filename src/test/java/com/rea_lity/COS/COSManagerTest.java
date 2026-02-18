package com.rea_lity.COS;

import com.qcloud.cos.model.PutObjectResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class COSManagerTest {

    @Resource
    COSManager cosManager;

    @Test
    void putObject() {
        File file = new File("D:\\test\\competitions\\模板.cpp");
        PutObjectResult putObjectResult = cosManager.putObject("test.cpp", file);
        System.out.println(putObjectResult);
    }

    @Test
    void uploadFile() {
        File file = new File("D:\\test\\competitions\\模板.cpp");
        String putObjectResult = cosManager.uploadFile("test01.cpp", file);
        System.out.println(putObjectResult);
    }
}