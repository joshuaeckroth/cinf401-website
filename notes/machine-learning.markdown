---
layout: post
title: Machine learning
---

# Machine learning

- "Unsupervised" learning takes unlabeled data and attempts to find
  relationships among the data.
- "Supervised" learning takes labeled data (the training set), builds
  a model, and uses this model on new data to predict unknown
  properties (such as class labels).

## Unsupervised learning

  - No feedback about true labels; only (often imperfect) measurements

  - Often group entities by their similarities to each other

  - Not easy to evaluate performance (since we don't have any true
    labels)

## Supervised learning

  - A set of data is labeled with true labels; this is the "training"
    set

  - Learning is based on measurements and true labels; how do certain
    measures provide evidence for certain labels?

  - Easy to evaluate performance: % of correctly-labeled cases on a
    "test" set (which is distinct from the "training" set; possibly,
    the original training set is split, with one small subset saved
    for testing purposes)
