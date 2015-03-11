---
layout: post
title: Group project 2
due: "Mar 24, 11:59pm"
categories: [assignments]
---

# Group project 2

Using a data dump from [StackExchange](http://stackexchange.com/) (all [sites](http://stackexchange.com/sites) dumped; [dataset torrent](https://archive.org/details/stackexchange)), answer the following questions:

- Do high-reputation users answer questions faster?

- Is it better to answer a question very quickly (or even first) to earn high reputation, or does time-to-answer not matter much at all? (Maybe quality of answer matters more.)

The data live in HDFS at `/data/stackexchange` ([web view](http://localhost:9000/hadoop/namenode:50070/explorer.html#/data/stackexchange)). Have a look at `readme.txt` to understand the fields in the various XML files.

Use MapReduce and then analyze in R. Add your MapReduce code to your git repository.

Study these questions across all the StackExchange sites combined. But also highlight/plot the results from a few large sites if they stand out as very different than the majority.

Use statistics where appropriate. Write a good English report; don't spew R code or output in your report.

## Extra credit

For a couple extra points, answer one or more of the following questions. You must also answer the questions above.

- Does the first answer usually get the most votes?

- Does the first answer usually get accepted?

- In summary, what are the advantages (if any) of giving the first answer?


