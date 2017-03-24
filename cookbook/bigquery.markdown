---
layout: post
title: "Cookbook: BigQuery"
---

# Cookbook: BigQuery

Start here: [BigQuery Console](https://bigquery.cloud.google.com/)

## SQL syntax

- See: [CINF 201 notes](http://cinf201.artifice.cc)
- See: [BigQuery docs](https://cloud.google.com/bigquery/docs/)

## Extracting hour, minute, etc. from dates

```
extract(HOUR FROM myfield)
extract(MINUTE FROM myfield)
extract(DAYOFWEEK FROM myfield)
```
