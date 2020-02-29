package com.trelloiii.jigul.web.httpcodes;

public class BadRequest extends HttpCode {
    public BadRequest(Object body){
        super(400,body);
    }
}
