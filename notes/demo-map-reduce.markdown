---
layout: post
title: "Demo: MapReduce"
---

# Demo: MapReduce

## Word counts

{% highlight java %}
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, IntWritable>{

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();

        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {
            StringTokenizer itr = new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()) {
                word.set(itr.nextToken());
                context.write(word, one);
            }
        }
    }

    public static class IntSumReducer
            extends Reducer<Text,IntWritable,Text,IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
{% endhighlight %}

Run on the delenn virtual cluster with the following command:

```
yarn jar wc.jar WordCount /datasets/westburylab-usenet/WestburyLab.NonRedundant.UsenetCorpus.txt \
  /jeckroth/wordcount/output-westburylab-usenet-1
```

## Baseball Friends

Consider this input file:

```
Aaden, Red Sox, Alannah, Alayna, Alex, Alondra, Amelia, Amir, Anika, ...
Aaliyah, Cardinals, Adley, Aliyah, Amirah, Ana, Anya, ...
Aarav, Cardinals, Addisyn, Ahmad, Aiyana, Alana, Andre, ...
...
```

Each row has a person's name (e.g., Aaden), then their favorite baseball team (only Red Sox or Cardinals), then that person's list of friends. Their friends may or may not like the same team; we would have to examine the row for each person to determine which team they like. Friendship is symmetric: if X is a friend of Y, then Y will also be a friend of X.

Given that input, produce an output file like:

```
Aaden,Red Sox,47,18
Aaliyah,Cardinals,30,43
Aarav,Cardinals,27,55
Aaron,Red Sox,55,24
Abbie,Cardinals,32,38
Abbigail,Red Sox,48,27
Abby,Red Sox,48,27
Abdiel,Cardinals,33,47
Abdullah,Red Sox,52,20
Abel,Red Sox,51,23
Abigail,Cardinals,33,49
Abraham,Red Sox,48,22
Abram,Red Sox,54,33
Abrielle,Red Sox,72,30
...
```

Each row in the output lists a person's name and a team, then the count of that person's friends who like the Red Sox (first number) and Cardinals (second number). Since Aaden likes the Red Sox, he is included in the 48 count, as well as 47 of his friends who also likes the Red Sox. In the second row, Aaden has 18 friends who like the Cardinals (this time, not including himself, since he likes the Red Sox and not the Cardinals).

### Smaller example

This input file:

```
Mary, Red Sox, Jane, John
Jane, Cardinals, John, Mike, Mary
John, Red Sox, Mary, Jane
Mike, Cardinals, Jane
```

should produce this output file:

```
Mary, Red Sox, Jane, John
Jane, Cardinals, John, Mike, Mary
John, Red Sox, Mary, Jane
Mike, Cardinals, Jane
```

### Strategy

### Implementation

You'll need the helper classes [CSVOutputFormat.java](/code/BaseballFriends/CSVOutputFormat.java) and [TextArrayWritable.java](/code/BaseballFriends/TextArrayWritable.java). The file below is [BaseballFriends.java](/code/BaseballFriends/BaseballFriends.java). The input file is [baseball_friends.csv](/code/BaseballFriends/baseball_friends.csv).

{% highlight java %}
import org.apache.commons.lang.ObjectUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.StringTokenizer;

public class BaseballFriends {

    public static class BaseballFriendsMapper extends Mapper<LongWritable, Text, Text, Text> {

        public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

            StringTokenizer st = new StringTokenizer(value.toString(), "\n,");
            String thisPerson = st.nextToken().trim();
            String thisFavoriteTeam = st.nextToken().trim();

            // kludge: should use a real tuple class for value
            System.out.println("emitting " + thisPerson + ", " + thisPerson + ", " + thisFavoriteTeam);
            context.write(new Text(thisPerson), new Text(thisPerson + "," + thisFavoriteTeam));

            while(st.hasMoreTokens()) {
                String friend = st.nextToken().trim();
                // kludge: should use a real tuple class for value
                System.out.println("emitting " + friend + ", " + thisPerson + ", " + thisFavoriteTeam);
                context.write(new Text(friend), new Text(thisPerson + "," + thisFavoriteTeam));
            }
        }
    }

    public static class BaseballFriendsReducer extends Reducer<Text, Text, TextArrayWritable, NullWritable> {
        public void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {

            Integer redsoxCount = 0;
            Integer cardinalsCount = 0;
            String thisPerson = key.toString();
            String thisTeam = null;
            System.out.println("thisPerson: " + thisPerson);
            for(Text valtext : values) {
                System.out.println("\tval: " + valtext.toString());
                String[] valParts = valtext.toString().split(",");
                String name = valParts[0];
                String team = valParts[1];
                if(thisPerson.equals(name))
                {
                    thisTeam = team;
                }
                else if(team.equals("Red Sox"))
                {
                    redsoxCount++;
                }
                else if(team.equals("Cardinals"))
                {
                    cardinalsCount++;
                }
            }
            Text[] row = new Text[4];
            row[0] = new Text(thisPerson);
            row[1] = new Text(thisTeam);
            row[2] = new Text(redsoxCount.toString());
            row[3] = new Text(cardinalsCount.toString());
            context.write(new TextArrayWritable(row), NullWritable.get());
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "baseball friends");
        job.setJarByClass(BaseballFriends.class);
        job.setMapperClass(BaseballFriendsMapper.class);
        job.setReducerClass(BaseballFriendsReducer.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        job.setOutputKeyClass(TextArrayWritable.class);
        job.setOutputValueClass(NullWritable.class);


        conf.set(CSVOutputFormat.CSV_TOKEN_SEPARATOR_CONFIG, ",");
        job.setOutputFormatClass(CSVOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
{% endhighlight %}
