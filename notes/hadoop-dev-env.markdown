---
layout: post
title: Hadoop dev environment
---

# Hadoop dev environment

## Local dev environment

It is important to be able to test code on your own machine before submitting to a Hadoop cluster. Below are instructions for doing so in Eclipse.

The basic idea is to create a normal Java application but add all the Hadoop JARs to the classpath. Your own code will have the `main()` function, so you run it as a normal Java application.

The Hadoop libraries have been packaged, for your convenience, into a single ZIP file.

### Windows installation

- Download this ZIP (for Windows 64-bit): [hadoop-2.6.0-windows64.zip](https://www.dropbox.com/s/vgsjjwxp2a7b83r/hadoop-2.6.0-windows64.zip?dl=0)
- Extract the ZIP. Notice there is a `hadooplibs` folder as well as some others such as `bin`.
- Create system environment variables to point to your extracted files.
  1. Open the Start menu.
  2. Type 'env' to search for that string, and select "Edit environment variables for your account"
  3. In the window that opens up, in the top panel, click "New" and type `HADOOP_HOME` for the name and the path to your unzipped files for the value, e.g., `C:\Users\Me\Documents\hadoop-2.6.0`
  4. Create another new variabled called `PATH` and set its value to `C:\Users\Me\Documents\hadoop-2.6.0\bin` (i.e., the `HADOOP_HOME` value plus `bin`). If you already have a `PATH` variable, edit it, add a semicolon `;` to the end, and then add the Hadoop bin path.
  5. Click "OK" until you are out of all the environment variable windows.
  6. Close/restart Eclipse or IntelliJ so that your IDE sees these new environment variables.

### Linux/Mac OS X installation

- Download this ZIP: [hadooplibs-2.6.0-mac-linux.zip](https://github.com/joshuaeckroth/cinf401-examples/raw/master/hadooplibs-2.6.0-mac-linux.zip)
- Extract the ZIP. Notice there is a `jars` folder and a `sources` folder.
- The files in `jars` will be imported as libraries in Eclipse (see [Hadoop workflow](/notes/hadoop-workflow.html) notes).
- The files in `sources` provide source code for the Hadoop JARs in the `jars` folder. If Eclipse ever complains about not having the source code for an external Hadoop library, tell it to associate the corresponding JAR in `sources`.

For reference, I copied the JARs from these locations in a fresh Hadoop 2.6.0 distribution ([from here](http://mirrors.advancedhosters.com/apache/hadoop/common/hadoop-2.6.0/), the non-source package), and added them to the ZIP.

- every JAR in `share/hadoop/common/lib`
- every JAR in `share/hadoop/mapreduce`
- every JAR in `share/hadoop/yarn`
- every JAR in `share/hadoop/yarn/lib`
- `hadoop-common-2.6.0.jar` in `share/hadoop/common`

Next, you can proceed to the [Hadoop workflow notes](/notes/hadoop-workflow.html).

## delenn environment

delenn is a moderately-sized server (132 GB RAM, 32 CPU cores, 15 TB disk dedicated to Hadoop) running Arch Linuxl. It runs about 15 virtual machines to simulate a Hadoop cluster. A real Hadoop cluster should be made up of physical commodity hardware (normal servers, not supercomputers). But buying lots of servers is significantly more expensive than simulating them, so we simluate them. However, performance suffers in simulated environments. See the [Hadoop notes](/notes/hadoop.html) for more details about simulation vs. real hardware.

![Network diagram](/images/network-diagram.png)

See the [Hadoop notes](/notes/hadoop.html) for definitions and explanations of these nodes. Below is a summary of their roles:

| Node | Link | Purpose |
| ---- | ---- | ------- |
| resourcemanager | [Link](http://localhost:9000/hadoop/resourcemanager:8088/) | Manages YARN "containers", i.e., jobs |
| namenode | [Link](http://localhost:9000/hadoop/namenode:50070/) | Manages HDFS metadata |
| mrjobhistory | [Link](http://localhost:9000/hadoop/mrjobhistory:19888/) | Records finished MapReduce jobs |

The three nodes identified above each run one daemon with the same name and purpose as described.

Each slave runs two daemons:

- datanode --- Manages the slave's HDFS contents
- nodemanager --- Manages the slave's "containers," i.e., work jobs; example: [slave8](http://localhost:9000/hadoop/slave8:8042/node)

## londo environment

londo has a local Hadoop installation as well. It is for testing purposes only, if you are unable to test on your own machine. It only runs in one thread, so it is quite slow. You would need to create a JAR package in your IDE and transfer it to londo, or compile your code on londo. Then execute the application as described in the [Hadoop workflow](/notes/hadoop-workflow.html).





