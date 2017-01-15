---
layout: post
title: Assignment 1
due: "Wed Jan 25"
categories: [assignments]
---

# Assignment 1

Create a new repository on Bitbucket, called "cinf401-assignment-1", and add me (joshuaeckroth) as a "reader". Then create a new project in RStudio with the same name and a new R Markdown file with the same name. See the [RStudio workflow](/notes/rstudio-workflow.html) for instructions.

Complete the tasks below. Write your answers, intermixed with R code and plots (if/when appropriate), in the single R Markdown file. Commit and push your changes to Bitbucket so I can retrieve them on my own machine. I will regenerate your final report by "knitting" your R Markdown file. See the [syllabus](/notes/syllabus.html) for the general grading rubric.

## Task 1

Find two projects that purport to require "big data analysis." We looked at three case studies in the [big data notes](/notes/big-data.html). Answer these questions about the projects:

- Who is responsible for this project?
- What are the project's aims? What is its contribution to an organization or to society?
- What kind of data will be (or is being) analyzed?
- How much data is there? Estimate this if you are unable to find out precisely.
- Why should the data be considered "big data"? Or should it not?
- What tools will they use (are they using)? This information might not be available, but try to find out.

## Task 2

### Part A

Create a vector of numeric values. Include at least seven values, two of which must be `NA`. Then show how to "normalize" the vector so that the magnitude of the vector (ignoring `NA`'s) is 1.0. Refer to MathWorld for definitions of [normalized vector](http://mathworld.wolfram.com/NormalizedVector.html) and [L2-norm](http://mathworld.wolfram.com/L2-Norm.html).

### Part B

Create a data frame with columns "A", "B", "C", and "D", satisfying the following properties:

- Column A contains logical (`TRUE`/`FALSE`) values.
- Column B contains numeric values.
- Column C contains character values.
- Column D contains dates.

Insert five rows of data. Print/show the data frame.

Next, set the column names of the data frame to "W", "X", "Y", and "Z". Print the data frame again.

Update column X (previously called B) so that all of its numeric values are multiplied by 2. Then print just column X, just rows 2-5 (inclusive).
