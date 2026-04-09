package com.example.semantic_search.Service;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class EmbeddingService {

    public String getEmbedding(String text) {
        try {

            ProcessBuilder pb = new ProcessBuilder(
                    "python",
                    "embedding/rag_service.py",
                    "embed",
                    text
            );

            pb.redirectErrorStream(true);

            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                // Only capture embedding JSON
                if (line.startsWith("[")) {
                    output.append(line);
                }
            }

            return output.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error generating embedding: " + e.getMessage());
        }
    }

    public String generateAnswer(String context, String question) {
        try {

            ProcessBuilder pb = new ProcessBuilder(
                    "python",
                    "embedding/rag_service.py",
                    "answer",   // 🔥 MODE
                    context,
                    question
            );

            pb.redirectErrorStream(true);

            Process process = pb.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            return output.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error generating answer: " + e.getMessage());
        }
    }
}