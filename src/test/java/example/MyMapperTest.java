package example;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Before;
import org.junit.Test;

public class MyMapperTest {

    private String s1 = "40.129.132.147 - - [26/May/2016:14:50:30 +0900] \"GET /category/cameras?from=0 HTTP/1.1\" 200 108 \"/search/?c=Cameras\" \"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.46 Safari/535.11\"";
    private String s2 = "120.222.144.140 - - [26/May/2016:14:50:30 +0900] \"GET /category/jewelry HTTP/1.1\" 200 67 \"-\" \"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; BTRS122159; GTB7.2; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729; BRI/2)\"";
    
    private MapDriver<LongWritable, Text, Text, IntWritable> driver;
    
    @Before
    public void setup() {
        driver = new MapDriver<LongWritable, Text, Text, IntWritable>().withMapper(new MyMapper());
    }
    
    @Test
    public void ログが一行の場合は出力も一行() throws IOException {
        driver = driver
                .withInput(new LongWritable(1), new Text(s1));
        
        List<Pair<Text, IntWritable>> results = driver.run();
        
        assertThat(results.size(), is(1));
        
        Pair<Text, IntWritable> result = results.get(0);
        assertThat(result.getFirst().toString(), is("/category/cameras"));
        assertThat(result.getSecond().get(), is(1));
    }

    @Test
    public void ログが二行の場合は出力も二行() throws IOException {
        driver = driver
                .withInput(new LongWritable(1), new Text(s1))
                .withInput(new LongWritable(2), new Text(s2));
        
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
    public void 同一URLへのログが存在しても出力は重複しない() throws IOException {
        driver = driver
                .withInput(new LongWritable(1), new Text(s1))
                .withInput(new LongWritable(2), new Text(s2))
                .withInput(new LongWritable(3), new Text(s2));
        
        List<Pair<Text, IntWritable>> results = driver.run();
        
        assertThat(results.size(), is(3));
        
        Pair<Text, IntWritable> result = results.get(0);
        assertThat(result.getFirst().toString(), is("/category/cameras"));
        assertThat(result.getSecond().get(), is(1));
        
        result = results.get(1);
        assertThat(result.getFirst().toString(), is("/category/jewelry"));
        assertThat(result.getSecond().get(), is(1));
        
        result = results.get(2);
        assertThat(result.getFirst().toString(), is("/category/jewelry"));
        assertThat(result.getSecond().get(), is(1));
    }
}
