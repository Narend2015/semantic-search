package com.example.semantic_search.Model;

import java.util.List;

public class DocumentChunk {

    private String text;
    private List<Double> embedding;

    public DocumentChunk(String text, List<Double> embedding) {
        this.text = text;
        this.embedding = embedding;
    }

    public String getText() { return text; }
    public List<Double> getEmbedding() { return embedding; }
}