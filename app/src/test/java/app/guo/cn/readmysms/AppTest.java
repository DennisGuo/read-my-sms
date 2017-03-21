package app.guo.cn.readmysms;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class AppTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void parse(){
        String msg = "Код:30447 сумма:2090.00 кошелек:R146253412019 Cессия:320325120";

        System.out.println(SmsRadarService.parseSms(msg));
    }

    @Test
    public void httpGet(){
        String url = "http://baidu.com";
        try {
            //System.out.println(SmsRadarService.httpGet(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}