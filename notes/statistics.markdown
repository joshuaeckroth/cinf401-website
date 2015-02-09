---
layout: post
title: Statistics
---

# Statistics

*Note: See the corresponding [cookbook about statistics](/cookbook/statistics.html). This page has lecture notes.*

## Descriptive statistics

The usual suspects: mean, median.

### Variance

The "spread" of the samples or population. A low variance means the values are grouped very closely together; they will be very close to the mean.

<p>
Variance is defined in two ways:
$$\sigma^2 = \frac{\sum_i (x_i-\mu)^2}{n} \qquad \textrm{(population)},$$
$$s^2 = \frac{\sum_i (x_i-\bar{x})^2}{n-1} \qquad \textrm{(sample)},$$
where $\mu$ is the population mean, $\bar{x}$ is the sample mean (calculated the same way as population mean), $x_i$ represents a data point, and $n$ is the number of $x_i$'s.
</p>

<p>
In other words, $\sigma^2$ is the average of the squared distance between each datum and the mean. We use the square to ensure the difference is positive (so differences above/below don't cancel out), and we don't use absolute value, generally, because it's not differentiable. (We like differentiable functions, generally, so we can find their min/max using calculus...)
</p>

<p>
The sample variance, $s^2$, differs from $\sigma^2$ only by dividing by $n-1$ rather than $n$. This makes $s^2 > \sigma^2$ assuming $\bar{x}=\mu$. This is done to handle bias in the measure.
</p>

#### Example

Recall the `InsectSprays` plot:

![Insect sprays boxplot](/images/plot-insectsprays-boxplot.png)

These are the variances of the counts of insects killed for the different sprays:

| Spray | Variance |
|-------|----------------|
| A | 22.27 |
| B | 18.24 |
| C | 3.90 |
| D | 6.27 |
| E | 3.00 |
| F | 38.61 |

These numbers should match your intuitions after looking at the plot. They tell us that sprays C and E are most "reliable", though they may not always perform best (kill most insects).

### Standard deviation

This is simply the square-root of the variance, i.e., $\sigma$ (population) or $s$ (sample).

### Correlation

When we have two variables changing (we might be controlling one and measuring the other), we often want to measure how correlated they are. Positive correlation occurs when both variables increase/decrease together, and inverse correlation occurs when they move in opposite directions of each other. A correlation is always between -1 and 1, with 0 meaning absolutely no correlation.

<p>
Correlation is defined as,
$$
r_{XY} = \frac{\sum_i (x_i-\bar{x})(y_i-\bar{y})}{(n-1)s_X s_Y},
$$
where $s_X, s_Y$ are standard deviations of the $X$ or $Y$ dataset (respectively), and all other variables have the same definitions as above (in the definition of variance). Note $n$ must be the same for both sets of values; we're finding the correlation by pairing the values, after all.
</p>

When makes a correlation "high?" There are some [rules of thumb](http://www.strath.ac.uk/aer/materials/4dataanalysisineducationalresearch/unit4/correlationsdirectionandstrength/) but no exact answers. Maybe don't bet your life on a <0.5 correlation.

From [Wikipedia](http://en.wikipedia.org/wiki/Correlation_and_dependence), here are some correlation examples. All the examples on the third row are symmetric, or nearly so, on the x-axis so the correlation is 0 (x does not seem to consistently impact y). Bear in mind that strong correlation does not always imply linearity, as the Wikipedia page notes.

![Correlation examples](/images/correlation-examples.png)

#### Example

Consider ozone versus temperature in New York City:

![Ozone vs. temperature](/images/assignment-3-task-1.png)

This is from the `airquality` dataset:

{% highlight r %}
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

We can measure the correlation between ozone and temperature as follows:

{% highlight r %}
> cor(airquality$Ozone, airquality$Temp, use="complete.obs")
[1] 0.6983603
{% endhighlight %}

The `use="complete.obs"` parameter means to drop rows where `NA` is found in one or both of the columns (see row 5 of ozone, above).

The correlation was about 0.70, which is positive, indicating positive correlation (both values move together, rather than inversely). It is also a moderately high correlation, suggesting that ozone does have some effect on temperature.

#### Correlation vs. causation?

It must be said, correlation does not equal causation. Even with very high correlation between, say, number of kids eating popsicles and number of kids swimming, neither causes the other (rather, temperature probably causes both). There is no way to make claims about causation without having some theory or model of how the system works. Numbers like correlation can't tell you about causation.

### Covariation

<p>
As our R book says (p. 199), covariance is like a measure of variance between variables. It is very similar to correlation but not scaled by the standard deviations. Thus, its formula is,
$$
cov(X,Y) = \frac{\sum_i (x_i-\bar{x})(y_i-\bar{y})}{n-1}.
$$
</p>

The covariance is not between -1 and 1 (usually), because it's not scaled like correlation is. The units are the units of $X$ times the units of $Y$.

<p>
Note, $cov(X,X)$ is just the variance of $X$.
</p>

Sometimes covariance is more useful than correlation, particularly when the original magnitude is desired. (Recall that correlation is just a normalized covariance.)

#### Example

Again, with `airquality`:

{% highlight r %}
> cov(airquality$Ozone, airquality$Temp, use="complete.obs")
[1] 218.5212
{% endhighlight %}

### Correlation/covariance matrices

Sometimes we want to check the correlation/covariance of multiple variables against each other, all pairs, all at once. We can give multiple columns to `cor()` or `cov()` to do this.

{% highlight r %}
> cor(airquality[,c("Ozone","Temp","Wind")], use="complete.obs")
           Ozone       Temp       Wind
Ozone  1.0000000  0.6983603 -0.6015465
Temp   0.6983603  1.0000000 -0.5110750
Wind  -0.6015465 -0.5110750  1.0000000
{% endhighlight %}

Naturally, a variable is always in perfect correlation with itself, so we'll always have 1.0 correlation on the diagonals. We see, at a glance, that ozone and wind are inversely correlated, moderately so. Maybe the wind blows away the ozone?

Using the `GGally` library and `ggpairs` function, you can create a (somewhat crazy) plot of multiple variables vs. each other, all pairs. Note that `ggpairs` does not produce a `ggplot`, so you have to use an older R technique for saving plots (if you care to):

{% highlight r %}
> png(file="plot-ggpairs-airquality.png")
> ggpairs(airquality[,c("Ozone","Temp","Wind")])
Warning messages:
1: Removed 37 rows containing non-finite values (stat_density).
2: In ggally_cor(ggally_data, ggplot2::aes(x = Temp, y = Ozone)) :
  Removed 37 rows containing missing values
3: In ggally_cor(ggally_data, ggplot2::aes(x = Wind, y = Ozone)) :
  Removed 37 rows containing missing values
4: Removed 37 rows containing missing values (geom_point).
5: Removed 37 rows containing missing values (geom_point).
> dev.off()
quartz
     2
{% endhighlight %}

![Pairs plot of air quality](/images/plot-ggpairs-airquality.png)

The scatter plots show each variable vs. the other. In the diagonal we see density plots for that variable (there is no reason to show a scatter plot vs. itself). The top-right shows correlation values.

## Applying summary statistics to groups

Use `by()` to apply a function to a data frame column according to values from another column or columns.

Consider the `InsectSprays` data,

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

We can ask for the mean to be calculated per spray:

{% highlight r %}
> by(InsectSprays[,"count"], InsectSprays[,"spray"], mean)
InsectSprays[, "spray"]: A
[1] 14.5
------------------------------------------------------------
InsectSprays[, "spray"]: B
[1] 15.33333
------------------------------------------------------------
InsectSprays[, "spray"]: C
[1] 2.083333
------------------------------------------------------------
InsectSprays[, "spray"]: D
[1] 4.916667
------------------------------------------------------------
InsectSprays[, "spray"]: E
[1] 3.5
------------------------------------------------------------
InsectSprays[, "spray"]: F
[1] 16.66667
{% endhighlight %}

Sometimes, it is convenient to use `with()` as well so we can more easily refer to column names:

{% highlight r %}
> with(InsectSprays, by(count, spray, mean))

... (same output as above)
{% endhighlight %}

Here is an easy way to get simple summary statistics for each spray:

{% highlight r %}
> with(InsectSprays, by(count, spray, summary))
spray: A
   Min. 1st Qu.  Median    Mean 3rd Qu.    Max.
   7.00   11.50   14.00   14.50   17.75   23.00
------------------------------------------------------------
spray: B
   Min. 1st Qu.  Median    Mean 3rd Qu.    Max.
   7.00   12.50   16.50   15.33   17.50   21.00
------------------------------------------------------------
spray: C
   Min. 1st Qu.  Median    Mean 3rd Qu.    Max.
  0.000   1.000   1.500   2.083   3.000   7.000
------------------------------------------------------------
spray: D
   Min. 1st Qu.  Median    Mean 3rd Qu.    Max.
  2.000   3.750   5.000   4.917   5.000  12.000
------------------------------------------------------------
spray: E
   Min. 1st Qu.  Median    Mean 3rd Qu.    Max.
   1.00    2.75    3.00    3.50    5.00    6.00
------------------------------------------------------------
spray: F
   Min. 1st Qu.  Median    Mean 3rd Qu.    Max.
   9.00   12.50   15.00   16.67   22.50   26.00
{% endhighlight %}

Note running `summary(InsectSprays)` is nearly useless because it will find the stats for all counts of all sprays. That's not interesting because we know (from the plot, and above summaries) that the sprays perform quite differently.

## Outlier/anomaly detection

https://blog.twitter.com/2015/introducing-practical-and-robust-anomaly-detection-in-a-time-series
