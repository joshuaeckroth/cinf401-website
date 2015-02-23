---
layout: post
title: MapReduce
---

# MapReduce

*Note: See the corresponding [cookbook about MapReduce](/cookbook/mapreduce.html). This page has lecture notes.*

## Overview

![MapReduce diagram](/images/mapreduce_mapshuffle.png)

Image from [Harvard's CS109 course](http://nbviewer.ipython.org/github/cs109/content/blob/master/labs/lab8/lab8_mapreduce.ipynb).

Original paper from 2004: [PDF](http://static.googleusercontent.com/media/research.google.com/es/us/archive/mapreduce-osdi04.pdf)


![MapReduce detailed diagram](/images/mapreduce.png)

## Map stage

## Reduce stage

The following is quoted from [Hadoop for Dummies](http://www.dummies.com/how-to/content/the-shuffle-phase-of-hadoops-mapreduce-application.html).

> To speed up the overall MapReduce process, data is immediately moved to the reducer tasks’ nodes, to avoid a flood of network activity when the final mapper task finishes its work. This transfer happens while the mapper task is running, as the outputs for each record are stored in the memory of a waiting reducer task.

> Keep in mind that even though a reducer task might have most of the mapper task’s output, the reduce task’s processing cannot begin until all mapper tasks have finished.

> To avoid scenarios where the performance of a MapReduce job is hampered by one straggling mapper task that’s running on a poorly performing slave node, the MapReduce framework uses a concept called speculative execution. [see below]

> In case some mapper tasks are running slower than what’s considered reasonable, the Application Master will spawn duplicate tasks. Whichever task finishes first — the duplicate or the original — its results are stored to disk, and the other task is killed. If you’re monitoring your jobs closely and are wondering why there are more mapper tasks running than you expect, this is a likely reason.

> The output from mapper tasks isn’t written to HDFS, but rather to local disk on the slave node where the mapper task was run. As such, it’s not replicated across the Hadoop cluster.

### Speculative execution

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

