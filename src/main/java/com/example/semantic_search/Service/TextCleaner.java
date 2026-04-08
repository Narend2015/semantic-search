package com.example.semantic_search.Service;

import org.springframework.stereotype.Service;

@Service
public class TextCleaner {

    public static String cleanText(String text) {
        return text
                .replaceAll("\\s+", " ")   // remove extra spaces/newlines
                .replaceAll("\\n+", " ")   // remove new lines
                .replaceAll("\\.\\.", ".") // FIX double dots
                .trim();
    }
}