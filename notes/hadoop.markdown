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

### Simulation vs. real hardware

### Storage considerations

No NAS/SAN!

### Replication and fault tolerance


