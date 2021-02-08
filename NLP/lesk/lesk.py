# import nltk
# from nltk.corpus import wordnet as wn
# print(wn.synsets('dog'))
# print(wn.synset('dog.n.01').definition())
# print(wn.synset('dog.n.01').examples()[0])

import sys
import re
from nltk.corpus import wordnet as wn

STOPWORDS = open("stopwords.txt").read()


def simplified_lesk(word, sentence):
    # most frequent sense of word
    best_sense = wn.synsets(word)[0]
    max_overlap = 0
    context = set(get_context(sentence)) - set(STOPWORDS.split('\n'))
    # print('context:', context)

    signature = set()
    overlap = 0

    for sense in wn.synsets(word):
        # print(sense)
        signature = signature | set(get_context(sense.definition()))
        for example in sense.examples():
            # print(example)
            signature = signature | set(get_context(example))

        overlap = compute_overlap(signature, context)
        if overlap > max_overlap:
            max_overlap = overlap
            best_sense = sense

    # print(signature)
    print("max_overlap =",max_overlap)
    return best_sense


def compute_overlap(signature, context):
    return len(signature & context)


def get_context(sentence):
    return re.compile("\w+").findall(sentence)


def main():
    # if len(sys.argv) > 1:
    #     print(sys.argv)
    best_sense=simplified_lesk(sys.argv[1], sys.argv[2])
    print("The best sense is", best_sense, ":", best_sense.definition())


if __name__ == "__main__":
    main()
