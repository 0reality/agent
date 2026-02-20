package com.rea_lity.AiService;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;

public interface VueProjectGeneratorService {
    @SystemMessage(fromResource = "prompt/codegen-vue-project-system-prompt.txt")
    String generateVueProject(@MemoryId Long memoryId, @UserMessage String userPrompt);

    @SystemMessage(fromResource = "prompt/codegen-vue-project-system-prompt.txt")
    TokenStream generateVueProjectStream(@MemoryId Long memoryId, @UserMessage String userPrompt);
}
