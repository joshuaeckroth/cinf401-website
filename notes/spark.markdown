---
layout: post
title: Spark
---

# Spark

*Note: See the corresponding [cookbook about Spark](/cookbook/spark.html). This page has lecture notes.*

## Running on delenn

```
spark-submit --master local[10] wordcount.py
```

Make sure the destination directory (from the `saveAsTextFile` function) does not exist.

## Example: Word count

```python
from pyspark import SparkContext, SparkConf
import re

if __name__ == "__main__":
    conf = SparkConf().setAppName("wordcount")
    sc = SparkContext(conf=conf)

    # large example:
    #books = sc.textFile("file:///bigdata/data/gutenberg/txts/*.txt")
    # small example:
    books = sc.textFile("file:///bigdata/data/gutenberg/gutenberg-small.txt")
    # split on non-word characters
    words = books.flatMap(lambda line: re.split(r'\W+', line)).map(lambda word: (word.lower(), 1))
    counts = words.reduceByKey(lambda cnt, wordcnt: cnt + wordcnt)
    counts.saveAsTextFile("file:///tmp/wordcounts")
```

## Example: StackExchange data

```python
from pyspark import SparkContext, SparkConf
import re

def findUserAge(line):
    try:
        acctmatch = re.match(r'.*AccountId\s*=\s*"(\d+)".*', line)
        agematch = re.match(r'.*Age\s*=\s*"(\d+)".*', line)
        if acctmatch and agematch:
            acct = acctmatch.group(1)
            age = agematch.group(1)
            return (acct,age)
    except:
        pass # returns None

if __name__ == "__main__":
    conf = SparkConf().setAppName("userages")
    sc = SparkContext(conf=conf)

    userfiles = sc.textFile("file:///bigdata/data/stackexchange/unzipped/*/Users.xml")
    acctages = userfiles.map(findUserAge)
    uniqueages = acctages.distinct()

    # notice the check for None
    agecounts = uniqueages.map(lambda x: (x[1],1) if x is not None else (None, 0))
    agesum = agecounts.reduceByKey(lambda cnt, s: cnt + s)
    agesum.saveAsTextFile("file:///tmp/agesum")
```


