---
layout: post
title: Assignment 3
due: "Feb 10, 11:59pm"
categories: [assignments]
---

# Assignment 3

## Task 1

Given this data frame,

{% highlight r %}
> library(datasets)
> head(airquality)
  Ozone Solar.R Wind Temp Month Day
1    41     190  7.4   67     5   1
2    36     118  8.0   72     5   2
3    12     149 12.6   74     5   3
4    18     313 11.5   62     5   4
5    NA      NA 14.3   56     5   5
6    28      NA 14.9   66     5   6
...
{% endhighlight %}

Produce this plot:

![Assignment 3, Task 1 plot](/images/assignment-3-task-1.png)

## Task 2

Given the data frame `d`, generated in the way shown,

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

Produce this plot:

![Assignment 3, Task 2 plot](/images/assignment-3-task-2.png)

## Task 3

Given this data frame,

{% highlight r %}
> library(datasets)
> head(mpg)
  manufacturer model displ year cyl      trans drv cty hwy fl   class
1         audi    a4   1.8 1999   4   auto(l5)   f  18  29  p compact
2         audi    a4   1.8 1999   4 manual(m5)   f  21  29  p compact
3         audi    a4   2.0 2008   4 manual(m6)   f  20  31  p compact
4         audi    a4   2.0 2008   4   auto(av)   f  21  30  p compact
5         audi    a4   2.8 1999   6   auto(l5)   f  16  26  p compact
6         audi    a4   2.8 1999   6 manual(m5)   f  18  26  p compact
...
{% endhighlight %}

Produce this plot:

![Assignment 3, Task 3 plot](/images/assignment-3-task-3.png)

Hints:

- I used `aggregate` and `reorder`. See this [blog post](https://kohske.wordpress.com/2010/12/29/faq-how-to-order-the-factor-variables-in-ggplot2/) about reordering factors to ensure the manufacturers in the plot are listed in order of highest hwy mpg first.
- I used `melt` after `aggregate`.

