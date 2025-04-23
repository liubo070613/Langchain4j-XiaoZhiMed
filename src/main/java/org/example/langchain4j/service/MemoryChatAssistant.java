package org.example.langchain4j.service;

import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel",//找到对应的bean进行绑定
        chatMemory = "chatMemory"//找到对应的bean进行绑定
)
public interface MemoryChatAssistant {
    String chat(String message);
}
