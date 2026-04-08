package com.example.semantic_search.Service;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TextChunker {

    public static List<String> chunkText(String text, int maxChunkLength) {

        List<String> chunks = new ArrayList<>();

        // Better sentence split (avoid breaking on numbers like 1.)
        String[] sentences = text.split("(?<!\\d)[.!?]\\s+");

        StringBuilder chunk = new StringBuilder();

        for (String sentence : sentences) {

            // Add period back (since split removes it)
            sentence = sentence.trim() + ".";

            if (chunk.length() + sentence.length() > maxChunkLength) {
                chunks.add(chunk.toString().trim());
                chunk = new StringBuilder();
            }

            sentence = sentence.replaceAll("\\.\\.", ".");
            chunk.append(sentence).append(" ");
        }

        if (chunk.length() > 0) {
            chunks.add(chunk.toString().trim());
        }

        return chunks;
    }
}