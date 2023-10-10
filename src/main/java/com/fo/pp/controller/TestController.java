package com.fo.pp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    @RequestMapping("/test_pay")
    public String test() {
        return "test_pay.html";
    }
}
