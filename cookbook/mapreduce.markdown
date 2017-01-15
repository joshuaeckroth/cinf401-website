---
layout: post
title: "Cookbook: MapReduce"
---

# Cookbook: MapReduce

*Note: See the corresponding [lecture notes about MapReduce](/notes/mapreduce.html). This page has cookbook recipes.*

## Kill a job

On delenn, first list the active jobs:

```
mapred job -list
```

Find yours, then kill it:

```
mapred job -kill <job-id>
```


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

## Map over files recursively

Want to process all files in a directory and subdirectories? Use this technique:

{% highlight java %}
// in main()

FileInputFormat.setInputDirRecursive(job, true);
{% endhighlight %}

## Only map over files that match a certain regex

Create a class that implements `PathFilter`. The class below can be configured to use any regular expression:

{% highlight java %}
// from: https://hadoopi.wordpress.com/2013/07/29/hadoop-filter-input-files-used-for-mapreduce/
public static class RegexPathFilter extends Configured implements PathFilter {

    Pattern pattern;
    Configuration conf;
    FileSystem fs;

    @Override
    public boolean accept(Path path) {
        try {
            if (fs.isDirectory(path)) {
                return true;
            } else {
                Matcher m = pattern.matcher(path.toString());
                System.out.println("Is path: " + path.toString() + " matches "
                        + conf.get("file.pattern") + " ? , " + m.matches());
                return m.matches();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void setConf(Configuration conf) {
        this.conf = conf;
        if (conf != null) {
            try {
                fs = FileSystem.get(conf);
                if(conf.get("file.pattern") == null) {
                    conf.set("file.pattern", ".*");
                }
                pattern = Pattern.compile(conf.get("file.pattern"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
{% endhighlight %}

Use it like so:

{% highlight java %}
// in main()
Configuration conf = new Configuration();
conf.set("file.pattern", ".*(Users\\.xml|postsanswers\\.txt)");
Job job = Job.getInstance(conf, "users reputation");

// ...
FileInputFormat.setInputPathFilter(job, RegexPathFilter.class);
{% endhighlight %}

It might be good to combine this technique with the 'recursive' technique above.
