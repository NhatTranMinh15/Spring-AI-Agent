package com.agent_java.orchestrator.chunking;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class DocumentTextExtractor {

    static final Logger logger = Logger.getLogger(DocumentTextExtractor.class.getName());

    public String extract(MultipartFile file, String ext) {
        return switch (ext) {
            case "pdf" ->
                extractPdf(file);

            case "docx" ->
                extractDocx(file);

            default ->
                extractPlainText(file);
        };
    }

    private String extractPdf(MultipartFile file) {
        try (InputStream resource = file.getInputStream()) {
            var doc = PDDocument.load(resource);
            return (new PDFTextStripper()).getText(doc);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex.getMessage());
            return "";
        }
    }

    private String extractPlainText(MultipartFile file) {
        try (InputStream resource = file.getInputStream()) {
            Scanner s = new Scanner(resource, StandardCharsets.UTF_8);
            StringBuilder sb = new StringBuilder();
            while (s.hasNext()) {
                sb.append(s.nextLine());
            }
            return sb.toString();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex.getMessage());
            return "";
        }
    }

    private String extractDocx(MultipartFile file) {
        return "[DOCX text extraction not implemented] - " + file.getOriginalFilename();
    }

}
