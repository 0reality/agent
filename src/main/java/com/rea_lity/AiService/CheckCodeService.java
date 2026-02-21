package com.rea_lity.AiService;


import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;

public interface CheckCodeService {

    @SystemMessage(fromResource = "prompt/code-quality-check-system-prompt.txt")
    String checkCode(@UserMessage("{{it}}") String code);

    @SystemMessage(fromResource = "prompt/code-quality-check-system-prompt.txt")
    TokenStream checkStream(@UserMessage("{{it}}") String code);
}
