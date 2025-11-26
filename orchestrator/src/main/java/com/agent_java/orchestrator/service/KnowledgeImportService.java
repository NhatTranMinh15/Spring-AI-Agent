package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.chunking.DocumentChunker;
import com.agent_java.orchestrator.exception.BadRequestException;
import com.agent_java.orchestrator.viewmodel.KnowledgeImportingResponseVm;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class KnowledgeImportService {

    private final KnowledgeChunkService chunkService;
    private final DocumentChunker documentChunker;

    @Autowired
    public KnowledgeImportService(KnowledgeChunkService chunkService, DocumentChunker documentChunker) {
        this.chunkService = chunkService;
        this.documentChunker = documentChunker;
    }

    private final Logger logger = LoggerFactory.getLogger(KnowledgeImportService.class);

    public KnowledgeImportingResponseVm importDocument(UUID knowledgeId, MultipartFile file) {
        String fileName = file.getOriginalFilename();
        logger.info("Importing document: {}, file type: {}", fileName, file.getContentType());

        // Split file into chunks
        var documents = documentChunker.splitDocumentIntoChunks(file, null);
        if (documents.isEmpty()) {
            throw new BadRequestException("File is empty or contains no readable text");
        }

        // Determine starting chunk_order for this knowledge
        int currentOrder = chunkService.getNextChunkOrderForKnowledge(knowledgeId);

        // Create DB chunks and add to vector store
        for (Document doc : documents) {
            chunkService.addChunk(knowledgeId, doc.getText(), doc.getMetadata(), currentOrder);
            currentOrder++;
        }

        int numberOfSegment = documents.size();
        logger.info("Document imported: {}, segments: {}", fileName, numberOfSegment);

        return new KnowledgeImportingResponseVm(fileName, numberOfSegment);
    }
}
