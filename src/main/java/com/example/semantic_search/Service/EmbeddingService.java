package com.example.semantic_search.Service;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class EmbeddingService {

    public String getEmbedding(String text) {
        try {

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "python",
                    "embedding/embedding_service.py",
                    text
            );

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("[")) {
                    output.append(line);
                }
            }

            return output.toString();

        } catch (Exception e) {
            throw new RuntimeException("Error calling Python: " + e.getMessage());
        }
    }
}