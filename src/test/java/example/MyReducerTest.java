package example;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Before;
import org.junit.Test;

public class MyReducerTest {

    private static final IntWritable one = new IntWritable(1);
    
    private String s1 = "/category/cameras";
    private String s2 = "/category/jewelry";
    
    private ReduceDriver<Text, IntWritable, Text, IntWritable> driver;
    
    @Before
    public void setup() {
        driver = new ReduceDriver<Text, IntWritable, Text, IntWritable>().withReducer(new MyReducer());
    }
    
    @Test
    public void 同じキーの値を集計する() throws IOException {
        driver = driver
                .withInput(new Text(s1), Arrays.asList(one, one));
        
        List<Pair<Text, IntWritable>> results = driver.run();
        
        assertThat(results.size(), is(1));
        
        Pair<Text, IntWritable> result = results.get(0);
        assertThat(result.getFirst().toString(), is("/category/cameras"));
        assertThat(result.getSecond().get(), is(2));
    }

    @Test
    public void キーごとに集計する() throws IOException {
        driver = driver
                .withInput(new Text(s1), Arrays.asList(one, one))
                .withInput(new Text(s2), Arrays.asList(one, one, one));
        
        List<Pair<Text, IntWritable>> results = driver.run();
        
        assertThat(results.size(), is(2));
        
        Pair<Text, IntWritable> result = results.get(0);
        assertThat(result.getFirst().toString(), is("/category/cameras"));
        assertThat(result.getSecond().get(), is(2));
        
        result = results.get(1);
        assertThat(result.getFirst().toString(), is("/category/jewelry"));
        assertThat(result.getSecond().get(), is(3));
    }
}
