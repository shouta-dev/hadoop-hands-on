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

    private String s1 = "10.20.30.40 - ID_HASH [26/May/2016:14:50:30 +0900] \"GET /category/cameras?from=0 HTTP/1.1\" 200 123 \"-\" \"USER_AGENT\"";
    private String s2 = "10.20.30.40 - ID_HASH [26/May/2016:14:50:30 +0900] \"GET /category/jewelry HTTP/1.1\" 200 123 \"-\" \"USER_AGENT\"";
    private String s3 = "10.20.30.40 - ID_HASH [30/May/2016:14:09:25 +0900] \"POST /search/?c=Health HTTP/1.1\" 200 123 \"-\" \"USER_AGENT\"";
    private String s4 = "10.20.30.40 - ID_HASH [30/May/2016:14:09:25 +0900] \"GET /item/software/637 HTTP/1.1\" 404 123 \"-\" \"USER_AGENT\"";
    
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
    
    @Test
    public void GETメソッド以外は除外() throws IOException {
        driver = driver
                .withInput(new LongWritable(1), new Text(s1))
                .withInput(new LongWritable(2), new Text(s3));
        
        List<Pair<Text, IntWritable>> results = driver.run();
        
        assertThat(results.size(), is(1));
        
        Pair<Text, IntWritable> result = results.get(0);
        assertThat(result.getFirst().toString(), is("/category/cameras"));
        assertThat(result.getSecond().get(), is(1));
    }
    
    @Test
    public void STATUS_OK以外は除外() throws IOException {
        driver = driver
                .withInput(new LongWritable(1), new Text(s1))
                .withInput(new LongWritable(2), new Text(s4));
        
        List<Pair<Text, IntWritable>> results = driver.run();
        
        assertThat(results.size(), is(1));
        
        Pair<Text, IntWritable> result = results.get(0);
        assertThat(result.getFirst().toString(), is("/category/cameras"));
        assertThat(result.getSecond().get(), is(1));
    }
}
