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

- `[,,]`
- `[,,drop=FALSE]` or `[,,drop=F]`
- `[[]]`

## Iteration

## Reshaping

### merge

### aggregate

### melt

### cast

## String operations

`require(stringr)`

`str_sub(string, start, end)`

## Statistics

