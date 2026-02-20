package com.rea_lity.tools;

import cn.hutool.system.SystemUtil;
import com.rea_lity.constant.CommonConstant;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agent.tool.ToolMemoryId;
import dev.langchain4j.service.MemoryId;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;


@Slf4j
public class VueProjectBuilder {

    @Tool("将 Vue 项目Build")
    public String build(@ToolMemoryId Long memoryId, @P("相对路径，例如：/")String path) {
        // 构建项目
        String fullPath = CommonConstant.ROOT_PATH + memoryId + path;
        log.info("构建项目路径：{}", fullPath);
        try {
            // 检查目录是否存在
            File projectDir = new File(fullPath);
            if (!projectDir.exists()) {
                return "项目目录不存在: " + fullPath;
            }
            
            // 异步执行构建命令，设置超时
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (SystemUtil.getOsInfo().isWindows()) {
                processBuilder.command("cmd", "/c", "cd /d " + fullPath + " && npm install && npm run build");
            } else {
                processBuilder.command("bash", "-c", "cd " + fullPath + " && npm install && npm run build");
            }
            
            processBuilder.directory(projectDir);
            Process process = processBuilder.start();
            
            // 设置5分钟超时
            boolean finished = process.waitFor(300, TimeUnit.SECONDS);
            if (!finished) {
                process.destroyForcibly();
                return "构建超时，已强制终止";
            }
            
            // 读取输出
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                log.info("构建成功完成");
                return "构建成功完成\n" + output.toString();
            } else {
                log.error("构建失败，退出码: {}", exitCode);
                return "构建失败，退出码: " + exitCode + "\n" + output.toString();
            }
        } catch (Exception e) {
            log.error("构建命令执行失败：", e);
            return "构建命令执行失败：" + e.getMessage();
        }
    }

    @Tool("将 Vue 项目运行起来")
    public String start(@ToolMemoryId Long memoryId, @P("相对路径，例如：/")String path) {
        // 启动项目
        String fullPath = CommonConstant.ROOT_PATH + memoryId + path;
        log.info("启动项目路径：{}", fullPath);
        try {
            // 检查目录是否存在
            File projectDir = new File(fullPath);
            if (!projectDir.exists()) {
                return "项目目录不存在: " + fullPath;
            }
            
            // 异步执行启动命令，设置超时
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (SystemUtil.getOsInfo().isWindows()) {
                processBuilder.command("cmd", "/c", "cd /d " + fullPath + " && npm install && npm run dev");
            } else {
                processBuilder.command("bash", "-c", "cd " + fullPath + " && npm install && npm run dev");
            }
            
            processBuilder.directory(projectDir);
            Process process = processBuilder.start();
            
            // 设置2分钟超时（开发服务器启动通常较快）
            boolean finished = process.waitFor(120, TimeUnit.SECONDS);
            if (!finished) {
                // 开发服务器通常是长期运行的，所以不强制终止
                log.info("开发服务器已在后台启动");
                return "开发服务器已在后台启动，请访问相应端口查看";
            }
            
            // 读取输出
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
            
            int exitCode = process.exitValue();
            if (exitCode == 0) {
                log.info("项目启动成功");
                return "项目启动成功\n" + output.toString();
            } else {
                log.error("项目启动失败，退出码: {}", exitCode);
                return "项目启动失败，退出码: " + exitCode + "\n" + output.toString();
            }
        } catch (Exception e) {
            log.error("启动命令执行失败：", e);
            return "启动命令执行失败：" + e.getMessage();
        }
    }
}
