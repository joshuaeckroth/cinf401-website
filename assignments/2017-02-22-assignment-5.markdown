---
layout: post
title: Assignment 5
due: "Wed Feb 22"
categories: [assignments]
---

# Assignment 5

Reproduce [these findings](http://blog.boomerangapp.com/2017/01/how-to-end-an-email-email-sign-offs/) using the entire archive of the [R-help mailing list](https://stat.ethz.ch/pipermail/r-help/). A CSV export of the emails is available on delenn at `/home/jeckroth/cinf401/r-help-threads.csv`. The CSV file contains the body of the first email in each thread plus some extra statistics. Replies and replies to replies, etc. in the same thread are not included. You can identify emails that have replies by checking if `msgcnt > 1`.

Write a report, with analysis and graphs mixed in (just like the original blog post), to argue that the blog's findings are reproduced or not reproduced by your analysis. Use proportion tests to determine if emails with "thanks" and variations more often receive replies.

**Do not include the CSV file in your git repository. I don't need to download it over and over again.**

