package com.rea_lity.tools;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.rea_lity.config.CustomConfig;
import com.rea_lity.modle.ImageResource;
import com.rea_lity.modle.enums.ImageCategoryEnum;
import com.rea_lity.utils.SpringContextUtils;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SearchImage {

    @Tool("search image by image API")
    public List<ImageResource> searchImage(@P("搜索的关键词")String query) {
        CustomConfig customConfig = SpringContextUtils.getBean(CustomConfig.class);
        String apiKey = customConfig.getPEXELS_API_KEY();

        String url = "https://api.pexels.com/v1/search";

        List<ImageResource> result = new ArrayList<>();

        // 构建查询参数
        Map<String, Object> params = new HashMap<>();
        params.put("query", query);

        try (HttpResponse response = HttpRequest.get(url)
                .header("Authorization", apiKey)
                .form(params)
                .timeout(10000)
                .execute()){

            if (response.isOk()) {
                String body = response.body();
                log.info("请求成功，响应: {}", body);
                JSON parse = JSONUtil.parse(body);
                Integer photosCount = parse.getByPath("per_page", Integer.class);
                for(int i = 0; i < Math.min(photosCount, 5); i++) {
                    ImageResource imageResource = ImageResource.builder()
                            .imageCategory(ImageCategoryEnum.CONTENT)
                            .imageName(query)
                            .imageDescription(parse.getByPath("photos[" + i + "].alt", String.class))
                            .imageUrl(parse.getByPath("photos[" + i + "].src.medium", String.class))
                            .build();
                    result.add(imageResource);
                }
            } else {
                log.error("请求失败: {}", response);
            }
            return result;
        } catch (Exception e) {
            log.error("请求发生异常: ", e);
            return result;
        }
    }

    @Tool("从UnDraw网站上搜索插图")
    public List<ImageResource> searchIllustration(@P("搜索的关键词")String query) {
        String url = "https://undraw.co/search/" + query;
        List<ImageResource> result = new ArrayList<>();

        // 设置所有请求头
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/145.0.0.0 Safari/537.36 Edg/145.0.0.0");
        headers.put("accept", "*/*");
        headers.put("accept-encoding", "gzip, deflate, br, zstd");
        headers.put("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6");
        headers.put("priority", "u=1, i");
        headers.put("referer", "https://undraw.co/search/blog");
        headers.put("sec-ch-ua", "\"Not:A-Brand\";v=\"99\", \"Microsoft Edge\";v=\"145\", \"Chromium\";v=\"145\"");
        headers.put("sec-ch-ua-mobile", "?0");
        headers.put("sec-ch-ua-platform", "\"Windows\"");
        headers.put("sec-fetch-dest", "empty");
        headers.put("sec-fetch-mode", "cors");
        headers.put("sec-fetch-site", "same-origin");

        try (HttpResponse response = HttpRequest.get("https://undraw.co/api/search?q=blog&offset=80")
                .addHeaders(headers)
                .cookie("_ga=GA1.1.1830288588.1771390857; " +
                        "_ga_JWE2C95CR2=GS2.1.s1771390877$o1$g0$t1771390880$j57$l0$h0; " +
                        "_ga_K3G333FB4M=GS2.1.s1771390857$o1$g1$t1771393081$j55$l0$h0")
                .execute()) {
            log.info("请求成功，响应: {}", response);
            JSON parse = JSONUtil.parse(response.body());
            if (response.isOk()) {
                for(int i = 0; i < 5;i ++) {
                    ImageResource imageResource = ImageResource.builder()
                            .imageCategory(ImageCategoryEnum.ILLUSTRATION)
                            .imageName(query)
                            .imageDescription(parse.getByPath("results[" + i + "].title",String.class))
                            .imageUrl(parse.getByPath("results[" + i + "].media", String.class))
                            .build();
                    result.add(imageResource);
                }
            } else {
                log.error("请求失败: {}", response);
            }
            return result;
        } catch (Exception e) {
            log.error("请求发生异常: ", e);
            return result;
        }
    }
}
