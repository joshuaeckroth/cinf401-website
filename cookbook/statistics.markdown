---
layout: post
title: "Cookbook: Statistics"
---

# Cookbook: Statistics

*Note: See the corresponding [lecture notes about statistics](/notes/statistics.html). This page has cookbook recipes.*

## Textbook: R for Everyone

- Chapters 14-20

## Tutorials

- [Here is a good one](http://math.arizona.edu/~ghystad/tutorial.html)


## Outlier/anomaly detection

https://blog.twitter.com/2015/introducing-practical-and-robust-anomaly-detection-in-a-time-series

## Forecasting

Contributed by Matt Klumb.

```
library(forecast)
library(ggplot2)
library(datasets)

#Forecast showing use of STL,ARFIMA,ETS and Holt’s Exponential Smoothing
# Forecasting with STL useful for seasonal data. STL will handle any type of seasonality, not only monthly and quarterly data.
plot(USAccDeaths)
USAccDeaths %>%
  stlm(modelfunction=ar) %>%
  forecast(h=36) %>%
  autoplot

# ARFIMA forecasts can handle both seasonality and cyclic behaviour.
library(fracdiff)
x <- fracdiff.sim( 100, ma=-.4, d=.3)$series
arfima(x) %>%
  forecast(h=30) %>%
  autoplot

#ETS model allows for seasonality but not cyclicity
AirPassengers %>%
  stlf(lambda=0) %>%
  autoplot

#Holt’s Exponential Smoothing useful for no seasonality. Works by using two paramaters alpha, for the estimate of the level at the current time point, and beta for the estimate of the slope b of the trend component at the current time point.
skirts <- scan("http://robjhyndman.com/tsdldata/roberts/skirts.dat",skip=5)
skirtsseries <- ts(skirts,start=c(1866))
skirtsseriesforecasts <- HoltWinters(skirtsseries, gamma=FALSE)
#skirtsseriesforecasts2 <- forecast.HoltWinters(skirtsseriesforecasts, h=19)
plot(skirtsseriesforecasts)
```

