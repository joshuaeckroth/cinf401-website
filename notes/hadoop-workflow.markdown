---
layout: post
title: Hadoop workflow
---

# Hadoop workflow

## Local dev environment

See the [Hadoop dev env](/notes/hadoop-dev-env.html) notes for downloading and setting up the necessary libraries.

The steps below are required for every new Hadoop application you create.

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

Name the class `WordCount` (just for this example). Write some code. See the [MapReduce demo](/notes/demo-map-reduce.html) for the `WordCount` class source as an example.

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

**Note: Your program will crash if the `output` folder already exists. You must delete it before every run.** If using HDFS, you can use a command like the following (change the username!):

```
hdfs dfs -rm -r /users/jeckroth/wordcount/output
```

![Eclipse 10](/images/eclipse-hadoop-10.png)

You can also debug your program, in the usual way. Here is an example of a breakpoint in the `map()` function. Clicking the Debug button (left of the normal Run button) will open a debug interface where you can inspect function arguments, etc.

![Eclipse debugging](/images/eclipse-hadoop-11.png)

Notice, in this example, that the argument `value` to the `map()` function is the first line of text from the [Iceland Wikipedia page](http://en.wikipedia.org/wiki/Iceland). This text comes from a file in the `input` folder. The `key` variable has the value `0` (not shown in the screenshot).

![Eclipse debugging](/images/eclipse-hadoop-12.png)

## delenn environment

First, put some files into HDFS:

```
hdfs dfs -mkdir -p /users/jeckroth/wordcount/input
hdfs dfs -put input/* /users/jeckroth/wordcount/input
```

Or, you can use existing data, such as: [`/data/westburylab-usenet/WestburyLab.NonRedundant.UsenetCorpus.txt`](http://localhost:9000/hadoop/namenode:50070/explorer.html#/data/westburylab-usenet)

Copy your JAR file to delenn. If you get an error about the Java version, you'll need to compile your code on delenn.

### How to compile on delenn

Copy your source files, and compile them as follows:

```
javac -cp `hadoop classpath`:. *.java
```

Then make a JAR (name your JAR whatever you want; `wc.jar` is used here):

```
jar cfv wc.jar *.class
```

### Submit the job

Submit the job as follows:

```
yarn jar wc.jar WordCount /users/jeckroth/wordcount/input /users/jeckroth/wordcount/output
```

In the command above, `WordCount` is the class with the `main()` function, and the rest (the file paths) are just arguments to `main()`. Your `main()` may have different kinds of arguments, or no arguments.

Monitor your job's status on the ResourceManager's [web interface](http://localhost:9000/hadoop/resourcemanager:8088/cluster).

When it's done, get the output out of HDFS:

```
hdfs dfs -get /users/jeckroth/wordcount/output/part-r-00000
```

...or use the [web interface](http://localhost:9000/hadoop/namenode:50070/explorer.html#/).

## londo environment

Since londo is not really running a cluster (virtual or otherwise), you will not use HDFS on londo. Just run YARN directly, after transferring your JAR or compiling it on londo:

```
yarn jar wc.jar WordCount my-input.txt output-folder
```

