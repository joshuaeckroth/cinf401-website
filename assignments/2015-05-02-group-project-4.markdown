---
layout: post
title: Group project 4
due: "May 2, 8:00am"
categories: [assignments]
---

# Group project 4

In a group of two people, classify spam/ham in the [TREC 2007](http://plg.uwaterloo.ca/~gvcormac/treccorpus07/) dataset (on delenn: `/bigdata/data/trec2007/trec07p`). The group with the best classifier wins cake.

## Requirements

- Create a naïve Bayesian classifier
- Create a random forest classifier
- Create two other classifiers (not decision tree, or random forest, or naïve Bayesian)

**Compare the performance of each classifier.** Note, the emails require preprocessing (at least converting text into word vectors). There are many ways to do this. Account for some varieties in your experiments.

**Present your findings on "finals" day.** Describe to us how your two other classifiers work. Tell us which among all attempted works best, and explain why.

You will be graded on several criteria:

- The appropriateness of how you munge the data
- How thoroughly you experiment with four different classifiers, and the appropriateness of your experimental methodology
- How well you present your findings and how well you explain your other classifiers

**Your deliverable is your presentation**, not any code or report. Plan a presentation of 9 minutes. This time limit will be strict. You will be cut-off mid-sentence. Say everything you need to say within 9 minutes.

You may use any tools you wish (Mahout, Weka, [BigML](https://bigml.com/), etc.). During your presentation, your choice of tools might be irrelevant. Prioritize telling us about your results and convincing us you found something approximating the truth.

## Suggestions

Consider these alternative classifiers:

- multilayer perceptrons (neural networks)
- support vector machines
- some other classifier plus [Word2Vec](https://code.google.com/p/word2vec/)
- look at existing literature and repeat their techniques
