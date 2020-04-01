package com.trelloiii.jigul.web.httpcodes;

public class Ok extends HttpCode {
//    private final int CODE=200;
//    private Object body;
    public Ok(Object body) {
        super(200,body);
        System.out.println("OK");
    }
    public Ok(){
        super(200,null);
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
