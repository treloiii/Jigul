package com.trelloiii.jigul.web.httpcodes;

public class HttpCode {
    private int CODE;
    private Object body;

    HttpCode(int CODE,Object body){
        this.CODE=CODE;
        this.body=body;
    }

    public HttpCode() {
    }

    public int getCODE() {
        return CODE;
    }

    public Object getBody() {
        return body;
    }
}
