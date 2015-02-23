---
layout: post
title: Hadoop
---

# Hadoop

## General strategy

Big data --> small data --> analysis

> "make big data as small as possible as quick as possible" -- [@EllieMcDonagh](https://twitter.com/EllieMcDonagh/status/469184554549248000)

## Overview

The following quotes are adapted from [Wikipedia](http://en.wikipedia.org/wiki/Apache_Hadoop). Some text was changed.

> Apache Hadoop is a set of algorithms (an open-source software framework written in Java) for distributed storage and distributed processing of very large data sets on computer clusters built from commodity hardware. All the modules in Hadoop are designed with a fundamental assumption that hardware failures (of individual machines, or racks of machines) are commonplace and thus should be automatically handled in software by the framework.

> The core of Apache Hadoop consists of a storage part (Hadoop Distributed File System (HDFS)) and a processing part (YARN). Hadoop splits files into large blocks (default 128MB) and distributes the blocks amongst the nodes in the cluster. To process the data, Hadoop YARN transfers code (specifically Java JAR files) to nodes (slaves) that have the required data, which the nodes then process in parallel. This approach takes advantage of data locality to allow the data to be processed faster and more efficiently via distributed processing than by using a more conventional supercomputer architecture that relies on a parallel file system where computation and data are connected via high-speed networking.

**The primary point is data locality:** The processing job is **sent to the machine with the data**. Thus, it is essential that each machine that holds data also be able to run processing tasks. Thus, data should not be kept on a network storage system or otherwise dumb storage media. **It is assumed that each slave machine has large disks, powerful processors, and lots of memory.**

Hadoop is designed for **horizontal** scaling rather than vertical scaling. There is very little hierarchy with respect to storage and processing capabilities. More slave nodes may be added without changes to the architecture of the cluster. **Each slave node is similar and does not have a special role.**

## YARN

"Yet Another Resource Negotiator" is the Hadoop "operating system." I.e., it manages jobs and resources.

The following notes are adapted from [Hortonworks](http://hortonworks.com/hadoop/yarn/), which offers a customized version of Hadoop.

YARN is made up of:

- a global ResourceManager
- a per-application ApplicationMaster
- a per-node slave NodeManager
- a per-application Container running on a NodeManager

> The ResourceManager and the NodeManager formed the new generic system for managing applications in a distributed manner. The ResourceManager is the ultimate authority that arbitrates resources among all applications in the system. The ApplicationMaster is a framework-specific entity that negotiates resources from the ResourceManager and works with the NodeManager(s) to execute and monitor the component tasks.

> The ResourceManager has a scheduler, which is responsible for allocating resources to the various applications running in the cluster, according to constraints such as queue capacities and user limits. The scheduler schedules based on the resource requirements of each application.

> Each ApplicationMaster has responsibility for negotiating appropriate resource containers from the scheduler, tracking their status, and monitoring their progress. From the system perspective, the ApplicationMaster runs as a normal container.

> The NodeManager is the per-machine slave, which is responsible for launching the applications’ containers, monitoring their resource usage (cpu, memory, disk, network) and reporting the same to the ResourceManager.

Finally, a definition of containers:

> A **container** is an application-specific process that's created by a NodeManager on behalf of an ApplicationMaster. The ApplicationMaster itself is also a container, created by the ResourceManager. A container created by an ApplicationMaster can be an arbitrary process [...]. This is the power of YARN---the ability to launch and manage any process across any node in a Hadoop cluster. --- from **Hadoop in Practice**, p. 29; typos fixed.

## History

Hadoop was created by [Doug Cutting](http://en.wikipedia.org/wiki/Doug_Cutting) and [Mike Cafarella](http://en.wikipedia.org/wiki/Mike_Cafarella) in 2005 in order to crawl the web with their [Nutch](http://nutch.apache.org/) software. Cutting was employed at Yahoo! at the time, and chose the name "Hadoop" and the elephant logo (his son had a toy elephant he called hadoop).

Hadoop implemented two significant systems developed by Google and published as research papers. One is [MapReduce](/notes/mapreduce.html), which has been generalized into YARN, and the Google File System, which has become [HDFS](/notes/hdfs.html).

## Cluster design

Some machines have dedicated roles:

- ResourceManager: Manages YARN "containers", i.e., jobs; starts/stops jobs, restarts failed jobs

- NameNode: Manages HDFS metadata and monitors slaves which store HDFS blocks

- Map-Reduce JobHistory: Records and archives finished MapReduce jobs

### Simulation vs. real hardware

> In real production clusters there is no server virtualization, no hypervisor layer.  That would only amount to unnecessary overhead impeding performance.  Hadoop runs best on Linux machines, working directly with the underlying hardware.  That said, Hadoop does work in a virtual machine.  That’s a great way to learn and get Hadoop up and running fast and cheap. -- [source](http://bradhedlund.com/2011/09/10/understanding-hadoop-clusters-and-the-network/)

### Replication and fault tolerance

You do not want any one slave to be the only place a block of data is kept. Rather, you want data to be replicated (not too much, but also not too little). A replication factor of 3 is the default. This means each block of data gets stored on 3 slaves.

Hadoop can be made "rack aware," meaning you can describe how the slaves are **physically** arranged in a rack in a storage facility. Sometimes, entire racks are lost (e.g., power or network outages). So, ideally, the replicated data won't all be replicated on machines in the same rack. However, machines on the same rack can communicate with each other faster (lower latency), so you want adjacent blocks of data to be stored on machines in the same rack. The rule is "for every block of data, two copies will exist in one rack, another copy in a different rack." ([source](http://bradhedlund.com/2011/09/10/understanding-hadoop-clusters-and-the-network/))

The NameNode detects and fixes problems with slaves according to the following diagram ([source](http://bradhedlund.com/2011/09/10/understanding-hadoop-clusters-and-the-network/)):

![NameNode roles](/images/namenode-roles.png)

## Related projects

Some of these notes are adapted from [this blog post](http://blog.dasroot.net/a-laymans-guide-to-the-big-data-ecosystem.html) and [this blog post](http://cloudfront.blogspot.in/2013/04/hadoop-herd-when-to-use-what.html).

Several companies produce Hadoop distributions, sometimes with significant enhancements:

- [Apache Hadoop](http://hadoop.apache.org/) -- the version we use, also known as "vanilla" Hadoop

- [Cloudera Hadoop](http://www.cloudera.com/content/cloudera/en/home.html)

- [Hortonworks Hadoop](http://hortonworks.com/)

- [MapR Hadoop](https://www.mapr.com/)

- [Pivotal HD](http://www.pivotal.io/big-data/pivotal-hd)

Some databases work on Hadoop/HDFS:

- [Apache HBase](http://hbase.apache.org/) -- better random read/write access to HDFS data; the data are represented in tables, up to billions of rows and millions of columns (but it does not support relations like in SQL databases)

- [Apache Hive](http://hive.apache.org/) -- supports a "data warehouse", provides SQL-like language for querying data stored in HDFS; you can give schemas for existing HDFS data, and then query it, or even query HBase data; technically, Hive queries are transformed into MapReduce jobs

Some projects generalize or specialize the processing workflow (i.e., beyond simple MapReduce):

- [Apache Pig](http://pig.apache.org/) -- provides the PigLatin language to more easily describe complex dataflow processing tasks; the progrems get transformed into MapReduce jobs and get executed in the usual way

- [Apache Oozie](http://oozie.apache.org/) -- "a scalable, reliable and extensible workflow scheduler system. You just define your workflows (which are Directed Acyclical Graphs) once and rest is taken care by Oozie. You can schedule MapReduce jobs, Pig jobs, Hive jobs, Sqoop imports and even your Java programs using Oozie. Tip: Use Oozie when you have a lot of jobs to run and want some efficient way to automate everything based on some time (frequency) and data availability." (from [this blog post](http://cloudfront.blogspot.in/2013/04/hadoop-herd-when-to-use-what.html))

- [Apache Spark](https://spark.apache.org/) -- "Spark is a fast and general processing engine compatible with Hadoop data. It can run in Hadoop clusters through YARN or Spark's standalone mode, and it can process data in HDFS, HBase, Cassandra, Hive, and any Hadoop InputFormat. It is designed to perform both batch processing (similar to MapReduce) and new workloads like streaming, interactive queries, and machine learning." ([source](https://spark.apache.org/faq.html)) -- "Run programs up to 100x faster than Hadoop MapReduce in memory, or 10x faster on disk." (from Spark homepage)

Other tools:

- [Apache Sqoop](http://sqoop.apache.org/) -- assists in transferring data to/from SQL databases and HDFS; recommendation from [this blog post](http://cloudfront.blogspot.in/2013/04/hadoop-herd-when-to-use-what.html): "Use Sqoop when you have lots of legacy data and you want it to be stored and processed over your Hadoop cluster or when you want to incrementally add the data to your existing storage."

- [Apache Flume](http://flume.apache.org/) -- supports "collecting, aggregating, and moving large amounts of log data" and other streaming/event data. In other words, Flume lets you continuously collect logging output and stick it into HDFS.

