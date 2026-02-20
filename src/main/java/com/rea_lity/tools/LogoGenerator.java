package com.rea_lity.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.http.HttpUtil;
import com.rea_lity.COS.COSManager;
import com.rea_lity.config.CustomConfig;
import com.rea_lity.modle.ImageResource;
import com.rea_lity.modle.enums.ImageCategoryEnum;
import com.rea_lity.utils.SpringContextUtils;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.model.output.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Collections;
import java.util.List;

@Slf4j
public class LogoGenerator {

    @Tool("根据你的描述生成一张logo图片")
    public List<ImageResource> generateLogo(@P("对logo图片的详细描述")String description) {
        CustomConfig customConfig = SpringContextUtils.getBean(CustomConfig.class);

        log.info("开始生成logo图片...");
        WanxImageModel wanxImageModel = WanxImageModel.builder()
                .apiKey(customConfig.getDASHSCOPE_API_KEY())
                .modelName("wanx2.1-t2i-plus")
                .watermark(false)
                .build();

        // 上传到 COS
        try {
//            // 获取生成的图片
//            Response<List<Image>> generated = wanxImageModel.generate(description, 1);
//            Image image = generated.content().getFirst();
//
//            // 存入暂时的 文件
//            String imageUrl = image.url().toURL().toString();
//            log.info("logo图片生成成功:{}", imageUrl);
//
//            File tempFile = FileUtil.createTempFile(UUID.fastUUID().toString(), ".png", true);
//            HttpUtil.downloadFile(imageUrl, tempFile);
//            log.info("logo图片保存成功:{}", tempFile.getAbsolutePath());
//
//            COSManager cosManager = SpringContextUtils.getBean(COSManager.class);
//            String cosUrl = cosManager.uploadFile(tempFile.getName(), tempFile);
            String cosUrl = "https://pic1.zhimg.com/v2-e457ec1139d2d84a8dfd516da773cbb4_b.jpg";
//            boolean delete = tempFile.delete();
//            if(!delete) {
//                log.error("删除临时文件失败: {}", tempFile.getAbsolutePath());
//            }
            return Collections.singletonList(ImageResource.builder()
                    .imageCategory(ImageCategoryEnum.LOGO)
                    .imageDescription(description)
                    .imageUrl(cosUrl)
                    .imageName(description)
                    .build());
        } catch (Exception e) {
            log.error("生成logo图片失败: {}", e.getMessage(), e);
            return List.of();
        }

    }
}
