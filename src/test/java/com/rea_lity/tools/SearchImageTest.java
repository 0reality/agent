package com.rea_lity.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class SearchImageTest {

    @Test
    void searchImage() {
        SearchImage searchImage = new SearchImage();
        String result = searchImage.searchImage("二次元");
        System.out.println(result);
    }
}