package com.rea_lity.controller;

import com.rea_lity.modle.entity.App;
import com.rea_lity.service.AppService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
    @Resource
    private AppService appService;

    @PostMapping("/app")
    public App createApp(@RequestBody App app) {
        appService.save(app);
        return app;
    }

}
