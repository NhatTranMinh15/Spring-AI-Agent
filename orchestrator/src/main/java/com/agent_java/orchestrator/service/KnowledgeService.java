package com.agent_java.orchestrator.service;

import com.agent_java.orchestrator.exception.BadRequestException;
import com.agent_java.orchestrator.viewmodel.KnowledgeImportingResponseVm;
import jakarta.annotation.Nullable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class KnowledgeService {

    @Autowired
    VectorStore vectorStore;
    
    private final Logger logger = LoggerFactory.getLogger(KnowledgeService.class);

    public KnowledgeImportingResponseVm importDocument(MultipartFile file) throws IOException {
        logger.info("Importing document: {}, file type: {}", file.getOriginalFilename(), file.getContentType());
        // Kotlin Elvis operator﻿
        var fileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : file.getName();
        var fileExtension = fileName != null ? fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase() : "";
        var contentType = file.getContentType();
        String text = null;
        if (isTextFile(fileExtension, contentType)) {
            text = new String(file.getBytes(), Charset.forName("UTF_8"));
        } else {
            if (isPDFFile(fileExtension, contentType)) {
                text = extractTextFromPdf(file);
            } else {
                throw new BadRequestException("Only support .txt, .md and .pdf");
            }
        }
        if (text.isBlank()) {
            throw new BadRequestException("File is empty or contains no readable text");
        }
        return importText(fileName, text);
    }

    KnowledgeImportingResponseVm importText(String fileName, String text) {
        var documents = chunkDocuments(fileName, text);
        vectorStore.accept(documents);
        return new KnowledgeImportingResponseVm(fileName, documents.size());
    }

    private List<Document> chunkDocuments(String fileName, String text) {
        return chunkDocuments(fileName, text, 500, 300, 10, 1000);
    }

    private List<Document> chunkDocuments(String fileName, String text, int maxChunkSizeChars, int minChunkSizeChars, int minChunkLengthToEmbed, int maxNumChunks) {
        boolean keepSeparator = true;
        var document = Document.builder().text(text).metadata("source", fileName).build();
        var splitter = new TokenTextSplitter(maxChunkSizeChars, minChunkSizeChars, minChunkLengthToEmbed, maxNumChunks, keepSeparator);
        return splitter.apply(List.of(document));
    }

    private static final List<String> textFileSupportedExtensions = List.of("txt", "md");

    private boolean isTextFile(String fileExtension, @Nullable String contentType) {
        boolean isSupported = textFileSupportedExtensions.stream().anyMatch((t) -> t.equals(fileExtension));
        return isSupported || (contentType != null && contentType.startsWith("text/") == true);
    }

    private boolean isPDFFile(String fileExtension, @Nullable String contentType) {
        return "pdf".equals(fileExtension) || (contentType != null && contentType.equalsIgnoreCase("application/pdf") == true);
    }

    private String extractTextFromPdf(MultipartFile file) throws IOException {
        PDDocument document = PDDocument.load(file.getInputStream());
        var stripper = new PDFTextStripper();
        return stripper.getText(document);
    }
}
