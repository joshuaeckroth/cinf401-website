---
layout: post
title: "Cookbook: ggplot"
---

# Cookbook: ggplot

*Note: See the corresponding [lecture notes about ggplot](/notes/ggplot.html). This page has cookbook recipes.*

## Common plots

### Points

### Lines

### Histograms

### Density plots

### Bar charts

Given data frame,

{% highlight r %}
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

### Box-and-whisker plots

(somewhat discouraged; hard to interpret for many)

## Axes

### Axis labels

### Axis marks

### Axis limits

cut off data, or cut off axis

## Legends

### Legend placement

### Hiding the legend

## Facets

### Facet labels