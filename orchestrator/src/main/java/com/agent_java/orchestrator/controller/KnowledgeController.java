package com.agent_java.orchestrator.controller;

import com.agent_java.orchestrator.service.KnowledgeService;
import com.agent_java.orchestrator.viewmodel.KnowledgeImportingResponseVm;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/knowledge")
public class KnowledgeController {

    @Autowired
    private KnowledgeService knowledgeService;

    @PostMapping()
    public ResponseEntity<KnowledgeImportingResponseVm> importFile(@RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(knowledgeService.importDocument(file));
    }
}
