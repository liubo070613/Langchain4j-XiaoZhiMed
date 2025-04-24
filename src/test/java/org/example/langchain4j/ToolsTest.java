package org.example.langchain4j;

import org.example.langchain4j.service.SeparateChatAssistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class ToolsTest {

    @Autowired
    private SeparateChatAssistant separateChatAssistant;

//    @Test
//    public void testCalculatorTools() {
//
//        String answer = separateChatAssistant.chat(4, "1+2等于几，475695037565的平方根是多少？",
//                LocalDateTime.now().toString());
//
//        //答案：3，689706.4865
//        System.out.println(answer);
//
//    }
}
