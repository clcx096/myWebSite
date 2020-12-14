package com.lcx.walle.utils;

public class ResultDto {

    private boolean flag;

    private int result;

    private String msg;

    private Object data;

    private static final String SUCCESS = "SUCCESS";

    private static final String FAIL = "FAIL";

    private static final String ERROR = "ERROR";

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResultDto(){

    }

    public static ResultDto error() {
        ResultDto dto = new ResultDto();
        dto.setFlag(false);
        dto.setResult(-1);
        dto.setMsg(ERROR);
        return dto;
    }

    public static ResultDto fail(String msg) {
        ResultDto dto = new ResultDto();
        dto.setFlag(false);
        dto.setResult(-1);
        dto.setMsg(msg);
        return dto;
    }

    public static ResultDto fail(int result,String msg) {
        ResultDto dto = new ResultDto();
        dto.setFlag(false);
        dto.setResult(result);
        dto.setMsg(msg);
        return dto;
    }

    public static ResultDto successMsg(String msg) {
        ResultDto dto = new ResultDto();
        dto.setFlag(true);
        dto.setResult(1);
        dto.setMsg(msg);
        return dto;
    }

    public static ResultDto success(String msg, Object data) {
        ResultDto dto = new ResultDto();
        dto.setFlag(true);
        dto.setResult(1);
        dto.setMsg(msg);
        dto.setData(data);
        return dto;
    }

    public static ResultDto success(int result, String msg, Object data) {
        ResultDto dto = new ResultDto();
        dto.setFlag(true);
        dto.setResult(result);
        dto.setMsg(msg);
        dto.setData(data);
        return dto;
    }

    public static ResultDto successData(Object data) {
        ResultDto dto = new ResultDto();
        dto.setFlag(true);
        dto.setResult(1);
        dto.setMsg(SUCCESS);
        dto.setData(data);
        return dto;
    }
}
