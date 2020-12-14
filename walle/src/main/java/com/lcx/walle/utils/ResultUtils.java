package com.lcx.walle.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 返回信息到页面的工具
 */
public class ResultUtils {

    private final static String UTF_8 = "UTF-8";

    private final static String APPLICATION_JSON = "application/json";

    private final static String TEXT_PLAIN = "text/plain";

    public static void sendError(HttpServletResponse response){
        send(response, APPLICATION_JSON, GsonUtils.toJsonString(ResultDto.error()));
    }

    public static void sendMessage(HttpServletResponse response, String msg){
        send(response, APPLICATION_JSON, GsonUtils.toJsonString(ResultDto.successMsg(msg)));
    }

    public static void sendFailMessage(HttpServletResponse response, String msg){
        send(response, APPLICATION_JSON, GsonUtils.toJsonString(ResultDto.fail(msg)));
    }

    public static void sendData(HttpServletResponse response, Object data){
        send(response, APPLICATION_JSON, GsonUtils.toJsonString(ResultDto.successData(data)));
    }

    private static void send(HttpServletResponse response, String contextType, String str){
        //设置编码格式
        response.setContentType(contextType+";charset="+UTF_8);
        response.setCharacterEncoding(UTF_8);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.write(str);
            out.flush();
            out.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static void send(HttpServletResponse response, String str){
        send(response, TEXT_PLAIN, str);
    }

}
