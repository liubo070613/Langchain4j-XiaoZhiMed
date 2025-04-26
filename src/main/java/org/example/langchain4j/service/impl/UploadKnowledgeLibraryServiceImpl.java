package org.example.langchain4j.service.impl;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.example.langchain4j.service.UploadKnowledgeLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class UploadKnowledgeLibraryServiceImpl implements UploadKnowledgeLibraryService {

    @Autowired
    private EmbeddingStore<TextSegment> embeddingStore;

    @Autowired
    private EmbeddingModel embeddingModel;

    @Override
    public void uploadKnowledgeLibrary(MultipartFile[] files) {
        List<Document> documents = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    // 保存为临时文件
                    File tempFile = File.createTempFile("upload-", "-" + file.getOriginalFilename());
                    file.transferTo(tempFile);

                    // 根据文件类型选择适当的解析器
                    String fileName = file.getOriginalFilename();
                    Document document;

                    if (fileName != null && fileName.toLowerCase().endsWith(".pdf")) {
                        // 针对PDF文件使用专用解析器
                        document = FileSystemDocumentLoader.loadDocument(tempFile.getAbsolutePath(),
                                new ApachePdfBoxDocumentParser());
                    } else {
                        // 其他文件使用默认解析器
                        document = FileSystemDocumentLoader.loadDocument(tempFile.getAbsolutePath());
                    }

                    documents.add(document);

                    // 删除临时文件
                    tempFile.delete();
                } catch (IOException e) {
                    throw new RuntimeException("处理文件失败: " + file.getOriginalFilename(), e);
                }
            }
        }

        // 将文档存入向量数据库
        EmbeddingStoreIngestor
                .builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .build()
                .ingest(documents);
    }

}
