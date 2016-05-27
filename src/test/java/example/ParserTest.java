package example;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

    @Test
    public void アクセスログからURLを取得する() {
        String s = "40.129.132.147 - - [26/May/2016:14:50:30 +0900] \"GET /category/cameras HTTP/1.1\" 200 108 \"/search/?c=Cameras\" \"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.46 Safari/535.11\"";
        String url = Parser.getUrl(s);
        assertThat(url, is("/category/cameras"));
    }
    
    @Test
    public void アクセスログからクエリ文字列を取り除いたURLを取得する() {
        String s = "40.129.132.147 - - [26/May/2016:14:50:30 +0900] \"GET /category/cameras?from=0 HTTP/1.1\" 200 108 \"/search/?c=Cameras\" \"Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.46 Safari/535.11\"";
        String url = Parser.getUrl(s);
        assertThat(url, is("/category/cameras"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void 不正な文字列を指定すると引数エラー() {
        Parser.getUrl("INVALID_STRING");
    }
}
