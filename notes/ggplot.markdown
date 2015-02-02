---
layout: post
title: ggplot
---

# ggplot

*Note: See the corresponding [cookbook about ggplot](/cookbook/ggplot.html). This page has lecture notes.*

The "gg" of ggplot stands for "grammar of graphics." The ggplot library provides a set of functions that may be added together to produce a plot. This plot may be shown on the screen or saved to a PDF or PNG file.

Every plot starts with the `ggplot()` function. Then, you "add" graphics to it (using `+`), such as `geom_line()` or `geom_point()`. The examples below and in the cookbook illustrate this point.

The [ggplot docs](http://docs.ggplot2.org/current/) might prove useful to you, as will the R book, pp. 86-98, and the [Cookbook for R](http://www.cookbook-r.com/Graphs/).

## Aesthetics

As part of this "grammar of graphics" concept, visual features of each subplot are defined by its "aesthetics", specified with `aes()`. The aesthetics include the values along the x-axis, y-axis, line colors, line types, fill colors, point shapes and sizes, etc. See the examples below and in the cookbook.

## Basic pattern of use

Typically, I use ggplot like this:

{% highlight r %}
library(ggplot2)
d <- data.frame(col1 = ..., col2 = ...)    # make a data frame
p <- ggplot(d)                             # start a plot, use data from d
p <- p + geom_point(aes(x=col1, y=col2))   # add a plot of points; col1, col2 refers to d
p <- p + geom_line(aes(x=col3, y=col4))    # add a plot of a line on top of the points
p                                          # view the result
ggsave("myplot.png", p, width=4, height=3) # save the result
{% endhighlight %}

If I want to be quick, and not save the result, I can just run `ggplot()` plus whatever `geom_*` I want and it will show on the screen:

{% highlight r %}
ggplot(d) + geom_point(aes(x=col1, y=col2))
{% endhighlight %}

## Data shape

**Important:** The values for any single aesthetic (x values, y values, fill color, line color, etc.) must come from a single column in the data frame.

If your values come from different columns (or the values are column names), you'll need to melt (and possibly cast) the data frame first.

Sometimes you want to set a color, shape, or facets based on some column. If that column is not already a "factor", you may need to cast it as one first, using `factor(col)` for the column name instead of just `col`.

### Example

Consider this data frame:

{% highlight r %}
> d
              Expense   1940   1945  1950 1955  1960
1    Food and Tobacco 22.200 44.500 59.60 73.2 86.80
2 Household Operation 10.500 15.500 29.00 36.5 46.20
3  Medical and Health  3.530  5.760  9.71 14.0 21.10
4       Personal Care  1.040  1.980  2.45  3.4  5.40
5   Private Education  0.341  0.974  1.80  2.6  3.64
{% endhighlight %}

Suppose I want this graph:

![Expenses plot](/images/plot-expenses-lines.png)

That's not (easily) possible with ggplot because the x-axis values may only come from a single column. Thus, we need to melt the data frame first:

{% highlight r %}
> dmelt <- melt(d, c("Expense"))
> dmelt
               Expense variable  value
1     Food and Tobacco     1940 22.200
2  Household Operation     1940 10.500
3   Medical and Health     1940  3.530
4        Personal Care     1940  1.040
5    Private Education     1940  0.341
6     Food and Tobacco     1945 44.500
7  Household Operation     1945 15.500
8   Medical and Health     1945  5.760
9        Personal Care     1945  1.980
10   Private Education     1945  0.974
11    Food and Tobacco     1950 59.600
12 Household Operation     1950 29.000
13  Medical and Health     1950  9.710
14       Personal Care     1950  2.450
15   Private Education     1950  1.800
16    Food and Tobacco     1955 73.200
17 Household Operation     1955 36.500
18  Medical and Health     1955 14.000
19       Personal Care     1955  3.400
20   Private Education     1955  2.600
21    Food and Tobacco     1960 86.800
22 Household Operation     1960 46.200
23  Medical and Health     1960 21.100
24       Personal Care     1960  5.400
25   Private Education     1960  3.640
{% endhighlight %}

Now, let's try to create this plot:

{% highlight r %}
> ggplot(dmelt) + geom_line(aes(x=variable, y=value, color=Expense))
geom_path: Each group consist of only one observation. Do you need to adjust the group aesthetic?
{% endhighlight %}

It turns out our `dmelt$variable` column is a factor, not numeric. So our x-axis is not acting as a numeric axis, so we can't draw a line across it.

We first verify this is the problem:

{% highlight r %}
> class(dmelt$variable)
[1] "factor"
{% endhighlight %}

Indeed, it is a factor. Let's convert it to a numeric. We need to use `as.numeric(as.character(...))` because we want to convert the character version of each value into a number. If we just convert each value into a number, it will use the factor index positions (1 through n).

{% highlight r %}
> dmelt$variable <- as.numeric(as.character(dmelt$variable))
{% endhighlight %}

Check our work:

{% highlight r %}
> class(dmelt$variable)
[1] "numeric"
{% endhighlight %}

Now this command gives us our plot:

{% highlight r %}
> ggplot(dmelt) + geom_line(aes(x=variable, y=value, color=Expense))
{% endhighlight %}
