
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
