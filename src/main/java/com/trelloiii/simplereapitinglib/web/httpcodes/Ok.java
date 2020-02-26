package com.trelloiii.simplereapitinglib.web.httpcodes;

public class Ok implements HttpCode {
    private final int CODE=200;
    private Object body;
    public Ok(Object body) {
        this.body=body;
        System.out.println("OK");
    }

    @Override
    public int getCODE() {
        return CODE;
    }
    @Override
    public Object getBody() {
        return body;
    }
}
