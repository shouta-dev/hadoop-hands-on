package example;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider.Text;

/**
 * Hello world!
 *
 */
public class App extends Configured implements Tool {
    
    public static void main(String[] args) throws Exception {
        App driver = new App(); 
        int status = ToolRunner.run(driver.getConf(), driver, args);
        System.exit(status);
    }

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            return -1;
        }
        
        Job job = Job.getInstance();
        job.setJobName("hadoop-hands-on");
        job.setJarByClass(App.class);
        
        job.setMapperClass(MyMapper.class);
        job.setReducerClass(MyReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
        return job.waitForCompletion(true) ? 0 : 1;
    }
}
