package com.fo.pp.common;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ResponseTests {

    @Test
    void testResponse(){
        Response res1 = Response.success();
        assert res1.getStatus() == 0;
        assert res1.getMsg().equals("");
        assert res1.getData() == null;
        Response res2 = Response.success("hello");
        assert res2.getStatus() == 0;
        assert res2.getMsg().equals("hello");
        assert res2.getData() == null;
        Response res3 = Response.success(3);
        assert res3.getStatus() == 0;
        assert res3.getMsg().equals("");
        assert res3.getData() == Integer.valueOf(3);
        Response res4 = Response.error(3, "错误");
        assert res4.getStatus() == 3;
        assert res4.getMsg().equals("错误");
        assert res4.getData() == null;
    }
}
