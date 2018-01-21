---
layout: post
title: Syllabus
---

# Syllabus

CINF 401 - 01, Spring 2018 &mdash; Big Data Mining and Analytics

MWF 2:30-3:20p Eliz Hall 205; pre-reqs: CSCI 221

This course is a survey of the means of acquiring, storing, accessing and analyzing large data sets. Topics include using common data sources and APIs for acquiring data related to social networks, science, including medicine and health, finance, economics, journalism, government and marketing, storing and accessing data via high performance distributed systems and relational and non-relational databases, and statistical and machine learning algorithms for mining and analyzing data.

## About me

- Joshua Eckroth, [jeckroth@stetson.edu](mailto:jeckroth@stetson.edu), [homepage](http://www2.stetson.edu/~jeckroth/)

- Eliz Hall 214, 386-740-2519

- Office hours: Mon 11a-12p, 3:30-4:30p; Wed 1:30-2:30p, 3:30-4:30p

## Textbook

This course has no required textbook. All required material can be found on this site or the web.

## Grading

<table>
<tr><td>Class demonstrations (2)</td><td>5% each, 10% overall</td></tr>
<tr><td>Assignments (4)</td><td>5% each, 20% overall</td></tr>
<tr><td>Projects 1-5</td><td>10% each, 50% overall</td></tr>
<tr><td>Project 5</td><td>20%</td></tr>
</table>

### Class demonstrations

You will be required to demonstrate (for 5 minutes) a data mining/analysis/programming technique in front of the class, on two separate occasions. You are required to submit to me (via email or git) some notes in Markdown format to add to a cookbook on this site. You will not receive credit if your demonstration is the same as a prior demonstration this semester or a prior demonstration documented in the various cookbooks.

The purpose of these demonstrations is to ensure you are engaged with the material but also to show others a wide variety of techniques for handling data. We will use a wide range of tools in this class, and will not have time to learn most of them in depth. Thus, it will be useful for everyone to learn from each other the various tricks each of us discovers as we munge, analyze, and visualize our datasets.

Demonstrations will happen most weeks, involving maybe 2-5 students each week. We'll establish a schedule early in the semester.

### Grading rubric

Assignments and projects are graded according to the following rubric:

| Category | Points |
| -------- | ------ |
| Completeness | 4pts |
| Clarity of writing & code | 1pt |

Thus, the maximum score you can receive on an assignment or project is 5/5.

### Assignments

Assignments are individual activities, done outside of class or during work days. You will turn in your materials via [git](/cookbook/git.html) and [Bitbucket](http://bitbucket.org). See the [RStudio workflow](/notes/rstudio-workflow.html) and [Hadoop workflow](/notes/hadoop-workflow.html) notes for details.

All assignments are due by 11:59pm on the stated due date.

### Projects

A project can involve 1 or 2 people. These projects are more complex than assignments. You will turn in your materials via [git](/cookbook/git.html) and [Bitbucket](http://bitbucket.org). Only one member of the group needs to submit the code to Bitbucket.

Members of the same group may receive different grades, if I have evidence or a strong belief that not all group members contributed equally.

All projects are due by 11:59pm on the stated due date, except for the final project, which you will present during our "final exam" time. There is no final exam on that day, only these presentations.

### Late work

Due to the complexity of these assignments and the timing of group work, I will only accept late work up to three days late. Late work is penalized 20% each day it is late. After three days, no credit will be given.

### Topics

- Week 1: R fundamentals
- Week 2: R fundamentals
- Week 3: ETL, SQL
- Week 4: ETL, SQL
- Week 5: Visualization
- Week 6: Statistics
- Week 7: Benchmarking
- Week 8: no class (break)
- Week 9: Hadoop, MapReduce
- Week 10: Hadoop, MapReduce
- Week 11: Hive, NoSQL
- Week 12: Storm
- Week 13: Spark
- Week 14: OpenCV
- Week 15: TBD
- Week 16: TBD

Assignment and project due dates:

<ul>
{% for p in site.pages sort_by:title order:ascending %}
{% if p.categories contains 'assignments' %}
<li>
<a href="{{ p.url }}">{{ p.title }}</a>, due {{ p.due }}
</li>
{% endif %}
{% endfor %}
</ul>

## Honor code

You are allowed to use a small amount of code from websites (assuming the code is open source). You must indicate where you got the code (put comments in the code). More than 50% of your work or your group's combined work must be original.

I am strongly in agreement with the [Stetson University Honor Code](http://www.stetson.edu/other/honor-system/). Any form of cheating is not acceptable, will not be tolerated, and could lead to dismissal from the University.

## Academic success center

If a student anticipates barriers related to the format or requirements of a course, she or he should meet with the course instructor to discuss ways to ensure full participation. If disability-related accommodations are necessary, please register with the Academic Success Center (822-7127; [www.stetson.edu/asc](http://www.stetson.edu/asc)) and notify the course instructor of your eligibility for reasonable accommodations. The student, course instructor, and the Academic Success Center will plan how best to coordinate accommodations. The Academic Success Center is located at 209 E Bert Fish Drive, and can be contacted using the email address [asc@stetson.edu](mailto:asc@stetson.edu).

## Publications related to this course

- J. Eckroth. "Teaching Future Big Data Analysts: Curriculum and Experience Report." *Proceedings of the 7th NSF/TCPP Workshop on Parallel and Distributed Computing Education (EduPar-17)*, pp. 346-351, 2017 ([PDF](http://www2.stetson.edu/~jeckroth/downloads/eckroth-edupar-17.pdf), [IEEE](http://ieeexplore.ieee.org/abstract/document/7965066/))


- J. Eckroth. "Teaching Big Data with a Virtual Cluster." *Proceedings of the 47th ACM Technical Symposium on Computing Science Education (SIGCSE)*, pp. 175–180, 2016 ([PDF](http://www2.stetson.edu/~jeckroth/downloads/eckroth-sigcse-2016.pdf), [ACM DL](http://dl.acm.org/citation.cfm?id=2844651))

- J. Eckroth. "Foundations of a Cross-Disciplinary Pedagogy for Big Data." *Journal of Computing Sciences in Colleges* 31(3), pp. 110–118, 2015 ([PDF](http://www2.stetson.edu/~jeckroth/downloads/eckroth-big-data-pedagogy-ccsc-eastern-2015-final.pdf), [ACM DL](http://dl.acm.org/citation.cfm?id=2835394))

