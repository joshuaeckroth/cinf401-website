---
layout: post
title: "Cookbook: Map-Reduce"
---

# Cookbook: Map-Reduce


```
$ yarn jar wc.jar WordCount /jeckroth/wordcount/input /jeckroth/wordcount/output
$ hdfs dfs -get /jeckroth/wordcount/output/part-r-00000
```
```