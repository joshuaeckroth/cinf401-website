---
layout: post
title: Assignment 2
due: "Feb 3, 11:59pm"
categories: [assignments]
---

# Assignment 2

## Task 1

Grab the first table of [Foreign Terrorist Organizations](http://www.state.gov/j/ct/rls/other/des/123085.htm) (the one called "Designated Foreign Terrorist Organizations") entirely using R. The R book, p. 80 has an example using the `XML` library and `readHTMLTable` function.

Produce a data frame sorted by terrorist name, i.e., the following:

{% highlight r %}
  Date Designated                           Name
49       5/30/2012  Abdallah Azzam Brigades (AAB)
2        10/8/1997   Abu Nidal Organization (ANO)
3        10/8/1997         Abu Sayyaf Group (ASG)
27       3/27/2002 Al-Aqsa Martyrs Brigade (AAMB)
54      12/19/2013        al-Mulathamun Battalion
59       5/15/2014                al-Nusrah Front
...
{% endhighlight %}

Produce another data frame that shows the number of terrorist organizations that began in each year. I.e.,

{% highlight r %}
   Year Name
1  1997   20
2  1999    1
3  2000    1
4  2001    3
5  2002    5
6  2003    1
7  2004    4
8  2005    1
9  2008    2
10 2009    2
11 2010    4
12 2011    2
13 2012    3
14 2013    4
15 2014    6
{% endhighlight %}

**Note:** For full credit, you must not manually modify any of the data. Use only R functions/features to manipulate the data. You should never type "2007", or "al-Nusrah Front", for example. You are allowed to rewrite the column names using `colnames()`.

## Task 2

Go to the [IPEDS site](http://nces.ed.gov/ipeds/datacenter/Default.aspx) and follow the directions on the [data sources cookbook](/cookbook/data-sources.html), in the IPEDS section, to grab CSV datasets with the following qualities:

- Final release data
- US Institutions (should be 7597 of them)
- Variables: 2010-2013, graduation rate with Bachelor degree within 4 years, total (all students)

You'll get four separate ZIP files, each with a CSV file inside. Combine these all in R, and rename the last column, so that you have a data frame that looks like this:

{% highlight r %}
> head(d)
  unitid                    institution.name year Rate
1 100654            Alabama A & M University 2010   12
2 100663 University of Alabama at Birmingham 2010   19
3 100690                  Amridge University 2010   23
4 100706 University of Alabama in Huntsville 2010   13
5 100724            Alabama State University 2010    7
6 100733 University of Alabama System Office 2010   NA
...
{% endhighlight %}

Running `summary` on the data frame will summarize each column (mean, max, min, etc.). Do this so we can be sure you've combined all the data (see how all the years are represented):

{% highlight r %}
> summary(d)
     unitid                                     institution.name      year
 Min.   :100654   Bryan University                      :   20   Min.   :2010
 1st Qu.:169655   Columbia College                      :   20   1st Qu.:2011
 Median :221607   Academy of Cosmetology                :   16   Median :2012
 Mean   :283812   California College San Diego          :   12   Mean   :2012
 3rd Qu.:442064   Cannella School of Hair Design-Chicago:   12   3rd Qu.:2012
 Max.   :483212   Capri Beauty College                  :   12   Max.   :2013
                  (Other)                               :30296
      Rate
 Min.   :  0.00
 1st Qu.: 14.00
 Median : 29.00
 Mean   : 32.48
 3rd Qu.: 49.00
 Max.   :100.00
 NA's   :21903
{% endhighlight %}

Now, make a data frame with the average 4-year graduation rate, across 2010-2013, for each school:

{% highlight r %}
> head(davg)
              institution.name  Rate
1 Abilene Christian University 38.25
2              Academy College 81.25
3    Academy of Art University  4.75
4       Academy of Couture Art 50.00
5       Adams State University 10.00
6           Adelphi University 55.00
...
{% endhighlight %}

Next show Steston's rate. It should be 54.75 :(

## Task 3

Formulate an interesting question. State this question and why it's interesting (2-3 sentences altogether).

Find your own **two** data sets, **from different sources**, that may help you answer this question, and merge them with `merge`. Then create a summary data frame (using `melt/dcast` and/or `aggregate`). This summary should lead to an answer to your question.

Convince me you have answered your question. If you cannot answer your question, formulate a new question and possibly find new data. Try to determine if you'll be able to answer it before you dive too deeply in useless data munging.
