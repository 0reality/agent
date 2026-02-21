package com.rea_lity.AiService;

import com.rea_lity.modle.enums.RouterEnums;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface RouterNodeService {
    @SystemMessage(fromResource = "prompt/router-prompt.txt")
    RouterEnums route(@MemoryId Long memoryId, @UserMessage("{{it}}")String userPrompt);
}
