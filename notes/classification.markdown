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

## k-nearest neighbor (KNN) algorithm

Summary:

- Classification is used for predicting the label on a new data point,
  using a model built from a set of training examples where the true
  labels were known.
- k-nearest neighbor determines the predicted label by asking the
  $k$-nearest neighbor points in the training set to "vote" for the
  label.
- k-nearest neighbor requires deciding upfront the value of $k$.
- This method is very simple but requires retaining all the training
  examples and searching through it.


Suppose we measure some properties of flowers, species of iris to be
precise. *But this time*, we also know their true "labels"
(species). We have a table like the following:

| Sepal length (cm) | Sepal width (cm) | Petal length (cm) | Petal width (cm) | Species         |
|-------------------|------------------|-------------------|------------------|-----------------|
|               5.1 |              3.5 |               1.4 |              0.2 | iris setosa     |
|               4.9 |              3.0 |               1.4 |              0.2 | iris setosa     |
|               4.7 |              3.2 |               1.3 |              0.2 | iris setosa     |
|               ... |              ... |               ... |              ... |                 |
|               7.0 |              3.2 |               4.7 |              1.4 | iris versicolor |
|               ... |              ... |               ... |              ... | ...             |
|               6.3 |              3.3 |               6.0 |              2.5 | iris virginica  |
|               ... |              ... |               ... |              ... | ...             |

Since we know the true labels, we do not need to use k-means
clustering to find (approximate) the species clusters.

If we get a new set of measurements from a new plant, we can predict
(approximate, guess) which species it is by calculating the mean of
each species cluster and finding which mean is closest.

However, instead of calculating means we typically use a different
approach. (Perhaps the two approaches are equivalent; I am not sure.)

Given a new set of measurements, perform the following test:

  1. Find (using Euclidean distance, for example), the $k$ nearest
     entities from the training set. These entities have known labels. The
     choice of $k$ is left to us.

  2. Among these $k$ entities, which label is most common? That is the
     label for the unknown entity.

What we have is a voting mechanism, where the most votes
wins. Sometimes it is a good idea to give each vote a weight, such as
the inverse of the entity's distant from the unknown entity (so more
distance votes count for less).

### Choosing k

![effect-of-k.png](/images/effect-of-k.png)

From [here](http://classes.engr.oregonstate.edu/eecs/fall2007/cs434/notes/knn-3.pdf).

The article "[Choosing $k$ for two-class nearest neighbour classifiers
with unbalanced classes](http://www.sciencedirect.com/science/article/pii/S016786550200394X)" by Hand and Vinciotti provides more
information and solutions for choosing $k$ when performing two-class
assignment (i.e., Yes/No assignments) when one class is substantially
larger than the other. They write in their conclusion,

Nearest neighbor supervised classification methods have many
attractive properties. However, many of these are based on asymptotic
arguments. In problems where the classification threshold lies near 0
or 1, as generally is the case in situations where one class is much
larger than the other, the fact that $k$ is finite results in a
non-monotonic relationship between the proportion of each class
correctly classified as $k$ varies. This means that, in general,
larger $k$ may not yield better performance than smaller $k$.

### Example: Handwritten digit recognition

Consider the following dataset, available from [UCI](http://archive.ics.uci.edu/ml/datasets/Optical+Recognition+of+Handwritten+Digits). Thirty people
wrote 3823 digits (0-9), which were then scanned as images and
translated into 64 features. Specifically, each digit was scanned into
a 32x32 pixel black-and-white image, which was divided into 4x4 pixel
blocks (64 of them); the number of black pixels in each 4x4 block was
counted. These counts constitute the 64 features.

The plot below shows each of the 3823 entities in the testing
set. Because the feature vector of each entity has 64 dimensions, we
cannot simply plot the feature vectors themselves. Instead, I reduced
the dimensionality to 2D using "multidimensional scaling." This
technique calculates the distance from each entity to each other
entity, creating a 3823x3823 matrix of distances. Then these distances
are reduced to two dimensions by finding an x,y pair for each entity
that respects its distances to the other entities. The scaling is not
perfect, but generally serves as a good approximation of how
"different" each entity is from each other entity.

![optdigits true classes](/images/optdigits-true-class-mds.png)

We see that each digit generally forms a cluster, although there is
some overlap. The overlap may well be an artifact of the
multidimensional scaling; perhaps there is no overlap if the data were
to be graphed in more dimensions.

The k-nearest neighbor algorithm can be visualized using this
plot. Say we get a new image, and we want our software to figure out
which digit it is. We find the $k$ nearest examples from the training
set, and figure out what is the most common digit among those
examples. Looking at the plot, we can imagine that if the new entity
would be placed in the bottom-middle area, that most of its nearest
neighbors would be green, representing the digit 4.

In fact, tests with this dataset have shown that the following $k$
values produce the corresponding recognition accuracies:

| k value | accuracy |
|---------|----------|
| 1       | 98.00    |
| 2       | 97.38    |
| 3       | 97.83    |
| 4       | 97.61    |
| 5       | 97.89    |
| 6       | 97.77    |
| 7       | 97.66    |
| ...     | ...      |

These are rather high accuracies for such a *simple* algorithm.

### How to handle noise

Some of the labeled training examples may be surround by the examples
from the other classes. Such a situation may arise by plenty of
reasons: (i) as a result of a random mistake, (ii) because insufficient
number of training example of this class (an isolated example appears
instead of cluster), (iii) because incomplete system of features (the
classes are separated in other dimensions which we do not know), (iv)
because there are too many training examples of other classes
(unbalanced classes) that create too dense "hostile" background for
the given small class and many other reasons. ([source](http://www.math.le.ac.uk/people/ag153/homepage/KNN/KNN3.html))

We can handle outliers by just removing them from the dataset. They
won't contribute any useful information for classification. Outliers
can be detected by defining two values $q \geq r > 0$ and say that a
point is an outlier with respect to some class if, among its $q$
nearest neighbors, there are more than $r$ examples from other
classes.

### KNN demo applet

Find a description of the applet [here](http://www.math.le.ac.uk/people/ag153/homepage/KNN/KNNexcercise.htm) and applet [here](http://www.math.le.ac.uk/people/ag153/homepage/KNN/KNN3.html).

Here is an example exercise. Ensure that the parameters are at default settings

  1. Create a class with green color by clicking at the top left
     corner of the work desk and also click on random to create some
     outliers

  2. Create a second class with blue color by clicking at the bottom
     right corner of the work desk and also click on random to create
     some outliers

  3. Test a query example at the center. (Hint: click on handle test
     menu, ensure the method is KNN and click at the center of the
     work desk. Example screen shot is shown below)

![Applet screenshot](/images/knn-applet-screenshot-1.jpg)

  1. Classify the test query using different values of $k$ = 3, 5, 10
     and 20. (To change $k$, Go to Parameter menu, change the Number
     of Nearest Neighbor, click Handle test Menu and click the point
     you want to classify, i.e., center of the work desk)

  2. Does varying the value of $k$ affect the classification and which
     $k$ gives a better classification?

  3. Calculate the map at the various $k$. What can you observe?

### Remarks

The following text is quoted from *Machine Learning* by Tom Mitchell, page 234.

What is the inductive bias of k-nearest neighbor? The inductive bias
corresponds to an assumption that the classification of an instance
$x$, will be most similar to the classification of other instances
that are nearby in Euclidean distance. 

One practical issue in applying k-nearest neighbor algorithms is that
the distance between instances is calculated based on all attributes
of the instance (i.e., on all axes in the Euclidean space containing
the instances). This lies in contrast to methods such as rule and
decision tree learning systems that select only a subset of the
instance attributes when forming the hypothesis. To see the effect of
this policy, consider applying k-nearest neighbor to a problem in
which each instance is described by 20 attributes, but where only 2 of
these attributes are relevant to determining the classification for
the particular target function. In this case, instances that have
identical values for the 2 relevant attributes may nevertheless be
distant from one another in the 20-dimensional instance space.  As a
result, the similarity metric used by k-nearest neighbor --- depending
on all 20 attributes-will be misleading. The distance between
neighbors will be dominated by the large number of irrelevant
attributes. This difficulty, which arises when many irrelevant
attributes are present, is sometimes referred to as the curse of
dimensionality. Nearest-neighbor approaches are especially sensitive
to this problem.

[...]

One additional practical issue in applying k-nearest neighbor is
efficient memory indexing. Because this algorithm delays all
processing until a new query is received, significant computation can
be required to process each new query. Various methods have been
developed for indexing the stored training examples so that the
nearest neighbors can be identified more efficiently at some
additional cost in memory. One such indexing method is the $kd$-tree,
in which instances are stored at the leaves of a tree, with nearby
instances stored at the same or nearby nodes. The internal nodes of
the tree sort the new query $x_q$, to the relevant leaf by testing
selected attributes of $x_q$.

## Naïve Bayesian classification

- Naïve Bayesian classification uses Bayes' rule to determine the probability of each class (e.g., spam, not spam) for a given new instance.
- The training set is used to remember the probability of each feature appearing in an instance, given each class. E.g., the probability that a spam email has the word "money".
- The "naïve assumption" is the assumption that the value of some feature in an instance is completely independent (in a statistical sense) than the values of all the other features. Without this assumption, Bayesian classification is computationally intractable (i.e., far too slow).

### Background: Bayesian reasoning

### The naïve assumption



### Training a naïve Bayesian model

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
\


### A problem with tiny values

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

### Evaluation

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

### Benefits of naïve Bayes

  - It is very fast. In the table above, while naïve Bayes does not
    perform as well, it is significantly more efficient than either
    k-nearest neighbor or support vector machines. The latter, support
    vector machines, are /painfully/ slow (at least in the training
    phase).

### Drawbacks of naïve Bayes

  - Accuracy is low, as seen in the table above.

## Neural networks

### The neuron

<div style="clear: both; float: right; width: 450px;">
<div style="text-align: center">
<b>The neuron</b><br/>
<img src="/images/neuron.png" />
<br/>
From <a
href="https://commons.wikimedia.org/wiki/File:Neuron-figure.svg">Wikimedia
Commons</a> </div></div>

The typical neuron consists of a cell body, a nucleus, the axon and
axon terminals, and dendrites. The dendrites split into complex trees
and receive signals from other neurons. Signals are sent through the
axon terminals to other neurons.

Neurons are found throughout the nervous system (brain, spinal cord,
periphery) but are most concentrated in the brain. A human brain has
about 100 billion neurons. Each neuron has, on average, about 7000
connections to other neurons.

This wouldn't be particularly remarkable except for the
generally-accepted theory that the brain is an "information processing
machine." That is to say, it is *computing* something with all those
neurons. The question is, then, can properties of the brain be the
reason humans are intelligent, and can those properties be used in
systems we create?

The most intriguing property of the brain, apart from its massive
connectivity, is that each neuron is more-or-less doing the same
thing. Most neurons "activate" (discharges an electrical signal down
the axon) based on whether or not neurons connected to it have
recently activated themselves. Each connection to another neuron has a
certain strength, so that if the connected neuron activates, a signal
of a certain strength is received. The signals received add up, and
may cause the receiving neuron itself to activate.

The signals have to add up in a certain time frame, otherwise they
will just dissipate. If the receiving neuron does activate, it
completely discharges. It is an "all or nothing" event. More received
electrical charge does not create greater discharges; the amount of
received charge only determines /if/ an activation will occur.

Some connections among neurons are excitatory, which increase the
chance of activation by the receiving neuron, and inhibitory which do
the opposite.

<div style="clear: both; float: right; width: 450px;">
<div style="text-align: center">
<b>Action potential</b><br/>
<img src="/images/action-potential.png" />
<br/>
From <a
href="http://en.wikipedia.org/wiki/File:Action_potential_vert.png">Wikipedia</a>
</div></div>

The connections among neurons and their strengths are not
hard-wired. "Learning" is essentially the process of changing
connections ("synaptic pruning") and their strengths ("synaptic
plasiticity").

One important theory about how these changes come about is the Hebbian
learning rule (from Donald Hebb, 1949). It attempts to explain how
connections strengthen:

Let us assume that the persistence or repetition of a reverberatory
activity (or "trace") tends to induce lasting cellular changes that
add to its stability. [...] When an axon of cell A is near enough to
excite a cell B and repeatedly or persistently takes part in firing
it, some growth process or metabolic change takes place in one or both
cells such that A's efficiency, as one of the cells firing B, is
increased.

This is often summarized as: "Cells that fire together, wire
together." The theory would basically explain associative memory: if
you repeatedly hear the birthday song while you are simultaneously
looking at a birthday cake, eventually whenever you see or think about
a birthday cake, you are reminded of the song.

### Neurons as inspiration: The perceptron

We want to build systems that exhibit some properties of the
brain. These properties are:

  - Simple, nearly-identical components (neurons)

  - Massively-parallel computation

  - A learning mechanism

These properties are important for several reasons. Systems built out
of many similar (well, let's just say "identical") components may be
more robust if we can be certain the design of the identical
components is a good design. Massively-parallel computation promises
that very complex tasks can be done in very little time; accomplishing
this is still just pie in the sky for most systems built by
humans. And if we have access to a reliable learning mechanisms,
systems do not so much have to be designed but rather "trained."
Perhaps we can build systems that begin like children but learn, on
their own, to be highly capable. Perhaps we can just throw together a
bunch of simple "neurons," show the system lots of training examples,
and the system will do the rest of the work.

The first attempt was by [Frank Rosenblatt](http://www.rutherfordjournal.org/article040101.html#rosenblatt) in 1957. He designed the
"perceptron," a computational unit that can be described by the
following diagram.

![Perceptron](/images/perceptron.png)

The perceptron is analogous to a single neuron. It receives inputs
from $x_1, x_2, x_3$, etc. (any number of inputs). For convenience in
the learning process, it also receives another input that is always
the value 1.0. Each of these inputs are multiplied by a weight, to
emulate the strength of the neural connection and whether it is
excitatory or inhibitory (excitatory weights are positive, inhibitory
weights are negative, usually). The inputs multiplied by the weights
come into the perceptron and are summed. Then, a question has to be
answered: does this perceptron (neuron) activate? A simple function
decides this: if the sum is large enough, say above 0.5, then the
perceptron activates, sending a 1 out the right side. Otherwise, it
does not, sending a 0 out the right side.

<div>
\begin{equation}
f\left(\sum_{i=1}^k x_{i} w_{ji} + w_{j0}\right) =
\begin{cases}
0 & \text{if sum } < 0.5 \\
1 & \text{if sum } \geq 0.5
\end{cases}
\end{equation}
</div>

Each perceptron may be connected to others, so that a perceptron's
output can be other perceptrons' inputs. However, it turns out that
the complexity of the network greatly affects how learning will work.

The first property to notice about perceptrons is that a single
perceptron can compute simple logic functions, assuming we have binary
inputs. For example,

![not-perceptron.png](/images/not-perceptron.png)
![and-perceptron.png](/images/and-perceptron.png)

Outputs of perceptrons can become inputs to other perceptrons,
allowing us to build all possible logic circuits. Thus, perceptrons
are capable of general computation, putting them in the large class of
fantastically different systems that are Turing-complete. A
Turing-complete system is able to compute anything that can be
computed; examples include electrical logic gates (i.e., a typical
computer), the Lambda calculus, cellular automata, and now neural
networks.

But can a complex network learn? When we say "learn," we mean: can the
weights be assigned via a training process rather than assigned
manually? This would be ideal: just put together a bunch of
perceptrons, give it lots of examples (e.g., examples of prime numbers
and composite numbers), and let it figure out how to set the weights
so that it turns into a machine that computes that function (e.g., the
function to determine if a number is prime or not).

### The perceptron learning algorithm

Look again at the definition of the threshold function:

<div>
\begin{equation}
f\left(\sum_{i=1}^k x_{i} w_{ji} + w_{j0}\right) =
\begin{cases}
0 & \text{if sum } < 0.5 \\
1 & \text{if sum } \geq 0.5
\end{cases}
\end{equation}
</div>

Suppose we want to train a single perceptron. We want that function to
be 1 for positive examples and 0 for negative examples. This means we
want the sum of the inputs to be at least 0.5 for positive examples,
and less for negative examples.

The sum is a linear function. It can be "trained" using typical
methods that work on linear functions. One such method is gradient
descent (or ascent), a.k.a. hill-climbing. Normally, with gradient
descent, we compute the derivative (gradient) of the objective
function (the threshold function $f$) and determine which direction is
the greatest gradient, and adjust the weights. However, the threshold
function is not differentiable (it is a step function, discontinuous at
0.5), but we have a shortcut.

Let $d_j$ be the true ($d$ for "desired") answer for some input
vector, and $y_j$ but the output computed by the $j$'th perceptron
with its current weights $w_{ij}$ (weight from input $i$ to this
perceptron $j$). We do not want to adjust the weights if the output is
correct. If the output is 0 and we should have yielded a 1, then the
weights are too low, and should be adjusted up. If the output is a 1
and we wanted a 0, the weights are too high. Of course, we only want
to adjust a tiny bit, so we do not oscillate. Thus, we have the
following adjustment for each weight:

<div>
$$w'_{ij} = w_{ij} + \alpha (d_j-y_j)x_i,$$
</div>

where $w'_{ij}$ is the new weight and $\alpha$ is a small number (0.1,
or 0.01, etc.). Each weight is adjusted based on the input $x_i$ that
corresponds to that weight since some inputs are large and others
small (possibly even negative if we get away from boolean
inputs). Weights may become large, small, or even negative depending
on the nature of the function being learned and the nature of the
inputs. Since each adjustment is so small, the same perceptron may
need to be shown the same (positive and negative) examples many
times. When a perceptron is shown all examples once, we say it has
trained on one "epoch." Often we train across hundreds or thousands of
epochs.

A single perceptron can have many inputs; perhaps thousands. What
kinds of functions can it learn? Recall the sum of the weighted inputs
is a linear function. The threshold function just says "yes" or "no"
based on this sum. Suppose there are two inputs: then the weighted sum
describes a line in 2D. The threshold function just says "yes" if an
input vector is above (or below) the line, or exactly on the line, and
"no" otherwise.

Here is a diagram of this threshold line:

![Linearly separable](/images/linearly-separable.png)

The answer is, therefore, that a perceptron can learn any "linearly
separable" function. If the positive examples and negative examples
can be plotted (possibly in higher dimensions), and there exists a
line/plane that separates the two classes, then a perceptron can learn
that line/plane. We know this because the line/plane can be described
by a weighted sum of the inputs. This fact is why you may find
perceptrons described in terms of "computational geometry" --- they
are effectively learning geometric functions.

It turns out that not all functions are linearly separable. The
simplest case is the XOR function. You can plot it like so:

![XOR plot](/images/xor-plot.png)

There is simply no line that separates the positive and negative
examples of the XOR function. A perceptron cannot learn this function.

This is such a basic function that, when Minsky and Papert proved in
1969 that XOR could not be learned, perceptrons took a huge
hit. Research in neural networks did not fully recover until the
1980s, when a new learning mechanism was discovered.

### Single-layer perceptron networks

Putting lots of perceptrons together in a single-layer network changes
nothing. They all learn independently. (So XOR still cannot be
learned.) However, we can get more than 0/1 as an output. We can get
any number of outputs we wish.

![Single layer perceptron network](/images/single-layer-perceptron-network.png)

These networks are "fully connected" and "feed-forward" --- fully
connected because each input connects to every perceptron, and
feed-forward because there are no loops. "Recurrent" networks have
loops, where the output of some perceptron becomes the input to
another perceptron earlier in the chain, or an input to
itself. Recurrent networks are decidedly more complex (they can be
dynamic non-linear systems) but are able to keep memories and other
such interesting features.

### Logistic perceptrons

When the function being learned is not linearly separable, the
threshold function causes some oscillating. This is because points
near the separator are continually changing classifications as the
weights update and move the separator.

We also may desire probabilistic answers rather than yes/no
answers. If the data cannot actually be separated, then
classifications near boundary should be less confident than
classifications far from the boundary. It would be nice if the
perceptrons can report this confidence as a classification
probability.

The threshold function we saw cannot do this for us. We need a
continuous function that looks the same but gives graded answers.

A good choice is the logistic function:

<div>
$$f(s_j) = \frac{1}{1+e^{-s_j}}$$
</div>
<p>
where $s_j = \sum_{i=1}^k x_{i} w_{ij} + w_{0j}$. This is what it
looks like:
</p>

![Logistic curve](/images/logistic-curve.png)

Standard logistic curve (a.k.a. sigmoid function), from [Wikipedia](http://en.wikipedia.org/w/index.php?title=File:Logistic-curve.svg&page=1)

Since we no longer have binary outputs, but rather probabilistic ones,
we need to change our evaluation function. An output is no longer
either correct or incorrect. If the true answer is 1.0, and the
logistic function gives 0.79, then we say the error is 0.21. For the
sake of easy derivatives, we'll go further and say the error, now
called the "loss," is the square of the difference:

<div>
$$L_j(d_j, p_j) = (d_j - p_j)^2,$$
</div>

where $d_j$ is the correct answer (1.0 or 0.0, binary), and
$p_j=f(s_j)$ is the probability from the logistic function. We'll say
the total loss is the sum of the loss from each perceptron. (Recall
that a single-layer perceptron network is just a collection of
independent perceptrons, which are essentially trained separately.)

Our learning algorithm needs to be revised. Luckily, we can just work
backwards from the loss function and attempt to walk up/down the
gradient. Since the logistic function and the loss function are
differentiable, this gradient is easy to find for each weight
separately.

<div>
\begin{eqnarray}
\frac{\partial L_j}{\partial w_{ij}}
&=& \frac{\partial}{\partial w_{ij}} (d_j - f(s_j))^2 \\
&=& 2 (d_j - f(s_j)) \frac{\partial}{\partial w_{ij}} (d_j - f(s_j)) \qquad \text{chain rule} \\
&=& -2 (d_j - f(s_j)) f'(s_j) \frac{\partial}{\partial w_{ij}} s_j \qquad \text{chain rule}\\
&=& -2 (d_j - f(s_j)) f'(s_j) \frac{\partial}{\partial w_{ij}} \sum_{i'=1}^k x_{i'} w_{ij'} + w_{j0} \qquad \text{expand } s_j\\
&=& -2 (d_j - f(s_j)) f'(s_j) x_{i} \qquad \text{picks out } x_{i} w_{ij} \text{, whose derivative is } x_{i}
\end{eqnarray}
</div>

Note that $f'(z) = f(z)(1-f(z))$. This makes the logistic function a
good choice (in addition to its shape, which approximates the
threshold function). We can push the $2$ into the $\alpha$
parameter, yielding:

<div>
$$\frac{\partial L_j}{\partial w_{ij}} = -\alpha (d_j - f(s_j)) f(s_j) (1 - f(s_j)) x_{i}$$
</div>

We want to update our weights in the opposite direction of the loss
gradient. So our weight update is the following, with $p_j = f(s_j)$:

<div>
$$w'_{ij} = w_{ij} + \alpha (d_j - p_j) p_j (1 - p_j)) x_{i}$$
</div>

As an example, consider the task of classifying handwritten digits
with classes 0-9. Here are some examples (intentionally pixelated to
reduce the input space, i.e., the number of pixels):

![Optdigits](/images/optdigits.jpg)

![Optdigits logistic](/images/optdigits-logistic-alpha.png)

### Multilayer perceptron networks

![multi-layer-perceptron-network.png](/images/multi-layer-perceptron-network.png)

#### Some definitions

For convenience later, we define the output node error gradient as:

<div>
$$\Delta_k = -2 (d_k - p_k)f'(s_k)$$
</div>

And the hidden node error gradient (contribution of error of next node $k$ on
this node $j$):

<div>
$$\Delta_{jk} = \Delta_k w_{jk} f'(s_j)$$
</div>

And the total error on hidden node $j$:

<div>
$$\Delta_j = \sum_k \Delta_k w_{jk} f'(s_j),$$
</div>

i.e., the sum across all nodes $k$ to which the node $j$ is connected.

#### Derivation of learning rule

For an output node $k$:

<div>
\begin{eqnarray}
\frac{\partial L_k}{\partial w_{jk}}
&=& -2(d_k - p_k) \frac{\partial p_k}{\partial w_{jk}} \\
&=& -2(d_k - p_k) \frac{\partial f(s_k)}{\partial w_{jk}} \\
&=& -2(d_k - p_k) f'(s_k) \frac{\partial s_k}{\partial w_{jk}} \\
&=& -2(d_k - p_k) f'(s_k) \frac{\partial}{\partial w_{jk}}\Big( \sum_j
w_{jk} p_j + w_{0k} \Big) \\
&=& -2(d_k - p_k) f'(s_k) p_j \\
&=& p_j \Delta_k
\end{eqnarray}
</div>

For a hidden node $j$, connected (only) to output node $k$:

<div>
\begin{eqnarray}
\frac{\partial L_k}{\partial w_{ij}}
&=& -2(d_k - p_k) \frac{\partial p_k}{\partial w_{ij}} \\
&=& -2(d_k - p_k) \frac{\partial f(s_k)}{\partial w_{ij}} \\
&=& -2(d_k - p_k) f'(s_k) \frac{\partial s_k}{\partial w_{ij}} \\
&=& \Delta_k \frac{\partial}{\partial w_{ij}} \Big( \sum_j w_{jk} p_j + w_{0k} \Big) \\
&=& \Delta_k w_{jk} \frac{\partial p_j}{\partial w_{ij}} \\
&=& \Delta_k w_{jk} \frac{\partial f(s_j)}{\partial w_{ij}} \\
&=& \Delta_k w_{jk} f'(s_j) \frac{\partial s_j}{\partial w_{ij}} \\
&=& \Delta_k w_{jk} f'(s_j) \frac{\partial}{\partial w_{ij}} \Big( \sum_i w_{ij} p_i + w_{0j} \Big) \\
&=& \Delta_k w_{jk} f'(s_j) p_i \\
&=& p_i \Delta_{jk}
\end{eqnarray}
</div>

If $j$ is connected to more nodes, we would use $\Delta_j$ instead of
$\Delta_{jk}$ at the end.

For input hidden node, $p_i = x_i$.

#### Performance of multilayer perceptron on optdigits

```
=== Evaluation on test split ===
=== Summary ===

Correctly Classified Instances         551               98.0427 %
Incorrectly Classified Instances        11                1.9573 %
Kappa statistic                          0.9782
Mean absolute error                      0.0058
Root mean squared error                  0.0612
Relative absolute error                  3.2483 %
Root relative squared error             20.4131 %
Total Number of Instances              562     

=== Detailed Accuracy By Class ===

               TP Rate   FP Rate   Precision   Recall  F-Measure   ROC Area  Class
                 0.962     0          1         0.962     0.981      0.999    0
                 0.957     0.002      0.978     0.957     0.968      0.998    1
                 1         0.002      0.982     1         0.991      1        2
                 0.939     0.004      0.969     0.939     0.954      1        3
                 1         0.002      0.984     1         0.992      1        4
                 0.982     0.008      0.931     0.982     0.956      0.999    5
                 1         0.002      0.982     1         0.991      1        6
                 0.984     0.002      0.984     0.984     0.984      1        7
                 0.983     0          1         0.983     0.991      0.999    8
                 1         0          1         1         1          1        9
Weighted Avg.    0.98      0.002      0.981     0.98      0.98       1    

=== Confusion Matrix ===

  a  b  c  d  e  f  g  h  i  j   <-- classified as
 51  0  1  0  0  0  1  0  0  0 |  a = 0
  0 45  0  0  1  0  0  1  0  0 |  b = 1
  0  0 55  0  0  0  0  0  0  0 |  c = 2
  0  0  0 62  0  4  0  0  0  0 |  d = 3
  0  0  0  0 63  0  0  0  0  0 |  e = 4
  0  0  0  1  0 54  0  0  0  0 |  f = 5
  0  0  0  0  0  0 56  0  0  0 |  g = 6
  0  0  0  1  0  0  0 62  0  0 |  h = 7
  0  1  0  0  0  0  0  0 58  0 |  i = 8
  0  0  0  0  0  0  0  0  0 45 |  j = 9
```

#### NetTalk example

![NetTalk](/images/nettalk.gif)

[Source](http://www.cs.trincoll.edu/~ram/cpsc352/notes/learning/ml.nn.html)

Like a biologist taking a scalpel to a rat, he can ablate his network,
removing a few cells from the simulation. These ''lesions'' might
cause NetTalk to act a little fuzzy, but it retains its general
ability to pronounce English.

And so it is with humans. If one cuts a few wires in a conventional
computer the whole system will crash. But brains have the kind of
robustness demonstrated by Dr. Sejnowski's invention. --- [NY Times,
1988](http://www.nytimes.com/1988/08/16/science/learning-then-talking.html)

[YouTube](http://youtu.be/4gzpd0irP58?t=38m) (38min - 49min)

