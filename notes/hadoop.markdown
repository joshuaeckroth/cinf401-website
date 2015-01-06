---
layout: post
title: Hadoop
---

# Hadoop

*Note: See the corresponding [cookbook about HDFS](/cookbook/hdfs.html) and [cookbook about Map-Reduce](/cookbook/map-reduce.html). This page has lecture notes.*

## YARN

From http://hortonworks.com/hadoop/yarn/

YARN is made up of:

- a global ResourceManager
- a per-application ApplicationMaster
- a per-node slave NodeManager
- a per-application Container running on a NodeManager

> The ResourceManager and the NodeManager formed the new generic system for managing applications in a distributed manner. The ResourceManager is the ultimate authority that arbitrates resources among all applications in the system. The ApplicationMaster is a framework-specific entity that negotiates resources from the ResourceManager and works with the NodeManager(s) to execute and monitor the component tasks.

> The ResourceManager has a scheduler, which is responsible for allocating resources to the various applications running in the cluster, according to constraints such as queue capacities and user limits. The scheduler schedules based on the resource requirements of each application.

> Each ApplicationMaster has responsibility for negotiating appropriate resource containers from the scheduler, tracking their status, and monitoring their progress. From the system perspective, the ApplicationMaster runs as a normal container.

> The NodeManager is the per-machine slave, which is responsible for launching the applications’ containers, monitoring their resource usage (cpu, memory, disk, network) and reporting the same to the ResourceManager.



### Containers

> A **container** is an application-specific process that's created by a NodeManager on behalf of an ApplicationMaster. The ApplicationMaster itself is also a container, created by the ResourceManager. A container created by an ApplicationMaster can be an arbitrary process [...]. This is the power of YARN---the ability to launch and manage any process across any node in a Hadoop cluster. --- from **Hadoop in Practice**, p. 29; typos fixed.

## Map-Reduce

![Map-Reduce diagram](/images/mapreduce_mapshuffle.png)

Image from [Harvard's CS109 course](http://nbviewer.ipython.org/github/cs109/content/blob/master/labs/lab8/lab8_mapreduce.ipynb).

### Map stage

### Reduce stage

## Cluster design

### Simulation vs. real hardware

### Storage considerations

No NAS/SAN!

### Replication and fault tolerance


