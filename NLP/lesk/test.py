import nltk
from nltk.corpus import wordnet as wn
from nltk.corpus import stopwords

print(wn.synsets('dog'))
print(wn.synset('frank.n.02').definition())
print(wn.synset('pawl.n.01').definition())

