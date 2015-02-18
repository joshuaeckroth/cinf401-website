---
layout: post
title: Regression
---

# Regression

## Overview

Consider the `faithful` dataset and the relation between waiting time and eruption time of Old Faithful:

![Old Faithful](/images/plot-faithful-points.png)

These data may be more useful if they can contribute to *predictions* of eruption time given assumptions of measures of waiting time. However, the points themselves in this particular sample are not useful directly. Even for virtually the same waiting time, there is a range of eruption times (look at waiting time 50min). But these data can serve as training examples for a *model* that we can then use for predictions.

Some models are *regression* models. Generally, you can think of regression as finding a smooth line/curve to fit the data. Being smooth, you can then evaluate any X values and get a corresponding Y value. A different kind of model is a *classifier*, which outputs a classification among a finite set (like factors in R). Classifiers are not smooth, continuous functions.

Models are (almost) always approximations and not completely accurate. As independent scientists, not in direct communication with God or some oracle (presumably), we have no way of knowing (without infinitely-many experiments) the actual cause-effect relation of waiting time and eruption time for Old Faithful. Given the data we have, for all we know, Old Faithful always erupts for exactly the times shown in the sample, and exactly for the given waiting times. Maybe there is no model but the data themselves... Or maybe Old Faithful follows a linear pattern: longer waiting time = longer eruption. We'll examine this theory first.

## Linear regression

The simplest possible regression is linear. Multiple linear regression is for modeling data with multiple inputs, say $x_1$ through $x_n$, and one output, say $y$. Simple linear regression is for modeling data with one input, say $x$, and one output, say $y$. We'll look at simple linear regression first.

### Simple linear regression

A simple line can be defined as $y = \alpha + \beta x$ for some $\alpha$ and $\beta$. We prefer to find values of $\alpha$ and $\beta$ that defines the line that best fits the data. In the case of Old Faithful, we want a line that goes up and to the right, and cuts through the cluster of points.

Of course, we should not just guess the values of $\alpha$ and $\beta$. We should somehow compute the optimal values. To do so, we need an estimate of error, so that we can then minimize the error.

We'll use calculus below to find the optimal values for $\alpha$ and $\beta$, and in order to do so, we'll need an error function that is differentiable. So we fall back to the "sum of squared errors" again:

<div>
$$
\text{sum of squared errors} = \sum_{i=1}^n (y_i - y')^2,
$$
</div>

where $y'$ is our prediction using our equation $y' = \alpha + \beta x_i$. Rewritten, we have:

<div>
$$
\text{sum of squared errors} = \sum_{i=1}^n (y_i - \alpha - \beta x_i)^2
$$
</div>

We want to find the optimal $\alpha$ and $\beta$ so that this sum is smallest, given our particular data $Y=y_1, y_2, \dots, y_n$ and $X=x_1, x_2, \dots, x_n$.

#### Find the optimal $\alpha$

Here is the calculus to find the optimal $\alpha$. In summary, take the partial derivative of the error function, with respect to $\alpha$, and solve for this partial derivative equal to 0. Since the error function is a sum of squares, which are all concave up, we'll be finding the minimum.

<div>
$$
\begin{eqnarray}
\frac{\partial}{\partial \alpha} \sum_{i=1}^n (y_i - \alpha - \beta x_i)^2 &=&
\sum_{i=1}^n 2(y_i - \alpha - \beta x_i) \frac{\partial}{\partial \alpha}(y_i - \alpha - \beta x_i) \\
&=& \sum_{i=1}^n -2(y_i - \alpha - \beta x_i) \\
\end{eqnarray}
$$
</div>

Setting the partial derivative equal to 0, we get:

<div>
$$
\begin{eqnarray}
\sum_{i=1}^n -2(y_i - \alpha - \beta x_i) &=& 0 \\
\sum_{i=1}^n (y_i - \alpha - \beta x_i) &=& 0 \\
\sum_{i=1}^n y_i - \sum_{i=1}^n \alpha - \sum_{i=1}^n \beta x_1 &=& 0 \\
\sum_{i=1}^n \alpha &=& \sum_{i=1}^n y_i - \sum_{i=1}^n \beta x_1 \\
\alpha n &=& \sum_{i=1}^n y_i - \beta \sum_{i=1}^n x_1 \\
\alpha &=& \frac{\sum_{i=1}^n y_i}{n} - \beta \frac{\sum_{i=1}^n x_1}{n} \\
\alpha &=& \overline{y} - \beta \overline{x},
\end{eqnarray}
$$
</div>

where $\overline{x}$ and $\overline{y}$ are the means of $x_i$ and $y_i$.

#### Find the optimal $\beta$

Here is the calculus for finding the optimal $\beta$. It works the same as above, except we'll eventually substitute in the optimal $\alpha$ as found above.

<div>
$$
\begin{eqnarray}
\frac{\partial}{\partial \beta} \sum_{i=1}^n (y_i - \alpha - \beta x_i)^2 &=&
\sum_{i=1}^n 2(y_i - \alpha - \beta x_i) \frac{\partial}{\partial \beta}(y_i - \alpha - \beta x_i) \\
&=& \sum_{i=1}^n -2(y_i - \alpha - \beta x_i) x_i \\
&=& -2 \left( \sum_{i=1}^n x_i y_i - \alpha \sum_{i=1}^n x_i - \beta \sum_{i=1}^n x_i^2 \right)
\end{eqnarray}
$$
</div>

Setting the partial derivative equal to 0, we get:

<div>
$$
\begin{eqnarray}
-2 \left( \sum_{i=1}^n x_i y_i - \alpha \sum_{i=1}^n x_i - \beta \sum_{i=1}^n x_i^2 \right) &=& 0 \\
\sum_{i=1}^n x_i y_i - \alpha \sum_{i=1}^n x_i - \beta \sum_{i=1}^n x_i^2 &=& 0 \\
\sum_{i=1}^n x_i y_i - \alpha \sum_{i=1}^n x_i &=& \beta \sum_{i=1}^n x_i^2 \\
\frac{\sum_{i=1}^n x_i y_i - \alpha \sum_{i=1}^n x_i}{\sum_{i=1}^n x_i^2} &=& \beta \\
\frac{n \overline{xy} - \alpha n \overline{x}}{n \overline{x^2}} &=& \beta \\
\frac{\overline{xy} - \alpha \overline{x}}{\overline{x^2}} &=& \beta \\
\end{eqnarray}
$$
</div>

Substituting $\alpha$ from above,

<div>
$$
\begin{eqnarray}
\frac{\overline{xy} - (\overline{y} - \beta \overline{x}) \overline{x}}{\overline{x^2}} &=& \beta \\
\frac{\overline{xy} - \overline{y}~\overline{x} + \beta \overline{x}^2}{\overline{x^2}} &=& \beta \\
\frac{\overline{xy} - \overline{y}~\overline{x}}{\overline{x^2}} &=& \beta - \frac{\beta \overline{x}^2}{\overline{x^2}} \\
\frac{\overline{xy} - \overline{y}~\overline{x}}{\overline{x^2}} &=& \frac{\beta \overline{x^2} - \beta \overline{x}^2}{\overline{x^2}} \\
\frac{\overline{xy} - \overline{y}~\overline{x}}{\overline{x^2}} &=& \beta \frac{\overline{x^2} - \overline{x}^2}{\overline{x^2}} \\
\frac{\overline{xy} - \overline{y}~\overline{x}}{\overline{x^2} - \overline{x}^2} &=& \beta \\
\end{eqnarray}
$$
</div>

<p>
The answer happens to equate to $\beta = r_{xy} \frac{s_y}{s_x}$, where $r_{xy}$ is the correlation of $x$ and $y$ and $s_x$, $s_y$ are the standard deviations.
</p>

Both $\alpha$ and $\beta$ can be easily computed. They simply combine means, correlations, and standard deviations, which themselves are easily computed. Of course, we can do this in R even more easily (see below).

### R code for simple linear regression

First, let's manually set $\alpha$ and $\beta$ and draw the line.

{% highlight r %}
> x <- faithful$waiting
> y <- faithful$eruptions
> beta <- (mean(x*y) - mean(y)*mean(x))/(mean(x*x)-mean(x)*mean(x))
> beta
[1] 0.07562795
> alpha <- mean(y)-beta*mean(x)
> alpha
[1] -1.874016
> p <- ggplot(faithful) + geom_point(aes(x=waiting, y=eruptions)) + geom_abline(intercept=alpha, slope=beta)
> ggsave("plot-faithful-linearreg.png", p, width=6, height=4, dpi=100)
{% endhighlight %}

![Old Faithful with linear regression](/images/plot-faithful-linearreg.png)

Seems to be optimal to my eyes. Anyway, of course it's optimal, we derived the formulas for $\alpha$ and $\beta$ using calculus. They aren't even estimates! They are truly optimal.

The R function `lm` does the same for us:

{% highlight r %}
> lm(eruptions ~ waiting, faithful)

Call:
lm(formula = eruptions ~ waiting, data = faithful)

Coefficients:
(Intercept)      waiting
   -1.87402      0.07563
{% endhighlight %}

### Multiple linear regression

We can also find a line of best fit through a higher-dimensional space. Assuming we just want one predicted ($y'$) value, but we have multiple input ($x_1, \dots, x_n$), we can specify them all in the formula to the `lm` function. Consider the `airquality` dataset, which has measures for ozone, window, and solar radiation. We suspect these three variables affect temperature. Let's find the line of best fit:

{% highlight r %}
> lm(Temp ~ Ozone + Solar.R + Wind, airquality)

Call:
lm(formula = Temp ~ Ozone + Solar.R + Wind, data = airquality)

Coefficients:
(Intercept)        Ozone      Solar.R         Wind
  72.418579     0.171966     0.007276    -0.322945
{% endhighlight %}

The best line is: `y = 72.42 + Ozone*0.172 + Solar.R*0.0073 - Wind*0.323`.

### Estimating goodness of fit

Even with an optimal line, the line may not closely fit the data. There may still be a large error. We measure the goodness of fit with R^2.

In order to define R^2, we first define $E_{\text{avg}}$:

<div>
$$
E_{\text{avg}} = \sum_{i=1}^n (y_i - \overline{y})^2
$$
</div>

This $E_{\text{avg}}$ is the sum-of-squared-errors of the original $y$ values compared to their mean. This is like estimating the error of a linear regression that is simply the mean of the $y$ values. That's almost never an optimal line. It is a horizontal line intercepting the y-axis at the mean of $y$:

![Old Faithful with average line](/images/plot-faithful-avgline.png)

Of course, any optimal linear regression is going to have less sum-of-squared-errors that that average line (unless the average line is optimal).

We can calculate the optimal line's error also:

<div>
$$
E_{\text{reg}} = \sum_{i=1}^n (y_i - y')^2
$$
</div>

The plot below, from [Wikipedia](http://en.wikipedia.org/wiki/Coefficient_of_determination), shows the error from the average line and the error from the optimal line:

![Coefficient of determination](/images/coefficient-of-determination.png)

Let's see the difference in these errors for Old Faithful:

{% highlight r %}
> E_avg <- sum((y-mean(y))*(y-mean(y)))
> E_avg
[1] 353.0394
> E_reg <- sum((y - (-1.87402 + 0.07563*x)) * (y - (-1.87402 + 0.07563*x)))
> E_reg
[1] 66.56178
{% endhighlight %}

Finally, we can consider the ratio of these errors:

<div>
$$
R^2 = 1 - \frac{E_{\text{reg}}}{E_{\text{avg}}}
$$
</div>

In R,

{% highlight r %}
> R2 <- 1 - E_reg/E_avg
> R2
[1] 0.8114607
{% endhighlight %}

The R^2 value is our "coefficient of determination" (w00t), and it ranges between -1 and 1. It can be negative if you're not comparing an optimal line but rather some crazy thing, so that the average line gives less error than that crazy thing. Anyway, usually it's between 0 and 1, and higher is better. A value of 1.0 means $E_{\text{reg}}$ is zero, i.e., it perfectly predicts every observed $y$ value.

Naturally, R can find R^2 for us if you run `summary` on the computed linear regression model:

{% highlight r %}
> linreg <- lm(eruptions ~ waiting, faithful)
> summary(linreg)

Call:
lm(formula = eruptions ~ waiting, data = faithful)

Residuals:
     Min       1Q   Median       3Q      Max
-1.29917 -0.37689  0.03508  0.34909  1.19329

Coefficients:
             Estimate Std. Error t value Pr(>|t|)
(Intercept) -1.874016   0.160143  -11.70   <2e-16 ***
waiting      0.075628   0.002219   34.09   <2e-16 ***
---
Signif. codes:  0 ‘***’ 0.001 ‘**’ 0.01 ‘*’ 0.05 ‘.’ 0.1 ‘ ’ 1

Residual standard error: 0.4965 on 270 degrees of freedom
Multiple R-squared:  0.8115,	Adjusted R-squared:  0.8108
F-statistic:  1162 on 1 and 270 DF,  p-value: < 2.2e-16
{% endhighlight %}

Notice `Multiple R-squared` down at the bottom-left. It works in multiple regression as well:

{% highlight r %}
> summary(lm(Temp ~ Ozone + Solar.R + Wind, airquality))

Call:
lm(formula = Temp ~ Ozone + Solar.R + Wind, data = airquality)

Residuals:
    Min      1Q  Median      3Q     Max
-20.942  -4.996   1.283   4.434  13.168

Coefficients:
             Estimate Std. Error t value Pr(>|t|)
(Intercept) 72.418579   3.215525  22.522  < 2e-16 ***
Ozone        0.171966   0.026390   6.516 2.42e-09 ***
Solar.R      0.007276   0.007678   0.948    0.345
Wind        -0.322945   0.233264  -1.384    0.169
---
Signif. codes:  0 ‘***’ 0.001 ‘**’ 0.01 ‘*’ 0.05 ‘.’ 0.1 ‘ ’ 1

Residual standard error: 6.834 on 107 degrees of freedom
  (42 observations deleted due to missingness)
Multiple R-squared:  0.4999,	Adjusted R-squared:  0.4858
F-statistic: 35.65 on 3 and 107 DF,  p-value: 4.729e-16
{% endhighlight %}

### Residuals

We can examine the error of each predicted value, given the model, and the true value. These differences are called "residuals," and they live in a vector in the output of the `lm` function:

{% highlight r %}
> linreg <- lm(eruptions ~ waiting, faithful)
> head(linreg$res)
          1           2           3           4           5           6
-0.50059190 -0.40989320 -0.38945216 -0.53191679 -0.02135959  0.59747885
{% endhighlight %}

Each residual corresponds to an $x$ value, in our case, the `waiting` column of the data frame. So we can graph the model's error for each $x$ value:

{% highlight r %}
> p <- ggplot(faithful) + geom_point(aes(x=waiting, y=linreg$res))
> ggsave("plot-faithful-linreg-residuals.png", p, width=3, height=2, dpi=100)
{% endhighlight %}

Compare the linear regression (left) with the residuals (right):

<div style="text-align: center">
<img src="/images/plot-faithful-linreg-small.png" style="display: inline;" /> <img src="/images/plot-faithful-linreg-residuals.png" style="display: inline;" />
</div>

## Nonlinear regression

{% highlight r %}
> library(car)
> head(USPop)
  year population
1 1790   3.929214
2 1800   5.308483
3 1810   7.239881
4 1820   9.638453
5 1830  12.860702
6 1840  17.063353
> p <- ggplot(USPop) + geom_point(aes(x=year, y=population))
> ggsave("plot-uspop.png", p, width=6, height=4, dpi=100)
{% endhighlight %}

![USPop dataset](/images/plot-uspop.png)

{% highlight r %}
> time <- 0:21
> model <- nls(population ~ beta1 / (1 + exp(beta2 + beta3 * time)), USPop,
+              start=list(beta1 = 350, beta2 = 4.5, beta3 = -0.3), trace=TRUE)
13217.35 :  350.0   4.5  -0.3
1861.728 :  370.4674576   3.7839326  -0.2153424
538.287 :  430.4181666   4.0390595  -0.2174231
457.8314 :  440.9958648   4.0313233  -0.2159307
457.8057 :  440.7942364   4.0324813  -0.2160736
457.8056 :  440.8363258   4.0323905  -0.2160578
457.8056 :  440.8331803   4.0324001  -0.2160592
> summary(model)

Formula: population ~ beta1/(1 + exp(beta2 + beta3 * time))

Parameters:
       Estimate Std. Error t value Pr(>|t|)
beta1 440.83318   35.00012   12.60 1.14e-10 ***
beta2   4.03240    0.06818   59.14  < 2e-16 ***
beta3  -0.21606    0.01007  -21.45 8.87e-15 ***
---
Signif. codes:  0 ‘***’ 0.001 ‘**’ 0.01 ‘*’ 0.05 ‘.’ 0.1 ‘ ’ 1

Residual standard error: 4.909 on 19 degrees of freedom

Number of iterations to convergence: 6
Achieved convergence tolerance: 3.409e-06

> fitted.values(model)
 [1]   7.680542   9.493013  11.721622  14.455891  17.801515  21.881703  26.837758  32.828367
 [9]  40.026888  48.615800  58.777435  70.680293  84.460697 100.200540 117.903175 137.471163
[17] 158.690865 181.229244 204.646850 228.427841 252.023531 274.902074
attr(,"label")
[1] "Fitted values"

> residuals(model)
 [1] -3.751328 -4.184530 -4.481741 -4.817438 -4.940813 -4.818350 -3.645882 -1.385046 -1.468517
[10]  1.573409  4.202331  5.531875  7.767799  5.820997  5.299449 -5.306594 -7.365067 -1.906069
[19] -1.344819 -1.885642 -3.313658  6.519832
attr(,"label")
[1] "Residuals"
{% endhighlight %}

{% highlight r %}
> p <- ggplot(USPop) + geom_point(aes(x=year, y=population)) + geom_line(aes(x=year, y=fitted.values(model)))
> ggsave("plot-uspop-logistic.png", p, width=6, height=4, dpi=100)

> p <- ggplot(USPop) + geom_point(aes(x=year, y=residuals(model))) + geom_line(aes(x=year, y=residuals(model)))
> ggsave("plot-uspop-logistic-residuals.png", p, width=6, height=4, dpi=100)
{% endhighlight %}

![USPop logistic regression](/images/plot-uspop-logistic.png)

![USPop logistic regression residuals](/images/plot-uspop-logistic-residuals.png)
