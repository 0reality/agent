package com.rea_lity.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DirectoryReaderTest {

    @Test
    void readDirectory() {
        System.out.println(new DirectoryReader().readDirectory(1L, "/"));
    }
}