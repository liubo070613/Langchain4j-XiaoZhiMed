package org.example.langchain4j;

import org.example.langchain4j.service.SeparateChatAssistant;
import org.example.langchain4j.service.XiaozhiAgent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class ToolsTest {

    @Autowired
    private SeparateChatAssistant separateChatAssistant;

    @Autowired
    private XiaozhiAgent xiaozhiAgent;

    @Test
    public void testCalculatorTools() {

        String answer = xiaozhiAgent.chat(15,"你们医院有哪些医生？");

        System.out.println(answer);

    }
}
