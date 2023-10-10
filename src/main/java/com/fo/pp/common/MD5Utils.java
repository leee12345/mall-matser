package com.fo.pp.common;

import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;

public class MD5Utils {
    public static String getMD5(String str) {
        String md5 = null;
        try {
            md5 = DigestUtils.md5DigestAsHex(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            return "";
        }
        return md5;
    }
}
