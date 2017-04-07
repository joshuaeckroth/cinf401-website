---
layout: post
title: "Cookbook: R"
---

# Cookbook: R

*Note: See the corresponding [lecture notes about R](/notes/r.html). This page has cookbook recipes.*

## External resources

- Someone else's [Cookbook for R](http://www.cookbook-r.com/)

## Avoiding console spamming, setting console width

```R
> getOption("max.print")
[1] 99999
> options(max.print = 100)
> getOption("max.print")
[1] 100
```

```R
> getOption("width")
[1] 80
> options(width = 120)
> getOption("width")
[1] 120
```

## Running examples from documentation

Most functions have examples at the bottom when you read their help pages (e.g., `?prop.test`). You can run these examples with the `example()` function:

{% highlight r %}
> example(prop.test)

prp.ts> heads <- rbinom(1, size = 100, prob = .5)

prp.ts> prop.test(heads, 100)          # continuity correction TRUE by default

	1-sample proportions test with continuity correction

data:  heads out of 100, null probability 0.5
X-squared = 1.21, df = 1, p-value = 0.2713
alternative hypothesis: true p is not equal to 0.5
95 percent confidence interval:
 0.4573588 0.6579781
sample estimates:
   p
0.56


prp.ts> prop.test(heads, 100, correct = FALSE)

	1-sample proportions test without continuity correction

data:  heads out of 100, null probability 0.5
X-squared = 1.44, df = 1, p-value = 0.2301
alternative hypothesis: true p is not equal to 0.5
95 percent confidence interval:
 0.4622810 0.6532797
sample estimates:
   p
0.56


prp.ts> ## Data from Fleiss (1981), p. 139.
prp.ts> ## H0: The null hypothesis is that the four populations from which
prp.ts> ##     the patients were drawn have the same true proportion of smokers.
prp.ts> ## A:  The alternative is that this proportion is different in at
prp.ts> ##     least one of the populations.
prp.ts>
prp.ts> smokers  <- c( 83, 90, 129, 70 )

prp.ts> patients <- c( 86, 93, 136, 82 )

prp.ts> prop.test(smokers, patients)

	4-sample test for equality of proportions without continuity
	correction

data:  smokers out of patients
X-squared = 12.6004, df = 3, p-value = 0.005585
alternative hypothesis: two.sided
sample estimates:
   prop 1    prop 2    prop 3    prop 4
0.9651163 0.9677419 0.9485294 0.8536585
{% endhighlight %}

## Reading files

### CSV files

```R
read.csv("foo.csv", strip.white=TRUE)
```

### Load big datasets across lots of CSV files

Contributed by Matt Samuels.

{% highlight r %}
library(data.table)
library(bit64)​
path <- "/bigdata/data/backblaze/2014/"
fileList <- list.files(path=path, pattern="*.csv")
dt <- lapply(fileList, function(x) {tt <- fread(paste(path, x, sep=""), header=TRUE, sep=",")})
d <- rbindlist(dt)
{% endhighlight %}



## Clean up ZIP codes

Chop off +4 portion:

{% highlight R %}
> require(stringr)
> d$Zip <- str_sub(d$Zip.Code, start=0, end=5)
{% endhighlight %}

## Convert a date to season

From [Josh O'Brien on StackOverflow](http://stackoverflow.com/a/9501225)

{% highlight R %}
getSeason <- function(DATES) {
    WS <- as.Date("2012-12-15", format = "%Y-%m-%d") # Winter Solstice
    SE <- as.Date("2012-3-15",  format = "%Y-%m-%d") # Spring Equinox
    SS <- as.Date("2012-6-15",  format = "%Y-%m-%d") # Summer Solstice
    FE <- as.Date("2012-9-15",  format = "%Y-%m-%d") # Fall Equinox

    # Convert dates from any year to 2012 dates
    d <- as.Date(strftime(DATES, format="2012-%m-%d"))

    ifelse (d >= WS | d < SE, "Winter",
      ifelse (d >= SE & d < SS, "Spring",
        ifelse (d >= SS & d < FE, "Summer", "Fall")))
}
{% endhighlight %}

```R
my.dates <- as.Date("2011-12-01", format = "%Y-%m-%d") + 0:60
head(getSeason(my.dates), 24)
  [1] "Fall"   "Fall"   "Fall"   "Fall"   "Fall"   "Fall"   "Fall"
  [8] "Fall"   "Fall"   "Fall"   "Fall"   "Fall"   "Fall"   "Fall"
 [15] "Winter" "Winter" "Winter" "Winter" "Winter" "Winter"
```

## Convert a vector into "bins"

The `cut` function allows you to rewrite a vector of values (as you might find in a data frame column) into particular "bins". The example below takes a vector of values between 2 and 14 and returns a vector with each value replaced with the corresponding bin. You can set the bin limits ("breaks") to whatever you want. You can also create labels for the bins; be sure you create the right number of labels for all the bin intervals.

{% highlight r %}
> 2:14
 [1]  2  3  4  5  6  7  8  9 10 11 12 13 14
> cut(2:14, breaks=c(0, 5, 10, Inf), labels=c("0-5", "5-10", "10+"))
 [1] 0-5  0-5  0-5  0-5  5-10 5-10 5-10 5-10 5-10 10+  10+  10+  10+
Levels: 0-5 5-10 10+
{% endhighlight %}

## "Transpose" a data frame

E.g., turn `InsectSprays` into a form with A, B, C, ... columns. Contributed by Matt Samuels. Later updated to handle several `vid` columns.

{% highlight r %}
data.frame.wide <- function(x, vid, vmeasure, long_col){
    x$id <- c(1:nrow(x))
    xmelt <- melt(x, cbind(c("id"), vid, c(long_col)), vmeasure)
    xc <- dcast(xmelt, as.formula(paste(paste(c("id", vid), collapse="+"), "~", long_col)))
    newcols <- as.vector(unique(x[[long_col]]))
    tmpd1 <- data.frame(xc[!is.na(xc[[newcols[1]]]),vid])
    colnames(tmpd1) <- vid
    return(cbind(tmpd1, lapply(newcols, function(c) {
      tmpd2 <- data.frame(na.omit(xc[[c]]));
      colnames(tmpd2) <- c(c);
      return(tmpd2);
    })))
}
{% endhighlight %}

Usage:

{% highlight r %}
> data.frame.wide(InsectSprays, c("spray"), c("count"), "spray")
    A  B C  D E  F
1  10 11 0  3 3 11
2   7 17 1  5 5  9
3  20 21 7 12 3 15
4  14 11 2  6 5 22
5  14 16 3  4 3 15
6  12 14 1  3 6 16
7  10 17 2  5 1 13
8  23 17 1  5 1 10
9  17 19 3  5 3 26
10 20 21 0  5 2 26
11 14  7 1  2 6 24
12 13 13 4  4 4 13
{% endhighlight %}

## Run Python code from R

Contributed by Christian Decker.

{% highlight r %}
install.packages("rPython")
library(rPython)
python.load("script.py") # loads and runs the script
python.get("x") # read a global variable from the script
d <- data.frame(python.get("varname")) # read a dict as a dataframe
python.exec("import math")
pi <- python.exec("math.pi")
{% endhighlight %}

You can also call R code from Python using the Python module [rpy2](http://rpy.sourceforge.net/).

## Shiny

Contributed by Michael Clay.

{% highlight r %}
library(ggplot2)
library(reshape2)

inputPanel(
  numericInput("rows", "Rows", 5)
)

renderTable({
  head(tips, input$rows)
})

inputPanel(
  selectInput("type", label="Column", choices=c("size", "tip", "day", "time", "sex", "smoker", "total_bill")),
  sliderInput("binwidth", label="Binwidth", min=0.2, max=2, value=1, step=0.2)
)

renderPlot({
  ggplot(tips) + geom_histogram(aes_string(x=input$type), binwidth=input$binwidth)
})

inputPanel(
  selectInput("type2", label="Column", choices=c("size", "tip"))
)

renderPlot({
  ggplot(tips) + geom_smooth(aes_string(x="total_bill", y=input$type2))
})
{% endhighlight %}

## Time/date manipulation

Contributed by Tom Wright.

{% highlight r %}
install.packages("chron")
library(chron)

dates(...)
times(...)
chron(...) # associate times with dates
leap.year(x)
day.of.week(x)
days(x)
months(x)
weekdays(x)
quarters(x)
years(x)
hours(x)
minutes(x)
seconds(x)
{% endhighlight %}

## Graph (network) manipulation

Contributed by John Salis.

{% highlight r %}
library(igraph)
g <- graph(c(1, 2, 2, 3, 3, 4, 2, 4))
plot(g)
g <- g + vertices(c(5, 6))
g[] # yields adjacency matrix
V(g) # vertices
E(g) # edges
g <- erdos.renyi.game(32, 0.2) # create a random graph
g$layout <- layout.circle # set the layout when plotting
g <- graph.lattice(c(16, 16)) # create a lattice graph
E(g)$weight <- runif(ecount(g), 1, 8) # give random weights to edges
plot(g, vertex.label=NA, edge.width=E(g)$weight) # plot a graph
{% endhighlight %}

## GUIs with Tcl/Tk

Contributed by George Robbins.

Use the `tcltk` library to show GUIs in R. [Here](http://thebiobucket.blogspot.com/2012/08/tcltk-gui-example-with-variable-input.html) is a good example, and [here](http://www.sciviews.org/_rgui/tcltk/) are lots more examples.

## Run queries against Google BigQuery

Contributed by Jacob Hell.

Google BigQuery is a SQL-like query system for huge datasets, stored on Google servers. Read more about [BigQuery](https://cloud.google.com/bigquery/what-is-bigquery).

Here are some resources:

- [The R package](https://github.com/hadley/bigrquery)
- [A tutorial](http://thinktostart.com/using-google-bigquery-with-r/)
- [A longer example for e-commerce](http://www.lunametrics.com/blog/2014/06/25/google-analytics-data-mining-bigquery-r/)

## Read an image into a matrix

{% highlight r %}
library(jpeg)
img <- readJPEG(file)
{% endhighlight %}

The `img` matrix will have dimensions (given by `dim(img)`) like:

{% highlight r %}
> dim(img)
500 375 3
{% endhighlight %}

...meaning: 500 height, 375 width, 3 color channels (RGB).

To get particular pixel color values (for RGB separately):

{% highlight r %}
> img[250, 100, 1] # this is y=250, x=100, red
> img[250, 100, 2] # this is y=250, x=100, green
> img[250, 100, 3] # this is y=250, x=100, blue
{% endhighlight %}

You can convert the matrix to a data frame if you'd like, with each row representing a different pixel (x, y, r, g, b):

{% highlight r %}
# from: http://www.r-bloggers.com/r-k-means-clustering-on-an-image/
image.to.data.frame <- function(img) {
    # Obtain the dimension
    imgDm <- dim(img)

    # Assign RGB channels to data frame
    data.frame(
            x = rep(1:imgDm[2], each = imgDm[1]),
            y = rep(imgDm[1]:1, imgDm[2]),
            R = as.vector(img[,,1]),
            G = as.vector(img[,,2]),
            B = as.vector(img[,,3])
            )
}
{% endhighlight %}

## Crop or resize an image

Assuming you've loaded an image into a matrix, as above, you can crop very easily:

{% highlight r %}
> imgCropped <- img[topY:bottomY,leftX:rightX,]
{% endhighlight %}

Note, in computer graphics, the y-coordinate increases as you go down the screen. So `topY < bottomY` in numeric value.

To resize, use the function below. Note, it needs the `EBImage` library which must be installed like so:

{% highlight r %}
# get EBImage with this code:
source("http://bioconductor.org/biocLite.R")
biocLite("EBImage")
{% endhighlight %}


This function takes an argument giving the maximum width or height (if the image is taller than wide, it will resize so the height is the specified `rsize`; and vice versa):

{% highlight r %}
library(EBImage)

# from: http://www.mepheoscience.com/colourful-ecology-part-1-
#   extracting-colours-from-an-image-and-selecting-them-using-
#   community-phylogenetics-theory/
resize.image <- function(img, rsize=100) {
  if (max(dim(img)[1:2]) > rsize) {
    if (dim(img)[1] > dim(img)[2]) {
      img <- resize(img, w = rsize)
    } else {
      img <- resize(img, h = rsize)
    }
  } else {
    img
  }
}
{% endhighlight %}

## Plot cats

Yeah.

{% highlight r %}
# get EBImage with this code:
# source("http://bioconductor.org/biocLite.R")
# biocLite("EBImage")

library(raster)
library(jpeg)
library(data.table)
library(bit64)
library(stringr)
library(EBImage)
library(reshape2)
library(ggplot2)
library(plyr)
library(fpc)

plotCats <- function(dfeatClusters)
{
    s <- cmdscale(dist(dfeatClusters[,c("color1", "color2")]))
    sdf <- data.frame(x=s[,1], y=s[,2], cluster=dfeatClusters$cluster,
             file=dfeatClusters$file, cat=str_sub(dfeat$file, 5),
             leftEar3Y=dfeat$leftEar3Y, leftEar3X=dfeat$leftEar3X,
             rightEar1X=dfeat$rightEar1X, leftEar1Y=dfeat$leftEar1Y,
             color1=rgb(dfeat$R_1, dfeat$G_1, dfeat$B_1),
             color2=rgb(dfeat$R_2, dfeat$G_2, dfeat$B_2),
             color3=rgb(dfeat$R_3, dfeat$G_3, dfeat$B_3))
    sdfmelt <- melt(sdf, c("x", "y", "cat"), c("file"))
    minX <- min(sdf$x)
    maxX <- max(sdf$x)
    minY <- min(sdf$y)
    maxY <- max(sdf$y)
    xsize <- (maxX-minX)/60.0
    ysize <- (maxY-minY)/60.0
    sdf$labelx1 <- sdf$x-xsize/4
    sdf$labely1 <- sdf$y+ysize
    sdf$labelx2 <- sdf$x
    sdf$labely2 <- sdf$y+ysize
    sdf$labelx3 <- sdf$x+xsize/4
    sdf$labely3 <- sdf$y+ysize
    count <- nrow(sdf)
    p <- ggplot(sdf[1:count,], aes(x=x, y=y)) +
        mapply(function(xx, yy, file, leftEar3Y, leftEar3X, rightEar1X, leftEar1Y) {
                img <- readJPEG(as.character(file))
                if(leftEar3Y < 0 && leftEar1Y > leftEar3Y) {
                    tmp = -leftEar3Y
                    leftEar3Y = -leftEar1Y
                    leftEar1Y = tmp
                }
                if(leftEar1Y < leftEar3Y) {
                    tmp = leftEar3Y
                    leftEar3Y = leftEar1Y
                    leftEar1Y = tmp
                }
                imgCropped <- img[max(1,leftEar3Y):(leftEar1Y+5),leftEar3X:rightEar1X,]
                imgResized <- resize.image(imgCropped, 100)
                xmin <- xx-xsize/2.0
                xmax <- xx+xsize/2.0
                ymin <- yy-ysize/2.0
                ymax <- yy+ysize/2.0
                annotation_raster(imgResized, xmin, xmax, ymin, ymax)
            },
            sdf[1:count,]$x, sdf[1:count,]$y, sdf[1:count,]$file,
            sdf[1:count,]$leftEar3Y, sdf[1:count,]$leftEar3X,
            sdf[1:count,]$rightEar1X, sdf[1:count,]$leftEar1Y) +
        geom_text(aes(x=labelx1, y=labely1, label=cluster, color=color1), size=4) +
        geom_text(aes(x=labelx2, y=labely2, label=cluster, color=color2), size=3) +
        geom_text(aes(label=file), size=1) +
        scale_color_identity(guide=FALSE)
    ggsave("cat-clusters.pdf", p, dpi=300, w=20, h=20)
    p
}
{% endhighlight %}

## dplyr SQL-style queries

Contributed by Chris Finkle.

Yet another package developed by Hadley Wickham, dplyr is a data manipulation tool designed specifically to work with dataframes and even SQL databases (hence the d in the name).

It has five main functionalities which work in a similar manner to the functions we're used to, but have better names and more intuitive inputs:

- Filter (pulls out rows that meet criteria)
- Select (pulls out specified columns; think SQL)
- Arrange (puts things in a specified order)
- Mutate (creates new values based on old ones)
- Summarise (like Aggregate, but cooler)
- Group\_by (used with Summarise)

The other big selling point is the pipe-style input using %>%, which increases R code's readability and flexibility.

Let's see some examples! First, how about Filter? Let's look at the reshape library's Tips dataset. Suppose we want to look at only the rows in which there were smokers in the party.

```
suppressMessages(library(reshape))
suppressMessages(library(dplyr))

head(tips)

filter(tips, smoker=="Yes") %>%
  head()
```

What about just on the weekdays?

```
filter(tips, smoker=="Yes", day %in% c("Thu", "Fri")) %>%
  head()
```

You'll notice that in the previous example I used 'head' with the %>% pipe. This can be used to chain together an arbitrary number of commands, even ones that aren't in dplyr. Let's demonstrate Select using it. Suppose we only care about total_bill, tip, party size, and time of the meal, and then only when the bill was more than $20 or the tip was more than $3 (for some reason).

```
tips %>%
  select(total_bill, tip, size, time) %>%
  filter(total_bill > 20 | tip > 3) %>%
  head()
```

Now what if we want to arrange them in descending order of tip amount? Clearly such rearrangements have created problems in the past, since we needed a whole blog post to do them on assignment three. Not so here! Arrange is very straightforward:

```
tips %>%
  select(total_bill, tip) %>%
  arrange(desc(tip)) %>%
  head()
```

(okay so if you want to do absolutely anything with preservation of rownames, no dice. But rownames suck anyway).

Now let's try mutating. Remember, Mutate produces new values based on the ones you already have. An obvious choice on this dataset is finding what percentage the party tipped.

```
#this just prints the new result
tips %>%
  select(total_bill, tip) %>%
  mutate(percentage = tip/total_bill * 100) %>%
  head()

#this stores it
pct_tips <- tips %>% mutate(percentage = tip/total_bill*100)
head(pct_tips)
```

Finally, let's look at Summarise and Group_by, which can do a lot of the heavy lifting we've relied on melt, dcast, and aggregate for. Specifically, let's try doing a couple of the practice problems on the R page on the website. (The ones with aggregate as the recommended method)

```
#Dataframe 1
tips %>%
  group_by(sex, day) %>%
  summarise_each(funs(mean), total_bill, tip) %>%
  head()

#Dataframe 2
tips %>%
  group_by(sex, day) %>%
  summarise(num_tips = n()) %>%
  arrange(day) %>%
  head()

#or (simpler but omits column name)
tips %>%
  group_by(sex, day) %>%
  tally() %>%
  arrange(day) %>%
  head()

#You can even do some mild reshaping by using group_by without summarise: behold data frame 4
tips %>%
  group_by(day) %>%
  select(smoker) %>%
  table() %>%
  head()
```

There are many more things you can do with dplyr, including window functions (which return a vector of values, e.g. lag or lead functions, cumulative aggregates), random samples, and connecting to proper SQL databases (select and filter commands can be converted directly into SQL by piping them into %>% explain(). It's really neat!). For more information and useful links, visit [this tutorial](http://rpubs.com/justmarkham/dplyr-tutorial) that I pretty much ripped off wholesale for this presentation.

## MySQL connections

Contributed by Matt Klumb.

```
library("RMySQL") 
#Connects to database this example uses a local. 
mydb = dbConnect(MySQL(), user='root', password='password', dbname='R', host='127.0.0.1', port=3305)
#Executes Query to pull information form database.
q1 <- dbSendQuery(mydb, "select * from student")
data <- fetch(q1)
data
##   id student_firstname student_lastname student_degreeID
## 1  1              Bill            Lopez                2
## 2  2             Frank          Johnson                2
## 3  3             Roger           Dogger                1
## 4  4              Mike          Trueman                1
q2 <- dbSendQuery(mydb, "select student_lastname from student")
data2 <- fetch(q2)
data2
##   student_lastname
## 1            Lopez
## 2          Johnson
## 3           Dogger
## 4          Trueman
#Executes left join to pull data from two tables.
q3 <- dbSendQuery(mydb, "select student_firstname, degree_name FROM student LEFT JOIN degree ON student.student_degreeID = degree.degree_Id;")
data3 <- fetch(q3)
data3
##   student_firstname degree_name
## 1             Roger         CIS
## 2              Mike         CIS
## 3              Bill        HIST
## 4             Frank        HIST
#Sends information into the Database using insert.
q4 <- dbSendQuery(mydb, "INSERT INTO student VALUES (NULL,'Mike','Trueman',1);")
data
##   id student_firstname student_lastname student_degreeID
## 1  1              Bill            Lopez                2
## 2  2             Frank          Johnson                2
## 3  3             Roger           Dogger                1
## 4  4              Mike          Trueman                1
dbDisconnect(mydb)
## [1] TRUE
```

## Audio processing

Contributed by Isaac Sarmiento.

```
library(tuneR)
```

Sound with 440Hz and followed by 220Hz:

```
Wav <- bind(sine(440), sine(220))
show(Wav)
plot(Wav)
plot(extractWave(Wav, from = 1, to = 500))
waspec <- periodogram(Wav,normalize=T,width=64)
```

The colors represent the most important acoustic peaks for a given time frame, with red representing the highest energies, then in decreasing order of importance, orange, yellow, green, cyan, blue, and magenta, with gray areas having even less energy and white areas below a threshold decibel level.

```
image(waspec,ylim=c(0,1500))
```

Now for MP3

```
mp <- readMP3("Tribe.mp3")
mp
summary(mp)
plot(mp)

mpmono <- mono(mp,"right")
dmpmono <-downsample(mpmono,20000)

summary(dmpmono)

wmp <- periodogram(dmpmono,normalize=T,width=64)

image(wmp,ylim=c(0,2000))
```

## Twitter with R

Contributed by Ou Zheng.

```
#key words

library(twitteR)
options(httr_oauth_cache=T)
api_key <- "..."
api_secret <- "..."
access_token <- "..."
access_token_secret <- "..."
setup_twitter_oauth(api_key, api_secret, access_token, access_token_secret)
searchTwitter("iphone")

#user

library(twitteR)
Tweets <- userTimeline("realDonaldTrump", n=100)
head(Tweets)

#for follower

library(twitteR)
StetsonU <- getUser('StetsonU')
follow.su <- StetsonU$getFollowers(n=500)
df.su <- do.call('rbind',lapply(follow.su,as.data.frame))

#statusesCount 
#followersCount 
#friendsCount 
#created 

df.sub <- subset(df.su,friendsCount<2300 & followersCount<3000 & statusesCount<10000)
#df.sub <- df.su
df.sub$time <- as.Date(df.sub$created)
df.sub$ntime <- as.numeric(df.sub$time)
library(ggplot2)
p <- ggplot(df.sub,aes(x=time))
p + geom_histogram(fill='red',colour='black',binwidth=30)


p <- ggplot(df.sub,aes(x=friendsCount))
p + geom_histogram(fill='red',colour='black',binwidth=30)


p <- ggplot(data=df.sub,aes(x=friendsCount,y=followersCount))
p + geom_point(aes(size=statusesCount,colour=ntime),alpha=0.8)

#source

library(dplyr)
library(purrr)
library(twitteR)
api_key <- "..."
api_secret <- "..."
access_token <- "..."
access_token_secret <- "..."
setup_twitter_oauth(api_key, api_secret, access_token, access_token_secret)

# We can request only 3200 tweets at a time; it will return fewer
# depending on the API
trump_tweets <- userTimeline("realDonaldTrump", n = 100)
trump_tweets_df <- tbl_df(map_df(trump_tweets, as.data.frame))
library(tidyr)

#We clean this data a bit, extracting the source application. (We’re looking
#only at the iPhone and Android tweets- a much smaller number are from the web
#client or iPad).

tweets <- trump_tweets_df %>%
  select(id, statusSource, text, created) %>%
  extract(statusSource, "source", "Twitter for (.*?)<") %>%
  filter(source %in% c("iPhone", "Android"))

library(lubridate)
library(scales)
library(ggplot2)

#Overall, this includes 628 tweets from iPhone, and 762 tweets from Android.
#One consideration is what time of day the tweets occur, which we’d expect to
#be a “signature” of their user. Here we can #certainly spot a difference:

tweets %>%
  count(source, hour = hour(with_tz(created, "EST"))) %>%
  mutate(percent = n / sum(n)) %>%
  ggplot(aes(hour, percent, color = source)) +
  geom_line() +
  scale_y_continuous(labels = percent_format()) +
  labs(x = "Hour of day (EST)",
       y = "% of tweets",
       color = "")
```

## String operations

###Basic String Operations

Contributed by Chris Finkle.

In R, the default handling of strings is not terribly easy or intuitive. Stringr makes things better. It has some basic string functionalities and also makes using regex much easier.

`str_c` replicates R's `paste` functionality but with some added options.

```
library(stringr)

str_c("Letter: ", letters)
str_c("Letter", letters, sep = ": ")
str_c(letters, " is for", "...")
str_c(letters[-26], " comes before ", letters[-1])

str_c(letters, collapse = "")
str_c(letters, collapse = ", ")
```

`str_sub` is like `substr` except it understands negative indices (you may be familiar with these from Python)

```
ex <- "This is an example string"

str_sub(ex, 5, 10)

str_sub(ex, -1)
str_sub(ex, -10)
str_sub(ex, end = -10)
```

`str_trim` removes whitespace from either end; `str_pad` adds it (cf. the infamous Leftpad)

###RegEx

`str_detect` returns a logical vector based on detection (or not) of a specified pattern.

```

ex <- c("This", "is", "an", "example", "vector")

str_detect(ex, "is")
str_detect(ex, "^[aeiou]")
```

`str_locate` works much like `regexpr`, returning a numeric matrix with the indices at which patterns occur.

```
ex <- c("This", "is", "an", "example", "vector", "ooooh")

str_locate(ex, "is")

#str_locate_all works like gregexp and returns a list of matrices for each string searched
str_locate_all(ex, "[aeiou]+")
```

`str_extract` actually takes out the matching pattern. `str_match` does so using capture groups. `str_replace` replaces the matching text.

```
ex <- c("Star Wars", "Battlestar Galactica", "The secret of Eckroth's server names is they're Babylon 5 characters", "Star Trek")

str_extract(ex, "[Ss]tar ([:alpha:]+)")

str_match(ex, "[Ss]tar ([:alpha:]+)") #[,1] is the whole pattern, [,2] is the first capture group

str_replace(ex, "[Ss]tar ([:alpha:]+)", "Star Wars Holiday Special")
```
