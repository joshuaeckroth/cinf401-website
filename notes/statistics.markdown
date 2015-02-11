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

## Applying descriptive statistics to groups

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

## Statistical inference

Usually, we only have samples from a population, not the population itself. So our descriptive statistics (mean, correlation, etc.) are calculated with respect to the sample. However, the purpose of sampling from the population is to be able to make claims about the population, without measuring every member of the population. Statistical inference allows us to try to make such claims. Of course, we only have a sample, so we can't be certain our claims are true or false, but there are a variety of tools that help us estimate the probability our claims are true, given the sample and some assumptions about the population.

### Hypothesis testing

A simple kind of claim is something along the lines of, "what this sample shows is not due to chance, but due to a real phenomenon in the actual population." We can ask this about the sample mean ("this mean is close to the true population mean"), correlation ("these variables are actually positively correlated"), or differences in populations ("these two sets of samples came from very different populations," e.g., a population receiving a placebo and a population receiving a drug).

#### The "null" hypothesis

The tools for hypothesis testing only support two hypotheses. One we call the "null" hypothesis, often indicated by $h_0$, which always means "there is no difference in the populations" or "these variables are not correlated" or whatever the case. You want the null hypothesis to be proven wrong beyond a reasonable doubt.

You never have evidence to prove the null hypothesis is true. Rather, you can only *fail to reject* the null hypothesis. If, on the other hand, there is enough evidence the null hypothesis is false, you may *succeed in rejecting the null hypothesis*, and conclude that the two populations are in fact different, or the variables are positively correlated, or whatever.

#### p-values

Often, the output of a statistical test is a "significance" value known as the p-value. From [Wikipedia](http://en.wikipedia.org/wiki/P-value),

> Before performing the test a threshold value is chosen, called the significance level of the test, traditionally 5% or 1% and denoted as $\alpha$. If the p-value is equal to or smaller than the significance level $\alpha$, it suggests that the observed data are inconsistent with the assumption that the null hypothesis is true, and thus that hypothesis must be rejected and the alternative hypothesis is accepted as true. When the p-value is calculated correctly, such a test is guaranteed to control the Type I error rate to be no greater than $\alpha$.

A Type I error is "incorrect rejection of a true null hypothesis (a false positive)" and a Type II error is "the failure to reject a false null hypothesis (a false negative)" (quoted from Wikipedia).

When a test tells us the p-value is <0.05 or, better, <0.01, we can confidently reject the null hypothesis. (Many moons ago, 0.05 or 0.01 were deemed good enough thresholds to be confident.)

### Distributions

Before we can perform inference, we have to make assumptions about the distributions of our populations. There are many statistical distributions. The normal distribution is the most common. It looks like a bell curve. The mean and standard deviation determine how the curve looks. For example, the R dataset `nhtemp` (average yearly temperatures in New Haven, Connecticut, 1912-1971) appears to be a normal distribution:

![nhtemp data](/images/plot-nhtemp.png)

A bell curve is defined by the mean and standard deviation. The mean of nhtemp is 51.16, and the standard deviation is 1.27. Using these values, we can overlay a plot of the normal distribution:

![nhtemp data plus bell curve](/images/plot-nhtemp-bellcurve.png)

#### Tests for different population distributions

We can test how "normal" a sample is using the Shapiro test. The Shapiro test uses a null hypothesis stating "the distribution is normal", so if the p-value is low and we should reject the null hypothesis, then we can believe the distribution that the values were sample from is not normal. In that case, we would use one of a large number of other tests depending on the kind of distribution we think we have.

{% highlight r %}
> with(InsectSprays, shapiro.test(InsectSprays[spray=="A",]$count))

	Shapiro-Wilk normality test

data:  InsectSprays[spray == "A", ]$count
W = 0.9576, p-value = 0.7487
{% endhighlight %}

It looks like kill counts for insect spray A are normally distributed (since we can't reject this null hypothesis).

Here is a density plot for each spray:

![Insect spray densities](/images/plot-insectspray-densities.png)

Sprays A, B, and F look "normal", but D does not. The Shapiro test confirms: the null hypothesis (the distribution is normal) can be rejected for spray D with p-value 0.0027. Perhaps more trials with spray D would bring back into a normal distribution. I expect that the way the spray actually works (the population data, after an infinite number of trials) would be a normal distribution.

Tips, apparently, are not normally distributed:

{% highlight r %}
> shapiro.test(tips$tip)

	Shapiro-Wilk normality test

data:  tips$tip
W = 0.8978, p-value = 8.2e-12
{% endhighlight %}

Here is the tips density plot:

![Tips density](/images/plot-tips-density.png)

I suspect that the sole reason tips are not normally distributed is because they are cut off at $0 (can't give a negative tip).

### t-tests

A t-test compares two sample means and supports inferences about the population means.

> A t-test asks whether a difference between two groups' [means] is unlikely to have occurred because of random chance in sample selection. A difference is more likely to be meaningful and "real" if (1) the difference between the averages is large, (2) the sample size is large, and (3) responses are consistently close to the average values and not widely spread out (the standard deviation is low). -- [Statwing](http://docs.statwing.com/examples-and-definitions/t-test/)

#### Claims about a population's mean

A sample's mean may not be the true population mean. We can test whether this is the case for a certain sample using a "t-test." We don't actually need a null hypothesis in this case.

Given a sample in a vector, such as the `tip` column in the `tips` dataset, we can get an interval estimating the population's mean:

{% highlight r %}
> t.test(tips$tip)

	One Sample t-test

data:  tips$tip
t = 33.8489, df = 243, p-value < 2.2e-16
alternative hypothesis: true mean is not equal to 0
95 percent confidence interval:
 2.823799 3.172758
sample estimates:
mean of x
 2.998279
{% endhighlight %}

The interval is 2.82-3.17 (the sample's mean is about 3.00). This is an interval of 95% confidence, meaning the true mean is in this interval with 95% certainty. I.e., there is an estimated 5% chance that the true mean is not in this interval, meaning there is a 5% chance this sample was not truly random but instead has some kind of bias.

The confidence interval would likely shrink if any of the following happen: we get more samples from the population, the standard deviation of the samples decreases (i.e., there is less variance, i.e., the samples are closer to the sample mean), or we decrease the desired confidence level from 95% (default) to something like 80% or lower (with a lower confidence, we can define a more narrow interval).

#### Claims about two samples coming from different populations

Often, we want to perform an experiment (or analyze the results from an experiment) that gives two groups different treatments. For example, you may give one group a placebo and another group a drug. You then want to find out if the drug is having a "significant" effect on the people it *would be* given to in the future. In other words, you want to estimate if any/all people given the placebo have different outcomes than any/all people given the drug. The people you actually studied are just samples, but you want to make inferences about the populations they represent.

Given the `InsectSprays` dataset, let's ask whether spray A is better than spray B. That is, we want to test our claim that the sprays perform differently, i.e., their means differ. The null hypothesis is that they do not perform differently. The alternative hypothesis is that they do. If they do perform significantly differently, it would be easy to see which performs better.

{% highlight r %}
> with(InsectSprays, t.test(InsectSprays[spray=="A",]$count, InsectSprays[spray=="B",]$count))

	Welch Two Sample t-test

data:  InsectSprays[spray == "A", ]$count and InsectSprays[spray == "B", ]$count
t = -0.4535, df = 21.784, p-value = 0.6547
alternative hypothesis: true difference in means is not equal to 0
95 percent confidence interval:
 -4.646182  2.979515
sample estimates:
mean of x mean of y
 14.50000  15.33333
{% endhighlight %}

Comparing sprays A and B, we see that the $p$ value is 0.65, which is not strong enough to reject the null hypothesis at the 0.05 level of significance. So, we cannot say that either A is a significantly better spray than B or vice versa.

Comparing sprays A and C gives a different story:

{% highlight r %}
> with(InsectSprays, t.test(InsectSprays[spray=="A",]$count, InsectSprays[spray=="C",]$count))

	Welch Two Sample t-test

data:  InsectSprays[spray == "A", ]$count and InsectSprays[spray == "C", ]$count
t = 8.4073, df = 14.739, p-value = 5.278e-07
alternative hypothesis: true difference in means is not equal to 0
95 percent confidence interval:
  9.263901 15.569433
sample estimates:
mean of x mean of y
14.500000  2.083333
{% endhighlight %}

Now, p<<0.01 ("very less" than 0.01) so we can confidently say A and C sprays perform differently. Looking at their means (last line of output), or the plot from before, we can say that spray A is significantly better at killing insects than spray C.

### Claims about a proportion

We can test if a certain event is happening more often than chance, or more often than some expected probability. For example, suppose somebody is guessing heads/tails of a coin flip. We expect that person to be right 50% of the time. But suppose the person is right 97 times out of a 150 flips. We can use `prop.test` to check if we should reject the null hypothesis (which states that the person is not any better than chance):

{% highlight r %}
> prop.test(x=97, n=150, p=0.5)

	1-sample proportions test with continuity correction

data:  97 out of 150, null probability 0.5
X-squared = 12.3267, df = 1, p-value = 0.0004465
alternative hypothesis: true p is not equal to 0.5
95 percent confidence interval:
 0.5639758 0.7217181
sample estimates:
        p
0.6466667
{% endhighlight %}

With 95% confidence, the person is actually achieving 56%-72% accuracy in their guesses, and with p<0.01 confidence, we can reject the null hypothesis.

#### Claims about proportions from two populations

We can also test if the proportion of some event in one group is significantly different than the proportion of some event in another group. For example, suppose Jane and Frank are guessing weights at a circus. Jane guesses people's weights correctly (within 10 pounds, say) 120 out of 200 tries. Frank gets 30 out of 52 tries correctly. Is Jane better than Frank, or vice versa?

Use `prop.test` again, but give a two-element vector to `x` and `n` (for Jane and Frank, respectively). We don't need `p` because we have no assumptions about how often somebody should be able to correctly guess a weight, and it doesn't matter anyway since we only want to compare Jane vs. Frank's performance.

{% highlight r %}
> prop.test(x=c(120, 30), n=c(200, 52))

	2-sample test for equality of proportions with continuity correction

data:  c(120, 30) out of c(200, 52)
X-squared = 0.0206, df = 1, p-value = 0.8859
alternative hypothesis: two.sided
95 percent confidence interval:
 -0.1395083  0.1856622
sample estimates:
   prop 1    prop 2
0.6000000 0.5769231
{% endhighlight %}

Since p>0.05, we can't reject the null hypothesis, so we have to believe Jane is just as good as Frank at guessing weights.

Suppose Frank begins studying human anatomy and gets the next 50 guesses correct. Now he has 80 out of 102 guesses correct. Is he better than Jane now?

{% highlight r %}
> prop.test(x=c(120, 80), n=c(200, 102))

	2-sample test for equality of proportions with continuity correction

data:  c(120, 80) out of c(200, 102)
X-squared = 9.4519, df = 1, p-value = 0.002109
alternative hypothesis: two.sided
95 percent confidence interval:
 -0.29650481 -0.07212264
sample estimates:
   prop 1    prop 2
0.6000000 0.7843137
{% endhighlight %}

It seems so. `prop 1` (Jane's performance) is 60%, `prop 2` (Frank's performance) is 78.4%, so Frank is better, and significantly so since p<0.01.

You can use any number of values in `x` and `n` (as long as they have the same length) to compare multiple proportions simultaneously.

## Worries about multiple hypothesis testing

If you do enough variations of similar tests, you're bound to hit that 5% or 1% (p<0.05, p<0.01) where "significance" happens by chance. That's what the p-value means.

![xkcd hypothesis testing](http://imgs.xkcd.com/comics/significant.png)