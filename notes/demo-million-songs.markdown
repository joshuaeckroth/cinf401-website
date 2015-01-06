---
layout: post
title: "Demo: Million songs"
---

# Demo: Million songs

## Tools used

- Bash, Perl, csvkit (See: [Unix tools cookbook](/cookbook/unix-tools.html)
- R (See: [R notes](/notes/r.html) and [R cookbook](/cookbook/r.html)

## Dataset

- http://labrosa.ee.columbia.edu/millionsong/pages/getting-dataset

```
$ h5stat data/A/A/A/TRAAAAW128F429D538.h5                                                  [58/9363]
Filename: data/A/A/A/TRAAAAW128F429D538.h5
File information
        # of unique groups: 4
        # of unique datasets: 24
        # of unique named datatypes: 0
        # of unique links: 0
        # of unique other: 0
        Max. # of links to object: 1
        Max. # of objects in group: 16
File space information for file metadata (in bytes):
        Superblock extension: 0
        User block: 0
        Object headers: (total/unused)
                Groups: 1208/0
                Datasets(exclude compact data): 21224/64
                Datatypes: 0/0
        Groups:
                B-tree/List: 4144
                Heap: 832
        Attributes:
                B-tree/List: 0
                Heap: 0
        Chunked datasets:
                Index: 47152
        Datasets:
                Heap: 0
        Shared Messages:
                Header: 0
                B-tree/List: 0
                Heap: 0
Small groups:
        # of groups of size 3: 2
        # of groups of size 5: 1
        Total # of small groups: 3
Group bins:
        # of groups of size 1 - 9: 3
        # of groups of size 10 - 99: 1
        Total # of groups: 4
Dataset dimension information:
        Max. rank of datasets: 2
        Dataset ranks:
                # of dataset with rank 1: 22
                # of dataset with rank 2: 2
1-D Dataset information:
        Max. dimension size of 1-D datasets: 1
        Small 1-D datasets:
                # of dataset dimensions of size 0: 2
                # of dataset dimensions of size 1: 3
                Total small datasets: 5
        1-D Dataset dimension bins:
                # of datasets of size 0: 2
                # of datasets of size 1 - 9: 3
                # of datasets of size 10 - 99: 7
                # of datasets of size 100 - 999: 10
                Total # of datasets: 22
Dataset storage information:
        Total raw data size: 209558
        Total external raw data size: 0
Dataset layout information:
        Dataset layout counts[COMPACT]: 0
        Dataset layout counts[CONTIG]: 0
        Dataset layout counts[CHUNKED]: 24
        Number of external files : 0
Dataset filters information:
        Number of datasets with:
                NO filter: 0
                GZIP filter: 24
                SHUFFLE filter: 24
                FLETCHER32 filter: 0
                SZIP filter: 0
                NBIT filter: 0
                SCALEOFFSET filter: 0
                USER-DEFINED filter: 0
Dataset datatype information:
        # of unique datatypes used by datasets: 7
        Dataset datatype #0:
                Count (total/named) = (17/0)
                Size (desc./elmt) = (22/8)
        Dataset datatype #1:
                Count (total/named) = (1/0)
                Size (desc./elmt) = (2070/220)
        Dataset datatype #2:
                Count (total/named) = (2/0)
                Size (desc./elmt) = (10/256)
        Dataset datatype #3:
                Count (total/named) = (1/0)
                Size (desc./elmt) = (10/20)
        Dataset datatype #4:
                Count (total/named) = (1/0)
                Size (desc./elmt) = (1254/5320)
        Dataset datatype #5:
                Count (total/named) = (1/0)
                Size (desc./elmt) = (14/4)
        Dataset datatype #6:
                Count (total/named) = (1/0)
                Size (desc./elmt) = (130/8)
        Total dataset datatype count: 24
Small # of attributes:
        # of objects with 4 attributes: 24
        # of objects with 5 attributes: 1
        # of objects with 8 attributes: 1
        Total # of objects with small # of attributes: 26
Attribute bins:
        # of objects with 1 - 9 attributes: 26
        # of objects with 10 - 99 attributes: 2
        Total # of objects with attributes: 28
        Max. # of attributes to objects: 66
```