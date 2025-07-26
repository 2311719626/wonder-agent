package com.hezhaohui.agent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Configuration
public class PgVectorVectorStoreConfig {

    @Resource
    private DocumentLoader documentLoader;

//    Only use it when you need to split the document
//    @Resource
//    private MyTokenTextSplitter myTokenTextSplitter;

    @Bean
    public VectorStore pgVectorVectorStore(JdbcTemplate jdbcTemplate, EmbeddingModel dashscopeEmbeddingModel) {
        VectorStore vectorStore = PgVectorStore.builder(jdbcTemplate, dashscopeEmbeddingModel)
                .dimensions(1536)
                .distanceType(PgVectorStore.PgDistanceType.COSINE_DISTANCE)
                .indexType(PgVectorStore.PgIndexType.HNSW)
                .initializeSchema(true)
                .schemaName("agent")
                .vectorTableName("vector_store")
                .maxDocumentBatchSize(10000)
                .build();
        List<Document> documents = documentLoader.loadMarkdowns();
        //    Only use it when you need to split the document
        //    List<Document> splitDocuments = myTokenTextSplitter.splitCustomized(documents);
        //    vectorStore.add(splitDocuments);
        vectorStore.add(documents);
        return vectorStore;
    }
}
