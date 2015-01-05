---
layout: post
title: "Cookbook: Hadoop dev environment"
---

# Cookbook: Hadoop dev environment

## Local dev environment

It is important to be able to test code on your own machine before submitting to a Hadoop cluster. Below are instructions for doing so in Eclipse.

The basic idea is to create a normal Java application but add all the Hadoop JARs to the classpath. Your own code will have the `main()` function, so you run it as a normal Java application.

The Hadoop libraries have been packaged, for your convenience, into a single ZIP file. Download [hadooplibs.zip](). Extract the ZIP. Notice there is a `jars` folder and a `sources` folder. The files in `jars` will be imported as libraries in Eclipse (see below). The files in `sources` provide source code for the Hadoop JARs in the `jars` folder. If Eclipse ever complains about not having the source code for an external Hadoop library, tell it to associate the corresponding JAR in `sources`.

For reference, I copied the JARs from these locations in a fresh Hadoop 2.6.0 distribution ([from here](http://mirrors.advancedhosters.com/apache/hadoop/common/hadoop-2.6.0/), the non-source package), and added them to the ZIP.

- every JAR in `share/hadoop/common/lib`
- every JAR in `share/hadoop/mapreduce`
- every JAR in `share/hadoop/yarn`
- every JAR in `share/hadoop/yarn/lib`
- `hadoop-common-2.6.0.jar` in `share/hadoop/common`

### Eclipse

Create a new Java project, as usual.

![Eclipse 1](/images/eclipse-hadoop-1.png)

In the "Libraries" tab of the new project window, click "Add External JARs..."

![Eclipse 2](/images/eclipse-hadoop-2.png)

Find the `jars` folder in the extracted `hadooplibs` ZIP file. Add every JAR here.

![Eclipse 3](/images/eclipse-hadoop-3.png)

Finish creating the project as usual.

Next, you need to create a Java source file. Right-click on the project name, "WordCountEclipse", and select New > Class.

![Eclipse 4](/images/eclipse-hadoop-4.png)

Name the class `WordCount` (just for this example). Write some code. See the [Map-Reduce demo](/notes/demo-map-reduce.html) for the `WordCount` class source as an example.

![Eclipse 8](/images/eclipse-hadoop-5.png)

Next, you need to create a "run configuration" that tells Eclipse how to execute your code. You already have a `main()` function in your main class, so we can run the code as a typical Java application. However, you do need to specify the input and output folders since the `WordCount` example expects these as inputs to the `main()` function.

Right-click on the project name, "WordCountEclipse", and select Run As > Run Configurations. Create a new run configuration modeled after the "Java Application" template.

![Eclipse 8](/images/eclipse-hadoop-6.png)

Specify the class with the `main()` function. Also name your run configuration.

![Eclipse 6](/images/eclipse-hadoop-7.png)

Click the "Arguments" tab and add the names of the input/output folders as program arguments.

![Eclipse 7](/images/eclipse-hadoop-8.png)

Then close the run configurations window.

Finally, create a folder called `input` (or whatever you named it in the run configuration program parameters), and add some text files in there. The `WordCount` example will read these text files and count the words. Below is a screenshot of some example files.

![Eclipse 8](/images/eclipse-hadoop-9.png)

Now you can run your program. Click the Run button (green/white arrow). If successful, your program will create a directory called `output` (or whatever you named it in the run configuration program parameters), and output a file called `part-r-00000` with word counts. You should also see log messages in the console window at the bottom of Eclipse.

**Note: Your program will crash if the `output` folder already exists. You must delete it before every run.**

![Eclipse 10](/images/eclipse-hadoop-10.png)

You can also debug your program, in the usual way. Here is an example of a breakpoint in the `map()` function. Clicking the Debug button (left of the normal Run button) will open a debug interface where you can inspect function arguments, etc.

![Eclipse debugging](/images/eclipse-hadoop-11.png)

Notice, in this example, that the argument `value` to the `map()` function is the first line of text from the [Iceland Wikipedia page](http://en.wikipedia.org/wiki/Iceland). This text comes from a file in the `input` folder. The `key` variable has the value `0` (not shown in the screenshot).

![Eclipse debugging](/images/eclipse-hadoop-12.png)

## delenn environment

![Network diagram](/images/network-diagram.png)

| Node | Port | Link | Purpose |
| ---- | ---- | ---- | ------- |
| namenode | 50070 | [Link](http://localhost:50070/) | |
| resourcemanager | 8088 | [Link](http://localhost:8088/) | |
| mrjobhistory | 19888 | [Link](http://localhost:19888/) | |
| delenn | 8080 | [Link](http://localhost:8080/) | RStudio lives here |

## SSH configurations

Web interfaces:

- Port 50070
- Port 8088
- Port 19888
- Port 8080

Except for 8080 (RStudio), these are the default ports with an Apache Hadoop installation.

### PuTTY (Windows)

### SSH (OS X, Linux)

Edit the file `~/.ssh/config` and add this to the bottom:

```
Host delenn
  HostName 1.2.3.4  <------ replace
  User jeckroth  <--------- replace
  LocalForward 8080 127.0.0.1:8080
  LocalForward 50070 127.0.0.1:50070
  LocalForward 8088 127.0.0.1:8088
  LocalForward 19888 127.0.0.1:19888
```

Now you can connect in the terminal with the command `ssh delenn`



