package com.fo.pp.common;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MD5UtilsTests {

    @Test
    void testGetMD5(){
        String md5 = (MD5Utils.getMD5("1234"));
        assert md5.equals("81dc9bdb52d04dc20036dbd8313ed055");
    }
}
