package com.fo.pp.common;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int status = 0;
    private String msg = "";
    private Object data = null;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public static Response success(){
        return new Response();
    }

    public static Response success(String msg){
        Response response = new Response();
        response.setMsg(msg);
        return response;
    }

    public static Response success(Object data){
        Response response = new Response();
        response.setData(data);
        return response;
    }

    public static Response success(String msg, Object data){
        Response response = new Response();
        response.setMsg(msg);
        response.setData(data);
        return response;
    }

    public static Response error(int code, String msg){
        Response response = new Response();
        response.setStatus(code);
        response.setMsg(msg);
        return response;
    }
}
