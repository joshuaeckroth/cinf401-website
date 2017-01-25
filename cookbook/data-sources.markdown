---
layout: post
title: "Cookbook: Data sources"
---

# Cookbook: Data sources

## Other lists

There are quite a number of "lists of data sources." Here are some:

- [A list of data sources](https://github.com/rasbt/pattern_classification/blob/master/resources/dataset_collections.md) as a Github repository.
- [Kaggle](http://www.kaggle.com/competitions) has several competitions with corresponding datasets.

## US Census

Go to [Data Tools and Apps](http://www.census.gov/data/data-tools.html) at census.gov and look at the top: [The American FactFinder](http://factfinder2.census.gov/). Click "Download Center" and then the button with the same text. Follow the directions. You'll ultimately download a ZIP with some CSV files.

## Weather

### National Climactic Data Center

- [Storm events](http://www.ncdc.noaa.gov/stormevents/) -- Use the "Select State" or search feature on the bottom of this page, then choose your dates and weather types. After these selections, you'll be given a table. On the top-left of that table is a CSV download link.

### Weather Underground

- [Historical Weather](http://www.wunderground.com/history/) -- Choose a location and date (on the next page, you can choose a range of dates). On the next page, you see some weather stats. You click the top tab "Custom" to give a range of dates (range can be specified in drop-downs above the tabs); click "Get History" button to update. At the bottom of this page, there is a link for the CSV file. **[Note: As of Feb 3, 2015, this site is broken.]**

## Biology, wildlife

- Pokemon stats: [pokemon.csv](https://github.com/veekun/pokedex/blob/master/pokedex/data/csv/pokemon.csv) (Contributed by Jacob Hell.)

## Health

[TODO; any suggestions?]

## Sports

### NFL

- [nfldb](https://github.com/BurntSushi/nfldb), a Python library that downloads NFL data and saves to a CSV file. Found by George Robbins.

### MLB

Contributed by Marisa Gomez.

Major league baseball data can be found in the `pitchRx` R package.

{% highlight r %}
library(pitchRx)
head(pitches)
animateFX(pitches, layer=list(facet_grid(pitcher_name ~ stand, labeller=label_both), theme_bw(), coord_equal()))
strikes <- subset(pitches, des == "Called Strike")
strikeFX(strikes, geom="tile", layer=facet_grid(.~stand))
{% endhighlight %}

## Education

### IPEDS Data Center

Visit the [IPEDS Data Center](http://nces.ed.gov/ipeds/datacenter/Default.aspx). IPEDS provides data about US universities.

- Choose "Compare Institutions"
- Choose "Final release data" if asked
- Select institutions "By Groups > EZ Group". Choose a group, e.g., US Only. Click "Search".
- Next, select variables with the "By Variables" link above the table. Choose "Browse / Search Variables".
- Drill down to find variables. Be sure to select each year, and select your variables for that year. Then click "Continue".
- When ready, click "Output" at the top of the menu bar. You should see a table of CSV links.
- If asked "Include imputation variables?" at the top of the output table, check "No" (i.e., do not replace missing data with most likely value based on rest of data)

## Financial

### IRS

- [IRS Tax Stats](http://www.irs.gov/uac/Tax-Stats-2) -- Choose the type of stats you're interested in; this may require several pages of drill-down. Eventually, you should come to a page of Excel file links. You can download a few you want, or download them all as we did in the [Student Loans demo](/notes/demo-student-loans.html).

### Lending Club loans

Lending Club is a "marketplace" where regular people offer money to lend other regular people. Lending Club has produced a [set of data](https://www.lendingclub.com/info/download-data.action) for loans made and loans requests rejected.

Later, we'll (possibly) use this data in Hadoop. It is accessible on HDFS under: [`/datasets/lendingclub`](http://localhost:50070/...)

If you want to download all the data, use these commands on delenn:

```
wget --no-check-certificate https://resources.lendingclub.com/LoanStats3a.csv.zip
wget --no-check-certificate https://resources.lendingclub.com/LoanStats3b.csv.zip
wget --no-check-certificate https://resources.lendingclub.com/LoanStats3c.csv.zip
wget --no-check-certificate https://resources.lendingclub.com/RejectStatsA.csv.zip
wget --no-check-certificate https://resources.lendingclub.com/RejectStatsB.csv.zip
wget --no-check-certificate https://resources.lendingclub.com/LCDataDictionary.xlsx
```

## Internet census

In 2012, a group scanned the whole internet and produced a census. More info is available in their [report](http://internetcensus2012.bitbucket.org/paper.html), and this [email message](http://seclists.org/fulldisclosure/2013/Mar/166).

It can be downloaded [here](http://internetcensus2012.github.io/InternetCensus2012/download.html). It's quite large (568GB), and uncompressed is 9TB. You may want to user their [data browser](http://internetcensus2012.github.io/InternetCensus2012/hilbert.html) to get an idea what's there.

## Google Cluster

Google has released data about their cluster machines. They have [some questions](http://googleresearch.blogspot.ch/2010/01/google-cluster-data.html) they'd like researchers to answer.

More information may be found [here](https://code.google.com/p/googleclusterdata/). The data is downloaded using Google's `gsutil` tool, as described in [this document](https://docs.google.com/file/d/0B5g07T_gRDg9Z0lsSTEtTWtpOW8/edit).

We might look at these data when we learn about Hadoop.

## GitHub Archive

GitHub provides a continuous stream of events from its millions of hosted projects. Look at their [overview page](https://www.githubarchive.org/) for details.

## Million Songs

The [Million Song dataset](http://labrosa.ee.columbia.edu/millionsong/) contains detailed information about songs. It comes in HDF5 format, which is difficult for us to process with Hadoop.

Kaggle hosted a [competition](http://www.kaggle.com/c/msdchallenge) using these data. You were asked to predict which songs a user would enjoy, based on their ratings of other songs.

## Stack Exchange

Get a dump of the full Stack Exchange site with [this torrent](https://archive.org/details/stackexchange). You can also explore the data with their [online query system](http://data.stackexchange.com/).

## Text

Text data requires special processing. It's not trivial to compare two paragraphs or books of text. You must transform the text first, typically into numeric form. One of doing this is the "bag of words" approach, where you convert each document into a vector of word counts. The vector at position 1 might correspond to the word "the", and the count associated would be the count of times "the" appeared in the document. The second word might be "furlough", etc. These vectors would be very high-dimensional, like 10,000 dimensions to represent the various words encountered. To compare multiple documents against each other, they would all use the same vector--word correspondence. If some document does not have the word "furlough", it would just put a 0 in its vector in that dimension.

### Enron emails

When Enron broke up, the US government released all their emails. They are available [here](https://www.cs.cmu.edu/~./enron/). There are about 500k messages from about 150 people, mostly senior management.

### SMS Spam

The [UCI Machine Learning Repository](https://archive.ics.uci.edu/ml/index.html) has many datasets. One contains [SMS Spam and Non-Spam](https://archive.ics.uci.edu/ml/datasets/SMS+Spam+Collection) ("Ham"). SMS is txt messages. A good use of this dataset is to train machine learning models to recognize SMS spam.

### Westbury Lab Usenet

Westbury Lab has released a [dump of Usenet data](http://www.psych.ualberta.ca/~westburylab/downloads/usenetcorpus.download.html) (Usenet is like web forums; it began before there was the web). It's about 37GB uncompressed.

We might look at it when we learn about Hadoop. It's stored in HDFS at [`/datasets/westburylab-usenet`](http://localhost:50070/explorer.html#/datasets/westburylab-usenet).

### Project Gutenberg ebooks

[Project Gutenberg](http://www.gutenberg.org) is a huge collection of scanned out-of-copyright books. You can download all of their books by following [their instructions](http://www.gutenberg.org/wiki/Gutenberg:Information_About_Robot_Access_to_our_Pages). This is the process:

Download the book metadata in RDF format:

```
wget http://www.gutenberg.org/cache/epub/feeds/rdf-files.tar.bz2
```

Then download all the books (English, TXT format only):

```
wget -w 2 -m -H "http://www.gutenberg.org/robot/harvest?filetypes[]=txt&langs[]=en"
```

That command waits 2 seconds between each request. This is recommended by Project Gutenberg. With 45k+ books, the crawl takes some time.

### Patents

Google has released all their [US patent data](http://www.google.com/googlebooks/uspto-patents.html), which makes up their [Google Patent search](https://www.google.com/?tbm=pts&gws_rd=ssl).

### Trademarks

Google has also released [US trademark data](http://www.google.com/googlebooks/uspto-trademarks.html).

