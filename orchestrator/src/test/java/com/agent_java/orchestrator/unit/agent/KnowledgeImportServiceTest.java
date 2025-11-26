package com.agent_java.orchestrator.unit.agent;

import com.agent_java.orchestrator.chunking.ChunkerProfileDetector;
import com.agent_java.orchestrator.chunking.DocumentChunker;
import com.agent_java.orchestrator.chunking.DocumentTextExtractor;
import com.agent_java.orchestrator.config.ChunkerProperties;
import com.agent_java.orchestrator.dto.KnowledgeChunkResponseDto;
import com.agent_java.orchestrator.exception.BadRequestException;
import com.agent_java.orchestrator.service.KnowledgeChunkService;
import com.agent_java.orchestrator.service.KnowledgeImportService;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import org.springframework.mock.web.MockMultipartFile;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

public class KnowledgeImportServiceTest {

    private KnowledgeChunkService chunkService;

    private DocumentChunker documentChunker;

    private KnowledgeImportService service;

    @BeforeEach
    private void setUp() {
        chunkService = Mockito.mock(KnowledgeChunkService.class);
        var chunkerProperties = new ChunkerProperties();
        var profiles = chunkerProperties.getProfiles();
        profiles.put("default", new ChunkerProperties.ChunkerProfile(50, 10, 5, 100, true));
        profiles.put("markdown", new ChunkerProperties.ChunkerProfile(50, 10, 5, 100, true));
        profiles.put("loose", new ChunkerProperties.ChunkerProfile(50, 10, 5, 100, true));
        profiles.put("tight", new ChunkerProperties.ChunkerProfile(30, 10, 5, 200, true));
        profiles.put("code", new ChunkerProperties.ChunkerProfile(50, 10, 5, 500, true));
        var textExtractor = new DocumentTextExtractor();
        var profileDetector = new ChunkerProfileDetector();
        documentChunker = new DocumentChunker(chunkerProperties, textExtractor, profileDetector);
        service = new KnowledgeImportService(chunkService, documentChunker);

        when(chunkService.getNextChunkOrderForKnowledge(any())).thenReturn(0);
        when(chunkService.addChunk(any(), any(), any(), any())).thenAnswer((args) -> new KnowledgeChunkResponseDto(UUID.randomUUID(), args.getArgument(1), args.getArgument(2)));
    }

    @Test
    public void importDocument_should_process_txt_file_correctly() {
        String content = IntStream.rangeClosed(1, 20).mapToObj(i -> "Sentence " + i).collect(Collectors.joining(" "));
        var file = new MockMultipartFile("file", "test.txt", "text/plain", content.getBytes(StandardCharsets.UTF_8));

        var response = service.importDocument(UUID.randomUUID(), file);

        assertEquals("test.txt", response.getOriginalFilename());
        assertTrue(response.getNumberOfSegment() > 1, "Expected multiple chunks for txt file");
    }

    @Test
    public void importDocument_should_process_md_file_correctly() {
        String content = IntStream.rangeClosed(1, 20).mapToObj(i -> "# Heading " + i + " Some markdown text.").collect(Collectors.joining(" "));
        var file = new MockMultipartFile("file", "test.md", "text/markdown", content.getBytes(StandardCharsets.UTF_8));

        var response = service.importDocument(UUID.randomUUID(), file);

        assertEquals("test.md", response.getOriginalFilename());
        assertTrue(response.getNumberOfSegment() > 1, "Expected multiple chunks for md file");
    }

    @Test
    public void importDocument_should_throw_BadRequestException_for_empty_file() {
        var file = new MockMultipartFile("file", "empty.txt", "text/plain", "".getBytes(StandardCharsets.UTF_8));

        var exception = assertThrows(BadRequestException.class, () -> service.importDocument(UUID.randomUUID(), file));

        assert (exception.getMessage().contains("empty or contains no readable text"));
    }

    @Test
    public void importDocument_should_process_pdf_file_correctly() {
        var out = new ByteArrayOutputStream();
        try (var document = new PDDocument()) {
            var page = new PDPage();
            document.addPage(page);
            try (var contentStream = new PDPageContentStream(document, page);) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12f);
                contentStream.newLineAtOffset(100f, 700f);
                contentStream.showText("PDF test content for chunking with multiple lines.");
                contentStream.endText();
            } catch (IOException ex) {
                Logger.getLogger(KnowledgeImportServiceTest.class.getName()).log(Level.SEVERE, null, ex.getMessage());
            }
            document.save(out);
        } catch (IOException ex) {
            Logger.getLogger(KnowledgeImportServiceTest.class.getName()).log(Level.SEVERE, null, ex.getMessage());
        }
        var pdfBytes = out.toByteArray();
        var file = new MockMultipartFile("file", "test.pdf", "application/pdf", pdfBytes);
        var response = service.importDocument(UUID.randomUUID(), file);

        assertEquals("test.pdf", response.getOriginalFilename());
        assertTrue(response.getNumberOfSegment() >= 1, "Expected at least one chunk for PDF");

    }
}
