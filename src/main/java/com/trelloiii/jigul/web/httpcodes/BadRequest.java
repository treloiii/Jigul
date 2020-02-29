package com.trelloiii.simplereapitinglib.web.httpcodes;

public class BadRequest extends HttpCode {
    public BadRequest(Object body){
        super(400,body);
    }
}
