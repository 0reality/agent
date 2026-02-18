package com.rea_lity.tools;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.rea_lity.config.CustomConfig;
import com.rea_lity.utils.SpringContextUtils;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SearchImage {

    @Tool("search image by image API")
    public String searchImage(@P("搜索的关键词")String query) {
        CustomConfig customConfig = SpringContextUtils.getBean(CustomConfig.class);
        String apiKey = customConfig.getPEXELS_API_KEY();

        String url = "https://api.pexels.com/v1/search";

        // 构建查询参数
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);

        try {
            HttpResponse response = HttpRequest.get(url)
                    .header("Authorization", apiKey)
                    .form(params)
                    .timeout(10000)
                    .execute();

            if (response.isOk()) {
                String body = response.body();
                log.info("请求成功，响应: {}", body);
                return "请求成功：" + body;
            } else {
                log.error("请求失败: {}", response);
                return "请求失败：" + response;
            }
        } catch (Exception e) {
            log.error("请求发生异常: ", e);
            return "请求发生异常：" + e.getMessage();
        }
    }
}
