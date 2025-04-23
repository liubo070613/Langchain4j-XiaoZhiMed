# 1.创建`Springboot`项目

## 添加依赖

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.6</version>
        <relativePath/>
    </parent>
    <groupId>org.example</groupId>
    <artifactId>langchain4j</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>langchain4j</name>
    <description>A Spring Boot project for LangChain integration</description>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <spring-boot.version>3.2.6</spring-boot.version>
        <knife4j.version>4.3.0</knife4j.version>
        <langchain4j.version>1.0.0-beta3</langchain4j.version>
        <mybatis-plus.version>3.5.11</mybatis-plus.version>
    </properties>

    <dependencies>
        <!-- Spring Boot 核心依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <!-- Web 应用程序依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <!-- 测试依赖 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- Knife4j 依赖 -->
        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
            <version>${knife4j.version}</version>
        </dependency>

        <dependency>
            <groupId>dev.langchain4j</groupId>
            <artifactId>langchain4j-open-ai</artifactId>
            <version>1.0.0-beta3</version>
        </dependency>
    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <!--引入langchain4j依赖管理清单-->
            <dependency>
                <groupId>dev.langchain4j</groupId>
                <artifactId>langchain4j-bom</artifactId>
                <version>${langchain4j.version}</version>
                <type>pom</type>
                <scope>import</scope>

            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

`Knife4j` 是一个 **增强版的 `Swagger UI`**，用于在 `Java` 项目（尤其是 `Spring Boot`）中自动生成 **接口文档**。

启动之后访问 http://localhost:8080/doc.html 查看程序能否成功运行并显示如下页面

![image-20250423104051494](./assets/image-20250423104051494.png)

## 测试`Langchain4j`

写一个测试类

```java
@SpringBootTest
class Langchain4jApplicationTests {

    @Test
    void contextLoads() {

        //初始化模型 OpenAiChatModel
        OpenAiChatModel model = OpenAiChatModel.builder()
                //阿里云千问大模型
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .apiKey("") //设置模型apiKey
                .modelName("qwen-plus") //设置模型名称
                .build();
        //向模型提问
        String answer = model.chat("你好");
        //输出结果
        System.out.println(answer);
    }

}
```

## Springboot集成

对于 OpenAI（`langchain4j-open-ai`），依赖项名称将是`langchain4j-open-ai-spring-boot-starter`：

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-spring-boot-starter</artifactId>
    <version>1.0.0-beta3</version>
</dependency>
```

然后，您可以在文件中配置模型参数，`application.properties`如下所示：

```ini
# 设置语言模型的API密钥和模型名称
langchain4j.open-ai.chat-model.base-url=https://dashscope.aliyuncs.com/compatible-mode/v1
langchain4j.open-ai.chat-model.api-key=你的key
langchain4j.open-ai.chat-model.model-name=qwen-plus
# 开启日志
langchain4j.open-ai.chat-model.log-requests=true
langchain4j.open-ai.chat-model.log-responses=true
#启用日志debug级别
logging.level.root=debug
```

在这种情况下，将自动创建一个实例`OpenAiChatModel`（一个实现），并且您可以在需要的地方注入该实例

```java
    @Autowired
    private OpenAiChatModel openAiChatModel;

    @Test
    public void testSpringBoot() {
        //向模型提问
        String answer = openAiChatModel.chat("你好");
        //输出结果
        System.out.println(answer);
    }
```

# 2.接入其他大模型

`LangChain4j`支持接入的大模型： https://docs.langchain4j.dev/integrations/language-models/

## 接入DeepSeek

访问官网： https://www.deepseek.com/ 注册账号，获取`base_url`和`api_key`，充值

然后`application.properties`配置

```ini
#DeepSeek 
langchain4j.open-ai.chat-model.base-url=https://api.deepseek.com 
langchain4j.open-ai.chat-model.api-key=${DEEP_SEEK_API_KEY} 
#DeepSeek-V3 
langchain4j.open-ai.chat-model.model-name=deepseek-chat 
#DeepSeek-R1 推理模型 
#langchain4j.open-ai.chat-model.model-name=deepseek-reasoner
```

## 接入阿里百炼平台

官网注册获取APIKEY

添加依赖

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-community-dashscope-spring-boot-starter</artifactId>
</dependency>

<dependencyManagement>
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j-community-bom</artifactId>
        <version>${langchain4j.version}<</version>
        <typ>pom</typ>
        <scope>import</scope>
    </dependency>
</dependencyManagement>
```

配置参数

```ini
#阿里百炼平台 
langchain4j.community.dashscope.chat-model.api-key=${DASH_SCOPE_API_KEY} langchain4j.community.dashscope.chat-model.model-name=qwen-plus
```

测试

```java
  @Autowired
  private QwenChatModel qwenChatModel;

  @Test
  public void testDashScopeQwen() {
      //向模型提问
      String answer = qwenChatModel.chat("你好");
      //输出结果
      System.out.println(answer);
  }
```

# 3.人工智能服务`AIService`

## 引入依赖

```xml
 <!--langchain4j高级功能-->
  <dependency>
      <groupId>dev.langchain4j</groupId>
      <artifactId>langchain4j-spring-boot-starter</artifactId>
  </dependency>
```

## 创建接口

使用`@AiService`注解，它可能用于标记一个接口，使其被框架（如 `langchain4j`）自动处理，生成 AI 服务的实现。  

- **动态代理**：框架会基于该接口生成代理类，处理方法调用（如 `chat(String message)`）。      
-  **依赖注入**：标记的接口会被 Spring 容器管理，允许通过 `@Autowired` 或其他方式注入。      
-  **AI 功能集成**：注解会将接口与 AI 模型（如 OpenAI 或其他语言模型）绑定，自动处理请求和响应。      

```java
@AiService
//如果你有很多AI模型实例，可用自定义绑定哪个模型
//@AiService(wiringMode = EXPLICIT, chatModel = "qwenChatModel")
public interface Assistant {
    String chat(String message);
}
```

## 测试

```
  @Autowired
  private Assistant assistant;

  @Test public void testAssistant() {
      String answer = assistant.chat("Hello");
      System.out.println(answer);
  }
```

## 工作原理

`AiServices`会组装`Assistant`接口以及其他组件，并使用**反射机制**创建一个实现`Assistant`接口的代理对象。 这个代理对象会**处理输入和输出**的所有转换工作。在这个例子中，`chat`方法的输入是一个字符串，但是大 模型需要一个 `UserMessage` 对象。所以，代理对象将这个字符串转换为 `UserMessage` ，并调用聊天语 言模型。`chat`方法的输出类型也是字符串，但是大模型返回的是 `AiMessage` 对象，代理对象会将其转换 为字符串。

# 聊天记忆 `Chat memory`

## 使用`ChatMemory`实现聊天记忆

`ChatMemory`接口的定义如下：

```java
public interface ChatMemory {
    Object id();
		//添加消息
    void add(ChatMessage var1);
		//消息集合，存储历史消息
    List<ChatMessage> messages();
		//清空消息
    void clear();
}
```

`ChatMemory`有两个实现

- `MessageWindowChatMemory`：基于消息数量的滑动窗口，保留最近的 `N` 条消息。
- `TokenWindowChatMemory`：基于 `token` 数量的滑动窗口，保留最近的 `N` 个 `token`。

以下是`MessageWindowChatMemory`使用示例：

```java
@Test
public void testChatMemory3() {

    //创建chatMemory
    MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);
    //创建AIService
    Assistant assistant = AiServices
            .builder(Assistant.class)
            .chatLanguageModel(openAiChatModel)
            .chatMemory(chatMemory)
            .build();
    //调用service的接口
    String answer1 = assistant.chat("我是环环");
    System.out.println(answer1);
    String answer2 = assistant.chat("我是谁");
    System.out.println(answer2);
}
```

## 结合`AIService`实现聊天记忆

### 创建记忆对话智能体

当`AIService`由多个组件（大模型，聊天记忆等）组成的时候，我们就可以称他为 **智能体** 了

```java
@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel",
        chatMemory = "chatMemory"
)
public interface MemoryChatAssistant {
    String chat(String message);
}
```

### 配置ChatMemory

```java
@Configuration
public class MemoryChatAssistantConfig {

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.withMaxMessages(10);
    }
}
```

### 测试

```java
@Autowired
private MemoryChatAssistant memoryChatAssistant;

@Test
public void testChatMemory4() {
    String answer1 = memoryChatAssistant.chat("我是环环");
    System.out.println(answer1);
    String answer2 = memoryChatAssistant.chat("我是谁");
    System.out.println(answer2);
}
```

## 隔离聊天记忆

隔离不同的聊天，比如我想开启一个新的聊天

### 创建记忆隔离对话智能体

```java
@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel",//找到对应的bean进行绑定
        chatMemory = "chatMemory",//找到对应的bean进行绑定
        chatMemoryProvider = "chatMemoryProvider"//找到对应的bean进行绑定
)
public interface SeparateChatAssistant {
    String chat(@MemoryId int memoryId, @UserMessage String userMessage);
}
```

为每个用户或会话提供独立的 `ChatMemory` 实例，根据提供的 `memoryId`（通常是用户 ID 或会话 ID）返回对应的 `ChatMemory` 实例。

- `@MemoryId` 注解用于标识方法参数，该参数的值将作为 `memoryId` 传递给 `chatMemoryProvider`，以获取对应的 `ChatMemory` 实例。
- `@UserMessage` 注解用于标识方法参数，该参数的值将作为用户消息发送给大语言模型（LLM）。

### 配置`ChatMemoryProvider`

```java
@Configuration
public class SeparateChatAssistantConfig {
    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(10)
                .build();
      //看不懂这种写法的，可以看下面这种
        return new ChatMemoryProvider() {
                  @Override
                  public ChatMemory get(Object memoryId) {
                      return MessageWindowChatMemory.builder()
                          .id(memoryId)
                          .maxMessages(10)
                          .build();
                  }
              }
      
    }
}
```

`ChatMemoryProvider`：这是一个函数式接口，定义了如何根据 `memoryId`（通常是用户 ID 或会话 ID）提供对应的 `ChatMemory` 实例。定义如下：

```java
@FunctionalInterface
public interface ChatMemoryProvider {
    ChatMemory get(Object var1);
}
```

每当有新的对话请求时，LangChain4j 会调用 `chatMemoryProvider` 的 `get` 方法，传入当前的 `memoryId`，以获取对应的 `ChatMemory` 实例。

这意味着，对于每个不同的 `memoryId`，都会有一个独立的对话记忆实例，确保多用户或多会话场景下的对话上下文不会混淆。

### 测试

```java
@Autowired
private SeparateChatAssistant separateChatAssistant;

@Test
public void testChatMemory5() {

    String answer1 = separateChatAssistant.chat(1, "我是环环");
    System.out.println(answer1);
    String answer2 = separateChatAssistant.chat(1, "我是谁");
    System.out.println(answer2);
    String answer3 = separateChatAssistant.chat(2, "我是谁");
    System.out.println(answer3);

}
```

