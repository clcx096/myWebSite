package com.lcx.walle.web;

import com.google.gson.JsonObject;
import com.lcx.walle.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 测试Controller
 */
@RestController
@RequestMapping("/test")
public class Test {

    @RequestMapping("/hello")
    public String hello(String name){
        return "hello,"+name;
    }

    @RequestMapping("/print")
    public void hello2(HttpServletResponse response){
        try{
            JsonObject json = new JsonObject();
            json.addProperty("name","lcx");
            json.addProperty("age","24");
            ResultUtils.sendData(response, json);
        } catch (Exception e){
            e.printStackTrace();
            ResultUtils.sendError(response);
        }
    }


}
