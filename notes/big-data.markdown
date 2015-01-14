---
layout: post
title: Big data
---

# Big data

This class is nominally about big data. It is worthwhile to figure out what we're talking about.

![Big Data](/images/big-data-logo.jpg)

<div style="text-align: center">More fascinating visualizations at <a href="http://bigdatapix.tumblr.com/">Pictures of Big Data</a>.</div>

## What? Why?

Data is everywhere. Like Neo in the Matrix, once you start looking for public datasets or compiling your own, you begin to realize that everything has a data stream. Website interlinks, traffic sensors, thermostats, governmental forms, click habits, phone calls, geo "check ins", hospital records, purchase histories, etc. etc. Data can be found everywhere, and some of it is huge.

This class is about two things (besides the quote at the top of the [syllabus](/notes/syllabus.html)): what can we do with data, and what techniques are unique to handling really big data.

In class, we'll discuss:

- What kinds of questions do we ask about data? (My observation: The sophistication of the question (usually) decreases as the data bigness increases; we're only human.)

- How do we make data most useful? (Hint: clean it, transform it, combine it.)

- What tools can we use to answer questions about data? (Answer: all of them!)

## What's big? First: The memory hierarchy

In order to make sense of why "big data" is a problem, requiring unique solutions, we must first understand the memory hierarchy.

![Memory hierarchy](/images/memory-hierarchy.png)

Image found at [technoducation.wordpress.com](http://technoducation.wordpress.com/).

Check out this [interactive visualization](http://www.eecs.berkeley.edu/~rcs/research/interactive_latency.html) of latency for various types of memory/disk/network access. It shows the staggering orders-of-magnitude differences between processor cache and RAM access vs. disk access vs. network access.

When you run out of memory at some level (say, RAM, or disk space), you have a few options for how to proceed:

1. Increase storage capacity or efficiency for some or all levels in the hierarchy. I.e., upgrade to a mainframe.
2. Create another level in the hierarchy (either/or: a faster, smaller memory above the current level; or slower, larger memory below the current level, so the current level acts as a cache for this new, slower level).
3. **Distribute the workload and storage to more machines,** each with their own memory hierarchy. You might call this "horizontal" expansion, whereas adding more memory or making it faster would be "vertical" expansion.

Depending on your circumstances, one of these options may give more bang for the buck than the others. For future reference, Hadoop is designed for the third option. Most references to "big data" today refer to the third option.

## A definition of "big data"

Big data is somewhat a fad, but also very important when the data is actually big. When the data is actually small, big data techniques should not be used (they are too slow). So what makes data "big"?

First, some auxiliary definitions:

- **Data volume**: The number of bits. Examples: All Wal-Mart purchases in the last year (they have 100 million customers per week; [source](http://www.businesspundit.com/stats-on-walmart/)). Or, performance metrics, in 1-second intervals, from every one of Google's datacenter machines (3+ million? [ref](https://plus.google.com/+JamesPearn/posts/VaQu9sNxJuY)).

- **Data velocity**: The speed at which bits need to be processed. Examples: All Tweets, in real-time (6k/sec, [ref](http://www.internetlivestats.com/twitter-statistics/)). Or, all Google searches (40k/sec, [ref](http://www.internetlivestats.com/google-search-statistics/)). Or, the stream of data from a particle accelerator (for the Large Hadron Collider, that's 300GB/s, and after filtering to 300MB/s, they still accumulate 25PB/year, [ref 1](http://en.wikipedia.org/wiki/Large_Hadron_Collider#Computing_resources), [ref 2](http://en.wikipedia.org/wiki/Worldwide_LHC_Computing_Grid)).


Now, here is my definition of "big data":

> A data mining/analysis task may be described as "big data" if the data to be processed has such high volume or velocity that **more than one** commodity machine is required to store and/or process the data.

I define a "commodity" data storage system and "commodity" machine as those that may be found in a typical small-to-medium business's data center. Current (2015) examples include:

Commodity data storage system:

- 20-1000 TB disk array, network addressable over 1 gigabit ethernet

Commodity machine:

- 2-8 core CPU, 2-3 GHz
- 16-32 GB RAM
- 2-16 TB disks
- 1 gigabit ethernet

*Note for future reference: Hadoop is designed to work with a collection of commodity machines, each with their own (internal) disks. It is not designed to work with network addressable storage, and performs poorly in those situations.*

## Case studies

![big data](/images/big-data-comic.jpg)

Comic by [Tom Fishburne](http://tomfishburne.com/2014/01/big-data.html).

### Ancestry.com

> According to the Chief Technology Officer, Scott Sorensen, Ancestry.com has more than 12 billion records that are part of a 10-petabyte data store. If you’re searching for "John Smith," he explained, it will likely yield results for about 80 million "Smith" results and about 4 million results for "John Smith," but you’re only interested in the handful that are relevant to your John Smith.
>
> They rely on big data stores to develop new statistical approaches to algorithmic development, such as record linking and search relevance algorithms. Today, the vast amount of user discoveries are determined by Ancestry.com hints derived from strategically linked records and past search behavior (e.g., Charles 'Westman' is the same person as Charles 'Westmont'). Two years ago, the majority of discoveries were based on user-initiated search.
>
> DNA genotyping to provide information about genetic genealogy is a new area of focus. Customers spit in a tube, send the package to Ancestry.com, and then molecular tests and computational analyses are performed to predict a person's ethnicity and identify relatives in the database. For every AncestryDNA customer, 700,000 SNPs (distinct variable regions in your DNA) are measured and analyzed, resulting in 10 million cousin predictions for users to-date.
>
> A portion of Ancestry.com’s data is processed on three clusters using MapR as the Hadoop distribution. One cluster is for DNA matching; another is for machine learning and the third, which is just being built-up, is for data mining.

From [MapR](https://www.mapr.com/resources/customer-case-studies), 2014.

### (Secret) electronics manufacturer

> The manufacturing company has an elaborate quality control mechanism and receives billions of readouts from factory sensors designed to detect failures.
>
> In order to achieve their product quality goals, the customer needed to correlate huge and disparate data sources. They needed a system capable of handling hundreds of terabytes of small files (sensor readouts and chip images) and analyzing hardware yields using image pattern matching and clustering techniques.

From [MapR](https://www.mapr.com/resources/customer-case-studies), 2014.

### Scientific data processing

> [W]e develop a Hadoop-based cloud computing application that processes sequences of microscope images of live cells.
>
> Our goal is to study the complex molecular interactions that regulate biological systems. To achieve this we are developing an imaging platform to acquire and analyze live cell data at single cell resolution from populations of cells studied under different experimental conditions. The key feature of our acquisition system is its capability to record data in high throughput, both in the number of images that can be captured for a single experimental condition and the number of different experimental conditions that can be studied simultaneously.
>
> The acquisition system has a data rate of 1.5 MBps, and a typical 48 hour experiment can generate more than 260 GB of images. Newer systems that we are currently evaluating can produce ten times more data in the same time.
>
> The data analysis task for this platform is daunting: thousands of cells in the videos need to be tracked and characterized individually. The output consists of precise motion, morphological and gene expression data of each cell at many different timepoints. [...] While image analysis is the current bottleneck in our data processing pipeline, it happens to be a good candidate step for parallelization. The data processing can be broken up into hundreds of independent video analysis tasks.
>
> To date we have used a local eight core server for data processing. A 600 video dataset takes up to 12h to process. This is the time required for one analysis of one experiment. Once the system will be fully operational, we will be acquiring large amounts of data (hundreds to thousands of GB per 48 hour experiment). We thus consider the development of a scalable and reliable cloud computing system for processing the data generated by our experiments of critical importance for our project.
>
> [T]he use of Hadoop allows to speed up calculations by a factor that equals the number of worker nodes, except for startup effects, which are relatively small when the execution time of individual tasks is large enough.

From: Chen Zhang, Hans De Sterck, Ashraf Aboulnaga, Haig Djambazian, and Rob Sladek. "Case study of scientific data processing on a cloud using Hadoop." In High performance computing systems and applications, pp. 400-415. Springer Berlin Heidelberg, 2010. [PDF](http://202.154.59.182/mfile/files/Information%20System/High%20Performance%20Computing%20Systems%20and%20Applications%3B%2023rd%20HPCS%202009/Chapter%2029%20Case%20Study%20of%20Scientific%20Data%20Processing%20on%20a%20Cloud%20Using%20Hadoop.pdf).

## More big data

<div style="text-align: center">
<iframe width="100%" height="166" scrolling="no" frameborder="no" src="https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/tracks/105081265&amp;color=ff5500&amp;auto_play=false&amp;hide_related=true&amp;show_comments=false&amp;show_user=false&amp;show_reposts=false"></iframe>
</div>