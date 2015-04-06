---
layout: post
title: "Cookbook: R"
---

# Cookbook: R

*Note: See the corresponding [lecture notes about R](/notes/r.html). This page has cookbook recipes.*

## External resources

- Someone else's [Cookbook for R](http://www.cookbook-r.com/)

## Avoiding console spamming, setting console width

```R
> getOption("max.print")
[1] 99999
> options(max.print = 100)
> getOption("max.print")
[1] 100
```

```R
> getOption("width")
[1] 80
> options(width = 120)
> getOption("width")
[1] 120
```

## Running examples from documentation

Most functions have examples at the bottom when you read their help pages (e.g., `?prop.test`). You can run these examples with the `example()` function:

{% highlight r %}
> example(prop.test)

prp.ts> heads <- rbinom(1, size = 100, prob = .5)

prp.ts> prop.test(heads, 100)          # continuity correction TRUE by default

	1-sample proportions test with continuity correction

data:  heads out of 100, null probability 0.5
X-squared = 1.21, df = 1, p-value = 0.2713
alternative hypothesis: true p is not equal to 0.5
95 percent confidence interval:
 0.4573588 0.6579781
sample estimates:
   p
0.56


prp.ts> prop.test(heads, 100, correct = FALSE)

	1-sample proportions test without continuity correction

data:  heads out of 100, null probability 0.5
X-squared = 1.44, df = 1, p-value = 0.2301
alternative hypothesis: true p is not equal to 0.5
95 percent confidence interval:
 0.4622810 0.6532797
sample estimates:
   p
0.56


prp.ts> ## Data from Fleiss (1981), p. 139.
prp.ts> ## H0: The null hypothesis is that the four populations from which
prp.ts> ##     the patients were drawn have the same true proportion of smokers.
prp.ts> ## A:  The alternative is that this proportion is different in at
prp.ts> ##     least one of the populations.
prp.ts>
prp.ts> smokers  <- c( 83, 90, 129, 70 )

prp.ts> patients <- c( 86, 93, 136, 82 )

prp.ts> prop.test(smokers, patients)

	4-sample test for equality of proportions without continuity
	correction

data:  smokers out of patients
X-squared = 12.6004, df = 3, p-value = 0.005585
alternative hypothesis: two.sided
sample estimates:
   prop 1    prop 2    prop 3    prop 4
0.9651163 0.9677419 0.9485294 0.8536585
{% endhighlight %}

## Reading files

### CSV files

```R
read.csv("foo.csv", strip.white=TRUE)
```

## Clean up ZIP codes

Chop off +4 portion:

{% highlight R %}
> require(stringr)
> d$Zip <- str_sub(d$Zip.Code, start=0, end=5)
{% endhighlight %}

## Convert a date to season

From [Josh O'Brien on StackOverflow](http://stackoverflow.com/a/9501225)

{% highlight R %}
getSeason <- function(DATES) {
    WS <- as.Date("2012-12-15", format = "%Y-%m-%d") # Winter Solstice
    SE <- as.Date("2012-3-15",  format = "%Y-%m-%d") # Spring Equinox
    SS <- as.Date("2012-6-15",  format = "%Y-%m-%d") # Summer Solstice
    FE <- as.Date("2012-9-15",  format = "%Y-%m-%d") # Fall Equinox

    # Convert dates from any year to 2012 dates
    d <- as.Date(strftime(DATES, format="2012-%m-%d"))

    ifelse (d >= WS | d < SE, "Winter",
      ifelse (d >= SE & d < SS, "Spring",
        ifelse (d >= SS & d < FE, "Summer", "Fall")))
}
{% endhighlight %}

```R
my.dates <- as.Date("2011-12-01", format = "%Y-%m-%d") + 0:60
head(getSeason(my.dates), 24)
  [1] "Fall"   "Fall"   "Fall"   "Fall"   "Fall"   "Fall"   "Fall"
  [8] "Fall"   "Fall"   "Fall"   "Fall"   "Fall"   "Fall"   "Fall"
 [15] "Winter" "Winter" "Winter" "Winter" "Winter" "Winter"
```

## Convert a vector into "bins"

The `cut` function allows you to rewrite a vector of values (as you might find in a data frame column) into particular "bins". The example below takes a vector of values between 2 and 14 and returns a vector with each value replaced with the corresponding bin. You can set the bin limits ("breaks") to whatever you want. You can also create labels for the bins; be sure you create the right number of labels for all the bin intervals.

{% highlight r %}
> 2:14
 [1]  2  3  4  5  6  7  8  9 10 11 12 13 14
> cut(2:14, breaks=c(0, 5, 10, Inf), labels=c("0-5", "5-10", "10+"))
 [1] 0-5  0-5  0-5  0-5  5-10 5-10 5-10 5-10 5-10 10+  10+  10+  10+
Levels: 0-5 5-10 10+
{% endhighlight %}

## "Transpose" a data frame

E.g., turn `InsectSprays` into a form with A, B, C, ... columns. Contributed by Matt Samuels. Later updated to handle several `vid` columns.

{% highlight r %}
data.frame.wide <- function(x, vid, vmeasure, long_col){
    x$id <- c(1:nrow(x))
    xmelt <- melt(x, cbind(c("id"), vid, c(long_col)), vmeasure)
    xc <- dcast(xmelt, as.formula(paste(paste(c("id", vid), collapse="+"), "~", long_col)))
    newcols <- as.vector(unique(x[[long_col]]))
    tmpd1 <- data.frame(xc[!is.na(xc[[newcols[1]]]),vid])
    colnames(tmpd1) <- vid
    return(cbind(tmpd1, lapply(newcols, function(c) { tmpd2 <- data.frame(na.omit(xc[[c]])); colnames(tmpd2) <- c(c); return(tmpd2); })))
}
{% endhighlight %}

Usage:

{% highlight r %}
> data.frame.wide(InsectSprays, c("spray"), c("count"), "spray")
    A  B C  D E  F
1  10 11 0  3 3 11
2   7 17 1  5 5  9
3  20 21 7 12 3 15
4  14 11 2  6 5 22
5  14 16 3  4 3 15
6  12 14 1  3 6 16
7  10 17 2  5 1 13
8  23 17 1  5 1 10
9  17 19 3  5 3 26
10 20 21 0  5 2 26
11 14  7 1  2 6 24
12 13 13 4  4 4 13
{% endhighlight %}

## Run Python code from R

Contributed by Christian Decker.

{% highlight r %}
install.packages("rPython")
library(rPython)
python.load("script.py") # loads and runs the script
python.get("x") # read a global variable from the script
d <- data.frame(python.get("varname")) # read a dict as a dataframe
python.exec("import math")
pi <- python.exec("math.pi")
{% endhighlight %}

You can also call R code from Python using the Python module [rpy2](http://rpy.sourceforge.net/).

## Load big datasets across lots of CSV files

Contributed by Matt Samuels.

{% highlight r %}
library(data.table)
library(bit64)​
path <- "/bigdata/data/backblaze/2014/"
fileList <- list.files(path=path, pattern="*.csv")
dt <- lapply(fileList, function(x) {tt <- fread(paste(path, x, sep=""), header=TRUE, sep=",")})
d <- rbindlist(dt)
{% endhighlight %}

## Shiny

Contributed by Michael Clay.

{% highlight r %}
library(ggplot2)
library(reshape2)

inputPanel(
  numericInput("rows", "Rows", 5)
)

renderTable({
  head(tips, input$rows)
})

inputPanel(
  selectInput("type", label="Column", choices=c("size", "tip", "day", "time", "sex", "smoker", "total_bill")),
  sliderInput("binwidth", label="Binwidth", min=0.2, max=2, value=1, step=0.2)
)

renderPlot({
  ggplot(tips) + geom_histogram(aes_string(x=input$type), binwidth=input$binwidth)
})

inputPanel(
  selectInput("type2", label="Column", choices=c("size", "tip"))
)

renderPlot({
  ggplot(tips) + geom_smooth(aes_string(x="total_bill", y=input$type2))
})
{% endhighlight %}

## Time/date manipulation

Contributed by Tom Wright.

{% highlight r %}
install.packages("chron")
library(chron)

dates(...)
times(...)
chron(...) # associate times with dates
leap.year(x)
day.of.week(x)
days(x)
months(x)
weekdays(x)
quarters(x)
years(x)
hours(x)
minutes(x)
seconds(x)
{% endhighlight %}

## Graph (network) manipulation

Contributed by John Salis.

{% highlight r %}
library(igraph)
g <- graph(c(1, 2, 2, 3, 3, 4, 2, 4))
plot(g)
g <- g + vertices(c(5, 6))
g[] # yields adjacency matrix
V(g) # vertices
E(g) # edges
g <- erdos.renyi.game(32, 0.2) # create a random graph
g$layout <- layout.circle # set the layout when plotting
g <- graph.lattice(c(16, 16)) # create a lattice graph
E(g)$weight <- runif(ecount(g), 1, 8) # give random weights to edges
plot(g, vertex.label=NA, edge.width=E(g)$weight) # plot a graph
{% endhighlight %}

## GUIs with Tcl/Tk

Contributed by George Robbins.

Use the `tcltk` library to show GUIs in R. [Here](http://thebiobucket.blogspot.com/2012/08/tcltk-gui-example-with-variable-input.html) is a good example, and [here](http://www.sciviews.org/_rgui/tcltk/) are lots more examples.

## Run queries against Google BigQuery

Contributed by Jacob Hell.

Google BigQuery is a SQL-like query system for huge datasets, stored on Google servers. Read more about [BigQuery](https://cloud.google.com/bigquery/what-is-bigquery).

Here are some resources:

- [The R package](https://github.com/hadley/bigrquery)
- [A tutorial](http://thinktostart.com/using-google-bigquery-with-r/)
- [A longer example for e-commerce](http://www.lunametrics.com/blog/2014/06/25/google-analytics-data-mining-bigquery-r/)
