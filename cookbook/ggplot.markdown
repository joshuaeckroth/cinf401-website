---
layout: post
title: "Cookbook: ggplot"
---

# Cookbook: ggplot

*Note: See the corresponding [lecture notes about ggplot](/notes/ggplot.html). This page has cookbook recipes.*

## Cheatsheet

Ou Zheng found this [amazing ggplot cheatsheet](https://www.rstudio.com/wp-content/uploads/2016/11/ggplot2-cheatsheet-2.1.pdf) produced by RStudio.

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

### Jittered points

Sometimes, points lie on top of each other or so close they're hard to see. Sometimes (though rarely), it's a good idea to "jitter" them so they are more separate.

For example, here is a plot of the miles-per-gallon data of engine size (displacement in liters) vs. highway mpg:

![MPG points plot](/images/plot-mpg-points.png)

This code made that plot:

{% highlight r %}
> p <- ggplot(mpg) + geom_point(aes(x=displ, y=hwy))
> ggsave("plot-mpg-points.png", p, width=6, height=4, dpi=100)
{% endhighlight %}

You can't tell, but there are actually lots of points on top of each other. Adding a jitter shows that:

![MPG points plot with jitter](/images/plot-mpg-points-jitter.png)

This code made that plot:

{% highlight r %}
> p <- ggplot(mpg) + geom_point(aes(x=displ, y=hwy), position=position_jitter())
> ggsave("plot-mpg-points-jitter.png", p, width=6, height=4, dpi=100)
{% endhighlight %}

You can use `width` and `height` arguments in `position_jitter()` to indicate how much jitter is allowed horizontally and vertically. Sometimes you want to disable vertical jittering, and only have horizontal jittering. Or vice versa.

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

### Text

Given this iris (flower) data,

{% highlight r %}
> head(iris)
  Sepal.Length Sepal.Width Petal.Length Petal.Width Species
1          5.1         3.5          1.4         0.2  setosa
2          4.9         3.0          1.4         0.2  setosa
3          4.7         3.2          1.3         0.2  setosa
4          4.6         3.1          1.5         0.2  setosa
5          5.0         3.6          1.4         0.2  setosa
6          5.4         3.9          1.7         0.4  setosa
{% endhighlight %}

Produce,

![Iris text](/images/plot-iris-text.png)

Technique:

{% highlight r %}
> p <- ggplot(iris) +
       geom_text(aes(x=Petal.Length, y=Petal.Width, label=Species, color=Species),
                 size=3, position=position_jitter()) +
       guides(color=FALSE)
> ggsave("plot-iris-text.png", p, width=6, height=4, dpi=100)
{% endhighlight %}

- `geom_text()` needs an `x`, `y`, and `label`
- `guides(color=FALSE)` means don't produce a legend for the `color` aesthetic

### Summary plots

Given the movies dataset,

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

![Average movie rating by year](/images/plot-movies-avg-rating-year.png)

Technique:

{% highlight r %}
> p <- ggplot(movies) +
       stat_summary(fun.y=median, geom="line", aes(x=year, y=rating)) +
       scale_x_continuous("Year") +
       scale_y_continuous("Median rating (0-10)")
> ggsave("plot-movies-avg-rating-year.png", p, width=6, height=4, dpi=100)
{% endhighlight %}

Note, you can achieve the same with `aggregate` and plot a normal `geom_line`:

{% highlight r %}
> d <- aggregate(rating ~ year, movies, median)
> ggplot(d) + geom_line(aes(x=year, y=rating))
{% endhighlight %}

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

## 3D Scatterplots

While not actually ggplot, there is a library for 3D scatterplots. Read its [PDF documentation](http://cran.r-project.org/web/packages/scatterplot3d/scatterplot3d.pdf). This library was demonstrated by Christian Micklisch.

Interactive 3D scatter plots can be done as follows. Contributed by Marisa Gomez.

{% highlight r %}
install.packages("rgl")
library(rgl)
with(airquality, plot3d(Ozone, Wind, Temp))
{% endhighlight %}

## Map plots

Using the `ggmap` library, you can plot on maps! For example, we can plot quake data on a map of Fiji. The built-in dataset `quakes` contains lat/long coordinates and quake magnitude:

{% highlight r %}
> head(quakes)
     lat   long depth mag stations
1 -20.42 181.62   562 4.8       41
2 -20.62 181.03   650 4.2       15
3 -26.00 184.10    42 5.4       43
4 -17.97 181.66   626 4.1       19
5 -20.42 181.96   649 4.0       11
6 -19.68 184.31   195 4.0       12
{% endhighlight %}

Using ggmap, we can download a map of Fiji,

{% highlight r %}
> library(ggmap)
> fiji <- get_map("fiji", zoom=4)  # save it so we don't have to download it again
{% endhighlight %}

And then make a plot with the points on top of the map. We'll make the points partially transparent, and their size relative to the quake magnitude:

{% highlight r %}
> p <- ggmap(fiji) + geom_point(data=quakes, aes(x=long, y=lat, size=mag), color="#00000022")
> ggsave("plot-map-fiji-quakes.png", p, width=6, height=4, dpi=100)
{% endhighlight %}

![Fiji quakes](/images/plot-map-fiji-quakes.png)

Or Houston crime data:

{% highlight r %}
> houston <- get_map("Houston", zoom=11)
> p <- ggmap(houston) + geom_point(data=subset(crime, date=="1/2/2010"), aes(x=lon, y=lat, shape=offense))
> ggsave("plot-map-houston-crime.png", p, width=6, height=4, dpi=100)
{% endhighlight %}

![Houston crime](/images/plot-map-houston-crime.png)

A 2d density plot can show you which areas have the most crime.

{% highlight r %}
> p <- ggmap(houston) + stat_density2d(data=crime, aes(x=lon, y=lat))
> ggsave("plot-map-houston-crime-contours.png", p, width=6, height=4, dpi=100)
{% endhighlight %}

![Houston crime contours](/images/plot-map-houston-crime-contours.png)

## googleVis package

Use the `googleVis` package to get interactive visualizations. Contributed by Marisa Gomez.

See this [article](http://journal.r-project.org/archive/2011-2/RJournal_2011-2_Gesmann+de~Castillo.pdf) from the R Journal for details. Marisa's example went as follows:

{% highlight r %}
install.packages("googleVis")
library(googleVis)
library(datasets)
d <- Orange
m <- gvisMotionChart(d, idvar = "Tree", timevar = "age")
plot(m)
{% endhighlight %}

## Animated plots

Contributed by Katie Porterfield.

Use the `caTools` library to save plots as animated GIFs. Use the `animate` library to create the animated plots. [This blog entry](http://www.r-bloggers.com/animations-and-gifs-using-ggplot2/) has some good examples.

## Multidimensional scaling

Plot relative distances in 2D between a bunch of high-dimensional points.

First, compute the all-pairs distances:

{% highlight r %}
myDists <- dist(d)
{% endhighlight %}

Then compute x,y values for each row, keeping the relative distances:

{% highlight r %}
s <- cmdscale(myDists)
{% endhighlight %}

Finally, plot it:

{% highlight r %}
ggplot(data.frame(x=s[,1], y=s[,2])) + geom_point(aes(x=x, y=y))
{% endhighlight %}

Here is an example on the iris dataset, which has 4-dimensional data:

{% highlight r %}
> head(iris)
  Sepal.Length Sepal.Width Petal.Length Petal.Width Species
1          5.1         3.5          1.4         0.2  setosa
2          4.9         3.0          1.4         0.2  setosa
3          4.7         3.2          1.3         0.2  setosa
4          4.6         3.1          1.5         0.2  setosa
5          5.0         3.6          1.4         0.2  setosa
6          5.4         3.9          1.7         0.4  setosa

> myDists <- dist(iris[,1:4])
> s <- cmdscale(myDists)
> head(s)
          [,1]       [,2]
[1,] -2.684126  0.3193972
[2,] -2.714142 -0.1770012
[3,] -2.888991 -0.1449494
[4,] -2.745343 -0.3182990
[5,] -2.728717  0.3267545
[6,] -2.280860  0.7413304

> ggplot(data.frame(x=s[,1], y=s[,2])) + geom_point(aes(x=x, y=y))
{% endhighlight %}

![Iris cmdscale](/images/iris-cmdscale.png)

Here is a better demo. Consider the [distances between several US cities](http://www.mapcrow.info/united_states.html).

Read this into R:

{% highlight r %}
library(XML)
table <- readHTMLTable("http://www.mapcrow.info/united_states.html", colClasses="numeric")$`NULL`[,2:32]
s <- cmdscale(as.matrix(table))
ggplot(data.frame(x=-s[,1], y=s[,2], city=colnames(table))) + geom_text(aes(x=x, y=y, label=city), size=2)
{% endhighlight %}

You get this graph (after flipping the x-axis). Notice that the city's relative locations are correct, since multidimensional scaling tries to respect these distances while arranging the points.

![MDS US Cities](/images/mds-us-cities.png)


## Interactive graphs

Contributed by Malak Patel.

```
library(ggplot2)
library(ggiraph)

#hover effect 
###Part 1
g <- ggplot(mpg, aes( x = displ, y = cty, color = hwy) ) + theme_minimal()
my_gg <- g + geom_point_interactive(aes(tooltip = model), size = 2) 

###Part 2
ggiraph(code = print(my_gg), width = .7)

#hover red effect
my_gg <- g + geom_point_interactive(aes(tooltip = model, data_id = model), size = 2) 
ggiraph(code = print(my_gg), width = .7, hover_css ="cursor:pointer;fill:red;stroke:red;")

#clickable graph
###Part 1
crimes <- data.frame(state = tolower(rownames(USArrests)), USArrests)
head(crimes)

###Part 2
crimes$onclick <- sprintf("window.open(\"%s%s\")",
  "http://en.wikipedia.org/wiki/", as.character(crimes$state) )

gg_crime <- ggplot(crimes, aes(x = Murder, y = Assault, color = UrbanPop )) + 
  geom_point_interactive(aes( data_id = state, tooltip = state, onclick = onclick ), size = 3 ) + 
  scale_colour_gradient(low = "#999999", high = "#FF3333") + 
  theme_minimal()

###Part 3
ggiraph(code = print(gg_crime),
        hover_css = "fill-opacity:.3;cursor:pointer;")

#Zoom effect
 ggiraph(code = print(gg_crime + theme_linedraw()), zoom_max = 5)
 
#https://rstudio.github.io/dygraphs/index.html
#Another visual
library(dygraphs)
lungDeaths <- cbind(mdeaths, fdeaths)
dygraph(lungDeaths)

#Even more detail 
dygraph(lungDeaths) %>% dyRangeSelector()

#3d graph
library(plotly)
plot_ly(z = ~volcano, type = "surface")
```


