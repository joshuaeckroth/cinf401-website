---
layout: post
title: "Cookbook: Regular expressions"
---

# Cookbook: Regular expressions

## External resources

- [RegViz](http://regviz.org/) -- visualize your regular expressions to see which text matches and does not match. Also shows grouping `()` results in vars like `$1`, `$2`, etc.

- [Java Regex tester](http://www.regexplanet.com/advanced/java/index.html) -- also see the cookbook link at the top

- Great [Regex cheatsheet](http://www.rexegg.com/regex-quickstart.html)


## Regex in Java

{% highlight java %}
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Foo {

    // this pattern will match name field in text like:
    // blah blah blah Name="Jane Doe" blah blah blah
    private Pattern namePattern = Pattern.compile(".*Name=\"(.*?)\".*");

    void myfunc() {
        Matcher m = namePattern.matcher(str);
        if(m.matches()) {
            String name = m.group(1);  // get submatch from parentheses
            // ...
        }
    }
}
{% endhighlight %}

## Find matching portions of lines

Using Perl, print only those portions of lines that match a certain regex, e.g., find links that start with `/site/...` and end with `.xls`:

{% highlight perl %}
#!/usr/bin/perl

# script name: get-links.pl

while(<>) {                                 # for each line of input
    $line = $_;                             # use a better variable name
    while($line =~ m!(/sites/.*?\.xls)!g) { # for each URL pattern on this line (g means 'all')
        print $1."\n";                      # print the part that matched in the regex parens
    }
}
{% endhighlight %}

Run it like this:

{% highlight bash %}
perl get-links.pl < my-file.txt
{% endhighlight %}

Here is the same thing using just `grep` (no Perl):

{% highlight bash %}
grep -oP '/sites/.*?\.xls' my-file.txt
{% endhighlight %}

The grep `-o` option means only print the part that matched the regex, not the whole line that matched (which is the default), and `-P` means use Perl-style regular expressions, which is needed for the `.*?` non-greedy match-anything syntax.