---
layout: post
title: Syllabus
---

# Syllabus

CINF 401 - 01, Spring 2017 &mdash; Big Data Mining and Analytics

MWF 11-11:50a Eliz Hall 205; pre-reqs: CSCI 221

Work hard in this class and you should be able to:

- <b>Get a high-paying job mining and analyzing big data.</b>


## About me

- Joshua Eckroth, [jeckroth@stetson.edu](mailto:jeckroth@stetson.edu), [homepage](http://www2.stetson.edu/~jeckroth/)

- Eliz Hall 214-9, 386-740-2519

- Office hours: Mon 2:30-4; Wed 12-1, 2:30-4

## Textbook

This course has no required textbook. All required material can be found on this site or the greater web.

## Grading

<table>
<tr><td>Class demonstrations (2)</td><td>5% each, 10% overall</td></tr>
<tr><td>Assignments (5)</td><td>5% each, 25% overall</td></tr>
<tr><td>Projects 1-4</td><td>10% each, 40% overall</td></tr>
<tr><td>Project 5</td><td>25%</td></tr>
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

All projects are due by 11:59pm on the stated due date, except for the final project, which you will present during our "final exam" time (May 6, 5-7pm). There is no final exam on that day, only these presentations.

### Late work

Due to the complexity of these assignments and the timing of group work, I will only accept late work up to three days late. Late work is penalized 20% each day it is late. After three days, no credit will be given.

### Topics

- Week 1: R fundamentals
- Week 2: R fundamentals
- Week 3: Data munging
- Week 4: Visualization
- Week 5: Statistical analysis
- Week 6: Hadoop, MapReduce
- Week 7: Hadoop, MapReduce
- Week 8: no class (break)
- Week 9: Hadoop, MapReduce
- Week 10: NoSQL, Hive, Pig
- Week 11: Spark
- Week 12: Spark
- Week 13: TBD
- Week 14: TBD
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

- J. Eckroth. "Teaching Future Big Data Analysts: Curriculum and Experience Report." *Proceedings of the 7th NSF/TCPP Workshop on Parallel and Distributed Computing Education (EduPar-17)*, 2017 (in press)


- J. Eckroth. "Teaching Big Data with a Virtual Cluster." *Proceedings of the 47th ACM Technical Symposium on Computing Science Education (SIGCSE)*, pp. 175–180, 2016 ([PDF](http://www2.stetson.edu/~jeckroth/downloads/eckroth-sigcse-2016.pdf), [ACM DL](http://dl.acm.org/citation.cfm?id=2844651))

- J. Eckroth. "Foundations of a Cross-Disciplinary Pedagogy for Big Data." *Journal of Computing Sciences in Colleges* 31(3), pp. 110–118, 2015 ([PDF](http://www2.stetson.edu/~jeckroth/downloads/eckroth-big-data-pedagogy-ccsc-eastern-2015-final.pdf), [ACM DL](http://dl.acm.org/citation.cfm?id=2835394))

