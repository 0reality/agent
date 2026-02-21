package com.rea_lity.nodes;

import org.junit.jupiter.api.Test;

class CheckCodeNodeTest {
    @Test
    void apply() {
        CheckCodeNode checkCodeNode = new CheckCodeNode();
        System.out.println(checkCodeNode.getCode(12L));
    }
}