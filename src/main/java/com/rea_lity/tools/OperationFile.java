package com.rea_lity.tools;

import cn.hutool.core.io.FileUtil;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;

import java.nio.file.Paths;

public class OperationFile {

    private static final String ROOT_PATH = System.getProperty("user.dir") + "/userCode/";

    @Tool("return the content of file")
    public String readFile(@P("the path of file") String filePath) {
        filePath = ROOT_PATH + filePath;
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "file error" + e.getMessage();
        }
    }

    @Tool("write content to file")
    public String writeFile(@P("the path of file") String filePath, @P("the content") String content) {
        filePath = ROOT_PATH + filePath;
        try {
            FileUtil.writeBytes(content.getBytes(), filePath);
            return "write file success";
        } catch (Exception e) {
            return "write file error" + e.getMessage();
        }
    }

    @Tool("delete file")
    public String deleteFile(@P("the path of file") String filePath) {
        filePath = ROOT_PATH + filePath;
        try {
            FileUtil.del(filePath);
            return "delete file success";
        } catch (Exception e) {
            return "delete file error" + e.getMessage();
        }
    }

    @Tool("rename file")
    public String renameFile(@P("the path of file") String filePath, @P("the new name") String newName) {
        filePath = ROOT_PATH + filePath;
        try {
            FileUtil.rename(Paths.get(filePath), newName, true);
            return "rename file success";
        } catch (Exception e) {
            return "rename file error" + e.getMessage();
        }
    }

    @Tool("move file")
    public String moveFile(@P("the path of file") String filePath, @P("the new path") String newPath) {
        filePath = ROOT_PATH + filePath;
        newPath = ROOT_PATH + newPath;
        try {
            FileUtil.move(Paths.get(filePath), Paths.get(newPath), true);
            return "move file success";
        } catch (Exception e) {
            return "move file error" + e.getMessage();
        }
    }

    @Tool("copy file")
    public String copyFile(@P("the path of file") String filePath, @P("the new path") String newPath) {
        filePath = ROOT_PATH + filePath;
        newPath = ROOT_PATH + newPath;
        try {
            FileUtil.copy(Paths.get(filePath), Paths.get(newPath));
            return "copy file success";
        } catch (Exception e) {
            return "copy file error";
        }
    }
}
