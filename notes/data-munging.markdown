---
layout: post
title: Data munging
---

# Data munging

> **munge**, v. Obs. rare.<br/>&nbsp;&nbsp;&nbsp;&nbsp;To wipe (a person's nose).<br/><br/>&mdash; Oxford English Dictionary

Though the term "munging" may be somewhat obscure, it's commonly used in data science to mean converting and formatting data into a reasonable form for analysis.

All too often, humans create datasets (often in Excel or, the horror, PDF or Word files), and humans format the datasets with various tables and colors and headers, etc. etc. that just get in the way when processed by machine. We'll need to remove these flourishes, and often remove entire columns or rows of data, in order to get to the raw values.

## Obtaining data

Before munging data, you'll need to obtain them. How those data are made available, and in what form(s), dictates how painful the munging experience will be.

A best-case scenario is the data is offered in CSV format with a simple download or torrent link. The worst-case scenario is the data is only available on various web pages, PDF files, or (absolute worst case) scanned forms. The [data sources cookbook](/cookbook/data-sources.html) lists some techniques for obtaining bulk data from various sources.

## Data cleaning

Data cleaning differs somewhat from data munging in that "cleaning" means to fix invalid values, add missing values, and so on. Data cleaning is not closely related to the structure of the data but rather refers to the validity or accuracy of the data.

Data cleaning is often necessary when working with human-entered data and joining datasets from disparate sources. Often, different groups do not use the same conventions, so one must look through all of the data very carefully and fix any discrepancies.

## Advice

- Keep only raw data, not summary statistics.

    - Some of the data you'll find will include summary statistics, such as "year to date" totals. You can compute these yourself with some code, as long as you have the raw data. The summary data just gets in the way, so remove them before you start mining.

## Common data formats

For reference, here are the most common file formats for datasets.

Comments from a "big data analytics" perspective:

- We need to be able to split large datasets. [Hadoop](/notes/hadoop.html) splits big data into smaller data to distribute the task. The easy way to split a dataset is to take break it apart by lines. Fancier splitting may require custom code.

- Generally, we (as big data analysts) prefer tabular data, as would be found in Excel spreadsheets or CSV files. The latter, CSV files, are best because there is no possibility for silly data formatting like fonts, multi-column/multi-row cells, cells calculated from formulas, etc.

 - We, generally, dislike hierarchical data because it's difficult to query and difficult to split into subsets. JSON, HDF5, and XML are examples of hierarchical data formats.

 - Finally, we truly despise data that come in presentation formats like PDF or Microsoft Word docs. Just the data please!

### Excel (XLS, XLSX)

### Comma-separated values (CSV)

The gold-standard for big data acolytes.

Here is an example of the beginning of a CSV file from the LendingClub data (see [data sources cookbook](/cookbook/data-sources.html)).

```
Amount Requested,Application Date,Loan Title,Risk_Score,Debt-To-Income Ratio,Zip Code,State,Employment Length,Policy Code
"1000.00","2007-05-26","Wedding Covered but No Honeymoon","693","10%","481xx","NM","4 years","0"
"1000.00","2007-05-26","Consolidating Debt","703","10%","010xx","MA","< 1 year","0"
"11000.00","2007-05-27","Want to consolidate my debt","715","10%","212xx","MD","1 year","0"
"6000.00","2007-05-27","waksman","698","38.64%","017xx","MA","< 1 year","0"
"1500.00","2007-05-27","mdrigo","509","9.43%","209xx","MD","< 1 year","0"
"15000.00","2007-05-27","Trinfiniti","645","0%","105xx","NY","3 years","0"
```

### Tab-separated values (TSV)

Just like CSV (above), but with tabs instead of commas.

### Javascript Object Notation (JSON)

JSON is a file format that resembles Javascript code. It is very flexible and supports arrays, dictionaries (hash maps), numbers, strings, etc. Arrays and dictionaries can be nested. Here is an example of what a row/column dataset might look like.

```json
[
	{
		color: "red",
		value: "#f00"
	},
	{
		color: "green",
		value: "#0f0"
	},
	{
		color: "blue",
		value: "#00f"
	},
]
```

A dataset like the example above is easy to work with because its just tabular (i.e., spreadsheet, i.e., row/column) data in another form. but JSON supports hierarchical structures, arbitrarily complex, which can really complicate data munging.

More info: [json.org](http://www.json.org/). JSON is supported by nearly every programming language; just do a search to find the right libraries.

### Hierarchical Data Format (HDF5)

Use with Hadoop: [Everything that HDF Users have Always Wanted to Know about Hadoop... But Were Ashamed to Ask (PDF)](http://www.hdfgroup.org/pubs/papers/Big_HDF_FAQs.pdf)

### PDF, Microsoft Word

In my experience, datasets in PDF or Word format are a disaster. These are (generally) not tabular document formats, so even if the data appear to be in tables, they may not copy as tabular data. Nevertheless, your best technique in these cases is to copy-paste the data into Excel or a text file and clean up from there.

Microsoft Word docs may include embedded tables, which copy cleanly to Excel. Should we be so lucky...

### HTML

Data in HTML can be as messy as those found in PDFs or MS Word documents. However, HTML has true tables (`<table>` tags) so in some cases the data will copy cleanly into Excel. From Excel, you'll probably want to save as a CSV format.

### XML

XML is a hierarchical data format.
