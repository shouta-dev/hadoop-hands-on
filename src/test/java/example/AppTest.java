package example;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Before;
import org.junit.Test;

public class AppTest {

    private String s1 = "40.129.132.147 - - [26/May/2016:14:50:30 +0900] \"GET /category/cameras?from=0 HTTP/1.1\" 200 108 \"/search/?c=Cameras\" \"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.46 Safari/535.11\"";
    private String s2 = "120.222.144.140 - - [26/May/2016:14:50:30 +0900] \"GET /category/jewelry HTTP/1.1\" 200 67 \"-\" \"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; BTRS122159; GTB7.2; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; BRI/2)\"";
    
    private Mapper<LongWritable, Text, Text, IntWritable> mapper;
    private Reducer<Text, IntWritable, Text, IntWritable> reducer;
    
    private MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, IntWritable> driver;
    
    @Before
    public void setup() {
        mapper = new MyMapper();
        reducer = new MyReducer();
        driver = new MapReduceDriver<LongWritable, Text, Text, IntWritable, Text, IntWritable>()
                .withMapper(mapper)
                .withReducer(reducer);
    }
    
    @Test
    public void ひとつのURLのPVが1() throws IOException {
        
        driver = driver
                .withInput(toInput(1, s1));
        List<Pair<Text, IntWritable>> results = driver.run();
        
        assertThat(results.size(), is(1));
        
        Pair<Text, IntWritable> result = results.get(0);
        assertThat(result.getFirst().toString(), is("/category/cameras"));
        assertThat(result.getSecond().get(), is(1));
    }

    @Test
    public void ふたつのURLのPVがそれぞれ1() throws IOException {
        
        driver = driver
                .withInput(toInput(1, s1))
                .withInput(toInput(2, s2));
        List<Pair<Text, IntWritable>> results = driver.run();
        
        assertThat(results.size(), is(2));
        
        Pair<Text, IntWritable> result = results.get(0);
        assertThat(result.getFirst().toString(), is("/category/cameras"));
        assertThat(result.getSecond().get(), is(1));
        
        result = results.get(1);
        assertThat(result.getFirst().toString(), is("/category/jewelry"));
        assertThat(result.getSecond().get(), is(1));
    }

    @Test
    public void URLごとにPVを集計する() throws IOException {
        
        driver = driver
                .withInput(toInput(1, s1))
                .withInput(toInput(2, s2))
                .withInput(toInput(3, s1))
                .withInput(toInput(4, s1))
                .withInput(toInput(5, s2));
        
        List<Pair<Text, IntWritable>> results = driver.run();
        
        assertThat(results.size(), is(2));
        
        Pair<Text, IntWritable> result = results.get(0);
        assertThat(result.getFirst().toString(), is("/category/cameras"));
        assertThat(result.getSecond().get(), is(3));
        
        result = results.get(1);
        assertThat(result.getFirst().toString(), is("/category/jewelry"));
        assertThat(result.getSecond().get(), is(2));
    }
    
    private Pair<LongWritable, Text> toInput(long l, String s) {
        return new Pair<LongWritable, Text>(new LongWritable(l), new Text(s));
    }
}
