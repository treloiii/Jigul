package com.trelloiii.simplereapitinglib.web.httpcodes;

public class NotFound extends HttpCode{
//    private final int CODE=404;
//    private Object body;
    public NotFound(Object body) {
        super(404,body);
        System.out.println("page not found");
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
