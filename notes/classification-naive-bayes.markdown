---
layout: post
title: "Classification: Naïve Bayes"
---

# Classification: Naïve Bayes

## Summary

- Naïve Bayesian classification uses Bayes' rule to determine the probability of each class (e.g., spam, not spam) for a given new instance.
- The training set is used to remember the probability of each feature appearing in an instance, given each class. E.g., the probability that a spam email has the word "money".
- The "naïve assumption" is the assumption that the value of some feature in an instance is completely independent (in a statistical sense) than the values of all the other features. Without this assumption, Bayesian classification is computationally intractable (i.e., far too slow).

## Background: Bayesian reasoning

## The naïve assumption

## Training a naïve Bayesian model

Each instance in the training set and each new, novel instance should have the same kinds of feature vectors. Some features might be continuous  (e.g., a person's height), some might be nominal/discrete (e.g., male vs. female).

In any case, we want to "learn" $P(f_i|C_k)$ for every feature $f_i$ and class $C_k$, that is, we want to know the probability of feature $i$ having the value it does given that we already know the feature is from an instance in the class $k$.

For example, we want to know $P(money|spam)$ and $P(money|notspam)$, among all the other words that may appear in an email.

For discrete features, like words in an email, we can simply count how often each word appeared in instances in each class. So, $P(money|spam) = 12/55$ if "money" appeared in 12 of 55 spam instances in the training set. Sometimes, a word, like "stetson", may not appear in any spam instances in our training set. But that word might appear in real spam in the future. Rather than make $P(stetson|spam)=0$, which seems logical, we add $+1$ to the top and bottom of every fraction (for every discrete feature) to be sure no probability is 0. (This is called [Laplace smoothing](http://en.wikipedia.org/wiki/Additive_smoothing).) That makes $P(money|spam)=(12+1)/(55+1)=13/56$, and $P(stetson|spam)=1/56$. You'll see in the remaining sections that this is a good idea. If we didn't do this, a new email having the word "stetson" would make its probability of being spam 0% because "stetson" never appeared in training spam.

<p>
For continuous features, we assume the values are normally distributed and use a bell curve to estimate $P(f_i|C_k)$. A bell curve, or Gaussian function, is defined by the mean and standard deviation: $P(f_i|C_k) = \left( 1/\sqrt{2\pi \sigma^2_k} \right) e^{-(f_i-\mu_k)^2/(2\sigma^2_k)}$, where $\sigma_k$ is the standard deviation of this feature for class $k$ and $\mu_k$ is the mean of this feature for class $k$. Of course, nobody actually codes this formula. We can use builtin functions, like the R function <code>pnorm(val, mean=m, sd=v)</code>.
</p>

Finally, we need to estimate the "prior" probabilities of any random instance being a member of each class. This is $P(C_k)$ for each class $k$. You can make them all equal (so 1/m for m classes) or count the number of training instances in each class and divide by the number of training instances total.

Given these probabilities, and applying Bayes' rule plus the naïve assumption, we can calculate the probability that a new instance has class $k$ given its features $\vec{f}$:

<div>
$$
\begin{eqnarray}
P(C_k|\vec{f}) &=& P(\vec{f}|C_k)P(C_k)/P(\vec{f}) \\\\
&=& P(f_1|C_k)P(f_2|C_k)\cdots P(f_n|C_k)P(C_k)/P(\vec{f}) \qquad \text{naïve assumption} \\\\
\end{eqnarray}
$$

We have each of these probabilities, except $P(\vec{f})$. We could compute that, but it's not important. We only want to figure out which class has a greater "posterior" probability:

<div>
$$
P(C_1|\vec{f}) > P(C_2|\vec{f}) \qquad \text{??}
$$
<div>

More generally, we want to find the $k$ that makes the probability maximum:

<div>
$$
\arg\max_k P(C_k|\vec{f})
$$
</div>


## A problem with tiny values

With a lot of features, we create very small values by multiplying
many probabilities. On a computer, the values may become so small
that they may "underflow" (run out of bits required to represent the
value). To prevent this, we just throw a logarithm around everything:

$$\log P(\hat{X}|C_c)P(C_c) = \log P(\hat{X}|C_c) + \log P(C_c),$$

and furthermore,

$$\log P(\hat{X}|C_c) = \log \prod_i P(w_i|C_c) = \sum_i \log P(w_i|C_c)$$

So our multiplications turn to sums, and we avoid the underflow
problem. Rewriting again, we ultimately have this problem:

$$\arg\max_{C_c} \sum_i \log P(w_i|C_c) + \log P(C_c)$$

## Evaluation

The book [Introduction to Information Retrieval](http://nlp.stanford.edu/IR-book/html/htmledition/irbook.html) gathered some published
results for classification tasks. We can see that naïve Bayes is
usually not as good as k-nearest neighbor (which we did learn about)
nor support vector machines (which we didn't learn about).

| Dataset  | Naïve Bayes | k-nearest neighbor | Support vector machines |
|----------|-------------|--------------------|-------------------------|
| earn     |        0.96 |               0.97 |                    0.98 |
| acq      |        0.88 |               0.92 |                    0.94 |
| money-fx |        0.57 |               0.78 |                    0.75 |
| grain    |        0.79 |               0.82 |                    0.95 |
| crude    |        0.80 |               0.86 |                    0.89 |
| trade    |        0.64 |               0.77 |                    0.76 |
| interest |        0.65 |               0.74 |                    0.78 |
| ship     |        0.85 |               0.79 |                    0.86 |
| wheat    |        0.70 |               0.77 |                    0.92 |
| corn     |        0.65 |               0.78 |                    0.90 |

## Benefits of naïve Bayes

  - It is very fast. In the table above, while naïve Bayes does not
    perform as well, it is significantly more efficient than either
    k-nearest neighbor or support vector machines. The latter, support
    vector machines, are /painfully/ slow (at least in the training
    phase).

## Drawbacks of naïve Bayes

  - Accuracy is low, as seen in the table above.
