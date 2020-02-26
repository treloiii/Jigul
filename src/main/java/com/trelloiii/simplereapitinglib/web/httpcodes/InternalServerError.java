package com.trelloiii.simplereapitinglib.web.httpcodes;

public class InternalServerError extends HttpCode {
    public InternalServerError(Object body) {
        super(500,body);
    }
}
