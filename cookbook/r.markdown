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



