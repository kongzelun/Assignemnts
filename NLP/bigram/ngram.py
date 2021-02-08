#!python3

import os
import sys
import re

# Paul Allen and Bill Gates are the founders of Microsoft software company
# Windows Phone Microsoft Office and Microsoft Surface are the products of the company

RESULT_FILENAME = "result.html"

# S1_PATTERNS = ("(Paul Allen)", "(and)", "(Bill Gates)", "(be|am|is|are|was|were|been|being)",
#                "(the)", "(founders*)", "(of)", "(Microsoft)", "(software)", "(company)")
S1_PATTERNS = ("(Paul Allen)", "(and)", "(Bill Gates)", "(are)",
               "(the)", "(founders)", "(of)", "(Microsoft)", "(software)", "(company)")
S1_UNIGRAM_COUNTS = []
S1_BIGRAM_COUNTS = []
S1_BIGRAM_PROBABILITIES = []
S1_SMOOTHED_BIGRAM_PROBABILITIES = []
S1_RECONSTITUTED_COUNTS = []
# S2_PATTERNS = ("(Windows)", "(Phone)", "(Microsoft)", "(Office)", "(and)", "(Microsoft)", "(Surface)",
#                "(be|am|is|are|was|were|been|being)", "(the)", "(products*)", "(of)", "(the)", "(company)")
S2_PATTERNS = ("(Windows)", "(Phone)", "(Microsoft)", "(Office)", "(and)", "(Microsoft)",
               "(Surface)", "(are)", "(the)", "(products*)", "(of)", "(the)", "(company)")
S2_UNIGRAM_COUNTS = []
S2_BIGRAM_COUNTS = []
S2_BIGRAM_PROBABILITIES = []
S2_SMOOTHED_BIGRAM_PROBABILITIES = []

CORPUS = ""

VOCABULARY_COUNT = 0

HTML = []
HTML_INDEX = 0


def main():
    ngram_init()
    sentence1()

    os.remove(RESULT_FILENAME)
    f_html = open(RESULT_FILENAME, mode='w+', encoding="UTF-8")
    f_html.writelines(HTML)

    return


def sentence1():
    global CORPUS
    global HTML_INDEX
    global VOCABULARY_COUNT

# get sentence unigram counts
    for p in S1_PATTERNS:
        S1_UNIGRAM_COUNTS.append(
            len(re.compile(p, re.IGNORECASE).findall(CORPUS)))

    # print(S1_UNIGRAM_COUNTS)

# get sentence bigram counts
    for i in range(len(S1_PATTERNS)):
        S1_BIGRAM_COUNTS.append([])
        for j in range(len(S1_PATTERNS)):
            # print(S1_PATTERNS[i] + "\s" + S1_PATTERNS[j])
            S1_BIGRAM_COUNTS[i].append(
                len(re.compile(S1_PATTERNS[i] + "\s" + S1_PATTERNS[j], re.IGNORECASE).findall(CORPUS)))

    # print(S1_BIGRAM_COUNTS)

# get sentence bigram probabilities
    for i in range(len(S1_PATTERNS)):
        S1_BIGRAM_PROBABILITIES.append([])
        for j in range(len(S1_PATTERNS)):
            S1_BIGRAM_PROBABILITIES[i].append(
                S1_BIGRAM_COUNTS[i][j] / S1_UNIGRAM_COUNTS[i])

    # print(S1_BIGRAM_PROBABILITIES)

# get sentence add-one smoothed bigram probabilities
    for i in range(len(S1_PATTERNS)):
        S1_SMOOTHED_BIGRAM_PROBABILITIES.append([])
        for j in range(len(S1_PATTERNS)):
            S1_SMOOTHED_BIGRAM_PROBABILITIES[i].append(
                (S1_BIGRAM_COUNTS[i][j] + 1) / (S1_UNIGRAM_COUNTS[i] + VOCABULARY_COUNT))

    # print(S1_SMOOTHED_BIGRAM_PROBABILITIES)

# get sentence add-one reconstituted counts
    for i in range(len(S1_PATTERNS)):
        S1_RECONSTITUTED_COUNTS.append([])
        for j in range(len(S1_PATTERNS)):
            S1_RECONSTITUTED_COUNTS[i].append(
                S1_SMOOTHED_BIGRAM_PROBABILITIES[i][j] * S1_UNIGRAM_COUNTS[i])

# display sentence 1
    HTML.insert(
        HTML_INDEX, "<h1>Sentence 1</h1>\n<p>Paul Allen and Bill Gates are the founders of Microsoft software company</p>\n")
    HTML_INDEX += 1

# display unigram counts
    HTML.insert(
        HTML_INDEX, "<h1>Sentence 1 Unigram Counts</h1>\n<table border=\"1\">\n")
    HTML_INDEX += 1

    # header
    HTML.insert(HTML_INDEX, "<tr>\n")
    HTML_INDEX += 1
    for pattern in S1_PATTERNS:
        HTML.insert(HTML_INDEX, "<th>%s</th>\n" % (pattern))
        HTML_INDEX += 1
    HTML.insert(HTML_INDEX, "</tr>\n")
    HTML_INDEX += 1

    # Counts
    HTML.insert(HTML_INDEX, "<tr>\n")
    HTML_INDEX += 1
    for number in S1_UNIGRAM_COUNTS:
        HTML.insert(HTML_INDEX, "<td>%d</td>\n" % (number))
        HTML_INDEX += 1
    HTML.insert(HTML_INDEX, "</tr>\n")
    HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "</table>\n")
    HTML_INDEX += 1

# display bigram counts
    HTML.insert(
        HTML_INDEX, "<h1>Sentence 1 Bigram Counts</h1>\n<table border=\"1\">\n")
    HTML_INDEX += 1

    # header
    HTML.insert(HTML_INDEX, "<tr>\n")
    HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "<th></th>\n")
    HTML_INDEX += 1
    for pattern in S1_PATTERNS:
        HTML.insert(HTML_INDEX, "<th>%s</th>\n" % (pattern))
        HTML_INDEX += 1
    HTML.insert(HTML_INDEX, "</tr>\n")
    HTML_INDEX += 1

    # counts
    for i in S1_BIGRAM_COUNTS:
        HTML.insert(HTML_INDEX, "<tr>\n")
        HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "<td>%s</td>\n" %
                    (S1_PATTERNS[S1_BIGRAM_COUNTS.index(i)]))
        HTML_INDEX += 1
        for j in i:
            HTML.insert(HTML_INDEX, "<td>%d</td>\n" % (j))
            HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "</tr>\n")
        HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "</table>\n")
    HTML_INDEX += 1

# display bigram probabilities
    HTML.insert(
        HTML_INDEX, "<h1>Sentence 1 Bigram Probabilities</h1>\n<table border=\"1\">\n")
    HTML_INDEX += 1

    # header
    HTML.insert(HTML_INDEX, "<tr>\n")
    HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "<th></th>\n")
    HTML_INDEX += 1
    for pattern in S1_PATTERNS:
        HTML.insert(HTML_INDEX, "<th>%s</th>\n" % (pattern))
        HTML_INDEX += 1
    HTML.insert(HTML_INDEX, "</tr>\n")
    HTML_INDEX += 1

    # probabilities
    for i in S1_BIGRAM_PROBABILITIES:
        HTML.insert(HTML_INDEX, "<tr>\n")
        HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "<td>%s</td>\n" %
                    (S1_PATTERNS[S1_BIGRAM_PROBABILITIES.index(i)]))
        HTML_INDEX += 1
        for j in i:
            HTML.insert(HTML_INDEX, "<td>%f</td>\n" % (j))
            HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "</tr>\n")
        HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "</table>\n")
    HTML_INDEX += 1

# display smoothed bigram probabilities
    HTML.insert(
        HTML_INDEX, "<h1>Sentence 1 Smoothed Bigram Probabilities</h1>\n<table border=\"1\">\n")
    HTML_INDEX += 1

    # header
    HTML.insert(HTML_INDEX, "<tr>\n")
    HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "<th></th>\n")
    HTML_INDEX += 1
    for pattern in S1_PATTERNS:
        HTML.insert(HTML_INDEX, "<th>%s</th>\n" % (pattern))
        HTML_INDEX += 1
    HTML.insert(HTML_INDEX, "</tr>\n")
    HTML_INDEX += 1

    # probabilities
    for i in S1_SMOOTHED_BIGRAM_PROBABILITIES:
        HTML.insert(HTML_INDEX, "<tr>\n")
        HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "<td>%s</td>\n" %
                    (S1_PATTERNS[S1_SMOOTHED_BIGRAM_PROBABILITIES.index(i)]))
        HTML_INDEX += 1
        for j in i:
            HTML.insert(HTML_INDEX, "<td>%f</td>\n" % (j))
            HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "</tr>\n")
        HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "</table>\n")
    HTML_INDEX += 1

# display sentence add-one reconstituted counts
    HTML.insert(
        HTML_INDEX, "<h1>Sentence 1 Reconsituted Counts</h1>\n<table border=\"1\">\n")
    HTML_INDEX += 1

    # header
    HTML.insert(HTML_INDEX, "<tr>\n")
    HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "<th></th>\n")
    HTML_INDEX += 1
    for pattern in S1_PATTERNS:
        HTML.insert(HTML_INDEX, "<th>%s</th>\n" % (pattern))
        HTML_INDEX += 1
    HTML.insert(HTML_INDEX, "</tr>\n")
    HTML_INDEX += 1

    # probabilities
    for i in S1_RECONSTITUTED_COUNTS:
        HTML.insert(HTML_INDEX, "<tr>\n")
        HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "<td>%s</td>\n" %
                    (S1_PATTERNS[S1_RECONSTITUTED_COUNTS.index(i)]))
        HTML_INDEX += 1
        for j in i:
            HTML.insert(HTML_INDEX, "<td>%f</td>\n" % (j))
            HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "</tr>\n")
        HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "</table>\n")
    HTML_INDEX += 1

    return


def ngram_init():
    global CORPUS
    global HTML_INDEX
    global VOCABULARY_COUNT
    # get the corpus
    f_corpus = open(sys.argv[1], encoding="UTF-8")
    CORPUS = f_corpus.read()
    f_corpus.close()

    # get the number of words
    vocabulary = []
    for word in re.compile("[a-z]+", re.IGNORECASE).findall(CORPUS):
        if word not in vocabulary:
            vocabulary.append(word)
    VOCABULARY_COUNT = len(vocabulary)

    # print(vocabulary)
    # print(VOCABULARY_COUNT)

    # initilize the html
    HTML.append("<!DOCKTYPE html>\n")
    HTML.append("<html>\n")
    HTML.append("<body>\n")
    HTML.append("</body>\n")
    HTML.append("</html>\n")
    HTML_INDEX += 3

    # get sentence unigram counts
    # for p in S1_PATTERNS:
    #     S1_UNIGRAM_COUNTS.append(
    #         len(re.compile(p, re.IGNORECASE).findall(CORPUS)))

    # for p in S2_PATTERNS:
    #     S2_UNIGRAM_COUNTS.append(
    #         len(re.compile(p, re.IGNORECASE).findall(CORPUS)))

    # get sentence bigram counts
    # for i in range(len(S1_PATTERNS)):
    #     S1_BIGRAM_COUNTS.append([])
    #     for j in range(len(S1_PATTERNS)):
    #         # print(S1_PATTERNS[i] + "\s" + S1_PATTERNS[j])
    #         S1_BIGRAM_COUNTS[i].append(
    #             len(re.compile(S1_PATTERNS[i] + "\s" + S1_PATTERNS[j], re.IGNORECASE).findall(CORPUS)))

    # for i in range(len(S2_PATTERNS)):
    #     S2_BIGRAM_COUNTS.append([])
    #     for j in range(len(S2_PATTERNS)):
    #         # print(S2_PATTERNS[i] + "\s" + S2_PATTERNS[j])
    #         S2_BIGRAM_COUNTS[i].append(
    #             len(re.compile(S2_PATTERNS[i] + "\s" + S2_PATTERNS[j], re.IGNORECASE).findall(CORPUS)))

    # print(S1_BIGRAM_COUNTS)
    # print(S2_BIGRAM_COUNTS)

    # get sentence bigram probabilities
    # for i in range(len(S1_PATTERNS)):
    #     S1_BIGRAM_PROBABILITIES.append([])
    #     for j in range(len(S1_PATTERNS)):
    #         S1_BIGRAM_PROBABILITIES[i].append(
    #             S1_BIGRAM_COUNTS[i][j] / S1_UNIGRAM_COUNTS[j])

    # for i in range(len(S2_PATTERNS)):
    #     S2_BIGRAM_PROBABILITIES.append([])
    #     for j in range(len(S2_PATTERNS)):
    #         S2_BIGRAM_PROBABILITIES[i].append(
    #             S2_BIGRAM_COUNTS[i][j] / S2_UNIGRAM_COUNTS[j])

    # get sentence add-one smoothed bigram probabilities
    # for i in range(len(S1_PATTERNS)):
    #     S1_SMOOTHED_BIGRAM_PROBABILITIES.append([])
    #     for j in range(len(S1_PATTERNS)):
    #         S1_SMOOTHED_BIGRAM_PROBABILITIES[i].append(
    #             (S1_BIGRAM_COUNTS[i][j] + 1) / (S1_UNIGRAM_COUNTS[j] + VOCABULARY_COUNT))

    # for i in range(len(S2_PATTERNS)):
    #     S2_SMOOTHED_BIGRAM_PROBABILITIES.append([])
    #     for j in range(len(S2_PATTERNS)):
    #         S2_SMOOTHED_BIGRAM_PROBABILITIES[i].append(
    #             (S2_BIGRAM_COUNTS[i][j] + 1) / (S2_UNIGRAM_COUNTS[j] + VOCABULARY_COUNT))

    return


def display_sentence_unigram_counts():
    global HTML_INDEX
    # sentence 1
    HTML.insert(
        HTML_INDEX, "<h1>Sentence 1 Unigram Counts</h1>\n<table border=\"1\">\n")
    HTML_INDEX += 1

    # header
    HTML.insert(HTML_INDEX, "<tr>\n")
    HTML_INDEX += 1
    for pattern in S1_PATTERNS:
        HTML.insert(HTML_INDEX, "<th>%s</th>\n" % (pattern))
        HTML_INDEX += 1
    HTML.insert(HTML_INDEX, "</tr>\n")
    HTML_INDEX += 1

    # Counts
    HTML.insert(HTML_INDEX, "<tr>\n")
    HTML_INDEX += 1
    for number in S1_UNIGRAM_COUNTS:
        HTML.insert(HTML_INDEX, "<td>%d</td>\n" % (number))
        HTML_INDEX += 1
    HTML.insert(HTML_INDEX, "</tr>\n")
    HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "</table>\n")
    HTML_INDEX += 1

    # sentence 2
    HTML.insert(
        HTML_INDEX, "<h1>Sentence 2 Unigram Counts</h1>\n<table border=\"1\">\n")
    HTML_INDEX += 1

    # header
    HTML.insert(HTML_INDEX, "<tr>\n")
    HTML_INDEX += 1
    for pattern in S2_PATTERNS:
        HTML.insert(HTML_INDEX, "<th>%s</th>\n" % (pattern))
        HTML_INDEX += 1
    HTML.insert(HTML_INDEX, "</tr>\n")
    HTML_INDEX += 1

    # Counts
    HTML.insert(HTML_INDEX, "<tr>\n")
    HTML_INDEX += 1
    for number in S2_UNIGRAM_COUNTS:
        HTML.insert(HTML_INDEX, "<td>%d</td>\n" % (number))
        HTML_INDEX += 1
    HTML.insert(HTML_INDEX, "</tr>\n")
    HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "</table>\n")
    HTML_INDEX += 1

    return


def display_sentence_bigram_counts():
    global HTML_INDEX
    # sentence 1
    HTML.insert(
        HTML_INDEX, "<h1>Sentence 1 Bigram Counts</h1>\n<table border=\"1\">\n")
    HTML_INDEX += 1

    # header
    HTML.insert(HTML_INDEX, "<tr>\n")
    HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "<th></th>\n")
    HTML_INDEX += 1
    for pattern in S1_PATTERNS:
        HTML.insert(HTML_INDEX, "<th>%s</th>\n" % (pattern))
        HTML_INDEX += 1
    HTML.insert(HTML_INDEX, "</tr>\n")
    HTML_INDEX += 1

    # counts
    for i in S1_BIGRAM_COUNTS:
        HTML.insert(HTML_INDEX, "<tr>\n")
        HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "<td>%s</td>\n" %
                    (S1_PATTERNS[S1_BIGRAM_COUNTS.index(i)]))
        HTML_INDEX += 1
        for j in i:
            HTML.insert(HTML_INDEX, "<td>%d</td>\n" % (j))
            HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "</tr>\n")
        HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "</table>\n")
    HTML_INDEX += 1

    # sentence 2
    HTML.insert(
        HTML_INDEX, "<h1>Sentence 2 Bigram Counts</h1>\n<table border=\"1\">\n")
    HTML_INDEX += 1

    # header
    HTML.insert(HTML_INDEX, "<tr>\n")
    HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "<th></th>\n")
    HTML_INDEX += 1
    for pattern in S2_PATTERNS:
        HTML.insert(HTML_INDEX, "<th>%s</th>\n" % (pattern))
        HTML_INDEX += 1
    HTML.insert(HTML_INDEX, "</tr>\n")
    HTML_INDEX += 1

    # counts
    for i in S2_BIGRAM_COUNTS:
        HTML.insert(HTML_INDEX, "<tr>\n")
        HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "<td>%s</td>\n" %
                    (S2_PATTERNS[S2_BIGRAM_COUNTS.index(i)]))
        HTML_INDEX += 1
        for j in i:
            HTML.insert(HTML_INDEX, "<td>%d</td>\n" % (j))
            HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "</tr>\n")
        HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "</table>\n")
    HTML_INDEX += 1

    return


def display_sentence_bigram_probabilities():
    global HTML_INDEX
    # sentence 1
    HTML.insert(
        HTML_INDEX, "<h1>Sentence 1 Bigram Probabilities</h1>\n<table border=\"1\">\n")
    HTML_INDEX += 1

    # header
    HTML.insert(HTML_INDEX, "<tr>\n")
    HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "<th></th>\n")
    HTML_INDEX += 1
    for pattern in S1_PATTERNS:
        HTML.insert(HTML_INDEX, "<th>%s</th>\n" % (pattern))
        HTML_INDEX += 1
    HTML.insert(HTML_INDEX, "</tr>\n")
    HTML_INDEX += 1

    # probabilities
    for i in S1_BIGRAM_PROBABILITIES:
        HTML.insert(HTML_INDEX, "<tr>\n")
        HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "<td>%s</td>\n" %
                    (S1_PATTERNS[S1_BIGRAM_PROBABILITIES.index(i)]))
        HTML_INDEX += 1
        for j in i:
            HTML.insert(HTML_INDEX, "<td>%f</td>\n" % (j))
            HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "</tr>\n")
        HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "</table>\n")
    HTML_INDEX += 1

    # sentence 2
    HTML.insert(
        HTML_INDEX, "<h1>Sentence 2 Bigram Probabilities</h1>\n<table border=\"1\">\n")
    HTML_INDEX += 1

    # header
    HTML.insert(HTML_INDEX, "<tr>\n")
    HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "<th></th>\n")
    HTML_INDEX += 1
    for pattern in S2_PATTERNS:
        HTML.insert(HTML_INDEX, "<th>%s</th>\n" % (pattern))
        HTML_INDEX += 1
    HTML.insert(HTML_INDEX, "</tr>\n")
    HTML_INDEX += 1

    # probabilities
    for i in S2_BIGRAM_PROBABILITIES:
        HTML.insert(HTML_INDEX, "<tr>\n")
        HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "<td>%s</td>\n" %
                    (S2_PATTERNS[S2_BIGRAM_PROBABILITIES.index(i)]))
        HTML_INDEX += 1
        for j in i:
            HTML.insert(HTML_INDEX, "<td>%f</td>\n" % (j))
            HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "</tr>\n")
        HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "</table>\n")
    HTML_INDEX += 1

    return


def display_sentence_smoothed_bigram_probabilities():
    global HTML_INDEX
    # sentence 1
    HTML.insert(
        HTML_INDEX, "<h1>Sentence 1 Smoothed Bigram Probabilities</h1>\n<table border=\"1\">\n")
    HTML_INDEX += 1

    # header
    HTML.insert(HTML_INDEX, "<tr>\n")
    HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "<th></th>\n")
    HTML_INDEX += 1
    for pattern in S1_PATTERNS:
        HTML.insert(HTML_INDEX, "<th>%s</th>\n" % (pattern))
        HTML_INDEX += 1
    HTML.insert(HTML_INDEX, "</tr>\n")
    HTML_INDEX += 1

    # probabilities
    for i in S1_SMOOTHED_BIGRAM_PROBABILITIES:
        HTML.insert(HTML_INDEX, "<tr>\n")
        HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "<td>%s</td>\n" %
                    (S1_PATTERNS[S1_SMOOTHED_BIGRAM_PROBABILITIES.index(i)]))
        HTML_INDEX += 1
        for j in i:
            HTML.insert(HTML_INDEX, "<td>%f</td>\n" % (j))
            HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "</tr>\n")
        HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "</table>\n")
    HTML_INDEX += 1

    # sentence 2
    HTML.insert(
        HTML_INDEX, "<h1>Sentence 2 Smoothed Bigram Probabilities</h1>\n<table border=\"1\">\n")
    HTML_INDEX += 1

    # header
    HTML.insert(HTML_INDEX, "<tr>\n")
    HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "<th></th>\n")
    HTML_INDEX += 1
    for pattern in S2_PATTERNS:
        HTML.insert(HTML_INDEX, "<th>%s</th>\n" % (pattern))
        HTML_INDEX += 1
    HTML.insert(HTML_INDEX, "</tr>\n")
    HTML_INDEX += 1

    # probabilities
    for i in S2_SMOOTHED_BIGRAM_PROBABILITIES:
        HTML.insert(HTML_INDEX, "<tr>\n")
        HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "<td>%s</td>\n" %
                    (S2_PATTERNS[S2_SMOOTHED_BIGRAM_PROBABILITIES.index(i)]))
        HTML_INDEX += 1
        for j in i:
            HTML.insert(HTML_INDEX, "<td>%f</td>\n" % (j))
            HTML_INDEX += 1

        HTML.insert(HTML_INDEX, "</tr>\n")
        HTML_INDEX += 1

    HTML.insert(HTML_INDEX, "</table>\n")
    HTML_INDEX += 1

    return


if __name__ == "__main__":
    main()
