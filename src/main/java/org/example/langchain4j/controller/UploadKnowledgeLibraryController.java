package org.example.langchain4j.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.langchain4j.service.UploadKnowledgeLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "上传知识库")
@RestController
@RequestMapping("/documents")
public class UploadKnowledgeLibraryController {

    @Autowired
    private UploadKnowledgeLibraryService uploadKnowledgeLibraryService;

    @PostMapping("/upload")
    public String uploadKnowledgeLibrary(MultipartFile[] files) {
        uploadKnowledgeLibraryService.uploadKnowledgeLibrary(files);
        return "上传成功";
    }

}
