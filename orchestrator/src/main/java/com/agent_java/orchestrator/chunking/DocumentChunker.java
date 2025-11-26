package com.agent_java.orchestrator.chunking;

import com.agent_java.orchestrator.config.ChunkerProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class DocumentChunker {

    ChunkerProperties chunkerProperties;
    DocumentTextExtractor textExtractor;
    ChunkerProfileDetector profileDetector;

    private Map<String, TokenTextSplitter> splitters;

    public DocumentChunker(ChunkerProperties chunkerProperties, DocumentTextExtractor textExtractor, ChunkerProfileDetector profileDetector) {
        this.chunkerProperties = chunkerProperties;
        this.textExtractor = textExtractor;
        this.profileDetector = profileDetector;
        this.splitters = new HashMap<>();
        chunkerProperties.getProfiles().forEach((t, u) -> {
            var p = new TokenTextSplitter(
                    u.getChunkSize(),
                    u.getMinChunkSizeChars(),
                    u.getMinChunkLengthToEmbed(),
                    u.getMaxNumChunks(),
                    u.isKeepSeparator());
            splitters.put(t, p);
        });
    }

    public List<Document> splitDocumentIntoChunks(MultipartFile file, String profileName) {
        List<Document> result;
        if (file.isEmpty()) {
            result = new ArrayList();
        } else {
            String fileName = file.getOriginalFilename();
            fileName = fileName != null ? fileName : "unknown";
            String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
            String text = textExtractor.extract(file, extension);
            if (text.isBlank()) {
                result = new ArrayList();
            } else {
                var chosenProfile = profileName != null ? profileName : profileDetector.detect(text, extension);
                var splitter = splitters.get(chosenProfile);
                if (splitter == null) {
                    throw new IllegalStateException("Unknown splitter profile: " + chosenProfile);
                }
                var metadata = new HashMap<String, Object>();
                if (!fileName.isBlank()) {
                    metadata.put("source", fileName);
                }
                metadata.put("profile", chosenProfile);
                var document = Document.builder()
                        .text(text)
                        .metadata(metadata)
                        .build();
                result = splitter.apply(List.of(document));
            }
        }
        return result;
    }
}
