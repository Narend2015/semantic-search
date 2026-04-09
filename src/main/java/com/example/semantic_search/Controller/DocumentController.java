package com.example.semantic_search.Controller;
import com.example.semantic_search.Model.DocumentChunk;
import com.example.semantic_search.Service.EmbeddingService;
import com.example.semantic_search.Service.SimilarityUtil;
import com.example.semantic_search.Service.TextChunker;
import com.example.semantic_search.Service.TextCleaner;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    private List<DocumentChunk> storedChunks = new ArrayList<>();

//    @PostMapping("/upload")
//    public List<String> uploadFile(@RequestParam("file") MultipartFile file) {
//        try {
//            Tika tika = new Tika();
//
//            String extractedText = tika.parseToString(file.getInputStream());
//            String cleanText = TextCleaner.cleanText(extractedText);
//
//            List<String> chunks = TextChunker.chunkText(cleanText, 200);
//
//            List<String> embeddings = new ArrayList<>();
//
//            for (String chunk : chunks) {
//                String embedding = embeddingService.getEmbedding(chunk);
//                embeddings.add(embedding);
//            }
//
//            return embeddings;
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    public List<Double> parseEmbedding(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, List.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            Tika tika = new Tika();

            String extractedText = tika.parseToString(file.getInputStream());
            String cleanText = TextCleaner.cleanText(extractedText);

            List<String> chunks = TextChunker.chunkText(cleanText, 200);

            // Clear old data (optional)
            storedChunks.clear();

            for (String chunk : chunks) {

                String embeddingJson = embeddingService.getEmbedding(chunk);

                List<Double> embedding = parseEmbedding(embeddingJson);

                storedChunks.add(new DocumentChunk(chunk, embedding));
            }

            return "Document processed and stored successfully! Total chunks: " + storedChunks.size();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/search")
    public String search(@RequestParam String query) {

        String queryEmbeddingJson = embeddingService.getEmbedding(query);
        List<Double> queryEmbedding = parseEmbedding(queryEmbeddingJson);

        double maxScore = -1;
        String bestMatch = "";

        for (DocumentChunk chunk : storedChunks) {

            double score = SimilarityUtil.cosineSimilarity(
                    queryEmbedding,
                    chunk.getEmbedding()
            );

            if (score > maxScore) {
                maxScore = score;
                bestMatch = chunk.getText();
            }
        }

        return bestMatch;
    }
}
