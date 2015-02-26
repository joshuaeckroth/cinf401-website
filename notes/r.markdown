---
layout: post
title: R
---

# R

*Note: See the corresponding [cookbook about R](/cookbook/r.html). This page has lecture notes.*

## Help system

On the R command line, you can use `?` syntax to open documentation about functions.

- `?topic` --- open a manual page about a topic or function
- `??topic` --- search manual pages for mentions of the topic; use `?specific_topic` after you find the right one

You can ask about operators also: <code>?\`+\`</code>

## Libraries

Many of the features we will use in R are actually defined in external libraries. For example, string manipulation is provided by the `stringr` library.

Here are the necessary commands:

- `install.packages("stringr")` --- install a library (or "package") if you have not already

- `library(stringr)` --- load a library so you can use its functions; notice `stringr` is not in quotes, unlike `install.packages`

Sometimes you'll see `require(stringr)` or similar instead of `library(stringr)`. These two functions basically do the same thing (load a library) but the `library` function is a better choice. See this [blog post](http://yihui.name/en/2014/07/library-vs-require/) for an explanation.

## Variables

Variable assignment should be done like this: `x <- 5`.

![Deal with it](/images/r-assignment-syntax.gif)

Or, you can do this: `5 -> z`. Ironically, the book (p. 37) has it backwards.

## Data structures

The data structures and data types in R are the most important features to understand.

### Data types

R has these main types (classes actually):

- `numeric` --- a.k.a. floats, a.k.a. decimal values; `integer` exists, too, if you write `5L` instead of `5`, but it's very uncommon (because we don't do `for` loops in R, by the way)
- `character` --- a.k.a. strings
- `Date` or `POSIXct`
- `logical` --- a.k.a. booleans (`TRUE` or `T`, and `FALSE` or `F`)

Note, regarding `T` and `F` shorthands for `TRUE` and `FALSE` logical values. The book (p. 42) has an interesting statement about this:

> R provides `T` and `F` as shortcuts for `TRUE` and `FALSE`, respectively, but it is best practice not to use them, as they are simply variables storing the values `TRUE` and `FALSE` and can be overwritten (!!), which can cause a great deal of frustration... (!! added by me)

You can query the type of a variable/value:

{% highlight r %}
> class(5)
[1] "numeric"
> typeof(5)
[1] "double"

> class(5L)
[1] "integer"
> typeof(5L)
[1] "integer"

> class("foo")
[1] "character"
> typeof("foo")
[1] "character"

> class(TRUE)
[1] "logical"
> typeof(TRUE)
[1] "logical"
{% endhighlight %}

### Vectors

Vectors contain values of the same type, much like arrays in other languages. Here are some vectors:

{% highlight r %}
v1 <- c(3, 7, 12)
v2 <- c("foo", "bar", "baz", "quux")
v3 <- c(TRUE, FALSE, FALSE, TRUE, FALSE)
{% endhighlight %}

R is a "vector language." Most functions can be applied to an entire vector at once (and the function operates on each element). This is how we avoid explicit loops.

R has a `list` type, as well, which can hold values of different types. Most functions expect vectors, so we don't often, if ever, use lists outright.

Here are some vector operations. See the book, pp. 44-48 for more examples.

{% highlight r %}
v1 <- c(3, 7, 12)

v1 + 5            # +5 is applied to each element
v1 * 5
v1 / 5
sqrt(v1)

v4 <- c(8, 1, 2)  # same length as v1

v1 + v4           # works element-by-element
v1 * v4
v1 / v4
v1 ^ v4
v1 < v4

length(v1)

all(v1 < v4)
any(v1 < v4)

max(v1)
min(v1)
mean(v1)
summary(v1)

# get individual elements, or ranges
v1[1]             # oh no! indexing starts at 1 !!!
v1[1:3]           # a range of elements
v1[c(1, 3)]       # get two specific elements
{% endhighlight %}

### What is `c()`?

From `?c` inside R:

> This is a generic function which combines its arguments.
>
> The default method combines its arguments to form a vector.  All arguments are coerced to a common type which is the type of the returned value, and all attributes except names are removed.

So it's a simple way to make a vector.

### More about vectors

Almost everything in R is a vector. Even a simple number is a vector. See how it's printed?

{% highlight r %}
> 5
[1] 5
{% endhighlight %}

That `[1]` is giving index values for the vector. This vector has one thing in it. You can also index into it:

{% highlight r %}
> 5[1]          # whoa!
[1] 5
{% endhighlight %}

And you can ask for its length:

{% highlight r %}
> length(5)
[1] 1
{% endhighlight %}

Even the output of `length` is a vector!

Anyway, those index numbers in the left column are more intuitive when you have many elements. We can produce many elements easily with `:` operator:

{% highlight r %}
> 1:10
 [1]  1  2  3  4  5  6  7  8  9 10
> 50:-50
  [1]  50  49  48  47  46  45  44  43  42  41  40  39  38  37  36  35  34  33
 [19]  32  31  30  29  28  27  26  25  24  23  22  21  20  19  18  17  16  15
 [37]  14  13  12  11  10   9   8   7   6   5   4   3   2   1   0  -1  -2  -3
 [55]  -4  -5  -6  -7  -8  -9 -10 -11 -12 -13 -14 -15 -16 -17 -18 -19 -20 -21
 [73] -22 -23 -24 -25 -26 -27 -28 -29 -30 -31 -32 -33 -34 -35 -36 -37 -38 -39
 [91] -40 -41 -42 -43 -44 -45 -46 -47 -48 -49 -50
{% endhighlight %}

The point about "everything is a vector" is that if you ask the type of a vector, it will give the type of its values.

{% highlight r %}
# we'll use seq(), a generalization of :

> seq(2.2, 5.5, 0.071)
 [1] 2.200 2.271 2.342 2.413 2.484 2.555 2.626 2.697 2.768 2.839 2.910 2.981
[13] 3.052 3.123 3.194 3.265 3.336 3.407 3.478 3.549 3.620 3.691 3.762 3.833
[25] 3.904 3.975 4.046 4.117 4.188 4.259 4.330 4.401 4.472 4.543 4.614 4.685
[37] 4.756 4.827 4.898 4.969 5.040 5.111 5.182 5.253 5.324 5.395 5.466
> class(seq(2.2, 5.5, 0.071))
[1] "numeric"
> typeof(seq(2.2, 5.5, 0.071))
[1] "double"
{% endhighlight %}

### NA

A vector can contain `NA` values, which are like "null" but not the same.

{% highlight r %}
# add some NAs to a vector
> v5 <- c(5, 1, NA, 7, NA, NA, 8)

# using a bad index gives NAs back
> v5[3:17]
 [1] NA  7 NA NA  8 NA NA NA NA NA NA NA NA NA NA
{% endhighlight %}

Use the `is.na` function to ask if each value in a vector is `NA` or not. `any.na` gives you one answer about the whole vector. `na.omit` returns the vector without `NA`s. It also includes metadata about which indices were omitted.

{% highlight r %}
> v6 <- na.omit(v5)
> v6
[1] 5 1 7 8
attr(,"na.action")
[1] 3 5 6
attr(,"class")
[1] "omit"
{% endhighlight %}

`NA` is the appropriate value (or non-value) for missing data.

### NULL

`NULL` cannot be put in a vector. It's like `void`, except that a variable can equal `NULL`. Sometimes, you can give a function `NULL` for one of its arguments, and a function can return `NULL`. That's all it's good for.

### Factors

Factors are vectors with the distinct values stored as metadata called "levels." They are like an array of "enum" types in Java. The vector itself actually only contains integers, indicating which of the unique levels that value represents.

You can convert a vector into a factor like so:

{% highlight r %}
> as.factor(c(1, 2, 3, 3, 2, 1))
[1] 1 2 3 3 2 1
Levels: 1 2 3
{% endhighlight %}

Here is a factor of character types:

{% highlight r %}
> as.factor(c("foo", "bar", "foo", "baz", "quux", "foo", "baz"))
[1] foo  bar  foo  baz  quux foo  baz
Levels: bar baz foo quux
{% endhighlight %}

The factor prints as if it was the original vector, but if you use `as.integer` you can see the internal numeric IDs and the levels that correspond to the IDs:

{% highlight r %}
> as.integer(as.factor(c("foo", "bar", "foo", "baz", "quux", "foo", "baz")))
[1] 3 1 3 2 4 3 2
> levels(as.factor(c("foo", "bar", "foo", "baz", "quux", "foo", "baz")))
[1] "bar"  "baz"  "foo"  "quux"
{% endhighlight %}

Factors are most useful to us when we are creating plots with ggplot.

### Data frames

A data frame is like a spreadsheet. Rows and columns can be named. Typically, just columns are named. Internally, each column is a vector (of row values), and of course they are all the same length (the number of rows). Since each column is a different vector, each column can hold a different type of data (but only one type of data per column).

Let's build a data frame:

{% highlight r %}
> data.frame(c(10, 20, 30, 80), c(40, 50, 60, 70), c(70, 80, 90, 100))
  c.10..20..30..80. c.40..50..60..70. c.70..80..90..100.
1                10                40                 70
2                20                50                 80
3                30                60                 90
4                80                70                100
{% endhighlight %}

Notice it gave us silly names for the columns. Let's name them ourselves:

{% highlight r %}
> data.frame(Foo = c(10, 20, 30, 80), Bar = c(40, 50, 60, 70), Baz = c(70, 80, 90, 100))
  Foo Bar Baz
1  10  40  70
2  20  50  80
3  30  60  90
4  80  70 100
{% endhighlight %}

Also notice the output shows row numbers, which can be used as indices.

You can ask about the number of rows and columns using `nrow` and `ncol`. Note that `length` of this data frame would give `3` because it has three columns (a data frame is a `list` of column vectors). The `dim` function gives the rows and columns. The `names` function gives the column names (as a vector of `character`).

{% highlight r %}
> d <- data.frame(Foo = c(10, 20, 30, 80), Bar = c(40, 50, 60, 70), Baz = c(70, 80, 90, 100))
> nrow(d)
[1] 4
> ncol(d)
[1] 3
> length(d)
[1] 3
> typeof(d)
[1] "list"
> class(d)
[1] "data.frame"
> dim(d)
[1] 4 3
> names(d)
[1] "Foo" "Bar" "Baz"
{% endhighlight %}

You can attach more columns with `cbind`:

{% highlight r %}
> cbind(d, c(3, 4, 2, 1))
  Foo Bar Baz c(3, 4, 2, 1)
1  10  40  70             3
2  20  50  80             4
3  30  60  90             2
4  80  70 100             1

# that gave the new column a silly name; let's try again

> cbind(d, Quux = c(3, 4, 2, 1))
  Foo Bar Baz Quux
1  10  40  70    3
2  20  50  80    4
3  30  60  90    2
4  80  70 100    1
{% endhighlight %}

The `rbind` function can add rows:

{% highlight r %}
> rbind(d, c(11, 12, 13), c(15, 16, 17))
  Foo Bar Baz
1  10  40  70
2  20  50  80
3  30  60  90
4  80  70 100
5  11  12  13
6  15  16  17
{% endhighlight %}

You can `cbind` or `rbind` two data frames as well:

{% highlight r %}
> cbind(d, d)
  Foo Bar Baz Foo Bar Baz
1  10  40  70  10  40  70
2  20  50  80  20  50  80
3  30  60  90  30  60  90
4  80  70 100  80  70 100

> rbind(d, d)
  Foo Bar Baz
1  10  40  70
2  20  50  80
3  30  60  90
4  80  70 100
5  10  40  70
6  20  50  80
7  30  60  90
8  80  70 100
{% endhighlight %}

#### Subsetting and filtering

- Use `dframe[row,]` to access a particular row in `dframe` (a data frame)
- Use `dframe[,col]` or `dframe[[col]]` or `dframe$col` to access a particular column
- Use `dframe[row,col]` to access a particular cell; note, a 1-element vector will result
- Use `dframe[row,col,drop=FALSE]` to get a particular cell as a data frame

You can also subset a data frame by complex boolean expressions:

{% highlight r %}
> d
  Foo Bar Baz
1  10  40  70
2  20  50  80
3  30  60  90
4  80  70 100

> subset(d, Foo >= 10 & Bar <= 50)
  Foo Bar Baz
1  10  40  70
2  20  50  80

> subset(d, Foo >= 10 & Bar <= 50, c("Foo", "Baz"))
  Foo Baz
1  10  70
2  20  80
{% endhighlight %}

## Reshaping data frames (`melt` and `dcast`)

(See the R book, pp. 149-152.)

The `reshape2` package provides some powerful functions for dramatic transforms of data frames. These transformations come in two forms (which are inverses of each other): melting and casting.

Both melting and casting assume that your data frames consist only of "identifier" and "measured" variables or columns:

- Identifier (id) variables are those that identify cases that have been measured. For example, id variables may be the person's first name and last name plus date of birth.

- Measured variables are those that are measured per case. A person's height or weight or GPA would be measure variables since they do not identify the case (the person) but are measures of that person.

You can also think about id variables as those you might put on the x-axis, and measure variables as those you might put on the y-axis.

Often we have data frames that look like the following, predefined data frame `USArrests`. We use the `head` function here to look at the first few rows.

{% highlight r %}
> head(USArrests)
           Murder Assault UrbanPop Rape
Alabama      13.2     236       58 21.2
Alaska       10.0     263       48 44.5
Arizona       8.1     294       80 31.0
Arkansas      8.8     190       50 19.5
California    9.0     276       91 40.6
Colorado      7.9     204       78 38.7
{% endhighlight %}

The first thing we're going to do (which has nothing to do with melting/casting) is put the row names (the states) into their own column, and then remove row names.

{% highlight r %}
> d <- cbind(State=rownames(USArrests), USArrests)
> head(d)
                  State Murder Assault UrbanPop Rape
  Alabama       Alabama   13.2     236       58 21.2
  Alaska         Alaska   10.0     263       48 44.5
  Arizona       Arizona    8.1     294       80 31.0
  Arkansas     Arkansas    8.8     190       50 19.5
  California California    9.0     276       91 40.6
  Colorado     Colorado    7.9     204       78 38.7
> rownames(d) <- NULL
> head(d)
       State Murder Assault UrbanPop Rape
1    Alabama   13.2     236       58 21.2
2     Alaska   10.0     263       48 44.5
3    Arizona    8.1     294       80 31.0
4   Arkansas    8.8     190       50 19.5
5 California    9.0     276       91 40.6
6   Colorado    7.9     204       78 38.7
{% endhighlight %}

Next, we'll "melt" the data frame. The id column is "State", the measured columns are "Murder", "Assault", "UrbanPop", and "Rape".

{% highlight r %}
> library(reshape2)

> dmelt <- melt(d, c("State"), c("Murder", "Assault", "UrbanPop", "Rape"))

> head(dmelt)
       State variable value
1    Alabama   Murder  13.2
2     Alaska   Murder  10.0
3    Arizona   Murder   8.1
4   Arkansas   Murder   8.8
5 California   Murder   9.0
6   Colorado   Murder   7.9
{% endhighlight %}

Notice how each of the measure variables is on a row of its own, and we have new columns "variable" and "value".

This format is easier to use with ggplot, which we'll see later.

### Casting

After "melting", we can "cast" the melted data frame to all kinds of different forms. The `dcast` (`d` for data frame) works as follows:

{% highlight r %}
> dcast(dataframe, formula)
{% endhighlight %}

Formulas are written like this (a few variations listed):

```
id-column-1 ~ measure-column-1
id-column-1 + id-column-2 ~ measure-column-1 + measure-column-2
id-column-1 + id-column-2 ~ ...
. ~ measure-column-1 + measure-column-2
etc.
```

The parts before the `~` become id columns in the resulting data frame, and the parts after the `~` become the measure columns. A `+` means make 2+ columns, just like a "truth table" where `A + B` means make columns `A` and `B` and list the rows so that for each value of `A`, go through all values of `B`.

The special syntax `...` means "all variables not already listed" and `.` means "no variable".

If a formula results in multiple values for each row (because you didn't mention all id variables, for example), then you need to provide an "aggregating" function, e.g., `mean` to average the multiple values. If you do not provide such a function, `length` will be used, meaning it will count how many values match the formula.

Note, `dcast` assumes the values are found in the `values` column, as produced by `melt`.

{% highlight r %}
# get the original data frame back
> head(dcast(dmelt, State ~ variable))
       State Murder Assault UrbanPop Rape
1    Alabama   13.2     236       58 21.2
2     Alaska   10.0     263       48 44.5
3    Arizona    8.1     294       80 31.0
4   Arkansas    8.8     190       50 19.5
5 California    9.0     276       91 40.6
6   Colorado    7.9     204       78 38.7

# flip the data frame (states as columns)
> head(dcast(dmelt, variable ~ State))
    variable Alabama Alaska Arizona Arkansas California Colorado ...
  1   Murder    13.2   10.0     8.1      8.8        9.0      7.9
  2  Assault   236.0  263.0   294.0    190.0      276.0    204.0
  3 UrbanPop    58.0   48.0    80.0     50.0       91.0     78.0
  4     Rape    21.2   44.5    31.0     19.5       40.6     38.7

# we can supply an aggregation function;
# the . means no id variable, i.e., all states combined
> head(dcast(dmelt, . ~ variable, mean))
  . Murder Assault UrbanPop   Rape
1 .  7.788  170.76    65.54 21.232
{% endhighlight %}

Here is another built-in data frame:

{% highlight r %}
> head(ChickWeight)
  weight Time Chick Diet
1     42    0     1    1
2     51    2     1    1
3     59    4     1    1
4     64    6     1    1
5     76    8     1    1
6     93   10     1    1
{% endhighlight %}

Let's melt it on id variables "Chick", "Diet", and "Time":

{% highlight r %}
> cmelt <- melt(ChickWeight, c("Chick", "Diet", "Time"), c("weight"))
> head(cmelt)
  Chick Diet Time variable value
1     1    1    0   weight    42
2     1    1    2   weight    51
3     1    1    4   weight    59
4     1    1    6   weight    64
5     1    1    8   weight    76
6     1    1   10   weight    93
{% endhighlight %}

This casting gives the mean of "Time" vs. "variable" (which is only "weight"):

{% highlight r %}
> head(dcast(cmelt, Time ~ variable, mean))
  Time    weight
1    0  41.06000
2    2  49.22000
3    4  59.95918
4    6  74.30612
5    8  91.24490
6   10 107.83673
{% endhighlight %}

Here we have "Time" vs. "Diet". The "Diet" unique values become columns.

{% highlight r %}
> head(dcast(cmelt, Time ~ Diet, mean))
  Time        1     2     3     4
1    0 41.40000  40.7  40.8  41.0
2    2 47.25000  49.4  50.4  51.8
3    4 56.47368  59.8  62.2  64.5
4    6 66.78947  75.4  77.9  83.9
5    8 79.68421  91.7  98.4 105.6
6   10 93.05263 108.5 117.1 126.0
{% endhighlight %}

If we don't provide `mean` as the aggregator, we'll get a warning and it will default to `length`. This is because for each "Diet" value (1-4), there are 10-20 chicks and therefore 10-20 weight measures.

{% highlight r %}
> head(dcast(cmelt, Time ~ Diet))
Aggregation function missing: defaulting to length
  Time  1  2  3  4
1    0 20 10 10 10
2    2 20 10 10 10
3    4 19 10 10 10
4    6 19 10 10 10
5    8 19 10 10 10
6   10 19 10 10 10
{% endhighlight %}

If you use `library(plyr)`, you can also do subsets. Notice the `subset = .(Time < 10)` part.

{% highlight r %}
> library(plyr)

> head(dcast(cmelt, Time ~ Diet, mean, subset = .(Time < 10)))
    Time        1    2    3     4
  1    0 41.40000 40.7 40.8  41.0
  2    2 47.25000 49.4 50.4  51.8
  3    4 56.47368 59.8 62.2  64.5
  4    6 66.78947 75.4 77.9  83.9
  5    8 79.68421 91.7 98.4 105.6
{% endhighlight %}

## Aggregation on data frames

A different way to produce column means or sums or whatever, without using `melt` and `dcast`, is to use `aggregate`. See the R book, pp. 120-123.

We'll use the `ChickWeight` data frame again.

{% highlight r %}
> head(ChickWeight)
  weight Time Chick Diet
1     42    0     1    1
2     51    2     1    1
3     59    4     1    1
4     64    6     1    1
5     76    8     1    1
6     93   10     1    1
{% endhighlight %}

`aggregate` uses "formulas", too, like `dcast`, but `aggregate`'s formulas are written the other way:

```
measure-column-1 ~ id-column-1
cbind(measure-column-1, measure-column-2) ~ id-column-1 + id-column-2
```

Here we go:

{% highlight r %}
# find average weight per diet
> aggregate(weight ~ Diet, ChickWeight, mean)
  Diet   weight
1    1 102.6455
2    2 122.6167
3    3 142.9500
4    4 135.2627

# find average weight per diet per time
> head(aggregate(weight ~ Diet + Time, ChickWeight, mean))
  Diet Time weight
1    1    0  41.40
2    2    0  40.70
3    3    0  40.80
4    4    0  41.00
5    1    2  47.25
6    2    2  49.40
{% endhighlight %}

Switching data frames to `diamonds` inside the `ggplot2` library:

{% highlight r %}
> library(ggplot2)
> head(diamonds)
  carat       cut color clarity depth table price    x    y    z
1  0.23     Ideal     E     SI2  61.5    55   326 3.95 3.98 2.43
2  0.21   Premium     E     SI1  59.8    61   326 3.89 3.84 2.31
3  0.23      Good     E     VS1  56.9    65   327 4.05 4.07 2.31
4  0.29   Premium     I     VS2  62.4    58   334 4.20 4.23 2.63
5  0.31      Good     J     SI2  63.3    58   335 4.34 4.35 2.75
6  0.24 Very Good     J    VVS2  62.8    57   336 3.94 3.96 2.48
{% endhighlight %}

Let's find the maximum carat per cut:

{% highlight r %}
> aggregate(carat ~ cut, diamonds, max)
        cut carat
1      Fair  5.01
2      Good  3.01
3 Very Good  4.00
4   Premium  4.01
5     Ideal  3.50
{% endhighlight %}

If you want two measured columns, use `cbind()`:

{% highlight r %}
> aggregate(cbind(carat, depth) ~ cut, diamonds, max)
        cut carat depth
1      Fair  5.01  79.0
2      Good  3.01  67.0
3 Very Good  4.00  64.9
4   Premium  4.01  63.0
5     Ideal  3.50  66.7
{% endhighlight %}

Next we'll find the count of diamonds in the data frame with various clarities. We'll use a bogus column `cut` just to do the aggregation, but use `length` to count how many `cut` values there are for each `clarity`. We could have used any column that's not `clarity` to count up the same way.

{% highlight r %}
> aggregate(cut ~ clarity, diamonds, length)
  clarity   cut
1      I1   741
2     SI2  9194
3     SI1 13065
4     VS2 12258
5     VS1  8171
6    VVS2  5066
7    VVS1  3655
8      IF  1790
{% endhighlight %}


### Practice

Take the `tips` data frame:

{% highlight r %}
> head(tips)
  total_bill  tip    sex smoker day   time size
1      16.99 1.01 Female     No Sun Dinner    2
2      10.34 1.66   Male     No Sun Dinner    3
3      21.01 3.50   Male     No Sun Dinner    3
4      23.68 3.31   Male     No Sun Dinner    2
5      24.59 3.61 Female     No Sun Dinner    4
6      25.29 4.71   Male     No Sun Dinner    4
{% endhighlight %}

And produce these data frames with `melt` and `dcast`:

{% highlight r %}
# Data frame 1 (melt + dcast)
     sex  day total_bill      tip
1 Female  Fri   14.14556 2.781111  <--- total_bill & tip are means
2 Female  Sat   19.68036 2.801786
3 Female  Sun   19.87222 3.367222
4 Female Thur   16.71531 2.575625
5   Male  Fri   19.85700 2.693000
6   Male  Sat   20.80254 3.083898
7   Male  Sun   21.88724 3.220345
8   Male Thur   18.71467 2.980333

# Data frame 2 (melt + dcast)
     sex  day total_bill    tip
1 Female  Fri     127.31  25.03    <--- total_bill & tip are sums
2 Female  Sat     551.05  78.45
3 Female  Sun     357.70  60.61
4 Female Thur     534.89  82.42
5   Male  Fri     198.57  26.93
6   Male  Sat    1227.35 181.95
7   Male  Sun    1269.46 186.78
8   Male Thur     561.44  89.41

# Data frame 3 (melt + dcast)
     sex  Fri_tip  Sat_tip  Sun_tip Thur_tip
1 Female 2.781111 2.801786 3.367222 2.575625  <--- these are means
2   Male 2.693000 3.083898 3.220345 2.980333

# Data frame 4 (melt + dcast)
# (note: this double-counts the records since there 
   day  No Yes   <--- No = non smoker, Yes = smoker
1  Fri   8  30   <--- these are counts (length)
2  Sat  90  84
3  Sun 114  38
4 Thur  90  34

# Data frame 4 (melt + dcast)
   day   time  No Yes   <--- No = non smoker, Yes = smoker
1  Fri Dinner   6  18   <--- these are counts (length)
2  Fri  Lunch   2  12
3  Sat Dinner  90  84
4  Sun Dinner 114  38
5 Thur Dinner   2   0
6 Thur  Lunch  88  34
{% endhighlight %}

Now do the following with `aggregate`:

{% highlight r %}
# Data frame 1 (aggregate)
     sex  day total_bill      tip
1 Female  Fri   14.14556 2.781111  <--- these are means
2   Male  Fri   19.85700 2.693000
3 Female  Sat   19.68036 2.801786
4   Male  Sat   20.80254 3.083898
5 Female  Sun   19.87222 3.367222
6   Male  Sun   21.88724 3.220345
7 Female Thur   16.71531 2.575625
8   Male Thur   18.71467 2.980333

# Data frame 2 (aggregate)
     sex  day tip  <--- bogus column name, just counting male/female per day
1 Female  Fri   9
2   Male  Fri  10
3 Female  Sat  28
4   Male  Sat  59
5 Female  Sun  18
6   Male  Sun  58
7 Female Thur  32
8   Male Thur  30
{% endhighlight %}

## Merging data frames

We can combine or "merge" two data frames in a way similar to a relational database. You must specify a column (or columns) in both data frames that acts as the "key".

Suppose we have these two data frames (from the example documentation `?merge`):

{% highlight r %}
> authors <- data.frame(
+     surname = I(c("Tukey", "Venables", "Tierney", "Ripley", "McNeil")),
+     nationality = c("US", "Australia", "US", "UK", "Australia"),
+     deceased = c("yes", rep("no", 4)))
> books <- data.frame(
+     name = I(c("Tukey", "Venables", "Tierney",
+                "Ripley", "Ripley", "McNeil", "R Core")),
+     title = c("Exploratory Data Analysis",
+               "Modern Applied Statistics ...",
+               "LISP-STAT",
+               "Spatial Statistics", "Stochastic Simulation",
+               "Interactive Data Analysis",
+               "An Introduction to R"),
+     other.author = c(NA, "Ripley", NA, NA, NA, NA,
+                      "Venables & Smith"))

> authors
   surname nationality deceased
1    Tukey          US      yes
2 Venables   Australia       no
3  Tierney          US       no
4   Ripley          UK       no
5   McNeil   Australia       no

> books
      name                         title     other.author
1    Tukey     Exploratory Data Analysis             <NA>
2 Venables Modern Applied Statistics ...           Ripley
3  Tierney                     LISP-STAT             <NA>
4   Ripley            Spatial Statistics             <NA>
5   Ripley         Stochastic Simulation             <NA>
6   McNeil     Interactive Data Analysis             <NA>
7   R Core          An Introduction to R Venables & Smith
{% endhighlight %}

We can create a new, merged data frame by combining the two on the "surname" column in `authors` and the "name" column in `books`:

{% highlight r %}
> merge(authors, books, by.x="surname", by.y="name")
   surname nationality deceased                         title other.author
1   McNeil   Australia       no     Interactive Data Analysis         <NA>
2   Ripley          UK       no            Spatial Statistics         <NA>
3   Ripley          UK       no         Stochastic Simulation         <NA>
4  Tierney          US       no                     LISP-STAT         <NA>
5    Tukey          US      yes     Exploratory Data Analysis         <NA>
6 Venables   Australia       no Modern Applied Statistics ...       Ripley
{% endhighlight %}

Note that "R Core" is in `authors` but not `books`, so it's left out of the merge. The `all=TRUE` option keeps it:

{% highlight r %}
> merge(authors, books, by.x="surname", by.y="name", all=TRUE)
   surname nationality deceased                         title     other.author
1   McNeil   Australia       no     Interactive Data Analysis             <NA>
2   R Core        <NA>     <NA>          An Introduction to R Venables & Smith
3   Ripley          UK       no            Spatial Statistics             <NA>
4   Ripley          UK       no         Stochastic Simulation             <NA>
5  Tierney          US       no                     LISP-STAT             <NA>
6    Tukey          US      yes     Exploratory Data Analysis             <NA>
7 Venables   Australia       no Modern Applied Statistics ...           Ripley
{% endhighlight %}

You can merge on a key composed of 2+ columns if they're named the same in both data frames. Here are two new data frames:

{% highlight r %}
> d1 <- data.frame(firstname=c("Mary", "Beth", "Beth"),
+                  lastname=c("Staples", "Wrench", "Hammer"),
+                  income=c(111230,27200,83000))
> d1
  firstname lastname income
1      Mary  Staples 111230
2      Beth   Wrench  27200
3      Beth   Hammer  83000
> d2 <- data.frame(firstname=c("Mary", "Beth", "Beth"),
+                  lastname=c("Staples", "Wrench", "Hammer"),
+                  birthdate=c(as.Date("1982-01-05"), as.Date("1960-10-25"), as.Date("1990-11-02")),
+                  eyecolor=c("blue", "green", "brown"))
> d2
  firstname lastname  birthdate eyecolor
1      Mary  Staples 1982-01-05     blue
2      Beth   Wrench 1960-10-25    green
3      Beth   Hammer 1990-11-02    brown
> merge(d1, d2, by=c("firstname", "lastname"))
  firstname lastname income  birthdate eyecolor
1      Beth   Wrench  27200 1960-10-25    green
2      Beth   Hammer  83000 1990-11-02    brown
3      Mary  Staples 111230 1982-01-05     blue
{% endhighlight %}

## Arrays



## String operations

`require(stringr)`

`str_sub(string, start, end)`

## Statistics
