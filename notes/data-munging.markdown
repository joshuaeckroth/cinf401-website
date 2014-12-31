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

### Excel (XLS, XLSX)

### Comma-separated values (CSV)

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

More info: [json.org](http://www.json.org/). JSON is supported by nearly every programming language; just do a search to find the right libraries.

### Hierarchical Data Format (HDF5)

### PDF

### HTML

### XML



