package org.example.langchain4j.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel",//找到对应的bean进行绑定
//        chatMemory = "chatMemory",//找到对应的bean进行绑定
        chatMemoryProvider = "chatMemoryProvider",//找到对应的bean进行绑定
        tools = "calculatorTools"//找到对应的bean进行绑定
)
public interface SeparateChatAssistant {
//    @SystemMessage("你是我的好朋友，请用湖南话回答问题。今天是{{current_date}}")
    @SystemMessage(fromResource = "prompts/assistant.txt")
    String chat(@MemoryId int memoryId, @UserMessage String userMessage);
}
