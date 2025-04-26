package org.example.langchain4j.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel",//找到对应的bean进行绑定
        chatMemoryProvider = "chatMemoryProviderXiaozhi",//找到对应的bean进行绑定
        tools = "appointmentTools",//找到对应的bean进行绑定
        contentRetriever = "contentRetrieverXiaozhi"
)
public interface XiaozhiAgent {

    @SystemMessage(fromResource = "prompts/xiaozhi-prompt-template.txt")
    String chat(@MemoryId int memoryId, @UserMessage String userMessage);
}
