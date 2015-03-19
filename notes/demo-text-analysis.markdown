---
layout: post
title: "Demo: Text analysis"
---

# Demo: Text analysis

The Twitter messages come from [Techtunk](http://www.techtunk.com/index.php?dove=downloads).


{% highlight r %}
twitter <- read.csv("/bigdata/data/superbowl/20140203_110522_e97baf5d-42b8-4d91-8b61-017afdbd4b89.csv", header=FALSE, sep="|", quote="")
colnames(twitter) <- c("username", "timestamp", "tweet", "retweetcount", "lon", "lat", "country", "name", "address", "type", "placeURL")

library(tm)
library(SnowballC) # required for stemming

corpus_small <- VCorpus(VectorSource(twitter[1:1000,]$tweet))

# by default, uses term frequency counts (not binary, not tf-idf, not normalized)
tdm_small <- TermDocumentMatrix(corpus_small, control = list(removePunctuation = TRUE, stopwords = TRUE, stemming = TRUE, tolower = TRUE))

library(wordcloud)

total_term_counts_small <- rowSums(as.matrix(tdm_small))
wordcloud(labels(total_term_counts_small), total_term_counts_small)

library(XML)

library(tm.plugin.webmining)

readStackExchangePostBody <- function(tree) { extractHTMLStrip(xmlAttrs(xmlRoot(tree))[["Body"]]) }

readStackExchangePost <- readXML(spec = list(content = list("function", readStackExchangePostBody), id = list("attribute", "//@Id")), doc = PlainTextDocument())

PostSource <- function(x) {
  XMLSource(x, function(tree) {
    nodes <- XML::xmlChildren(XML::xmlRoot(tree))
    nodes[names(nodes) == "row"]
  }, readStackExchangePost)
}

corpus_se <- VCorpus(PostSource("/bigdata/data/stackexchange/unzipped/meta.chess.stackexchange.com/Posts.xml"))

is_non_empty <- function(doc)
{
  nchar(as.character(content(doc))) > 0
}

corpus_se <- tm_filter(corpus_se, is_non_empty)

# with tf-idf
tdm_se <- TermDocumentMatrix(corpus_se, control = list(weighting = function(x) { weightTfIdf(x, normalize = TRUE) }, removePunctuation = TRUE, stopwords = TRUE, stemming = TRUE, tolower = TRUE, removeNumbers = TRUE))

total_term_counts_se <- rowSums(as.matrix(tdm_se))
wordcloud(labels(total_term_counts_se), total_term_counts_se)

normalize <- function(x) {
  x / (sqrt(sum(x*x)))
}

search <- function(corpus, query) {
  query_corpus <- VCorpus(VectorSource(c(query)))
  combined_corpus <- c(corpus, query_corpus)
  tdm <- TermDocumentMatrix(combined_corpus, control = list(weighting = function(x) { weightTfIdf(x, normalize = TRUE) }, removePunctuation = TRUE, stopwords = TRUE, stemming = TRUE, tolower = TRUE, removeNumbers = TRUE))
  tdm_norms <- apply(tdm, 2, normalize)
  query_idx <- ncol(tdm_norms)
  sims <- sapply(1:length(corpus), function(i) { tdm_norms[,i] %*% tdm_norms[,query_idx] })
  docs_sims <- data.frame(doc=names(corpus), score=sims)
  docs_sims[order(docs_sims$score, decreasing=TRUE),]
}

# query search:
# head(search(corpus_se, "diagram"))
# then: corpus[["147"]] or whatever (document name)

# find doc similar to doc 278:
# head(search(corpus_se, content(corpus_se[["278"]])))
{% endhighlight %}
