package org.example.langchain4j;

import org.example.langchain4j.service.MemoryChatAssistant;
import org.example.langchain4j.service.SeparateChatAssistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class PromptTest {
    @Autowired
    private SeparateChatAssistant separateChatAssistant;

    @Test
    public void testSystemMessage() {

        String answer = separateChatAssistant.chat(7, "今天几号");

        System.out.println(answer);

    }

    @Autowired
    private MemoryChatAssistant memoryChatAssistant;

    @Test
    public void testUserMessage() {

        String answer = memoryChatAssistant.chat(LocalDateTime.now().toString());
        System.out.println(answer);
    }

}
