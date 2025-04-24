package org.example.langchain4j.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.langchain4j.entity.ChatFormDTO;
import org.example.langchain4j.service.XiaozhiAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "小智")
@RestController
@RequestMapping("/xiaozhi")
public class XiaozhiController {

    @Autowired
    private XiaozhiAgent xiaozhiAgent;

    @Operation(summary = "对话")
    @PostMapping("/chat")
    public String chat(@RequestBody ChatFormDTO chatFormDTO) {
        return xiaozhiAgent.chat(chatFormDTO.getMemoryId(), chatFormDTO.getUserMessage());
    }
}
