---
layout: post
title: "Cookbook: ggplot"
---

# Cookbook: ggplot

*Note: See the corresponding [lecture notes about ggplot](/notes/ggplot.html). This page has cookbook recipes.*

Also see the R book, pp. 86-98.

## Common plots

### Points

Given this dataset,

{% highlight r %}
> head(faithful)
  eruptions waiting
1     3.600      79
2     1.800      54
3     3.333      74
4     2.283      62
5     4.533      85
6     2.883      55
{% endhighlight %}

Described as follows (from `?faithful`),

> Waiting time between eruptions and the duration of the eruption for the Old Faithful geyser in Yellowstone National Park, Wyoming, USA. `eruptions` is eruption time in minutes, `waiting` is waiting time to next eruption

Produce,

![Faithful plot](/images/plot-faithful-points.png)

Technique:

{% highlight r %}
> p <- ggplot(faithful) +
       geom_point(aes(x=waiting, y=eruptions)) +
       scale_x_continuous("Waiting time until next eruption (min)") +
       scale_y_continuous("Eruption duration (min)")
> ggsave("plot-faithful-points.png", p, width=6, height=4, dpi=100)
{% endhighlight %}

### Lines

Given this dataset,

{% highlight r %}
> head(Loblolly)
   height age Seed
1    4.51   3  301
15  10.89   5  301
29  28.72  10  301
43  41.74  15  301
57  52.70  20  301
71  60.92  25  301
{% endhighlight %}

Produce,

![Loblolly plot](/images/plot-loblolly-lines.png)

Technique:

{% highlight r %}
> p <- ggplot(Loblolly) +
       geom_line(aes(x=age, y=height, color=Seed)) +
       scale_x_continuous("Age (years)") +
       scale_y_continuous("Height (feet)")
> ggsave("plot-loblolly-lines.png", p, width=6, height=4, dpi=100)
{% endhighlight %}

### Histograms

Given this dataset,

{% highlight r %}
> head(movies[,c("title", "year", "length", "rating", "votes")])
                     title year length rating votes
1                        $ 1971    121    6.4   348
2        $1000 a Touchdown 1939     71    6.0    20
3   $21 a Day Once a Month 1941      7    8.2     5
4                  $40,000 1996     70    8.2     6
5 $50,000 Climax Show, The 1975     71    3.4    17
6                    $pent 2000     91    4.3    45
{% endhighlight %}

Produce,

![Movie ratings plot](/images/plot-movies-histogram.png)

Technique:

{% highlight r %}
> p <- ggplot(movies) +
       geom_histogram(aes(x=rating)) +
       scale_x_continuous("Avg. rating (0-10)") +
       scale_y_continuous("Count of movies with rating")
> ggsave("plot-movies-histogram.png", p, width=6, height=4, dpi=100)
stat_bin: binwidth defaulted to range/30. Use 'binwidth = x' to adjust this.
{% endhighlight %}

### Density

Given this dataset,

{% highlight r %}
> head(InsectSprays)
  count spray
1    10     A
2     7     A
3    20     A
4    14     A
5    14     A
6    12     A
{% endhighlight %}

Produce,

![Insect sprays densities](/images/plot-insectsprays-density.png)

Technique:

{% highlight r %}
> p <- ggplot(InsectSprays) +
       geom_density(aes(x=count)) +
       facet_grid(. ~ spray, labeller=label_both) +
       scale_x_continuous("Insects killed") +
       scale_y_continuous("Density")
> ggsave("plot-insectsprays-density.png", p, width=8, height=3, dpi=100)
{% endhighlight %}

- `facet_grid(. ~ spray, labeller=label_both)` creates the horizontal faceting on the `spray` column values. There is no vertical faceting because `.` was used in the `vert ~ horz` formula. (See faceting notes below.) The facet labels have both the variable name and value because I set the labeller to `label_both`.

### Box-and-whisker plots

Box-and-whisker plots show:

- the "min" and "max" (the whiskers), which are not always the true min/max (see below)
- the lower 25% quartile (bottom of box)
- the upper 75% quartile (top of box)
- the median (middle line in box)

A quartile is a quarter of the data, after sorting. So the 25% quartile is the number at the 25% position of the sorted data.

Sometimes, extra dots are shown beyond the whiskers to indicate values that fall outside the range `median - 1.5*IQR` or `median + 1.5*IQR` where `IQR` is the interquartile range, or the value calculated by subtracting the 25% quartile value from the 75% quartile value (75% val - 25% val).

They are hard to interpret for many people, so I discourage their use.

Given the dataset,

{% highlight r %}
> head(InsectSprays)
  count spray
1    10     A
2     7     A
3    20     A
4    14     A
5    14     A
6    12     A
{% endhighlight %}

Produce,

![Insect sprays boxplot](/images/plot-insectsprays-boxplot.png)

Technique:

{% highlight r %}
> p <- ggplot(InsectSprays) +
       geom_boxplot(aes(x=factor(spray), y=count)) +
       scale_x_discrete("Spray") +
       scale_y_continuous("Insects killed")
> ggsave("plot-insectsprays-boxplot.png", p, width=8, height=3, dpi=100)
{% endhighlight %}

### Bar charts

Given this dataset,

{% highlight r %}
> library(datasets)
> d <- as.data.frame(USPersonalExpenditure)
> d <- cbind(Expense=rownames(d), d)
> rownames(d) <- NULL
> d
              Expense   1940   1945  1950 1955  1960
1    Food and Tobacco 22.200 44.500 59.60 73.2 86.80
2 Household Operation 10.500 15.500 29.00 36.5 46.20
3  Medical and Health  3.530  5.760  9.71 14.0 21.10
4       Personal Care  1.040  1.980  2.45  3.4  5.40
5   Private Education  0.341  0.974  1.80  2.6  3.64
{% endhighlight %}

Produce,

![Expenses plot](/images/plot-expenses-bars.png)

Technique:

{% highlight r %}
> dmelt <- melt(d, c("Expense"))
> p <- ggplot(dmelt) +
       geom_bar(aes(x=variable, y=value, fill=Expense), stat="identity", position="dodge") +
       scale_x_discrete("Year") +
       scale_y_continuous("Expense") +
       labs(fill="Expense Type")
> ggsave("plot-expenses-bars.png", p, width=6, height=4, dpi=100)
{% endhighlight %}

- `stat="identity"` means use the values from the data (the expense values) for the bar heights

- `position="dodge"` means to place the bars next to each other instead of on top of each other

- `labs(fill="Expense Type")` means to rename the legend label for the fill colors

## Axes

See the [Cookbook for R](http://www.cookbook-r.com/Graphs/Axes_\(ggplot2\)/).

## Legends

See the [Cookbook for R](http://www.cookbook-r.com/Graphs/Legends_\(ggplot2\)/).

## Facets

Facets are horizontal/vertical grids of subplots. You can create facets based on one or two columns. Each subplot shows only those data that have the facet cell's particular value in the faceted columns.

Facets are created with the following code (to be added to a plot with `+`):

{% highlight r %}
facet_grid(vert ~ horiz)
{% endhighlight %}

`vert` and `horiz` should be column names. You can use a `.` in either `vert` or `horiz` place to indicate no vertical or no horizontal faceting.

Many examples may be found in the [Cookbook for R](http://www.cookbook-r.com/Graphs/Facets_\(ggplot2\)/).

### Facet labels

By default, the facet labels only show the variables' values. If you want to show the variable name as well, use the `label_both` labeller:

{% highlight r %}
facet_grid(vert ~ horiz, labeller = label_both)
{% endhighlight %}
