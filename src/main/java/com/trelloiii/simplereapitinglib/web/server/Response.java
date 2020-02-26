package com.trelloiii.simplereapitinglib.web.server;

import com.google.gson.Gson;
import com.trelloiii.simplereapitinglib.web.httpcodes.HttpCode;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class Response {
    private final String PROTOCOL="HTTP/1.1";
    private final String CONTENT_TYPE="Content-Type: application/json; charset=utf-8";
    private PrintWriter output;
    private Gson gson;
    public Response(OutputStream stream) {
        this.output=new PrintWriter(new OutputStreamWriter(stream,StandardCharsets.UTF_8));
        this.gson=new Gson();
    }

    public void sendResponse(HttpCode code){
        output.println(PROTOCOL+" "+code.getCODE());
        output.println(CONTENT_TYPE);
        output.println();
        if(code.getBody()!=null)
          output.println(gson.toJson(code.getBody()));
        output.flush();
    }
}
