import os
os.environ["TF_CPP_MIN_LOG_LEVEL"] = "3"

import warnings
warnings.filterwarnings("ignore")

import sys
import json
from sentence_transformers import SentenceTransformer
from transformers import pipeline

# Load models (loaded once)
embedding_model = SentenceTransformer('all-MiniLM-L6-v2')

# Lightweight free LLM
qa_pipeline = pipeline("text2text-generation", model="google/flan-t5-base")


def get_embedding(text):
    embedding = embedding_model.encode(text)
    return embedding.tolist()


def generate_answer(context, question):
    prompt = f"""
    You are an AI assistant. Answer the question in a complete sentence.

    Context: {context}
    Question: {question}

    Answer:
    """
    result = qa_pipeline(prompt, max_length=100, do_sample=False)
    return result[0]['generated_text']


if __name__ == "__main__":
    mode = sys.argv[1]

    if mode == "embed":
        text = sys.argv[2]
        embedding = get_embedding(text)
        print(json.dumps(embedding))

    elif mode == "answer":
        context = sys.argv[2]
        question = sys.argv[3]
        answer = generate_answer(context, question)
        print(answer)