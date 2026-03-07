package com.oxn.xiaoxiong.ai;

import dev.langchain4j.service.SystemMessage;

public interface AiCodeGeneratorService {
    @SystemMessage(fromResource = "prompt/info.txt")
    String chatWithAi(String userMessage);
}