package com.rea_lity.tools;

import dev.langchain4j.agent.tool.Tool;

public class ExistTool {

    @Tool("退出工具调用")
    public String exit() {
        return "任务完成，退出";
    }
}
