package org.example.langchain4j;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.FileSystemDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.onnx.HuggingFaceTokenizer;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RAGTest {

    @Test
    public void testReadDocument() {
        //使用FileSystemDocumentLoader读取指定目录下的知识库文档
        // 并使用默认的文档解析器TextDocumentParser对文档进行解析
        Document document = FileSystemDocumentLoader.loadDocument("src/main/resources/knowledge/测试.txt");
        System.out.println(document.text());
    }

    @Test
    public void testParsePDF() {
        Document document = FileSystemDocumentLoader.loadDocument("src/main/resources/knowledge/医院信息.pdf", new ApachePdfBoxDocumentParser());
        System.out.println(document);
    }

    /**
     * 加载文档并存入向量数据库
     */
    @Test
    public void testReadDocumentAndStore() {
        // 加载文档
        Document document = FileSystemDocumentLoader.loadDocument("src/main/resources/knowledge/人工智能.md");

        // 创建内存向量存储
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        // 文档分割与嵌入生成
        EmbeddingStoreIngestor.ingest(document, embeddingStore);

        // 查看向量存储内容
        System.out.println(embeddingStore);

    }

    @Test
    public void testDocumentSplitter() {
        // 加载文档
        Document document = FileSystemDocumentLoader.loadDocument("src/main/resources/knowledge/人工智能.md");

        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        DocumentByParagraphSplitter documentSplitter = new DocumentByParagraphSplitter(
                300, 30, new HuggingFaceTokenizer());
        EmbeddingStoreIngestor
                .builder()
                .embeddingStore(embeddingStore)
                .documentSplitter(documentSplitter)
                .build()
                .ingest(document);
        System.out.println(embeddingStore);
    }
}
