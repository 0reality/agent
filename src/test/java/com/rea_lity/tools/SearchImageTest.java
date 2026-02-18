package com.rea_lity.tools;

import com.rea_lity.modle.ImageResource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class SearchImageTest {

    @Test
    void searchImage() {
        SearchImage searchImage = new SearchImage();
        List<ImageResource> resources = searchImage.searchImage("二次元");
        System.out.println(resources);
    }

    @Test
    void searchIllustration() {
        SearchImage searchImage = new SearchImage();
        List<ImageResource> resources = searchImage.searchIllustration("blog");
        System.out.println(resources);
    }
}