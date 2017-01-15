---
layout: post
title: "Cookbook: Unix tools"
---

# Cookbook: Unix tools

## A virtual machine with all these tools

Contributed by Antonio Ruiz. Download this [VirtualBox image](http://datascienceatthecommandline.com/#dst) to have access to a wide range of command line tools for data science.

## Regular expressions

Regular expressions are not only found in Unix, but a lot of Unix tools use them. For example, `grep`.


## Menagerie of little tools

### cat, head, tail

Concatenate (combine) two or more files:

{% highlight bash %}
cat a.txt b.txt
{% endhighlight %}

You probably want to save the output in another file:

{% highlight bash %}
cat a.txt b.txt > ab.txt
{% endhighlight %}

View just the top 10 lines of a file (first command), or top 5 lines (second command):

{% highlight bash %}
head a.txt
head -n 5 a.txt
{% endhighlight %}

View just the last 10 lines of a file, or variations:

{% highlight bash %}
tail a.txt         # last 10 lines
tail -n 5 a.txt    # last 5 lines
tail -n +5 a.txt   # first 5 lines
{% endhighlight %}

`tail` is also useful to watch a file as it changes, as you would a log file:

{% highlight bash %}
tail -f a.txt
{% endhighlight %}

### grep

- `-o` --- only print matching part, not whole line with the match
- `-P` --- use Perl-style regular expressions (lots of extra features)

## Bash

### Loops

{% highlight bash %}
for VAR in SOME STUFF SEPARATED BY SPACES OR NEWLINES OR TABS; do \
  EXECUTE SOMETHING WITH $VAR; \
  PERHAPS SOMETHING ELSE; \
done
{% endhighlight %}

E.g.,

{% highlight bash %}
for x in my list of stuff separated by spaces; do echo $x; done
{% endhighlight %}

The backtick <code>\`</code> is often useful to execute a command and use its (space- or newline-separated output) to feed into the `for` loop:

{% highlight bash %}
for x in `ls foo*`; do mkdir dir_$x; mv $x dir_$x; done
{% endhighlight %}

## csvkit

- [csvkit download and documentation](http://csvkit.readthedocs.org/en/0.9.0/)

Convert Excel to CSV:

{% highlight bash %}
in2csv foo.xlsx > foo.csv
{% endhighlight %}

Display column names:

{% highlight bash %}
csvcut -n foo.csv
{% endhighlight %}

## wget

`wget` is for downloading web pages or FTP directories.

{% highlight bash %}
wget http://example.com  # download a web page
{% endhighlight %}

Use `--no-check-certificate` if `wget` complains about a certificate (SSL):

{% highlight bash %}
wget --no-check-certificate http://example.com
{% endhighlight %}

## cron

The system `cron` tool (which is always running) allows you to specify commands to run at certain times in the day/week/month. Contributed by Nathan Hilliard.

Start by running `crontab -e`, then specify the time frame and command to run, one per line. See this [quick reference](http://www.adminschoice.com/crontab-quick-reference) for details about that file.


