---
layout: post
title: MapReduce
---

# MapReduce

*Note: See the corresponding [cookbook about MapReduce](/cookbook/mapreduce.html). This page has lecture notes.*

## Overview

It's difficult to write parallelizable algorithms. Most algorithms involve a lot of "global state" and careful coordination of reads/writes to this state. By "global state" I mean something like a database table that is read/written by the algorithm in arbitrary ways at arbitrary times. Or in the case of sorting, the global state is the whole array or list, and the sorting algorithm needs to read/write various parts of it at various times.

Automatic parallelization, i.e., taking any algorithm and automatically rewriting it so it can be executed in a distributed, parallel fashion, is active research but still has not been figured out. So, we need to design algorithms to be parallelizable upfront.

There are many valid designs for parallel algorithms. One is MapReduce.

MapReduce was developed by Google and published in 2004 ([PDF](http://static.googleusercontent.com/media/research.google.com/es/us/archive/mapreduce-osdi04.pdf)). Their motivation was:

- Google uses lots of relatively simple algorithms

- But these algorithms have to be run on huge datasets and must be run on hundreds or thousands of machines to finish in a reasonable time

> The issues of how to parallelize the computation, distribute the data, and handle failures conspire to obscure the original simple computation with large amounts of complex code to deal with these issues.

So rather than add special handling for parallel/distributed processing to each program they write, they decided to write a general architecture, and associated libraries, that can keep your algorithm simple but still enable parallel/distributed processing.

That architecture is MapReduce. It is quite limiting. But those limitations are exploited to enable massively parallel computation. It works as follows:

- Your algorithm must be split into a "map" function and a "reduce" function (some algorithms won't use both functions).

- The map function is given an input file name and spits out key-value pairs.

- The reduce function is given key-value pairs, where the value is a list of all values with that same key as produced by the map function. The reduce function collects and reduces the information for this key-value and spits out another key-value, which is saved to the output file.

Each map and reduce function may be executed on a different machine in a cluster, at the same time. Hence the parallelism. (Note: the reduce stage cannot start until the map stage is completed.)

That's it. If you can't make your algorithm work as a separate map function and reduce function, you may not be able to use MapReduce. Not all algorithms can be split in that way. (The Hadoop YARN architecture may still be used, in most cases, to support parallel processing even for those algorithms that are not MapReduce algorithms.)

Between the map and reduce stages, the keys (output from map) get sorted and the values for each unique key get merged together, before handing off to reduce.

![MapReduce diagram](/images/mapreduce_mapshuffle.png)

Image from [Harvard's CS109 course](http://nbviewer.ipython.org/github/cs109/content/blob/master/labs/lab8/lab8_mapreduce.ipynb).

Here is another diagram with some more detail. Take note that "k1" and "k2" are keys and may be different types (Integer and String, or whatever). Same with "v1", "v2", and "v3", which are values, with possibly different types. Also, take note that the map function can output 0 or 1 or more new key-value pairs, and the reduce function may do likewise.

![MapReduce detailed diagram](/images/mapreduce.png)

MapReduce is sometimes called a "sort-merge" architecture, because of how the reduce stage works:

(1) The keys produced by the map stage are **sorted** before the reduce stage is started.

They look like this at first:

```
(k=92, v2=abc)
(k=17, v2=foo)
(k=10, v2=bar)
(k=12, v2=wsf)
(k=12, v2=fha)
(k=17, v2=jhd)
```

After sorting, they look like this:

```
(k=10, v2=bar)
(k=12, v2=wsf)
(k=12, v2=fha)
(k=17, v2=foo)
(k=17, v2=jhd)
(k=92, v2=abc)
```

(2) Next, values from duplicate keys are **merged** so that each unique key has a list of values.

After merging, the sorted keys look like this:

```
(k=10, v2=(bar))
(k=12, v2=(wsf, fha))
(k=17, v2=(foo, jhd))
(k=92, v2=(abc))
```

(3) One node from the cluster is then activated for each unique key in the merged result. So, it might look like this:

```
Box 34: run "reduce" operation on key 10, vals (bar)
Box 18: run "reduce" operation on key 12, vals (wsf, fha)
Box 10: run "reduce" operation on key 17, vals (foo, jhd)
Box 98: run "reduce" operation on key 92, vals (abc)
```

## Properties of the reduce stage

The following is quoted from [Hadoop for Dummies](http://www.dummies.com/how-to/content/the-shuffle-phase-of-hadoops-mapreduce-application.html).

> To speed up the overall MapReduce process, data is immediately moved to the reducer tasks’ nodes, to avoid a flood of network activity when the final mapper task finishes its work. This transfer happens while the mapper task is running, as the outputs for each record are stored in the memory of a waiting reducer task.

> Keep in mind that even though a reducer task might have most of the mapper task’s output, the reduce task’s processing cannot begin until all mapper tasks have finished.

> To avoid scenarios where the performance of a MapReduce job is hampered by one straggling mapper task that’s running on a poorly performing slave node, the MapReduce framework uses a concept called speculative execution. [see below]

> In case some mapper tasks are running slower than what’s considered reasonable, the Application Master will spawn duplicate tasks. Whichever task finishes first — the duplicate or the original — its results are stored to disk, and the other task is killed. If you’re monitoring your jobs closely and are wondering why there are more mapper tasks running than you expect, this is a likely reason.

> The output from mapper tasks isn’t written to HDFS, but rather to local disk on the slave node where the mapper task was run. As such, it’s not replicated across the Hadoop cluster.

## Speculative execution

From this [StackOverflow post](http://stackoverflow.com/a/15165199).

> One problem with the Hadoop system is that by dividing the tasks across many nodes, it is possible for a few slow nodes to rate-limit the rest of the program.

> Tasks may be slow for various reasons, including hardware degradation, or software mis-configuration, but the causes may be hard to detect since the tasks still complete successfully, albeit after a longer time than expected. Hadoop doesn’t try to diagnose and fix slow-running tasks; instead, it tries to detect when a task is running slower than expected and launches another, equivalent, task as a backup. This is termed speculative execution of tasks.

> For example if one node has a slow disk controller, then it may be reading its input at only 10% the speed of all the other nodes. So when 99 map tasks are already complete, the system is still waiting for the final map task to check in, which takes much longer than all the other nodes.

> By forcing tasks to run in isolation from one another, individual tasks do not know where their inputs come from. Tasks trust the Hadoop platform to just deliver the appropriate input. Therefore, the same input can be processed multiple times in parallel, to exploit differences in machine capabilities. As most of the tasks in a job are coming to a close, the Hadoop platform will schedule redundant copies of the remaining tasks across several nodes which do not have other work to perform. When tasks complete, they announce this fact to the [Application Master]. Whichever copy of a task finishes first becomes the definitive copy. If other copies were executing speculatively, Hadoop tells the them to abandon the tasks and discard their outputs. The Reducers then receive their inputs from whichever Mapper completed successfully, first.

> Speculative execution is enabled by default.

## Example uses

From the original paper ([PDF](http://static.googleusercontent.com/media/research.google.com/es/us/archive/mapreduce-osdi04.pdf))

> **Distributed Grep**: The map function emits a line if it matches a supplied pattern. The reduce function is an identity function that just copies the supplied intermediate data to the output.

> **Count of URL Access Frequency**: The map function processes logs of web page requests and outputs `<URL, 1>`. The reduce function adds together all values for the same URL and emits a `<URL, total count>` pair.

> **Reverse Web-Link Graph**: The map function outputs `<target, source>` pairs for each link to a target URL found in a page named source. The reduce function concatenates the list of all source URLs associated with a given target URL and emits the pair: `<target, list(source)>`

> **Term-Vector per Host**: A term vector summarizes the most important words that occur in a document or a set of documents as a list of `<word, frequency>` pairs. The map function emits a `<hostname, term vector>` pair for each input document (where the hostname is extracted from the URL of the document). The reduce function is passed all per-document term vectors for a given host. It adds these term vectors together, throwing away infrequent terms, and then emits a final `<hostname, term vector>` pair.

> **Inverted Index**: The map function parses each document, and emits a sequence of `<word, document ID>` pairs. The reduce function accepts all pairs for a given word, sorts the corresponding document IDs and emits a `<word, list(document ID)>` pair. The set of all output pairs forms a simple inverted index. It is easy to augment this computation to keep track of word positions.

> **Distributed Sort**: The map function extracts the key from each record, and emits a `<key, record>` pair. The reduce function emits all pairs unchanged. (The normal reduce process sorts the results of the map process according to the keys.)

