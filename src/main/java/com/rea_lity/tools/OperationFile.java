package com.rea_lity.tools;

import cn.hutool.core.io.FileUtil;
import com.rea_lity.constant.CommonConstant;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Paths;

@Slf4j
public class OperationFile {

    private static String getFullPath (Long memoryId, String filePath) {
        if(!filePath.startsWith("/")) filePath = "/" + filePath;
        return CommonConstant.ROOT_PATH + memoryId + filePath;
    }
    
    @Tool("return the content of file")
    public String readFile(@ToolMemoryId Long memoryId, @P("文件的相对路径") String filePath) {
        filePath = getFullPath(memoryId, filePath);
        log.info("开始读取文件:{}", filePath);
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "file error" + e.getMessage();
        }
    }

    @Tool("write content to file")
    public String writeFile(@ToolMemoryId Long memoryId, @P("文件的相对路径") String filePath, @P("the content") String content) {
        filePath = getFullPath(memoryId, filePath);
        log.info("开始写入文件:{}", filePath);
        try {
            FileUtil.writeBytes(content.getBytes(), filePath);
            return "write file success";
        } catch (Exception e) {
            return "write file error" + e.getMessage();
        }
    }

    @Tool("delete file")
    public String deleteFile(@ToolMemoryId Long memoryId, @P("文件的相对路径") String filePath) {
        filePath = getFullPath(memoryId, filePath);
        log.info("开始删除文件:{}", filePath);
        try {
            FileUtil.del(filePath);
            return "delete file success";
        } catch (Exception e) {
            return "delete file error" + e.getMessage();
        }
    }

    @Tool("rename file")
    public String renameFile(@ToolMemoryId Long memoryId, @P("文件的相对路径") String filePath, @P("the new name") String newName) {
        filePath = getFullPath(memoryId, filePath);
        log.info("开始重命名文件:{}", filePath);
        try {
            FileUtil.rename(Paths.get(filePath), newName, true);
            return "rename file success";
        } catch (Exception e) {
            return "rename file error" + e.getMessage();
        }
    }

    @Tool("move file")
    public String moveFile(@ToolMemoryId Long memoryId, @P("文件的相对路径") String filePath, @P("目标位置的相对路径") String newPath) {
        filePath = getFullPath(memoryId, filePath);
        newPath = getFullPath(memoryId, newPath);
        log.info("开始移动文件:{}", filePath);
        try {
            FileUtil.move(Paths.get(filePath), Paths.get(newPath), true);
            return "move file success";
        } catch (Exception e) {
            return "move file error" + e.getMessage();
        }
    }

    @Tool("copy file")
    public String copyFile(@ToolMemoryId Long memoryId, @P("文件的相对路径") String filePath, @P("目标位置的相对路径") String newPath) {
        filePath = getFullPath(memoryId, filePath);
        newPath = getFullPath(memoryId, newPath);
        log.info("开始复制文件:{}", filePath);
        try {
            FileUtil.copy(Paths.get(filePath), Paths.get(newPath));
            return "copy file success";
        } catch (Exception e) {
            return "copy file error";
        }
    }
}
