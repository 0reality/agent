package com.rea_lity.AiService;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface ProjectDesignService {

    @SystemMessage(fromResource = "prompt/project-design-prompt.txt")
    String design(@MemoryId Long memoryId, @UserMessage String userPrompt);

}
