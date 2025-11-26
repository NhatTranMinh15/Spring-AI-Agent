package com.agent_java.orchestrator.controller;

import com.agent_java.orchestrator.dto.KnowledgeChunkRequestDto;
import com.agent_java.orchestrator.dto.KnowledgeChunkResponseDto;
import com.agent_java.orchestrator.service.KnowledgeChunkService;
import com.agent_java.orchestrator.service.KnowledgeImportService;
import com.agent_java.orchestrator.viewmodel.KnowledgeImportingResponseVm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/knowledge/{knowledgeId}/chunks")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Tag(name = "Knowledge Chunks", description = "Manage text chunks and embeddings for knowledge sources")
public class KnowledgeChunkController {

    KnowledgeChunkService chunkService;
    KnowledgeImportService knowledgeService;

    @Autowired
    public KnowledgeChunkController(KnowledgeChunkService chunkService, KnowledgeImportService knowledgeService) {
        this.chunkService = chunkService;
        this.knowledgeService = knowledgeService;
    }

    @GetMapping
    @Operation(summary = "List all chunks for a knowledge source")
    public List<KnowledgeChunkResponseDto> listChunks(@PathVariable UUID knowledgeId) {
        return chunkService.getByKnowledge(knowledgeId);
    }

    @GetMapping("/count")
    @Operation(summary = "Count all chunks for a knowledge source")
    public long countChunks(@PathVariable UUID knowledgeId) {
        return chunkService.countByKnowledge(knowledgeId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a single chunk manually")
    public KnowledgeChunkResponseDto createChunk(@PathVariable UUID knowledgeId, @Valid @RequestBody KnowledgeChunkRequestDto req) {
        return chunkService.addChunk(knowledgeId, req.getContent(), req.getMetadata(), null);
    }

    @PutMapping("/{chunkId}")
    @Operation(summary = "Update a chunk's content and metadata")
    public KnowledgeChunkResponseDto updateChunk(@PathVariable UUID chunkId, @Valid @RequestBody KnowledgeChunkRequestDto req) {
        return chunkService.updateChunk(chunkId, req.getContent(), req.getMetadata());
    }

    @PostMapping("/import")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Import a document and split into chunks")
    public KnowledgeImportingResponseVm importFile(@PathVariable UUID knowledgeId, @RequestParam MultipartFile file) {
        return knowledgeService.importDocument(knowledgeId, file);
    }

    @GetMapping("/search")
    @Operation(summary = "Search similar chunks by text query")
    public void searchSimilarChunks(@RequestParam String query, @RequestParam(defaultValue = "5") int topK) {
        chunkService.searchSimilarChunks(query, topK);
    }
}
