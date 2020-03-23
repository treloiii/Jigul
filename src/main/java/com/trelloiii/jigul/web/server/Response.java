package com.trelloiii.jigul.web.server;

import com.google.gson.Gson;
import com.trelloiii.jigul.web.httpcodes.HttpCode;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class Response {
    private final String PROTOCOL="HTTP/1.1";
    private final String CONTENT_TYPE="Content-Type: application/json; charset=utf-8";
    private final String CONTENT_TYPE_2="Content-Type: text/html; charset=utf-8";
    private final String ALLOW_METHODS="Access-Control-Allow-Methods: POST, GET, OPTIONS";
    private final String ALLOW_HEADERS="Access-Control-Allow-Headers: *";
    private final String ALLOW_ORIGIN="Access-Control-Allow-Origin: ";
    private final String MAX_AGE="Access-Control-Max-Age: 3600";
    private PrintWriter output;
    private Gson gson;
    public Response(OutputStream stream) {
        this.output=new PrintWriter(new OutputStreamWriter(stream,StandardCharsets.UTF_8));
        this.gson=new Gson();
    }

    public void sendResponse(HttpCode code,String corsPolicy,ConnectionType type){
        output.println(PROTOCOL+" "+code.getCODE());
        if(type.equals(ConnectionType.REST))
            output.println(CONTENT_TYPE);
        else
            output.println(CONTENT_TYPE_2);
        output.println(ALLOW_HEADERS);
        output.println(ALLOW_METHODS);
        if(corsPolicy==null)
            output.println(ALLOW_ORIGIN+"*");
        else
            output.println(ALLOW_ORIGIN+corsPolicy);
        output.println(MAX_AGE);
        output.println();
        if(code.getBody()!=null) {
            if(type.equals(ConnectionType.REST))
                output.println(gson.toJson(code.getBody()));
            else
                output.println(code.getBody());
        }
        output.flush();
    }
}
