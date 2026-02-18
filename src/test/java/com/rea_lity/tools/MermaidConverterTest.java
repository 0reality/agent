package com.rea_lity.tools;

import com.rea_lity.modle.ImageResource;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class MermaidConverterTest {

    @Test
    void generateMermaidDiagram() {
        MermaidConverter mermaidConverter = new MermaidConverter();
        List<ImageResource> imageResources = mermaidConverter.generateMermaidDiagram("graph LR\n" +
                "A[Christmas] -->|Get money| B(Go shopping)\n" +
                "B --> C{Lots of fun?}\n" +
                "C -->|No| D[Sad face]\n" +
                "C -->|Yes| E[Happy face]", "测试");
        System.out.println(imageResources);
        System.out.println("测试完成");
    }
}