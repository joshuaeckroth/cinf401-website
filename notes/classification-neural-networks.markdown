---
layout: post
title: "Classification: Neural networks"
---

# Classification: Neural networks

## The neuron

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

## Neurons as inspiration: The perceptron

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

## The perceptron learning algorithm

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

## Single-layer perceptron networks

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

## Logistic perceptrons

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

## Multilayer perceptron networks

![multi-layer-perceptron-network.png](/images/multi-layer-perceptron-network.png)

### Some definitions

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

### Derivation of learning rule

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

### Performance of multilayer perceptron on optdigits

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

## NetTalk example

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

