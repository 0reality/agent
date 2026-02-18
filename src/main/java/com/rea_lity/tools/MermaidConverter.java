package com.rea_lity.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.rea_lity.COS.COSManager;
import com.rea_lity.common.ErrorCode;
import com.rea_lity.exception.BusinessException;
import com.rea_lity.modle.ImageResource;
import com.rea_lity.modle.enums.ImageCategoryEnum;
import com.rea_lity.utils.SpringContextUtils;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
public class MermaidConverter {

    @Tool("将MermaidCode转换为SVG图片")
    public List<ImageResource> generateMermaidDiagram(@P("MermaidCode")String mermaidCode, @P("描述")String description) {
        if (StrUtil.isBlank(mermaidCode)) {
            return List.of();
        }
        try {
            // 转换为SVG图片
            File diagramFile = convertMermaidToSvg(mermaidCode);

            String key = diagramFile.getName();

            // 上传到COS
            COSManager cosManager = SpringContextUtils.getBean(COSManager.class);
            String cosUrl = cosManager.uploadFile(key, diagramFile);

            return Collections.singletonList(ImageResource.builder()
                    .imageCategory(ImageCategoryEnum.ARCHITECTURE)
                    .imageDescription(description)
                    .imageUrl(cosUrl)
                    .imageName(description)
                    .build());
        } catch (Exception e) {
            log.error("生成架构图失败: {}", e.getMessage(), e);
        }
        return List.of();
    }

    /**
     * 将Mermaid代码转换为SVG图片
     */
    private File convertMermaidToSvg(String mermaidCode) {
        // 创建临时输入文件
        File tempInputFile = FileUtil.createTempFile(UUID.randomUUID().toString(), ".mmd", true);
        FileUtil.writeUtf8String(mermaidCode, tempInputFile);
        // 创建临时输出文件
        File tempOutputFile = FileUtil.createTempFile(UUID.randomUUID().toString(), ".svg", true);
        // 根据操作系统选择命令
        String command = SystemUtil.getOsInfo().isWindows() ? "mmdc.cmd" : "mmdc";
        // 构建命令
        String cmdLine = String.format("%s -i %s -o %s -b transparent",
                command,
                tempInputFile.getAbsolutePath(),
                tempOutputFile.getAbsolutePath()
        );
        // 执行命令
        RuntimeUtil.execForStr(cmdLine);
        // 检查输出文件
        if (!tempOutputFile.exists() || tempOutputFile.length() == 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Mermaid CLI 执行失败");
        }
        // 清理输入文件，保留输出文件供上传使用
        FileUtil.del(tempInputFile);
        return tempOutputFile;
    }
}
