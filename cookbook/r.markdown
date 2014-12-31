---
layout: post
title: "Cookbook: R"
---

# Cookbook: R

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

## Reading files

```R
read.csv("foo.csv", strip.white=TRUE)
```

## Clean up ZIP codes

Chop of +4 portion:

```R


## Convert a date to season

From [Josh O'Brien on StackOverflow](http://stackoverflow.com/a/9501225)

```R
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

my.dates <- as.Date("2011-12-01", format = "%Y-%m-%d") + 0:60
head(getSeason(my.dates), 24)
#  [1] "Fall"   "Fall"   "Fall"   "Fall"   "Fall"   "Fall"   "Fall"
#  [8] "Fall"   "Fall"   "Fall"   "Fall"   "Fall"   "Fall"   "Fall"
# [15] "Winter" "Winter" "Winter" "Winter" "Winter" "Winter"
```




