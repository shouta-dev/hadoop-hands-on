package example;

import java.io.IOException;
import java.util.stream.StreamSupport;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values,
            Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
        
        int sum = StreamSupport.stream(values.spliterator(), false)
                    .map(v -> v.get())
                    .reduce((a, b) -> a + b)
                    .get();
        
        context.write(key, new IntWritable(sum));
    }
}
