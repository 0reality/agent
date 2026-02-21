package com.rea_lity.AiService;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface ToolService {

    @SystemMessage("你是一个有利的助手")
    String tool(@MemoryId Long memoryId,@UserMessage("{{it}}")String input);
}
