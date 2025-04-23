package org.example.langchain4j;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.example.langchain4j.service.Assistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Langchain4jApplicationTests {

    @Test
    void contextLoads() {

        //初始化模型 OpenAiChatModel
        //LangChain4j提供的代理服务器，该代理服务器会将演示密钥替换成真实密钥， 再将请求转 发给OpenAI API

        OpenAiChatModel model = OpenAiChatModel.builder()
                //设置模型api地址（如 果apiKey="demo"，则可省略baseUrl的配置）
                //阿里云千问大模型
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .apiKey("sk-a962d14acb334e2dbf4917fc4f2eef83") //设置模型apiKey
                .modelName("qwen-plus") //设置模型名称
                .build();

        //向模型提问
        String answer = model.chat("你好");
        //输出结果
        System.out.println(answer);
    }

    /**
     * 整合SpringBoot
     */
    @Autowired
    private OpenAiChatModel openAiChatModel;

    @Test
    public void testSpringBoot() {
        //向模型提问
        String answer = openAiChatModel.chat("你好");
        //输出结果
        System.out.println(answer);
    }

//    @Autowired
//    private QwenChatModel qwenChatModel;
//
//    @Test
//    public void testDashScopeQwen() {
//        //向模型提问
//        String answer = qwenChatModel.chat("你好");
//        //输出结果
//        System.out.println(answer);
//    }

    @Autowired
    private Assistant assistant;

    @Test public void testAssistant() {
        String answer = assistant.chat("Hello");
        System.out.println(answer);
    }

}
