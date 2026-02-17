package com.rea_lity.factory;

import com.rea_lity.AiService.RouterNodeService;
import com.rea_lity.modle.enums.RouterEnums;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RouterNodeServiceFactoryTest {

    @Resource
    private RouterNodeServiceFactory routerNodeServiceFactory;

    @Resource
    private RouterNodeService routerNodeService;

    @Test
    void createRouterNodeService() {
        RouterEnums route = routerNodeService.route(1L,"我是rea_lity");
        System.out.println(route);
        RouterEnums route1 = routerNodeService.route(1L,"我是谁？");
        System.out.println(route1);
    }
}