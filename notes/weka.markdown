---
layout: post
title: Weka
---

# Weka

Download from the [Weka website](http://www.cs.waikato.ac.nz/ml/weka/), click "Download" on the left, and select one of the "Stable book 3rd ed." versions for your machine.

You may need to increase the memory available to Weka. On Windows,
check out [Weka's page on the topic](http://weka.wikispaces.com/Java+Virtual+Machine). On a Mac, use this command in the
Terminal:

```
java -Xmx4096m -jar /Applications/weka-3-7-12-apple-jvm.app/Contents/Resources/Java/weka.jar
```

## k-means clustering

Click "Choose" to choose the clustering algorithm.

![Cluster 1](/images/weka-cluster-1.png)

Choose "SimpleKMeans."

![Cluster 2](/images/weka-cluster-2.png)

Click the name of the algorithm to get more options.

![Cluster 3](/images/weka-cluster-3.png)

Possibly change the number of clusters or distance function.

![Cluster 4](/images/weka-cluster-4.png)

For evaluation (including the confusion matrix), choose Classes to clusters evaluation and make sure "(Nom) class" (nominal variable called "class") is selected for comparison.

![Cluster 5](/images/weka-cluster-5.png)


## k-nearest neighbor classification

Click "Choose" to choose the classification algorithm.

![Classify 1](/images/weka-classify-1.png)

Choose "IBk."

![Classify 2](/images/weka-classify-2.png)

Click the name of the algorithm to get more options.

![Classify 3](/images/weka-classify-3.png)

Possibly change the number of clusters.

![Classify 4](/images/weka-classify-4.png)

For evaluation (including the confusion matrix), choose "Cross-validation" with 10 folds.

![Classify 5](/images/weka-classify-5.png)

## Preprocessing text

As discussed in the [text analysis notes](/notes/text-analysis.html), text files typically
need to be converted into "feature vectors" before machine learning
algorithms can be applied. The most common feature vector is a vector
where each dimension is a different word, and the value in that
dimension is either 0/1 binary value (yes or no the word is in that
document), an integer count (the frequency of the word in that
document), or a real value (often the tf-idf score of that word in
that document).

The ARFF files we will be working with will always look like this:

```
@relation some-description-of-the-data
@attribute contents string
@attribute class {ham,spam}

@data
'Go until jurong point, crazy.. Available only in bugis n great world la e buffet...',ham
'Ok lar... Joking wif u oni...',ham
'Free entry in 2 a wkly comp to win FA Cup ... Text FA to 87121 to receive entry ...',spam
'U dun say so early hor... U c already then say...',ham
'Nah I dont think he goes to usf, he lives around here though',ham
...
```

Of course, the classes (ham/spam) may change. These files are very
easy to generate, should you actually wish to use text classification
for your own purposes.

We'll practice with the [sms-spam.arff](/sms-spam.arff) file, which has examples of SMS
text msg ham and spam. This collection was produced by Tiago
A. Almeida from the Federal University of Sao Carlos and José María
Gómez Hidalgo from the R&D Department of Optenet. More information
[available here](http://www.dt.fee.unicamp.br/~tiago/smsspamcollection/). You may find [their paper](http://dl.acm.org/citation.cfm?id=2034742) interesting; it examines the
performance of various machine learning algorithms on this data set.

### Strings to binary feature vectors

Load the ARFF file and choose a filter.

![String preprocess 1](/images/weka-preprocess-1.png)

Choose the StringToWordVector filter, in the Unsupervised > Attribute section.

![String preprocess 2](/images/weka-preprocess-2.png)

Click the filter name to get more options.

![String preprocess 3](/images/weka-preprocess-3.png)

Change "attributeIndices" to "1" to just select the first attribute (the contents of the SMS message). Change "attributeNamePrefix" to "word-" so that all the new features look like "word-foo" and "word-bar", etc. Change "wordsToKeep" to "100000" to keep them all.

![String preprocess 4](/images/weka-preprocess-4.png)

Click "Apply"

![String preprocess 5](/images/weka-preprocess-5.png)

Now you'll have lots more attributes. You can click "Undo" if you don't like it.

![String preprocess 6](/images/weka-preprocess-6.png)

Everything works better if the class (ham/spam) is last. After our filter, it's not anymore. Let's fix that. Choose a new filter, "Reorder," under Unsupervised > Attribute. Click the filter name to change its properties. Type "2-last,1" to make the new order the second-to-last attributes followed by the first attribute. This puts the first attribute at the end.

![String preprocess 7](/images/weka-preprocess-7.png)

Click "Apply." Now the class (ham/spam) should be the last attribute again.

![String preprocess 8](/images/weka-preprocess-8.png)

### Strings to frequency vectors

Follow the steps above ("Strings to binary feature vectors") except
use the following changes in the StringToWordVector filter:

Change "outputWordCounts" to "True."

![String preprocess 9](/images/weka-preprocess-9.png)

### Strings to tf-idf vectors

Follow the steps above ("Strings to binary feature vectors") except
use the following changes in the StringToWordVector filter:

Change "IDFTransform" to "True" and "TFTransform" to "True," as well as "normalizeDocLength" to "Normalize all data."

![String preprocess 10](/images/weka-preprocess-10.png)

