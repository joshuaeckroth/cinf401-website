---
layout: post
title: Text analysis
---

# Text analysis

## Tokenization

> [T]okenization is the process of breaking a stream of text up into words, phrases, symbols, or other meaningful elements called tokens. The list of tokens becomes input for further processing such as parsing or text mining. ([Wikipedia](http://en.wikipedia.org/wiki/Tokenization_(lexical_analysis\)))

Also known as word segmentation.

Example:

```
"Here's a phrase: Three blind mice."
=>
c("Here", "a", "phrase", "Three", "blind", "mice")
```

## Stemming

> In linguistic morphology and information retrieval, stemming is the process for reducing inflected (or sometimes derived) words to their stem, base or root form---generally a written word form. The stem need not be identical to the morphological root of the word; it is usually sufficient that related words map to the same stem, even if this stem is not in itself a valid root.
>
> [...]
>
> A stemmer for English, for example, should identify the string "cats" (and possibly "catlike", "catty" etc.) as based on the root "cat", and "stemmer", "stemming", "stemmed" as based on "stem". A stemming algorithm reduces the words "fishing", "fished", "fish", and "fisher" to the root word, "fish". ([Wikipedia](http://en.wikipedia.org/wiki/Stemming))

## Feature vectors

A document $j$ is represented as:

$$X\_{j} = (x\_{j1}, x\_{j2}, \dots, x\_{jc}),$$

where $c$ is the count of unique words across all documents in the corpus. Thus, all documents have vectors of the same number of dimensions ($c$ dimensions).

### Binary features

$$x\_{ji} = \cases{1 & \text{if $j$ is in $i$} \cr 0 & \text{otherwise}}$$

### Frequency count features

$$x\_{ji} = tf\_{ji},$$

where $tf\_{ji}$ is the frequency (count) of the term in the document.

### Normalized frequency count features

$$x\_{ji} = \frac{tf\_{ji}}{\sqrt{\sum\_{k} tf\_{ki}\^2}}$$

### tf-idf

The document vectors described above all suffer from over-scoring common words. A common word like "the" does not give any evidence that one document is closely related to another one just because they both contain many instances of that word. So in addition to recording how often a term appears in a document, we need to divide out some measure of how common is the term across different documents. A term appearing in nearly every document is not as relevant as a term appearing in few. Additionally, if a rare term appears more often in a document, it is even more indicative of the content of that document.

#### Motivation

> Suppose we have a set of English text documents and wish to determine which document is most relevant to the query "the brown cow." A simple way to start out is by eliminating documents that do not contain all three words "the", "brown", and "cow", but this still leaves many documents. To further distinguish them, we might count the number of times each term occurs in each document and sum them all together; the number of times a term occurs in a document is called its term frequency.

> However, because the term "the" is so common, this will tend to incorrectly emphasize documents which happen to use the word "the" more frequently, without giving enough weight to the more meaningful terms "brown" and "cow". The term "the" is not a good keyword to distinguish relevant and non-relevant documents and terms, unlike the less common words "brown" and "cow". Hence an inverse document frequency factor is incorporated which diminishes the weight of terms that occur very frequently in the collection and increases the weight of terms that occur rarely. ([Wikipedia](http://en.wikipedia.org/wiki/Tf*idf))

#### The math

Every dimension (word) in the document vector has the following value:

$$x\_{ji} = \log(tf\_{ji} + 1) \log(n/df\_j + 1),$$

where $j$ is the index of a word, $i$ is the index of a document, $x\_{ji}$ is the value in position $j$ (for word $j$) in the vector representing document $i$, $tf\_{ji}$ is the frequency (count) of the term $j$ in the document $i$, $n$ is the number of documents in the database, and $df\_j$ is the number of documents in the database that contain at least one occurrence of the term $j$.

![tf-idf graph](/images/tfidf-graph.png)

This graph shows the tf-idf value for a particular term in a document that comes from a database of 1,010 documents. The bottom axis is $df$ (document frequency for that term); the top numbered axis (the "depth" axis) is $tf$ or (term frequency; the number of times that term appears in the document under consideration); the right axis is the tf-idf value.

We see in the graph that an uncommon term ($df$ is low) produces a higher tf-idf value. However, due to the use of logarithms, the tf-idf value grows little as the term appears more often in the document. In all, a term that's appears several times in the document and is somewhat unique to the document is a strong indicator (of category membership, of relevance to a query containing that term, etc.).

An example of binary, frequency, and tfidf document vectors can be found in the [doc-vectors.xls](/doc-vectors.xls) spreadsheet.

## Document similarity

Since we have different ways of representing the document vectors, we have different "distance" calculations. Below, take $\hat{X} = (\hat{x}\_0, \dots, \hat{x}\_m)$ to be the document we want to compare against others, and $X\_j = (x\_{j0}, \dots, x\_{jn})$ to be a document from the corpus ($j$ will range so we check the distance to every document in the corpus).

### With binary features

$$d = \frac{\sum\_{i} \hat{x}\_{i} x\_{ji}}{\sum\_{i} \hat{x}\_{i}}$$

The numerator of the fraction gives the number of shared words; the
denominator gives the number of words in the document to be
compared. Thus, $0 \leq d \leq 1$.

### With frequency count features

$$d = \sqrt{\sum\_{i} (\hat{x}\_{i} - x\_{ji})\^2}$$

This is just Euclidean distance between the two document
vectors. Thus, $0 \leq d < \infty$.

### With tf-idf

$$d = \sum\_{i} \hat{x}\_{i} x\_{ji},$$

assuming the vectors $\hat{X}$ (the document we are trying to
compare) and $X_j$ (each document in the corpus) have been
normalized so that each of their Euclidean distances to the origin vector (all zeros) is equal to 1.0.

The above is a dot-product of two vectors. Because these vectors have been normalized, the dot-product can also be called the cosine similarity:

$$\cos \theta = \hat{X} \cdot X\_j,$$

where $0 \leq \theta \leq \pi/2$ and $\theta$ is the angle (from the origin) between the two document vectors in the higher-dimensional space. More similar documents have a smaller angle between them, meaning their cosine similarity is closer to 1.0. Documents with absolutely no terms in common have a dot-product equal to 0.0, or equivalently, a 90-degree angle between them. The cosine function ranges from -1 to 1, but our documents will never have an angle < 0&deg; or > 180&deg; since the values in the document vector cannot be negative (you can't have a negative term count, or negative tf-idf).

![Cosine similarity](/images/cosine-similarity.png)

