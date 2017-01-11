---
layout: post
title: "Classification: Random forests"
---

# Classification: Random forests

## Background: Decision trees

Should I go for a run? Consider the weather "Outlook" and "Humidity" and "Wind" attributes of this day:

![Decision tree: Run?](/images/decision-tree-run.png)

This decision tree examines the attributes and, depending on what they equal, moves down the tree and examines other attributes, until a leaf node (conclusion node) is reached.

Here is another example, for deciding whether or not to wait in line for a seat at a restaurant.

![Decision tree: Wait?](/images/decision-tree-restaurant.jpg)

### Learning decision trees

Given a bunch of instances with various attributes plus a "answer" or "class" attribute (with the true answer for these training instances), we can automatically build a decision tree that can predict the answer for novel instances.

When you start out, there is no tree, so the first choice is "what's the top root attribute to decide on?" You want to find the most *contentious* attribute, in other words, the attribute whose value would best *segregate* the instances. You want a near-even split of the instances.

Given this data,

| A | B | C  | answer |
|---|---|----|--------|
| 3 | 8 | 1  | Y      |
| 4 | 7 | 0  | X      |
| 3 | 8 | 0  | Y      |
| 3 | 7 | 1  | Y      |
| 3 | 8 | 0  | Y      |
| 3 | 8 | 0  | Y      |
| 3 | 7 | 0  | X      |

A bad choice would be to decide A first. The choices for A are 3 and 4. If a novel instance has A=3, you know almost nothing about whether that instance is an X or a Y.

A better choice is B: when B=8, 4/4 instances are Y's. When B=7, 1/3 instances are Y's.

Here is a display of the learned decision tree.

![Decision tree Ex 1](/images/decision-tree-ex-1.png)

The next display shows the proportion of X/Y answer attributes for each part of the tree. E.g., at the root (before any attributes are decided), 71% have answer=Y. After choosing that B=7, 33% of instances have answer=Y.

![Decision tree Ex 2](/images/decision-tree-ex-2.png)

We can mathematically decide which attribute is the best choice to separate the instances. "Information entropy" and "information gain" are used for this purpose. 

#### Information entropy

<div>
$$
H(S) = -\sum_{c \in C} P(a)\log_2 P(a),
$$
</div>

where $S$ is the set of instances, $C$ is the "answer" class, $a$ is each possible answer (X or Y), and $P(a)$ is the count of instances with that answer divided by the count of instances.

Entropy is greatest when each $a_i$ is equally likely. The plot below shows how entropy relates to the probability of a binary "answer" attribute.

![Information entropy](/images/information-entropy.png)

Given our example data, we need to compute $H$ after pretending to split on each attribute.

For attribute A, we have A=3 or A=4. When A=3, 1/6 instances are X and 5/6 are Y; when A=4, 1/1 are X. So,

<div>
$$
H(S_\text{when A=3}) = -(1/6)\log_2(1/6) - (5/6)\log_2(5/6) = 0.65
$$
</div>

<div>
$$
H(S_\text{when A=4}) = -(1/1)\log(1/1) = 0
$$
</div>

#### Information gain

What we really want to know is how much gain in information, or lowering of entropy, occurs when we split of A or B or C? Information gain is calculated as:

<div>
$$
Gain(S, Att) = H(S) - \sum_j P(Att=j) H(S_\text{when Att=j}),
$$
</div>

where $S$ is all instances (which may have been filtered from earlier attribute choices) and $Att$ is an attribute choice like A, B, or C, and $j$ ranges among the possible choices for that attribute (e.g., if $Att$ is A, then $j$ is 3 and 4), and $P(Att=j)$ is the fraction of instances in $S$ that have $Att=j$.

For example, to evaluate the information gain from choosing to split at the root on attribute A, we have:

<div>
$$
\begin{eqnarray}
Gain(S, \text{A}) &=& H(S) - \sum_j P(A=j) H(S_\text{when A=j}) \\
&=& (-(2/7)\log_2 (2/7) - (5/7)\log_2 (5/7)) - \sum_j P(Att=j) H(S_\text{when A=j}) \\
&=& 0.863 - \sum_j P(A=j) H(S_\text{when A=j}) \\
&=& 0.863 - (P(A=3)H(S_\text{when A=3}) + P(A=4)H(S_\text{when A=4})) \\
&=& 0.863 - ((6/7)H(S_\text{when A=3}) + (1/7)H(S_\text{when A=4})) \\
&=& 0.863 - ((6/7)\cdot 0.65 + (1/7)\cdot 0) \\
&=& 0.306
\end{eqnarray}
$$
</div>

Compare this to $Gain(S, \text{B})$, the information gain of choosing to split on attribute B:

<div>
$$
\begin{eqnarray}
Gain(S, \text{B}) &=& H(S) - \sum_j P(B=j) H(S_\text{when B=j}) \\
&=& 0.863 - \sum_j P(B=j) H(S_\text{when B=j}) \\
&=& 0.863 - (P(B=7)H(S_\text{when B=7}) + P(B=8)H(S_\text{when B=8})) \\
&=& 0.863 - ((3/7)H(S_\text{when B=7}) + (4/7)H(S_\text{when B=8})) \\
&=& 0.863 - ((3/7)(-(2/3)\log_2(2/3) - (1/3)\log_2(1/3)) + (4/7)(-(0/4)\log_2(0/4) - (4/4)\log_2(4/4)) \\
&=& 0.863 - ((3/7)(-(2/3)\log_2(2/3) - (1/3)\log_2(1/3)) + 0)\\
&=& 0.469
\end{eqnarray}
$$
</div>

So it is better to split at the root node on B than A. The same will be found by checking on C. B is the best root split.

After splitting on B, the instances are filtered and grouped according to their value of B. Then the same decision is made for each subtree (corresponding to each value of B).

## Random forests

A random forest is a collection of small decision trees. The idea was to reduce the error and possibility of "overfitting" (making a decision tree that is very specific for the training data but fails on test data and new instances). In order to reduce overfitting, many decision trees are produced using only a random subset of the training set and a random subset of the attributes of those instances. So each tree in a random forest was training on a fraction of data and only has splits (nodes in the tree) for a fraction of the attributes.

Since you have so many trees, now you need to figure out a final prediction by consulting each tree. Each tree will make its prediction about the answer for a new instance. The answer that's predicted most often wins.
