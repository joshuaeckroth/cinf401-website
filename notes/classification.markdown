---
layout: post
title: Classification
---

# Classification

## Evaluation

Summary:

- $precision = \frac{tp}{tp+fp}$.
- $recall = \frac{tp}{tp+fn}$.
- High precision can be achieved trivially, at the cost of recall; and
  vice versa.
- $fscore = 2\*precision\*recall/(precision+recall)$ is the
  harmonic mean of precision and recall, which produces a better
  average of the two measures than an arithmetic mean.
- 10-fold cross validation means we choose 90% of the data to be the
  training set, and 10% to be the testing set. We evaluate the
  precision/recall/etc. with this split, then choose a different 90/10
  split and do it again. Because there are 10 possible splits, we do
  it 10 times and average 10 results.

### Precision, Recall, F1

Assume we have a classifier that adds labels (classes) to each
document or data point. How do we evaluate how well it is doing its
job? We have three cases, considering all predicted labels as a group:

- True positive (tp) --- predicted labels that are true labels

- False positive (fp) --- predicted labels that are not true labels

- False negatives (fn) --- true labels that were not predicted

Since these measures depend on the number of labels (predicted and
true), and that may differ depending on the document/data point or
experiment, we calculate the following normalized scores:

- Precision --- $tp/(tp+fp)$ --- higher precision means the labels that were predicted were more often true labels

- Recall --- $tp/(tp+fn)$ --- higher recall means more of the true labels were predicted

We can achieve very high precision by only predicting labels for which
we have very high confidence (until we nearly choose no labels at
all); this would decrease recall, however. We can achieve high recall
by predicting every label for every data point; this would decrease
precision, however.

Best would be high precision and high recall. We can measure this by
combining precision and recall into a single formula, called the
"F-score" or "F1-measure":

- F-score --- $2\*precision\*recall / (precision + recall)$

Notice that precision and recall are treated equally in this
calculation. Technically, the F-score is the harmonic mean of precision
and recall.

This graph (from [Wikipedia](http://en.wikipedia.org/wiki/File:Harmonic_mean_3D_plot_from_0_to_100.png)) shows the harmonic mean of two numbers
($x$ and $y$, which you can interpret as $precision$ and $recall$). It
shows the relationship we want: if either $x$ or $y$ is $0$, the
F-score is $0$. Otherwise, the F-score generally increases with
increases in $x$ and $y$.

![harmonic-mean.png](/images/harmonic-mean.png)

### Multiple-Fold Cross Validation

When we have one dataset with the answers (the "class" for each data
point), we can split this dataset into a training and testing
portion. The training portion is used to build a model of the dataset,
and the testing version is used to test that model. We'll want to split
the dataset multiple times at random places and then average the
results. That way, we can be sure that we didn't just get lucky (or
unlucky) and split the dataset into training/testing portions that are
not representative of all the data.

Most common is 10-fold cross validation. This means we choose 90% of
the data to be the training set, and 10% to be the testing set. We
evaluate the precision/recall/etc. with this split, then choose a
different 90/10 split and do it again. Because there are 10 possible
splits, we do it 10 times and average 10 results. The graph below
(from [Selecting Representative Data Sets](http://www.intechopen.com/books/advances-in-data-mining-knowledge-discovery-and-applications/selecting-representative-data-sets) by Borovicka, et al.)
illustrates cross validation.

![Multiple-fold cross validation](/images/cross-validation.jpg)

