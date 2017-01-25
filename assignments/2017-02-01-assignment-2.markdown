---
layout: post
title: Assignment 2
due: "Wed Feb 1"
categories: [assignments]
---

# Assignment 2

Write all of your code in an RMarkdown file. Show every step of your process.

## Task 1

Grab the first table of [Foreign Terrorist Organizations](http://www.state.gov/j/ct/rls/other/des/123085.htm) (the one called "Designated Foreign Terrorist Organizations") **entirely using R** (do not copy-paste the content or download ahead of time). Consider using the `readHTMLTable` function in the `XML` library in R. Note that `readHTMLTable` cannot open `https:` connections, so you'll need to do some googling to figure this out.

Produce a data frame sorted by organization name, i.e., the following:

{% highlight r %}
   DateDesignated                           Name
48      5/30/2012  Abdallah Azzam Brigades (AAB)
3       10/8/1997   Abu Nidal Organization (ANO)
4       10/8/1997         Abu Sayyaf Group (ASG)
27      3/27/2002 Al-Aqsa Martyrs Brigade (AAMB)
53     12/19/2013        al-Mulathamun Battalion
58      5/15/2014                al-Nusrah Front
...
{% endhighlight %}

Produce another data frame that shows the number of terrorist organizations that began in each year. Use the same column names shown below, and ensure it is ordered by year.

{% highlight r %}
   Year Count
1  1997    19
2  1999     1
3  2000     1
4  2001     3
5  2002     5
6  2003     1
7  2004     3
8  2005     1
9  2008     2
10 2009     2
11 2010     4
12 2011     2
13 2012     3
14 2013     4
15 2014     6
16 2015     1
17 2016     3
{% endhighlight %}

**Note:** For full credit, you must not manually modify any of the data. Use only R functions/features to manipulate the data. You should never type "2007", or "al-Nusrah Front", for example. You are allowed to rewrite the column names using `colnames()`. And as stated above, for full credit you must download the HTML page from R itself, and not save any intermediate CSV files.

## Task 2

Go to the [IPEDS site](http://nces.ed.gov/ipeds/datacenter/Default.aspx) and follow the directions on the [data sources cookbook](/cookbook/data-sources.html), in the IPEDS section, to grab CSV datasets with the following qualities:

- Final release data
- US Institutions (should be about 7500 of them)
- Variables: Graduation rate with Bachelor degree within 4 years, total (all students), all years

You'll get a CSV file with several columns (institution fields, plus a column for each year). Cast/melt/merge/munge until you have a data frame that looks like this, with the exact column names shown (the rows may appear in a different order, that's no problem):

{% highlight r %}
   UnitID                                    InstitutionName Year Rate
1  457590                         A & W Healthcare Educators 2014   NA
2  177834            A T Still University of Health Sciences 2014   NA
3  180203                             Aaniiih Nakoda College 2014   NA
4  161615                          Aaron's Academy of Beauty 2014   NA
5  459523                                 ABC Beauty Academy 2014   NA
6  106281                             ABC Beauty College Inc 2014   NA
7  485500                                    ABCO Technology 2014   NA
8  461892                                   Abcott Institute 2014   NA
9  208123                          Abdill Career College Inc 2014   NA
10 222178                       Abilene Christian University 2014   45
11 210456 Abington Memorial Hospital Dixon School of Nursing 2014   NA
12 138558               Abraham Baldwin Agricultural College 2014    3
13 172866                                    Academy College 2014    0
14 449463           Academy Di Capelli-School of Cosmetology 2014   NA
15 476957                                 Academy di Firenze 2014   NA
...
{% endhighlight %}

Running `summary` on the data frame will summarize each column (mean, max, min, etc.). Do this so we can be sure you've combined all the data (see how all the years are represented):

{% highlight r %}
     UnitID                                      InstitutionName     Year            Rate       
 Min.   :100654   Stevens-Henager College               :   77   2014   : 7481   Min.   :  0.00  
 1st Qu.:170587   Bryan University                      :   55   2013   : 7481   1st Qu.: 15.00  
 Median :223083   Columbia College                      :   55   2012   : 7481   Median : 30.00  
 Mean   :287506   Academy of Cosmetology                :   44   2011   : 7481   Mean   : 33.22  
 3rd Qu.:444723   All Beauty College                    :   33   2010   : 7481   3rd Qu.: 49.00  
 Max.   :487728   Cannella School of Hair Design-Chicago:   33   2009   : 7481   Max.   :100.00  
                  (Other)                               :81994   (Other):37405   NA's   :60939   
{% endhighlight %}

Now, make a data frame with the average 4-year graduation rate, across all years, for each school:

{% highlight r %}
  UnitID                      InstitutionName      Rate
1 222178         Abilene Christian University 37.454545
2 138558 Abraham Baldwin Agricultural College  3.000000
3 172866                      Academy College 60.714286
4 108232            Academy of Art University  4.909091
5 475635               Academy of Couture Art 75.000000
6 126182               Adams State University 12.818182
...
{% endhighlight %}

Next show Stetson's individual year rates:

{% highlight r %}
      UnitID    InstitutionName Year Rate
6042  137546 Stetson University 2014   56
13523 137546 Stetson University 2013   56
21004 137546 Stetson University 2012   56
28485 137546 Stetson University 2011   56
35966 137546 Stetson University 2010   51
43447 137546 Stetson University 2009   50
50928 137546 Stetson University 2008   55
58409 137546 Stetson University 2007   55
65890 137546 Stetson University 2006   53
73371 137546 Stetson University 2005   57
80852 137546 Stetson University 2004   52
{% endhighlight %}

Finally, show Steston's average rate. It should be 54.27 :(

## Task 3

Formulate an interesting question. State this question and why it's interesting (2-3 sentences altogether). (Note: each person should have a different question and probably use different data.)

Find your own **two** data sets, **from different sources**, that may help you answer this question, and merge them with `merge`. Then create a summary data frame (using `melt/dcast` and/or `aggregate`). This summary should lead to an answer to your question.

Convince me you have answered your question. If you cannot answer your question, formulate a new question and possibly find new data. Try to determine if you'll be able to answer it before you dive too deeply in useless data munging.

