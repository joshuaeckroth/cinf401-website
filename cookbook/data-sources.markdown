---
layout: post
title: "Cookbook: Data sources"
---

# Cookbook: Data sources

- [A list of data sources](https://github.com/rasbt/pattern_classification/blob/master/resources/dataset_collections.md)

## US Census

## World stats

## Weather

### National Climactic Data Center

- [Storm events](http://www.ncdc.noaa.gov/stormevents/)

## Biology, wildlife

## Health

## Education

### IPEDS Data Center

- http://nces.ed.gov/ipeds/datacenter/Default.aspx
- Download custom data files
- Select institutions by groups (e.g., US Institutions)
- Continue to select variables
- Be sure to select each year, and choose your variables again (for that year)
- Include imputation variables? No (i.e., do not replace missing data with most likely value based on rest of data)
- Download as a single file (CSV)

## Financial

- [IRS Tax Stats](http://www.irs.gov/uac/Tax-Stats-2)

### Lending Club loans

- https://www.lendingclub.com/info/download-data.action

HDFS location: [`/datasets/lendingclub`](http://localhost:50070/...)

```
wget --no-check-certificate https://resources.lendingclub.com/LoanStats3a.csv.zip
wget --no-check-certificate https://resources.lendingclub.com/LoanStats3b.csv.zip
wget --no-check-certificate https://resources.lendingclub.com/LoanStats3c.csv.zip
wget --no-check-certificate https://resources.lendingclub.com/RejectStatsA.csv.zip
wget --no-check-certificate https://resources.lendingclub.com/RejectStatsB.csv.zip
wget --no-check-certificate https://resources.lendingclub.com/LCDataDictionary.xlsx
```

## Internet census

- http://internetcensus2012.bitbucket.org/paper.html

- http://seclists.org/fulldisclosure/2013/Mar/166

- http://internetcensus2012.github.io/InternetCensus2012/download.html

## Computer usage

### Google Cluster

- https://code.google.com/p/googleclusterdata/

- http://googleresearch.blogspot.ch/2010/01/google-cluster-data.html

## Recommendation challenges

### Million Songs

- http://labrosa.ee.columbia.edu/millionsong/

- http://www.kaggle.com/c/msdchallenge

## Email, SMS

### Enron emails

### SMS Spam



## Other text

### Westbury Lab Usenet

- [Directory on Namenode](http://localhost:50070/explorer.html#/datasets/westburylab-usenet)

### Project Gutenberg ebooks

- http://www.gutenberg.org/wiki/Gutenberg:Information_About_Robot_Access_to_our_Pages

Metadata (RDF):

```
wget http://www.gutenberg.org/cache/epub/feeds/rdf-files.tar.bz2
```

Books (English, TXT only):

```
wget -w 2 -m -H "http://www.gutenberg.org/robot/harvest?filetypes[]=txt&langs[]=en"
```

That command waits 2 seconds between each request. This is recommended by Project Gutenberg. With 45k+ books, the crawl takes some time.

### Patents

- http://www.google.com/googlebooks/uspto-patents.html

### Trademarks

- http://www.google.com/googlebooks/uspto-trademarks.html

