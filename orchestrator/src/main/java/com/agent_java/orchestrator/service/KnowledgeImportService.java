package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.chunking.DocumentChunker;
import com.agent_java.orchestrator.exception.BadRequestException;
import com.agent_java.orchestrator.repository.AgentKnowledgeRepository;
import com.agent_java.orchestrator.viewmodel.KnowledgeImportingResponseVm;
import jakarta.persistence.EntityNotFoundException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class KnowledgeImportService {

    private final KnowledgeChunkService chunkService;
    private final AgentKnowledgeRepository knowledgeRepo;
    private final DocumentChunker documentChunker;

    @Autowired
    public KnowledgeImportService(KnowledgeChunkService chunkService, AgentKnowledgeRepository knowledgeRepo, DocumentChunker documentChunker) {
        this.chunkService = chunkService;
        this.knowledgeRepo = knowledgeRepo;
        this.documentChunker = documentChunker;
    }

    private final Logger logger = LoggerFactory.getLogger(KnowledgeImportService.class);

    @Transactional
    public KnowledgeImportingResponseVm importDocument(UUID agentId, UUID knowledgeId, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        logger.info("Importing document for agent {}: {}, file type: {}", agentId, fileName, file.getContentType());

        // Check that knowledge exists for this agent
        if (!knowledgeRepo.existsByIdAndAgentId(knowledgeId, agentId)) {
            throw new EntityNotFoundException("Knowledge " + knowledgeId + " not found for agent " + agentId);
        }

        // Split file into chunks
        var documents = documentChunker.splitDocumentIntoChunks(file, null);
        if (documents.isEmpty()) {
            throw new BadRequestException("File is empty or contains no readable text");
        }

        // Determine starting chunk_order for this knowledge
        int currentOrder = chunkService.getNextChunkOrderForKnowledge(agentId, knowledgeId);

        // Create DB chunks and add to vector store
        for (Document doc : documents) {
            chunkService.addChunk(agentId, knowledgeId, doc.getText(), doc.getMetadata(), currentOrder);
            currentOrder++;
        }

        int numberOfSegments = documents.size();
        logger.info("Document imported for agent {}: {}, segments: {}", agentId, fileName, numberOfSegments);

        return new KnowledgeImportingResponseVm(fileName, numberOfSegments);
    }
}
