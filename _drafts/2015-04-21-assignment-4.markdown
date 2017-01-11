---
layout: post
title: Assignment 4
due: "May 1, 11:59pm"
categories: [assignments]
---

# Assignment 4

Read [Big Data Ethics](/big-data-ethics-zwitter.pdf) by Andrej Zwitter. Answer the questions below. Make a git repository as usual.

## Scenario 1

K works at a big data analytics firm that offers a new startup company some machine learning technology. This startup company has created a new "social photo sharing" app that allows anonymous users to upload photos, like others' photos, add tags and comments, etc. K's analytics firm has partnered with this startup and offers an automatic photo categorization and recommendation engine. With this technology, users of the app will see recommendations for other photos they might like, and they can browse photos by category (e.g., "travel," "pets," etc.). The startup provides K's firm with lots of example photos and categories, for training purposes, and continually provides logs of users' "likes" in order to continually improve the recommendation engine.

Now, K is just an engineer. But suppose at some point K receives an email from an (anonymous) user X. This user says that something very bad (e.g., child porn, terrorist propaganda, etc.) is being categorized as something benign (e.g., "travel") and in some cases is even recommended to X based on his/her likes. X is shocked and offended and threatens legal action.

- **Who has "moral agency" in this situation?** For this person or persons, give some details about "causality," "knowledge," and "choice," as defined in Zwitter's paper.

- **Who are the big data collectors, utilizers, and generators,** as defined by Zwitter?

- **Define a company policy for K's analytics firm that guides K and the firm towards an appropriate response to these kinds of situations.** Make it clear whether K and/or the big data analytics firm should be proactive (search for bad photos on a regular basis) or just wait for complaints from users. Make it clear who is responsible (the analytics firm and/or the startup company and/or the users). Argue that this policy is a good policy; do not just lay out a series of actions to take without arguing that these are the right actions.

## Scenario 2

Suppose you are tasked with working in a team to create a spam detector (as we are doing in group project 4). Suppose your training data comes from Stetson's email server based on users marking some emails as "spam." The training set consists of about 50% spam and 50% random non-spam messages, from all Stetson email accounts.

**What are the ethical dimensions of building a spam classifier.** What could go wrong? Who could be hurt? Think of three or more situations.

Suppose you designate certain people in your team to be the only people who actually read the original emails (either with algorithms or their own eyes). These people will be responsible for "anonymizing" the emails before they are given to the people who will build and test machine learning models.

What should be done in order to "anonymize" the emails? **Define a process** (e.g., "strip out anything that looks like an email address," and so on; note, it's not going to be that simple).

After you have defined that process, suppose that the people on your team who are responsible for building and testing the classifier tell you, "Hey, you removed too many good features, now we can't classify spam with good enough accuracy. We need more complete training data." **What would you add back that was stripped out in your process?** (Note, the people building the classifier typically can't give you any more information than, "we need better training data.")
