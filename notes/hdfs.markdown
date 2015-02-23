---
layout: post
title: HDFS
---

# HDFS

*Note: See the corresponding [cookbook about HDFS](/cookbook/hdfs.html). This page has lecture notes.*

## Overview

Original paper (2003): [PDF](http://static.googleusercontent.com/media/research.google.com/en//archive/gfs-sosp2003.pdf)

### Assumptions

These quotes are copied from the original paper ([PDF](http://static.googleusercontent.com/media/research.google.com/en//archive/gfs-sosp2003.pdf)). Emphasis added.

> **The system is built from many inexpensive commodity components that often fail.** It must constantly monitor itself and detect, tolerate, and recover promptly from component failures on a routine basis.

> **The system stores a modest number of large files.** We expect a few million files, each typically 100 MB or larger in size. Multi-GB files are the common case and should be managed efficiently. **Small files must be supported, but we need not optimize for them.**

> **The workloads primarily consist of two kinds of reads: large streaming reads and small random reads.** In large streaming reads, individual operations typically read hundreds of KBs, more commonly 1 MB or more. Successive operations from the same client often read through a contiguous region of a file. A small random read typically reads a few KBs at some arbitrary offset. **Performance-conscious applications often batch and sort their small reads to advance steadily through the file rather than go back and forth.**

> **The workloads also have many large, sequential writes that append data to files.** Typical operation sizes are similar to those for reads. Once written, files are seldom modified again. Small writes at arbitrary positions in a file are supported but do not have to be efficient.

> **The system must efficiently implement well-defined semantics for multiple clients that concurrently append to the same file.** Our files are often used as producer-consumer queues or for many-way merging. Hundreds of producers, running one per machine, will concurrently append to a file. Atomicity with minimal synchronization overhead is essential. The file may be read later, or a consumer may be reading through the file simultaneously.

> **High sustained bandwidth is more important than low latency.** Most of our target applications place a premium on processing data in bulk at a high rate, while few have stringent response time requirements for an individual read or write.

## Small files

These notes are adapted from our Hadoop book, p. 148.

There are three major issues with processing many small files in Hadoop:

- Recall that the NameNode keeps HDFS metadata in memory. Only the NameNode knows where every chunk of every file is stored (which slaves, i.e., data nodes, store which data). Each file has about 600 bytes of metadata, so 1 billion files requires 600 GB of memory on the NameNode.

- MapReduce splits the map task according to the number of blocks for the input data (assuming the files are splittable). Each block gets one map job. Running MapReduce on thousands of input files produces thousands of map jobs, which would ruin performance due to the overhead of creating and destroying map jobs.

- Hadoop uses a scheduler to determine which jobs run at which times. In some configurations, the scheduler may reject a MapReduce job if there are too many map jobs. As stated in the above bullet, a small file will take one block, and each block gets its own map job, hence MapReduce over many files will produce many map jobs, which may get rejected by the scheduler.

The default HDFS block size is 128 MB. If you find the need to store many files less than 128 MB, then you should consider compaction techniques. These techniques may be found in the [HDFS cookbook](/cookbook/hdfs.html).
