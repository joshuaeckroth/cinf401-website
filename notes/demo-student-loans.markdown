---
layout: post
title: "Demo: Student loans"
---

# Demo: Student loans

## Tools used

- Bash
- Perl
- [csvkit](http://csvkit.readthedocs.org/en/0.9.0/)


## Find the data

FAFSA by Postsecondary School (several years’ and quarters’ worth)

- https://studentaid.ed.gov/about/data-center/student/application-volume/fafsa-school-state

Start by downloading the reports' documentation:

- https://studentaid.ed.gov/sites/default/files/fsawg/datacenter/library/FAFSAReportDefinitions.doc

Next, download the web page so we can extract the links programmatically:

```
wget https://studentaid.ed.gov/about/data-center/student/application-volume/fafsa-school-state
```

Notice the page source (the HTML) has links in the `<option>` tags. E.g.,

```
<option value="/sites/default/files/fsawg/datacenter/library/2012_13App_Data_by_State_Q4.xls">2012-2013, Q4</option>
```

Download it all:

```perl
#!/usr/bin/perl

# script name: get-links.pl

while(<>) {
    $line = $_;
    while($line =~ m!(/sites/.*?\.xls)!g) {
        print $1."\n";
    }
}
```

```bash
for url in `perl get-links.pl < fafsa-school-state.html`; do wget https://studentaid.ed.gov/$url; don
```

I don't want FAFSA data by state, only by school. So I'll delete the "State" files:

```bash
rm *State*
```

## Assess the data

Open a few of these downloaded XLS files to see what we're working with.

![FAFSA XLS](/images/fafsa-xls.png)

We see some special formatting (colors, multi-column cells, etc.) but, luckily, the valuable data seem to be in consistent table format.

Next, take a look at `FAFSAReportDefinitions.doc`. After closely reading the documentation and studying the spreadsheets, my assessment of these data is as follows:

- The spreadsheets are broken up by quarter (1-6; students may submit applications for 18-months, hence six "quarters") and year.

- The row differentiator (what makes each row distinct) is the school/state/zip. A school name may appear multiple times if it exists in several states/zips.

- Students are differentiated according to whether they are "dependent" or "independent." For financial aid purposes, a dependent student reports their parents' income, while an independent student does not. There is a complicated definition, but basically, dependent students are those 23 years old or younger. See: [Dependency Status](https://studentaid.ed.gov/fafsa/filling-out/dependency) from the FAFSA website.

- For each row (school), we have the number of dependent applications (received by the school), and independent applications. The first two columns show the number received in that quarter, the latter two columns show year-to-date. Thus, Q6 will have final year-to-date totals. If we added up the per-quarter values across the six quarters, we would get the year-to-date values in the Q6 spreadsheet. (I confirmed this with 2012-2013 year, at least.) Thus, we don't need the last two columns, because we can add up the quarterly values ourselves (with code).

Note: I misunderstood these data at first, for a variety of reasons. I didn't understand how the columns added up, and just started transforming the data. Ultimately, I threw away a lot of that work. It is very important to carefully read the documentation and make sure you understand all the data columns before proceeding. Data like these is often very complex and full of subtleties.

## Transform the data

Let's try converting each spreadsheet to CSV format. We can use [csvkit](http://csvkit.readthedocs.org/en/0.9.0/) to do this (as discussed on this [StackOverflow](http://stackoverflow.com/questions/9884353/xls-to-csv-convertor) post).

First, install csvkit:

```bash
pip install csvkit
```

This gives us the program `in2csv`, which we can execute as follows:

```bash
in2csv 2011_12App_Data_by_School_Q6.xls > test.csv
```

Open `test.csv` to see what we're working with:

```
Application Data by School,_unnamed,_unnamed_2,_unnamed_3,_unnamed_4,_unnamed_5,_unnamed_6,_unnamed_7,_unnamed_8,_unnamed_9,_unnamed_10
2011-2012 Application Cycle,,,,,,,,,,
Quarter 6 (04/01/12 - 06/30/12),,,,,,,,,,
,,,,,,,,,,
,,,,,"NUMBER OF FAFSA APPLICATIONS
PROCESSED IN Q6",,,"NUMBER OF FAFSA APPLICATIONS
AWARD YEAR TO DATE",,
OPE ID,School,State,Zip Code,School Type,Dependent Students,Independent Students,Quarterly Total,Dependent Students,Independent Students,Award Year To Date Total
00100200,ALABAMA AGRCLTL & MECHL UNIV            ,AL,35762-1357,Public       ,45.0,63.0,108.0,5944.0,3631.0,9575.0
00100300,FAULKNER UNIVERSITY                     ,AL,36109-3398,Private      ,23.0,133.0,156.0,1733.0,4436.0,6169.0
00100400,UNIVERSITY OF MONTEVALLO                ,AL,35115-6000,Public       ,16.0,27.0,43.0,2227.0,1637.0,3864.0
00100500,ALABAMA STATE UNIVERSITY                ,AL,36101-0000,Public       ,241.0,147.0,388.0,8341.0,5699.0,14040.0
00100700,CENTRAL ALABAMA COMMUNITY COLLEGE       ,AL,35011-0000,Public       ,49.0,109.0,158.0,1605.0,2630.0,4235.0
```

Notice that the dates are not found in the rows. The dates are in the third line: `Quarter 6 (04/01/12 - 06/30/12)`. We'll want those start/end dates in the rows so each record has that information. Here's a little Perl script to grab these dates and add them to each row:

```perl
#!/usr/bin/perl

# script name: add-dates.pl

$appcycle = "";
$quarter = "";
$start = "";
$end = "";

while($line = <>) {
    # detect "2011-2012 Application Cycle" or similar
    if($line =~ m!(\d{4}-\d{4}) Application Cycle!) {
        $appcycle = $1;
    }
    # detect quarter number and start/end dates
    elsif($line =~ m!Quarter (\d+) \((\d\d\/\d\d\/\d\d) - (\d\d\/\d\d\/\d\d)\)!) {
        $quarter = $1;
        $start = $2;
        $end = $3;
    }

    # if the current line is the header row, add our headers
    if($line =~ m!OPE ID,School,State,Zip Code!) {
        print "Application Cycle,Quarter,Start,End,$line";
    # otherwise, print the data we grabbed
    } else {
        print "$appcycle,$quarter,$start,$end,$line";
    }
}
```

Test it:

```bash
perl add-dates.pl < 2011_12App_Data_by_School_Q6.csv | less
```

Output:

```
,,,Application Data by School,_unnamed,_unnamed_2,_unnamed_3,_unnamed_4,_unnamed_5,_unnamed_6,_unnamed_7,_unnamed_8,_unnamed_9,_unnamed_10
2011-2012,,,,2011-2012 Application Cycle,,,,,,,,,,
2011-2012,6,04/01/12,06/30/12,Quarter 6 (04/01/12 - 06/30/12),,,,,,,,,,
2011-2012,6,04/01/12,06/30/12,,,,,,,,,,,
2011-2012,6,04/01/12,06/30/12,,,,,,"NUMBER OF FAFSA APPLICATIONS
2011-2012,6,04/01/12,06/30/12,PROCESSED IN Q6",,,"NUMBER OF FAFSA APPLICATIONS
2011-2012,6,04/01/12,06/30/12,AWARD YEAR TO DATE",,
Application Cycle,Quarter,Start,End,OPE ID,School,State,Zip Code,School Type,Dependent Students,Independent Students,Quarterly Total,Dependent Students,Independent Students,Award Year To Date Total
2011-2012,6,04/01/12,06/30/12,00100200,ALABAMA AGRCLTL & MECHL UNIV            ,AL,35762-1357,Public       ,45.0,63.0,108.0,5944.0,3631.0,9575.0
2011-2012,6,04/01/12,06/30/12,00100300,FAULKNER UNIVERSITY                     ,AL,36109-3398,Private      ,23.0,133.0,156.0,1733.0,4436.0,6169.0
2011-2012,6,04/01/12,06/30/12,00100400,UNIVERSITY OF MONTEVALLO                ,AL,35115-6000,Public       ,16.0,27.0,43.0,2227.0,1637.0,3864.0
```

Looks good. We won't need the first eight lines now. Have a look:

```bash
perl add-dates.pl < 2011_12App_Data_by_School_Q6.csv | tail -n +8 | less
```

Output:

```
Application Cycle,Quarter,Start,End,OPE ID,School,State,Zip Code,School Type,Dependent Students,Independent Students,Quarterly Total,Dependent Students,Independent Students,Award Year To Date Total
2011-2012,6,04/01/12,06/30/12,00100200,ALABAMA AGRCLTL & MECHL UNIV            ,AL,35762-1357,Public       ,45.0,63.0,108.0,5944.0,3631.0,9575.0
2011-2012,6,04/01/12,06/30/12,00100300,FAULKNER UNIVERSITY                     ,AL,36109-3398,Private      ,23.0,133.0,156.0,1733.0,4436.0,6169.0
2011-2012,6,04/01/12,06/30/12,00100400,UNIVERSITY OF MONTEVALLO                ,AL,35115-6000,Public       ,16.0,27.0,43.0,2227.0,1637.0,3864.0
```


Notice that "Dependent Students" and "Independent Students" are repeated. This is because the last three columns are "year to date" sums, which we don't need (we can generate that ourselves using all of the quarterly data). So let's chop those columns off, using csvkit. Also, "Quarterly Total" is just the sum of the previous two columns. We can add numbers ourselves, when needed.

Run `csvcut -n` to see what the columns are numbered:

```bash
perl add-dates.pl < 2011_12App_Data_by_School_Q6.csv | tail -n +8 | csvcut -n
```

Output:

```
  1: Application Cycle
  2: Quarter
  3: Start
  4: End
  5: OPE ID
  6: School
  7: State
  8: Zip Code
  9: School Type
 10: Dependent Students
 11: Independent Students
 12: Quarterly Total
 13: Dependent Students
 14: Independent Students
 15: Award Year To Date Total
```

Now we'll choose the columns we want to keep, and use `less` to see the result.

```bash
perl add-dates.pl < 2011_12App_Data_by_School_Q6.csv | tail -n +8 | csvcut -c 1,2,3,4,5,6,7,8,9,10,11 | less
```

Output:

```
Application Cycle,Quarter,Start,End,OPE ID,School,State,Zip Code,School Type,Dependent Students,Independent Students
2011-2012,6,04/01/12,06/30/12,00100200,ALABAMA AGRCLTL & MECHL UNIV            ,AL,35762-1357,Public       ,45.0,63.0
2011-2012,6,04/01/12,06/30/12,00100300,FAULKNER UNIVERSITY                     ,AL,36109-3398,Private      ,23.0,133.0
2011-2012,6,04/01/12,06/30/12,00100400,UNIVERSITY OF MONTEVALLO                ,AL,35115-6000,Public       ,16.0,27.0
```

That looks good. Now run this conversion on all the files.

```bash
for f in `ls *.xls`; do fname=`basename $f .xls`; in2csv $f | perl add-dates.pl | tail -n +8 | csvcut -c 1,2,3,4,5,6,7,8,9,10,11 > $fname.csv; done
```

Take a look at all the generated CSV files (type `:n` to switch to the next file, `:p` for the previous):

```
less *.csv
```

We see consistency among the files. Fantastic! (Finding a correct set of transformations took hours of work.)

Use `csvstat` (from csvkit, installed earlier) to give a quick analysis of a file:

```
csvstat 2011_12App_Data_by_School_Q6.csv
```

This gives us:

```
  1. Application Cycle
	<type 'unicode'>
	Nulls: False
	Values: 2011-2012
  2. Quarter
	<type 'int'>
	Nulls: False
	Values: 6
  3. Start
	<type 'datetime.date'>
	Nulls: False
	Values: 2012-04-01
  4. End
	<type 'datetime.date'>
	Nulls: False
	Values: 2012-06-30
  5. OPE ID
	<type 'unicode'>
	Nulls: True
	Unique values: 7476
	Max length: 8
  6. School
	<type 'unicode'>
	Nulls: False
	Unique values: 6841
	5 most frequent values:
		ITT TECHNICAL INSTITUTE:	41
		EMPIRE BEAUTY SCHOOL:	40
		EVEREST COLLEGE:	25
		SALON PROFESSIONAL ACADEMY (THE):	21
		KAPLAN COLLEGE:	20
	Max length: 40
  7. State
	<type 'unicode'>
	Nulls: True
	Unique values: 62
	5 most frequent values:
		CA:	708
		NY:	464
		TX:	399
		FC:	375
		FL:	369
	Max length: 4
  8. Zip Code
	<type 'unicode'>
	Nulls: False
	Unique values: 6714
	5 most frequent values:
		00000-0000:	475
		44106-0000:	11
		02138-0000:	10
		00960-0000:	5
		00674-0000:	5
	Max length: 10
  9. School Type
	<type 'unicode'>
	Nulls: True
	Values: Proprietary, Public, Private
 10. Dependent Students
	<type 'float'>
	Nulls: False
	Min: 0.0
	Max: 1272.0
	Sum: 130367.0
	Mean: 17.4334046537
	Median: 4.0
	Standard Deviation: 47.7871639982
	Unique values: 248
	5 most frequent values:
		0.0:	1823
		1.0:	811
		2.0:	520
		3.0:	507
		4.0:	341
 11. Independent Students
	<type 'float'>
	Nulls: False
	Min: 0.0
	Max: 26797.0
	Sum: 463920.0
	Mean: 62.037978069
	Median: 14.0
	Standard Deviation: 376.196191342
	Unique values: 473
	5 most frequent values:
		0.0:	909
		1.0:	437
		2.0:	357
		3.0:	281
		4.0:	253

Row count: 7478
```

So just with csvkit, we get some useful information:

- ITT Tech has 41 schools in various states (in 2011). It is the most common school (among schools whose students apply for federal aid). Empire Beauty School is second most common, etc.
- California has the most schools. New York is second.
- Lots of Zip codes are written `000000-0000`. This is effectively a "null" value, and might cause trouble later when try to correlate poverty with federal aid.
- Some school had 26797 independent students funded in Q6 2011. Searching for this number in the CSV file shows us it is the University of Phoenix. Makes sense.

Because we included the quarter and dates in the rows, we can concatenate (join) all the CSV files into one. We can use `csvstack` (from csvkit) to do this.

```bash
csvstack *.csv > fafsa-by-school.csv
```

Take a look at the result with `less` and `csvstat`. Below is `csvstat`'s output. Note, it took some time (64 seconds) for `csvstat` to process the joined file. While not yet "big data," we're playing a dangerous game here. Joining data files will be a bad idea in the near future.

For future reference, note that `csvstack` can be used with the `--filenames` option to automatically include a column for every row that indicates which filename the row came from.

```
  1. Application Cycle
	<type 'unicode'>
	Nulls: False
	Unique values: 9
	5 most frequent values:
		2010-2011:	44585
		2013-2014:	44576
		2012-2013:	44421
		2011-2012:	44149
		2009-2010:	43756
	Max length: 9
  2. Quarter
	<type 'int'>
	Nulls: False
	Min: 1
	Max: 6
	Sum: 1247543
	Mean: 3.35453000554
	Median: 3.0
	Standard Deviation: 1.72326543378
	Unique values: 6
	5 most frequent values:
		1:	71608
		3:	65583
		2:	65021
		6:	59405
		4:	58691
  3. Start
	<type 'datetime.date'>
	Nulls: False
	Min: 2006-01-01
	Max: 2014-07-01
	Unique values: 38
	5 most frequent values:
		2011-04-01:	14862
		2013-04-01:	14846
		2014-04-01:	14837
		2012-04-01:	14834
		2010-04-01:	14804
  4. End
	<type 'datetime.date'>
	Nulls: False
	Min: 2006-03-31
	Max: 2014-09-30
	Unique values: 39
	5 most frequent values:
		2011-06-30:	14862
		2013-06-30:	14846
		2014-06-30:	14837
		2012-06-30:	14834
		2010-06-30:	14804
  5. OPE ID
	<type 'unicode'>
	Nulls: True
	Unique values: 9016
	5 most frequent values:
		03874300:	57
		00247600:	57
		00476500:	55
		00247400:	55
		00248500:	55
	Max length: 45
  6. School
	<type 'unicode'>
	Nulls: True
	Unique values: 8704
	5 most frequent values:
		ITT TECHNICAL INSTITUTE:	1849
		EMPIRE BEAUTY SCHOOL:	1748
		EVEREST COLLEGE:	1252
		KAPLAN COLLEGE:	871
		LINCOLN TECHNICAL INSTITUTE:	778
	Max length: 40
  7. State
	<type 'unicode'>
	Nulls: True
	Unique values: 62
	5 most frequent values:
		CA:	34177
		NY:	23588
		TX:	19813
		FC:	19045
		PA:	18784
	Max length: 4
  8. Zip Code
	<type 'unicode'>
	Nulls: True
	Unique values: 8218
	5 most frequent values:
		00000-0000:	24105
		44106-0000:	579
		02138-0000:	528
		10027-0000:	513
		00960-0000:	274
	Max length: 10
  9. School Type
	<type 'unicode'>
	Nulls: True
	Values: Proprietary, Public, Private
 10. Dependent Students
	<type 'float'>
	Nulls: True
	Min: 0.0
	Max: 72219.0
	Sum: 119672792.0
	Mean: 321.799669791
	Median: 19.0
	Standard Deviation: 1589.19570442
	Unique values: 8053
	5 most frequent values:
		0.0:	45686
		1.0:	21278
		2.0:	15492
		3.0:	12916
		4.0:	10842
 11. Independent Students
	<type 'float'>
	Nulls: True
	Min: 0.0
	Max: 190415.0
	Sum: 125039205.0
	Mean: 336.229933367
	Median: 55.0
	Standard Deviation: 1446.29329899
	Unique values: 7016
	5 most frequent values:
		0.0:	21276
		1.0:	11294
		2.0:	8766
		3.0:	7543
		4.0:	6664

Row count: 371898
```

## Load the data into R

```R
> d <- read.csv("fafsa-by-school.csv", strip.white=TRUE)
> nrow(d)
[1] 371898
> colnames(d)
 [1] "Application.Cycle"    "Quarter"              "Start"
 [4] "End"                  "OPE.ID"               "School"
 [7] "State"                "Zip.Code"             "School.Type"
[10] "Dependent.Students"   "Independent.Students"
> d[1:5,]
  Application.Cycle Quarter    Start      End   OPE.ID                            School State   Zip.Code School.Type
1         2011-2012       6 04/01/12 06/30/12 00100200      ALABAMA AGRCLTL & MECHL UNIV    AL 35762-1357      Public
2         2011-2012       6 04/01/12 06/30/12 00100300               FAULKNER UNIVERSITY    AL 36109-3398     Private
3         2011-2012       6 04/01/12 06/30/12 00100400          UNIVERSITY OF MONTEVALLO    AL 35115-6000      Public
4         2011-2012       6 04/01/12 06/30/12 00100500          ALABAMA STATE UNIVERSITY    AL 36101-0000      Public
5         2011-2012       6 04/01/12 06/30/12 00100700 CENTRAL ALABAMA COMMUNITY COLLEGE    AL 35011-0000      Public
  Dependent.Students Independent.Students
1                 45                   63
2                 23                  133
3                 16                   27
4                241                  147
5                 49                  109
```

```R
> require(stringr)
> d$Zip <- str_sub(d$Zip.Code, start=0, end=5)
> d$Year <- str_sub(d$Application.Cycle, start=0, end=4)
```

```R
> d_dep_sum_cycle <- aggregate(cbind(Dependent.Students, Independent.Students) ~ School + Year + Zip + School.Type, d, sum)
> d_dep_sum_cycle
                                        School Year   Zip School.Type Dependent.Students Independent.Students
1                          INVALID SCHOOL CODE 2006 00000                          29572                27354
2                          INVALID SCHOOL CODE 2007 00000                          19544                20978
3                          INVALID SCHOOL CODE 2008 00000                           7223                10625
4                          INVALID SCHOOL CODE 2009 00000                           4454                10684
5                          INVALID SCHOOL CODE 2010 00000                           2615                 8758
6                          INVALID SCHOOL CODE 2011 00000                           3985                 9934
7                          INVALID SCHOOL CODE 2012 00000                           2108                 7578
8                          INVALID SCHOOL CODE 2013 00000                           1418                 6440
9                          INVALID SCHOOL CODE 2014 00000                            781                 3675
10                FEDERAL STUDENT AID USE ONLY 2007 20202                              0                    1
11                FEDERAL STUDENT AID USE ONLY 2008 20202                             11                 1740
12                FEDERAL STUDENT AID USE ONLY 2009 20202                              2                  116
13                FEDERAL STUDENT AID USE ONLY 2010 20202                              0                    1
14                           OFFICIAL USE ONLY 2010 20202                              1                    0
15                           OFFICIAL USE ONLY 2011 20202                              3                    1
16                FEDERAL STUDENT AID USE ONLY 2012 20202                              1                    2
17                           OFFICIAL USE ONLY 2012 20202                              0                    1
18                FEDERAL STUDENT AID USE ONLY 2013 20202                              3                    4
19                           OFFICIAL USE ONLY 2013 20202                              0                    2
20                FEDERAL STUDENT AID USE ONLY 2014 20202                              0                    2
21          AISH HATORAH CLG OF JEWISH STUDIES 2006 00000     Private                  1                    6
22    AMERICAN GRAD SCH OF INTERNTL REL & DIPL 2006 00000     Private                  1                   45
23               AMERICAN UNIVERSITY OF BEIRUT 2006 00000     Private                 69                   60
24                AMERICAN UNIVERSITY OF CAIRO 2006 00000     Private                 84                  155
25                AMERICAN UNIVERSITY OF PARIS 2006 00000     Private                239                  114
26                 AMERICAN UNIVERSITY OF ROME 2006 00000     Private                119                   56
27               ANGELES UNIVERSITY FOUNDATION 2006 00000     Private                  0                    3
28         ARCHITECTURAL ASSOC SCH OF ARCHITEC 2006 00000     Private                  3                   27
29               ASIAN INSTITUTE OF MANAGEMENT 2006 00000     Private                  2                    3
30                AUGUSTANA UNIVERSITY COLLEGE 2006 00000     Private                  0                    1
31        AVE MARIA UNIVERSITY - LATIN AMERICA 2006 00000     Private                156                   43
32                         BAR-ILAN UNIVERSITY 2006 00000     Private                  7                   18
33                       BETHANY BIBLE COLLEGE 2006 00000     Private                 90                   26
 [ reached getOption("max.print") -- omitted 67779 rows ]
```

Choose an application year to examine, say 2011-2012, and compare it to the spreadsheet `2011_12App_Data_by_School_Q6.xls` (column I; the spreadsheets are not sorted by school, so you'll need to search on particular school names). You should find that our "aggregated" data matches the spreadsheet's Q6 year-to-date numbers. Thus, we are able to recreate those numbers from the raw data, just like we expected (see the advice from the [data munging](/notes/data-munging.html) notes).

```R
> d_dep_sum_cycle[d_dep_sum_cycle$Year == "2011",]
                                        School Year   Zip School.Type Dependent.Students Independent.Students
6                          INVALID SCHOOL CODE 2011 00000                           3985                 9934
15                           OFFICIAL USE ONLY 2011 20202                              3                    1
633         AISH HATORAH CLG OF JEWISH STUDIES 2011 00000     Private                  2                    2
634   ALRA (ACADEMY OF LIVE AND RECORDED ARTS) 2011 00000     Private                  3                    5
635                 AMBROSE UNIVERSITY COLLEGE 2011 00000     Private                  6                   12
636   AMERICAN COLLEGE, DUBLIN LIMITED (THE)   2011 00000     Private                  8                   14
637   AMERICAN GRAD SCH OF INTERNTL REL & DIPL 2011 00000     Private                  7                   68
638              AMERICAN UNIVERSITY OF BEIRUT 2011 00000     Private                 96                  116
639               AMERICAN UNIVERSITY OF CAIRO 2011 00000     Private                 71                  238
640               AMERICAN UNIVERSITY OF PARIS 2011 00000     Private                245                  308
641                AMERICAN UNIVERSITY OF ROME 2011 00000     Private                179                   74
642                  ANGLIA  RUSKIN UNIVERSITY 2011 00000     Private                  1                    8
643     ANGLO EUROPEAN COLLEGE OF CHIROPRACTIC 2011 00000     Private                  1                    2
644              ASIAN INSTITUTE OF MANAGEMENT 2011 00000     Private                  3                   19
645                ATLANTIC BAPTIST UNIVERSITY 2011 00000     Private                  3                    0
```

```R
> d_dep_sum_cycle[d_dep_sum_cycle$School == "STETSON UNIVERSITY",]
                 School Year   Zip School.Type Dependent.Students Independent.Students
9384 STETSON UNIVERSITY 2006 32723     Private               2344                  656
9385 STETSON UNIVERSITY 2007 32723     Private               2323                  644
9386 STETSON UNIVERSITY 2008 32723     Private               2728                  735
9387 STETSON UNIVERSITY 2009 32723     Private               3039                 1047
9388 STETSON UNIVERSITY 2010 32723     Private               2912                 1056
9389 STETSON UNIVERSITY 2011 32723     Private               3746                 1258
9390 STETSON UNIVERSITY 2012 32723     Private               5366                 1296
9391 STETSON UNIVERSITY 2013 32723     Private               7216                 1209
9392 STETSON UNIVERSITY 2014 32723     Private               6868                 1021
```



## Correlate IPEDS data

```R
> app2007 <- read.csv("applicants-2007.csv", strip.white=TRUE)
> app2008 <- read.csv("applicants-2008.csv", strip.white=TRUE)
> app2009 <- read.csv("applicants-2009.csv", strip.white=TRUE)
> app2010 <- read.csv("applicants-2010.csv", strip.white=TRUE)
> app2011 <- read.csv("applicants-2011.csv", strip.white=TRUE)
> app2012 <- read.csv("applicants-2012.csv", strip.white=TRUE)
> app2013 <- read.csv("applicants-2013.csv", strip.white=TRUE)
```

```
> colnames(app2007)
[1] "unitid"                  "institution.name"        "year"                    "IC2007.Applicants.total" "HD2007.ZIP.code"
> colnames(app2007) <- c("Unitid", "School", "Year", "Applicants", "Zip")
> colnames(app2007)
[1] "Unitid"     "School"     "Year"       "Applicants" "Zip"
> colnames(app2008) <- c("Unitid", "School", "Year", "Applicants", "Zip")
> colnames(app2009) <- c("Unitid", "School", "Year", "Applicants", "Zip")
> colnames(app2010) <- c("Unitid", "School", "Year", "Applicants", "Zip")
> colnames(app2011) <- c("Unitid", "School", "Year", "Applicants", "Zip")
> colnames(app2012) <- c("Unitid", "School", "Year", "Applicants", "Zip")
> colnames(app2013) <- c("Unitid", "School", "Year", "Applicants", "Zip")
```

```R
> apps <- rbind(app2007, app2008, app2009, app2010, app2011, app2012, app2013)
> apps
      Unitid                                                                                      School Year Applicants        Zip
1     100654                                                                    Alabama A & M University 2007       6470      35762
2     100663                                                         University of Alabama at Birmingham 2007       4221 35294-0110
3     100690                                                                          Amridge University 2007         NA 36117-3553
4     100706                                                         University of Alabama in Huntsville 2007       1850      35899
5     100724                                                                    Alabama State University 2007      12436 36101-0271
6     100733                                                         University of Alabama System Office 2007         NA      35401
7     100751                                                                   The University of Alabama 2007      14313 35487-0166
8     100760                                                           Central Alabama Community College 2007         NA      35010
9     100812                                                                     Athens State University 2007         NA      35611
10    100830                                                             Auburn University at Montgomery 2007       1210 36117-3596
11    100858                                                                           Auburn University 2007      17688      36849
12    100937                                                                 Birmingham Southern College 2007       2282      35254
13    101028                                                      Chattahoochee Valley Community College 2007         NA      36869
14    101073                                                                   Concordia College Alabama 2007         NA      36701
15    101116                                                                 South University-Montgomery 2007         92 36116-1120
16    101143                                                          Enterprise State Community College 2007         NA 36330-1300
17    101161                                                    James H Faulkner State Community College 2007         NA 36507-2698
18    101189                                                                         Faulkner University 2007        982 36109-3378
19    101240                                                             Gadsden State Community College 2007         NA 35902-0227
20    101277                                                        New Beginning College of Cosmetology 2007         NA
 [ reached getOption("max.print") -- omitted 53159 rows ]
```

```R
> str_sub(apps$Zip, 0, 5)
  [1] "35762" "35294" "36117" "35899" "36101" "35401" "35487" "35010" "35611" "36117" "36849" "35254" "36869" "36701" "36116" "36330" "36507" "36109"
 [19] "35902" ""      "36303" "35077" "36703" "35209" "36106" "35630" "35811" "36022" "36265" "36426" "35215" "35671" "36756" "35221" "35470" "36420"
 [37] "36756" "35064" "36663" "35115" "35661" "35632" "35986" "35896" "36460" "36117" "36401" "36603" "35229" ""      "35405" "35957" "36688" "36608"
 [55] "35244" "35403" "35160" "36108" "36082" "36088" "36526" "35148" "99508" ""      "99775" "99801" "99508" "99664" "99508" "99686" "99507" "99775"
 [73] "85021" "85281" "85032" "85716" "85306" "85021" "85043" "85051" ""      ""      "86001" "85712" "85301" "85019" "85287" "85365" "85721" "85716"
 [91] "85251" "85228" "85712" "86403" "85607" "86004" "85224" "85710" "85021" "85210"
 [ reached getOption("max.print") -- omitted 53079 entries ]
> apps$Zip <- str_sub(apps$Zip, 0, 5)
> apps
      Unitid                                                                                      School Year Applicants   Zip
1     100654                                                                    Alabama A & M University 2007       6470 35762
2     100663                                                         University of Alabama at Birmingham 2007       4221 35294
3     100690                                                                          Amridge University 2007         NA 36117
4     100706                                                         University of Alabama in Huntsville 2007       1850 35899
5     100724                                                                    Alabama State University 2007      12436 36101
6     100733                                                         University of Alabama System Office 2007         NA 35401
7     100751                                                                   The University of Alabama 2007      14313 35487
8     100760                                                           Central Alabama Community College 2007         NA 35010
9     100812                                                                     Athens State University 2007         NA 35611
10    100830                                                             Auburn University at Montgomery 2007       1210 36117
11    100858                                                                           Auburn University 2007      17688 36849
12    100937                                                                 Birmingham Southern College 2007       2282 35254
13    101028                                                      Chattahoochee Valley Community College 2007         NA 36869
14    101073                                                                   Concordia College Alabama 2007         NA 36701
15    101116                                                                 South University-Montgomery 2007         92 36116
16    101143                                                          Enterprise State Community College 2007         NA 36330
17    101161                                                    James H Faulkner State Community College 2007         NA 36507
18    101189                                                                         Faulkner University 2007        982 36109
19    101240                                                             Gadsden State Community College 2007         NA 35902
20    101277                                                        New Beginning College of Cosmetology 2007         NA
 [ reached getOption("max.print") -- omitted 53159 rows ]
```

```R
> appschools <- unique(apps[,c("School","Zip"),drop=FALSE])
> appschools
                                                                                           School   Zip
1                                                                        Alabama A & M University 35762
2                                                             University of Alabama at Birmingham 35294
3                                                                              Amridge University 36117
4                                                             University of Alabama in Huntsville 35899
5                                                                        Alabama State University 36101
6                                                             University of Alabama System Office 35401
7                                                                       The University of Alabama 35487
8                                                               Central Alabama Community College 35010
9                                                                         Athens State University 35611
10                                                                Auburn University at Montgomery 36117
11                                                                              Auburn University 36849
12                                                                    Birmingham Southern College 35254
13                                                         Chattahoochee Valley Community College 36869
14                                                                      Concordia College Alabama 36701
15                                                                    South University-Montgomery 36116
16                                                             Enterprise State Community College 36330
17                                                       James H Faulkner State Community College 36507
18                                                                            Faulkner University 36109
19                                                                Gadsden State Community College 35902
20                                                           New Beginning College of Cosmetology
> dim(appschools)
[1] 9592    2
> dschools <- unique(d_dep_sum_cycle[,c("School","Zip"),drop=FALSE])
> dim(dschools)
[1] 9694    2
```

```R
> findMatchingSchool <- function(x, schools) { ssub <- subset(schools, Zip==x[["Zip"]]); if(nrow(ssub) > 0) { dists <- adist(tolower(x[["School"]]), apply(ssub, 1, function(y) tolower(y[["School"]]) )); if(min(dists) < str_length(x[["School"]]) && min(dists) < str_length(as.character(ssub$School[which.min(dists)]))) { as.character(ssub$School[which.min(dists)]) } else { NA } } else { NA } }
> data.frame(aschool = appschools[1:50,,drop=F]$School, dschool = apply(appschools[1:50,,drop=F], 1, function(x) findMatchingSchool(x, dschools)))
                                               aschool                                  dschool
1                             Alabama A & M University             ALABAMA AGRCLTL & MECHL UNIV
2                  University of Alabama at Birmingham      UNIVERSITY OF ALABAMA AT BIRMINGHAM
3                                   Amridge University                                     <NA>
4                  University of Alabama in Huntsville      UNIVERSITY OF ALABAMA IN HUNTSVILLE
5                             Alabama State University                 ALABAMA STATE UNIVERSITY
6                  University of Alabama System Office                                     <NA>
7                            The University of Alabama              UNIVERSITY OF ALABAMA (THE)
8                    Central Alabama Community College                                     <NA>
9                              Athens State University                  ATHENS STATE UNIVERSITY
10                     Auburn University at Montgomery            VIRGINIA COLLEGE - MONTGOMERY
11                                   Auburn University                        AUBURN UNIVERSITY
12                         Birmingham Southern College              BIRMINGHAM SOUTHERN COLLEGE
13              Chattahoochee Valley Community College            CHATTAHOOCHEE VALLEY CMTY CLG
14                           Concordia College Alabama                        CONCORDIA COLLEGE
15                         South University-Montgomery            SOUTH UNIVERSITY - MONTGOMERY
16                  Enterprise State Community College                                     <NA>
17            James H Faulkner State Community College         FAULKNER STATE COMMUNITY COLLEGE
18                                 Faulkner University                      FAULKNER UNIVERSITY
19                     Gadsden State Community College                                     <NA>
20                New Beginning College of Cosmetology                                     <NA>
21     George C Wallace State Community College-Dothan       GEORGE C WALLACE COMMUNITY COLLEGE
22 George C Wallace State Community College-Hanceville          GEORGE C WALLACE STATE CMTY CLG
23      George C Wallace State Community College-Selma      GEORGE C WALLACE STATE CMTY COLLEGE
24                       Herzing University-Birmingham          HERZING UNIVERSITY - BIRMINGHAM
25                                  Huntingdon College                       HUNTINGDON COLLEGE
26                       Heritage Christian University            HERITAGE CHRISTIAN UNIVERSITY
27     J F Drake State Community and Technical College            J.F. DRAKE STATE TECH COLLEGE
28                  J F Ingram State Technical College                                     <NA>
29                       Jacksonville State University            JACKSONVILLE STATE UNIVERSITY
30                   Jefferson Davis Community College                                     <NA>
31                   Jefferson State Community College        JEFFERSON STATE COMMUNITY COLLEGE
32              John C Calhoun State Community College                                     <NA>
33                                      Judson College                           JUDSON COLLEGE
34    Lawson State Community College-Birmingham Campus           LAWSON STATE COMMUNITY COLLEGE
35                          University of West Alabama               UNIVERSITY OF WEST ALABAMA
36                 Lurleen B Wallace Community College      LURLEEN B WALLACE COMMUNITY COLLEGE
37                           Marion Military Institute                MARION MILITARY INSTITUTE
38                                       Miles College                            MILES COLLEGE
39                                University of Mobile                     UNIVERSITY OF MOBILE
40                            University of Montevallo                 UNIVERSITY OF MONTEVALLO
41                  Northwest-Shoals Community College      NORTHWEST- SHOALS COMMUNITY COLLEGE
42                         University of North Alabama              UNIVERSITY OF NORTH ALABAMA
43                 Northeast Alabama Community College       NORTHEAST ALABAMA STATE CMNTY COLG
44                                  Oakwood University                          OAKWOOD COLLEGE
45                  Alabama Southern Community College       ALABAMA SOUTHERN COMMUNITY COLLEGE
46                          Prince Institute-Southeast PRINCE INSTITUTE OF PROFESSIONAL STUDIES
47                        Reid State Technical College             REID STATE TECHNICAL COLLEGE
48                      Bishop State Community College           BISHOP STATE COMMUNITY COLLEGE
49                                  Samford University                       SAMFORD UNIVERSITY
50                                    Selma University                                     <NA>
```

```R
> schoolnames <- data.frame(aschool = appschools$School, dschool = apply(appschools, 1, function(x) findMatchingSchool(x, dschools)))
```

```R
> merge(d_dep_sum_cycle, schoolnames, by.x="School", by.y="dschool")
                                        School Year   Zip School.Type Dependent.Students Independent.Students                                                                   aschool
1                   A & W HEALTHCARE EDUCATORS 2011 70122 Proprietary                 33                  234                                                A & W Healthcare Educators
2                   A & W HEALTHCARE EDUCATORS 2014 70122 Proprietary                 58                  384                                                A & W Healthcare Educators
3                   A & W HEALTHCARE EDUCATORS 2010 70122 Proprietary                 29                  207                                                A & W Healthcare Educators
4                   A & W HEALTHCARE EDUCATORS 2013 70122 Proprietary                 75                  454                                                A & W Healthcare Educators
5                   A & W HEALTHCARE EDUCATORS 2012 70122 Proprietary                 67                  443                                                A & W Healthcare Educators
6                   A & W HEALTHCARE EDUCATORS 2009 70122 Proprietary                  2                   21                                                A & W Healthcare Educators
7           A.T. STILL UNIV OF HEALTH SCIENCES 2009 63501     Private                 12                 3020                                   A T Still University of Health Sciences
8           A.T. STILL UNIV OF HEALTH SCIENCES 2012 63501     Private                 25                 3052                                   A T Still University of Health Sciences
9           A.T. STILL UNIV OF HEALTH SCIENCES 2011 63501     Private                 15                 2984                                   A T Still University of Health Sciences
10          A.T. STILL UNIV OF HEALTH SCIENCES 2007 63501     Private                  6                 2318                                   A T Still University of Health Sciences
 [ reached getOption("max.print") -- omitted 96428 rows ]
```

Let's see what we're working with before we do the merges:

```R
> subset(d_dep_sum_cycle, School == "A & W HEALTHCARE EDUCATORS")
                          School Year   Zip School.Type Dependent.Students Independent.Students
35672 A & W HEALTHCARE EDUCATORS 2009 70122 Proprietary                  2                   21
35673 A & W HEALTHCARE EDUCATORS 2010 70122 Proprietary                 29                  207
35674 A & W HEALTHCARE EDUCATORS 2011 70122 Proprietary                 33                  234
35675 A & W HEALTHCARE EDUCATORS 2012 70122 Proprietary                 67                  443
35676 A & W HEALTHCARE EDUCATORS 2013 70122 Proprietary                 75                  454
35678 A & W HEALTHCARE EDUCATORS 2014 70122 Proprietary                 58                  384
> subset(apps, School == "A & W Healthcare Educators")
      Unitid                     School Year Applicants   Zip                    aschool
6705  457590 A & W Healthcare Educators 2007         NA       A & W Healthcare Educators
14302 457590 A & W Healthcare Educators 2008         NA       A & W Healthcare Educators
21899 457590 A & W Healthcare Educators 2009         NA       A & W Healthcare Educators
29496 457590 A & W Healthcare Educators 2010         NA 70122 A & W Healthcare Educators
37093 457590 A & W Healthcare Educators 2011         NA 70126 A & W Healthcare Educators
44690 457590 A & W Healthcare Educators 2012         NA 70126 A & W Healthcare Educators
52287 457590 A & W Healthcare Educators 2013         NA 70126 A & W Healthcare Educators
```

For A & W Healthcare Educators, we only have a match across the two datasets for 2010. (Unfortunately, applicants is NA for that year, so we'll throw it out anyway.)

```
> subset(d_dep_sum_cycle, School == "STETSON UNIVERSITY")
                 School Year   Zip School.Type Dependent.Students Independent.Students
9384 STETSON UNIVERSITY 2006 32723     Private               2344                  656
9385 STETSON UNIVERSITY 2007 32723     Private               2323                  644
9386 STETSON UNIVERSITY 2008 32723     Private               2728                  735
9387 STETSON UNIVERSITY 2009 32723     Private               3039                 1047
9388 STETSON UNIVERSITY 2010 32723     Private               2912                 1056
9389 STETSON UNIVERSITY 2011 32723     Private               3746                 1258
9390 STETSON UNIVERSITY 2012 32723     Private               5366                 1296
9391 STETSON UNIVERSITY 2013 32723     Private               7216                 1209
9392 STETSON UNIVERSITY 2014 32723     Private               6868                 1021
> subset(apps, School == "Stetson University")
      Unitid             School Year Applicants   Zip            aschool
899   137546 Stetson University 2007       2948 32723 Stetson University
8496  137546 Stetson University 2008       4110 32723 Stetson University
16093 137546 Stetson University 2009       4640 32723 Stetson University
23690 137546 Stetson University 2010       3884 32723 Stetson University
31287 137546 Stetson University 2011       3454 32723 Stetson University
38884 137546 Stetson University 2012       4862 32723 Stetson University
46481 137546 Stetson University 2013      10509 32723 Stetson University
```

For Stetson University, years 2006 and 2014 are not represented in the applicants counts. But years 2007-2013 should match up correctly.

```R
> subset(merge(merge(d_dep_sum_cycle, schoolnames, by.x="School", by.y="dschool"), apps, by=c("aschool", "Year", "Zip")), aschool == "A & W Healthcare Educators")

                     aschool Year   Zip                   School.x School.Type Dependent.Students Independent.Students Unitid                   School.y Applicants
1 A & W Healthcare Educators 2010 70122 A & W HEALTHCARE EDUCATORS Proprietary                 29                  207 457590 A & W Healthcare Educators         NA

> subset(merge(merge(d_dep_sum_cycle, schoolnames, by.x="School", by.y="dschool"), apps, by=c("aschool", "Year", "Zip")), aschool == "Stetson University")

                 aschool Year   Zip           School.x School.Type Dependent.Students Independent.Students Unitid           School.y Applicants
30555 Stetson University 2007 32723 STETSON UNIVERSITY     Private               2323                  644 137546 Stetson University       2948
30556 Stetson University 2008 32723 STETSON UNIVERSITY     Private               2728                  735 137546 Stetson University       4110
30557 Stetson University 2009 32723 STETSON UNIVERSITY     Private               3039                 1047 137546 Stetson University       4640
30558 Stetson University 2010 32723 STETSON UNIVERSITY     Private               2912                 1056 137546 Stetson University       3884
30559 Stetson University 2011 32723 STETSON UNIVERSITY     Private               3746                 1258 137546 Stetson University       3454
30560 Stetson University 2012 32723 STETSON UNIVERSITY     Private               5366                 1296 137546 Stetson University       4862
30561 Stetson University 2013 32723 STETSON UNIVERSITY     Private               7216                 1209 137546 Stetson University      10509
```

Let's save the merge:

```
> appfasfa <- merge(merge(d_dep_sum_cycle, schoolnames, by.x="School", by.y="dschool"), apps, by=c("aschool", "Year", "Zip"))
> nrow(appfasfa)
[1] 37665
```

Let's drop some columns and fix their names:

```R
> appfasfa <- appfasfa[,!(names(appfasfa) %in% c("School.x", "School.y", "Unitid"))]
> colnames(appfasfa) <- c("School", "Year", "Zip", "School.Type", "Dependent.Students", "Independent.Students", "Applicants")
> appfasfa
                                                                         School Year   Zip School.Type Dependent.Students Independent.Students Applicants
1                                                    A & W Healthcare Educators 2010 70122 Proprietary                 29                  207         NA
2                                       A T Still University of Health Sciences 2007 63501     Private                  6                 2318         NA
3                                       A T Still University of Health Sciences 2008 63501     Private                  5                 2594         NA
4                                       A T Still University of Health Sciences 2009 63501     Private                 12                 3020         NA
5                                       A T Still University of Health Sciences 2010 63501     Private                 18                 3098         NA
6                                       A T Still University of Health Sciences 2011 63501     Private                 15                 2984         NA
7                                       A T Still University of Health Sciences 2012 63501     Private                 25                 3052         NA
8                                       A T Still University of Health Sciences 2013 63501     Private                 29                 2903         NA
9                                                        Aaniiih Nakoda College 2007 59526      Public                 64                  132         NA
10                                                       Aaniiih Nakoda College 2008 59526      Public                 54                  125         NA
 [ reached getOption("max.print") -- omitted 37655 rows ]
```


## Correlate IRS income data

Average AGI for a Zip: AGI field (column J) / Number of returns (column C)

Only need the first record for each Zip (record with no "Size of Adjusted Gross Income" category).


1. Remove all CSV files first (so we know which CSV files we generated).

```bash
find . -name "*.csv" -print0 | xargs -0 rm
```

2. Convert XLS files to CSV, and add year column.

```bash
find . -name "*.xls" -print0 | xargs -0 -I % ./convert-irs-xls.sh %
```

3. Stack (join) all the CSV files.

```bash
csvstack *zp*.csv > zip-agi-2009-2012.csv
```

```bash
$ head zip-agi-2009-2012.csv
2009,35004.0,4605.0,194892871.0
2009,35005.0,3438.0,105772485.0
2009,35006.0,1317.0,46032012.0
2009,35007.0,11345.0,489346137.0
2009,35010.0,9447.0,271595411.0
2009,35014.0,1586.0,49637632.0
2009,35016.0,6961.0,238316392.0
2009,35019.0,877.0,26087030.0
2009,35020.0,10706.0,223172672.0
2009,35022.0,9018.0,378475890.0
```

```R
> zipagi <- read.csv("../irs-soi-tax-stats/zip-agi-2009-2012.csv", header=F, col.names=c("Year", "Zip", "NumberReturns", "AGI"))
> zipagi
       Year   Zip NumberReturns        AGI
1      2009 35004          4605  194892871
2      2009 35005          3438  105772485
3      2009 35006          1317   46032012
4      2009 35007         11345  489346137
5      2009 35010          9447  271595411
6      2009 35014          1586   49637632
7      2009 35016          6961  238316392
8      2009 35019           877   26087030
9      2009 35020         10706  223172672
10     2009 35022          9018  378475890
11     2009 35023         10517  366146786
12     2009 35031          3069   86645823
13     2009 35033          1329   45553919
14     2009 35034          1524   39201669
15     2009 35035           562   17788082
16     2009 35040          6517  266155922
17     2009 35042          2157   70358562
18     2009 35043          3955  225728279
 [ reached getOption("max.print") -- omitted 111029 rows ]
> zipagi[zipagi$Year>2010,]
       Year   Zip NumberReturns      AGI
55543  2011 35005          3440   107143
55544  2011 35006          1298    46566
55545  2011 35007         11768   507778
55546  2011 35010          8941   263085
55547  2011 35014          1651    51706
55548  2011 35016          7050   237748
55549  2011 35019           864    25486
55550  2011 35020         10774   226138
55551  2011 35022          9356   398977
55552  2011 35023         10417   368014
55553  2011 35031          2981    84368
55554  2011 35033          1318    46088
55555  2011 35034          1491    39132
55556  2011 35035           584    18699
55557  2011 35040          6929   281690
55558  2011 35042          2181    73996
55559  2011 35043          4180   237805
55560  2011 35044          3225    93213
 [ reached getOption("max.print") -- omitted 55487 rows ]
```

```R
> zipagi2011up <- cbind(zipagi[zipagi$Year > 2010,], data.frame(AvgIncome = zipagi[zipagi$Year > 2010,]$AGI / zipagi[zipagi$Year > 2010,]$NumberReturns * 1000))
> zipagi2010down <- cbind(zipagi[zipagi$Year <= 2010,], data.frame(AvgIncome = zipagi[zipagi$Year <= 2010,]$AGI / zipagi[zipagi$Year <= 2010,]$NumberReturns))
> zipagi2011up
       Year   Zip NumberReturns      AGI   AvgIncome
55543  2011 35005          3440   107143   31146.221
55544  2011 35006          1298    46566   35875.193
55545  2011 35007         11768   507778   43149.048
55546  2011 35010          8941   263085   29424.561
55547  2011 35014          1651    51706   31317.989
55548  2011 35016          7050   237748   33723.121
55549  2011 35019           864    25486   29497.685
55550  2011 35020         10774   226138   20989.233
55551  2011 35022          9356   398977   42643.972
55552  2011 35023         10417   368014   35328.213
55553  2011 35031          2981    84368   28301.912
55554  2011 35033          1318    46088   34968.134
55555  2011 35034          1491    39132   26245.473
55556  2011 35035           584    18699   32018.836
55557  2011 35040          6929   281690   40653.774
 [ reached getOption("max.print") -- omitted 55490 rows ]
> zipagi2010down
      Year   Zip NumberReturns        AGI    AvgIncome
1     2009 35004          4605  194892871  42322.01325
2     2009 35005          3438  105772485  30765.70244
3     2009 35006          1317   46032012  34952.17312
4     2009 35007         11345  489346137  43133.19850
5     2009 35010          9447  271595411  28749.38192
6     2009 35014          1586   49637632  31297.37201
7     2009 35016          6961  238316392  34235.94196
8     2009 35019           877   26087030  29745.75827
9     2009 35020         10706  223172672  20845.56996
10    2009 35022          9018  378475890  41968.93879
11    2009 35023         10517  366146786  34814.75573
12    2009 35031          3069   86645823  28232.59140
13    2009 35033          1329   45553919  34276.83898
14    2009 35034          1524   39201669  25722.87992
15    2009 35035           562   17788082  31651.39146
 [ reached getOption("max.print") -- omitted 55527 rows ]
> zipagi <- rbind(zipagi2010down, zipagi2011up)
> zipagi
       Year   Zip NumberReturns        AGI    AvgIncome
1      2009 35004          4605  194892871 4.232201e+04
2      2009 35005          3438  105772485 3.076570e+04
3      2009 35006          1317   46032012 3.495217e+04
4      2009 35007         11345  489346137 4.313320e+04
5      2009 35010          9447  271595411 2.874938e+04
6      2009 35014          1586   49637632 3.129737e+04
7      2009 35016          6961  238316392 3.423594e+04
8      2009 35019           877   26087030 2.974576e+04
9      2009 35020         10706  223172672 2.084557e+04
10     2009 35022          9018  378475890 4.196894e+04
11     2009 35023         10517  366146786 3.481476e+04
12     2009 35031          3069   86645823 2.823259e+04
13     2009 35033          1329   45553919 3.427684e+04
14     2009 35034          1524   39201669 2.572288e+04
15     2009 35035           562   17788082 3.165139e+04
 [ reached getOption("max.print") -- omitted 111032 rows ]
```

Self-check: are average incomes comparable across each year?

```R
> aggregate(AvgIncome ~ Year, zipagi, mean)
  Year AvgIncome
1 2009  34887.18
2 2010  36163.23
3 2011  36658.47
4 2012  57528.93
```

Seems so, more or less. At least, no year is off by a factor of 1000.

Notice some ZIPs are written without leading zeros. For example, Harvard University has ZIP 02138, but this appears as 2138 in the `zipagi` dataset.

```R
> zipagi$Zip <- apply(zipagi[,"Zip",drop=F], 1, function(x) sprintf("%05d", x))
> subset(zipagi, Zip == "02138")
      Year   Zip NumberReturns       AGI AvgIncome
11025 2009 02138         15051 954968044  63448.81
38789 2010 02138         15225 995712861  65399.86
66572 2011 02138         15344   1077245  70206.27
94333 2012 02138         14320   2633364 183894.13
```

```R
> fasfaincome <- merge(appfasfa, zipagi, by=c("Year", "Zip"))
> fasfaincome
      Year   Zip                                                                    School School.Type Dependent.Students Independent.Students Applicants NumberReturns        AGI    AvgIncome
1     2009 01002                                                           Amherst College     Private               4638                  177       7679          9889  404902740 4.094476e+04
2     2009 01002                                                         Hampshire College     Private               2412                  110       2515          9889  404902740 4.094476e+04
3     2009 01040                                                 Holyoke Community College      Public               3746                 4643         NA         15737  468704273 2.978358e+04
4     2009 01056                                      Jolie Hair and Beauty Academy-Ludlow Proprietary                 29                  173         NA          9980  401489423 4.022940e+04
5     2009 01075                                                     Mount Holyoke College     Private               2531                  322       3061          8515  347877772 4.085470e+04
6     2009 01089                                           DiGrigoli School of Cosmetology Proprietary                 27                   57         NA         13787  508514135 3.688360e+04
7     2009 01089                                         Kay Harvey Academy of Hair Design Proprietary                 25                   55         NA         13787  508514135 3.688360e+04
 [ reached getOption("max.print") -- omitted 20348 rows ]
> fasfaincome <- fasfaincome[!is.na(fasfaincome$Applicants),]
> fasfaincome <- fasfaincome[fasfaincome$Applicants > 0,]
> fasfaincome
      Year   Zip                                                                 School School.Type Dependent.Students Independent.Students Applicants NumberReturns        AGI    AvgIncome
1     2009 01002                                                        Amherst College     Private               4638                  177       7679          9889  404902740 4.094476e+04
2     2009 01002                                                      Hampshire College     Private               2412                  110       2515          9889  404902740 4.094476e+04
5     2009 01075                                                  Mount Holyoke College     Private               2531                  322       3061          8515  347877772 4.085470e+04
10    2009 01106                                                       Bay Path College     Private                838                 1951       1099          7465  575123619 7.704268e+04
11    2009 01109                                         American International College     Private               2072                 2628       1469         10675  259913626 2.434788e+04
12    2009 01109                                                    Springfield College     Private               3971                 4376       2391         10675  259913626 2.434788e+04
15    2009 01230                                           Bard College at Simon's Rock     Private                483                   23        330          3592  123043560 3.425489e+04
 [ reached getOption("max.print") -- omitted 6642 rows ]
```

```R
> fasfaincome$PctFasfa <- (fasfaincome$Dependent.Students + fasfaincome$Independent.Students) / fasfaincome$Applicants * 100
>
```

```R
> data(zipcode)
> zipcode
        zip                       city state   latitude  longitude
1     00210                 Portsmouth    NH  43.005895  -71.01320
2     00211                 Portsmouth    NH  43.005895  -71.01320
3     00212                 Portsmouth    NH  43.005895  -71.01320
4     00213                 Portsmouth    NH  43.005895  -71.01320
5     00214                 Portsmouth    NH  43.005895  -71.01320
6     00215                 Portsmouth    NH  43.005895  -71.01320
7     00501                 Holtsville    NY  40.922326  -72.63708
8     00544                 Holtsville    NY  40.922326  -72.63708
9     00601                   Adjuntas    PR  18.180103  -66.74947
10    00602                     Aguada    PR  18.363285  -67.18024
11    00603                  Aguadilla    PR  18.448619  -67.13422
12    00604                  Aguadilla    PR  18.498987  -67.13699
13    00605                  Aguadilla    PR  18.465162  -67.14149
14    00606                    Maricao    PR  18.182151  -66.95880
15    00607               Aguas Buenas    PR  18.256995  -66.10466
 [ reached getOption("max.print") -- omitted 44321 rows ]
> fasfaincome <- merge(fasfaincome, zipcode, by.x="Zip", by.y="zip")
> fasfaincome
       Zip Year                                                                 School School.Type Dependent.Students Independent.Students Applicants NumberReturns        AGI    AvgIncome
1    01002 2009                                                        Amherst College     Private               4638                  177       7679          9889  404902740 4.094476e+04
2    01002 2009                                                      Hampshire College     Private               2412                  110       2515          9889  404902740 4.094476e+04
3    01002 2011                                                      Hampshire College     Private               2533                  234       2517         10168     414201 4.073574e+04
4    01002 2011                                                        Amherst College     Private               5140                  413       8460         10168     414201 4.073574e+04
5    01002 2012                                                      Hampshire College     Private               2926                  253       2861          9460     732849 7.746818e+04
         PctFasfa                 city state latitude  longitude
1    6.039849e+01              Amherst    MA 42.37765  -72.50323
2    9.590457e+01              Amherst    MA 42.37765  -72.50323
3    1.006357e+02              Amherst    MA 42.37765  -72.50323
4    6.075650e+01              Amherst    MA 42.37765  -72.50323
5    1.022719e+02              Amherst    MA 42.37765  -72.50323
 [ reached getOption("max.print") -- omitted 6644 rows ]
```

