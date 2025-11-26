package com.agent_java.orchestrator.dto;

import com.agent_java.orchestrator.entity.agent.knowledge.KnowledgeChunk;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.ai.document.Document;

@Data
@AllArgsConstructor
public class KnowledgeChunkResponseDto {

    UUID id;
    String content;
    Map<String, Object> metadata;

    public static KnowledgeChunkResponseDto from(KnowledgeChunk entity) {
        return new KnowledgeChunkResponseDto(entity.getId(), entity.getContent(), entity.getMetadata());
    }

    public static KnowledgeChunkResponseDto fromDocument(Document doc) {
        return new KnowledgeChunkResponseDto(UUID.fromString(doc.getId()), doc.getText(), doc.getMetadata());
    }
}
