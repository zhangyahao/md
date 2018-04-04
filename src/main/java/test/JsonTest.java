package test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: md
 * @description: json
 * @author: Zhang
 * @create: 2018-03-29 18:17
 **/
public class JsonTest {
    public static void main(String[] args) {
        String m = " {\"result\": \"OK\",\"streamingIp\":\"172.16.5.123\",\"streamingPort\":\"554\",\"freqKHz\":\"850000\",\"progNo\":\"9\",\"ipqamIp\":\"172.16.5.234\",\"ipqamPort\":\"55056\"}";
        JSONObject jsonObject = JSONObject.parseObject(m);
        System.out.println(jsonObject);
        String s = jsonObject.remove("result").toString();
        System.out.println(s);
        System.out.println(jsonObject);
    }
}
