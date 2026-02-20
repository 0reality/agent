package com.rea_lity.tools;

import cn.hutool.core.util.RuntimeUtil;
import com.rea_lity.constant.CommonConstant;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DirectoryReader {

    @Tool("读取指定目录下的文件结构")
    public String readDirectory(@ToolMemoryId Long memoryId, @P("相对路径,例如：/")String path) {
        path = getPath(memoryId, path).replace("/", "\\");
        log.info("开始读取目录:{}", path);
        try {
            Process process = RuntimeUtil.exec("cmd", "/c", "tree " + path, "/F");
            String result = RuntimeUtil.getResult(process);
            log.info("读取目录成功:{}", result);
            return result;
        } catch (Exception e) {
            log.error("读取目录失败: ", e);
            return "读取目录失败: " + e.getMessage();
        }
    }

    private String getPath(Long memoryId, String path) {
        return CommonConstant.ROOT_PATH + memoryId + path;
    }
}
