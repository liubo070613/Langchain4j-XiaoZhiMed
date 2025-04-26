package org.example.langchain4j.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadKnowledgeLibraryService {
    public void uploadKnowledgeLibrary(MultipartFile[] files);
}
