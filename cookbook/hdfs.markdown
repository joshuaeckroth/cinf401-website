---
layout: post
title: "Cookbook: HDFS"
---

# Cookbook: HDFS

## Get help

```
$ hdfs dfs -help


## List files

```
$ hdfs dfs -ls /
Found 1 items
drwxrwx---   - root supergroup          0 2015-01-02 11:15 /tmp
```

```
$ hdfs dfs -mkdir -p /jeckroth/wordcount/input
$ hdfs dfs -put input/* /jeckroth/wordcount/input
```

