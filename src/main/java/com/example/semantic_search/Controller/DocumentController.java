package com.example.semantic_search.Controller;
import com.example.semantic_search.Service.EmbeddingService;
import com.example.semantic_search.Service.TextChunker;
import com.example.semantic_search.Service.TextCleaner;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DocumentController {

    @Autowired
    public TextChunker textChunker;

    @Autowired
    public TextCleaner textCleaner;

    @Autowired
    private EmbeddingService embeddingService;

//    @PostMapping("/upload")
//    public List<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        try {
//            Tika tika = new Tika();
//
//            // Extract text from uploaded file
//            String extractedText = tika.parseToString(file.getInputStream());
//
//            // CLEAN TEXT
//            String cleanText = TextCleaner.cleanText(extractedText);
//
//            //  CHUNK
//            List<String> chunks = TextChunker.chunkText(cleanText, 200);
//
//            return chunks;
//
//        } catch (Exception e) {
//            throw new RuntimeException("Error: " + e.getMessage());
//        }

    @PostMapping("/upload")
    public List<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Tika tika = new Tika();

            String extractedText = tika.parseToString(file.getInputStream());
            String cleanText = TextCleaner.cleanText(extractedText);

            List<String> chunks = TextChunker.chunkText(cleanText, 200);

            List<String> embeddings = new ArrayList<>();

            for (String chunk : chunks) {
                String embedding = embeddingService.getEmbedding(chunk);
                embeddings.add(embedding);
            }

            return embeddings;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
