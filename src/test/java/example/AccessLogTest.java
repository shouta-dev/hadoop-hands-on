package example;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class AccessLogTest {

    private AccessLog log;
    
    @Test(expected = IllegalArgumentException.class)
    public void 不正な文字列を指定すると引数エラー() {
        log = AccessLog.parse("INVALID_STRING");
    }
    
    @Test
    public void GETメソッドの場合() {
        log = AccessLog.parse("10.20.30.40 - ID_HASH [30/May/2016:14:09:25 +0900] \"GET /item/electronics/2101 HTTP/1.1\" 200 123 \"-\" \"USER_AGENT\"");
        assertTrue(log.isGetMethod());
    }
    
    @Test
    public void POSTメソッドの場合() {
        log = AccessLog.parse("10.20.30.40 - ID_HASH [30/May/2016:14:09:25 +0900] \"POST /search/?c=Health HTTP/1.1\" 200 123 \"-\" \"USER_AGENT\"");
        assertFalse(log.isGetMethod());
    }
    
    @Test
    public void STATUS_OKの場合() {
        log = AccessLog.parse("10.20.30.40 - ID_HASH [30/May/2016:14:09:25 +0900] \"GET /item/electronics/2101 HTTP/1.1\" 200 123 \"-\" \"USER_AGENT\"");
        assertTrue(log.isOkStatus());
    }
    
    @Test
    public void STATUS_NOT_FOUNDの場合() {
        log = AccessLog.parse("10.20.30.40 - ID_HASH [30/May/2016:14:09:25 +0900] \"GET /item/software/637 HTTP/1.1\" 404 123 \"-\" \"USER_AGENT\"");
        assertFalse(log.isOkStatus());
    }
    
    @Test
    public void アクセスログからURLを取得する() {
        log = AccessLog.parse("10.20.30.40 - ID_HASH [30/May/2016:14:09:25 +0900] \"GET /category/networking HTTP/1.1\" 200 123 \"-\" \"USER_AGENT\"");
        assertThat(log.getUrl(), is("/category/networking"));
    }
    
    @Test
    public void アクセスログからクエリ文字列を取り除いたURLを取得する() {
        log = AccessLog.parse("10.20.30.40 - ID_HASH [30/May/2016:14:09:25 +0900] \"GET /category/networking?from=0 HTTP/1.1\" 200 123 \"-\" \"USER_AGENT\"");
        assertThat(log.getUrl(), is("/category/networking"));
    }

}
