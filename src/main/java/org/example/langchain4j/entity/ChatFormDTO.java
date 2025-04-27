package org.example.langchain4j.entity;

import lombok.Data;

//聊天数据传输对象
@Data
public class ChatFormDTO {
    //会话id
    private int memoryId;
    //用户消息
    private String message;

}
