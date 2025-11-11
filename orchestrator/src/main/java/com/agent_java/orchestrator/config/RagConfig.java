package com.agent_java.orchestrator.config;

import com.agent_java.orchestrator.utils.Constant;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RagConfig {

    @Autowired
    private VectorStore vectorStore;

    @Bean

    public VectorStoreDocumentRetriever retriever() {
        return VectorStoreDocumentRetriever
                .builder()
                .vectorStore(vectorStore)
                .topK(Constant.TOP_K)
                .build();
    }

    @Bean
    public QuestionAnswerAdvisor qaAdvisor() {
        return QuestionAnswerAdvisor
                .builder(vectorStore)
                .searchRequest(
                        SearchRequest
                                .builder()
                                .topK(Constant.TOP_K)
                                .build()
                ).build();
    }
}
