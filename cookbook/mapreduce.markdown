---
layout: post
title: "Cookbook: MapReduce"
---

# Cookbook: MapReduce

*Note: See the corresponding [lecture notes about MapReduce](/notes/mapreduce.html). This page has cookbook recipes.*


## Find out which file is being processed by map

From [StackOverflow](http://stackoverflow.com/a/19012715).

{% highlight java %}
public void map(Object key, Text value, Context context)
    throws IOException, InterruptedException
{
    // full path:
    String filePathString = ((FileSplit) context.getInputSplit()).getPath().toString();

    // just file name:
    String fileName = ((FileSplit) context.getInputSplit()).getPath().getName();

    // ...
}
{% endhighlight %}
