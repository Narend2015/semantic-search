import os
os.environ["TF_CPP_MIN_LOG_LEVEL"] = "3"

import sys
import json
from sentence_transformers import SentenceTransformer

model = SentenceTransformer('all-MiniLM-L6-v2')

def get_embedding(text):
    embedding = model.encode(text)
    return embedding.tolist()

if __name__ == "__main__":
    input_text = sys.argv[1]
    embedding = get_embedding(input_text)

    # ONLY print embedding
    print(json.dumps(embedding))