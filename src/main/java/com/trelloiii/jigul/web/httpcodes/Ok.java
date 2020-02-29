package com.trelloiii.simplereapitinglib.web.httpcodes;

public class Ok extends HttpCode {
//    private final int CODE=200;
//    private Object body;
    public Ok(Object body) {
        super(200,body);
        System.out.println("OK");
    }

//    @Override
//    public int getCODE() {
//        return CODE;
//    }
//    @Override
//    public Object getBody() {
//        return body;
//    }
}
