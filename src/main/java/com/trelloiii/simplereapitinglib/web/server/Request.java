package com.trelloiii.simplereapitinglib.web.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Request {
    private Socket socket;
    private BufferedReader input;
    public Request(Socket socket) {
        this.socket=socket;
        try {
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


    public Map<String,String[]> processPath(){//return map String - controller method name, String[] - params for method
        try{

            while (!input.ready()) ;


            String in=input.readLine();
            System.out.println(in);
            String pathParams = in.split(" ")[1];
            String[] splitted = pathParams.split("[?]");
            String path = splitted[0];
            Map<String, String[]> result = new HashMap<>();
            try {// Request with params
                String[] params = splitted[1].split("&");
                System.out.println(Arrays.toString(processParams(params)));
                System.out.println(path);
                result.put(path, processParams(params));
                return result;
            }
            catch (ArrayIndexOutOfBoundsException e){//request without params
                System.out.println(path);
                result.put(path,null);
                return result;
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private String[] processParams(String[] params){
        List<String> result=new ArrayList<>();
        for(String param:params){
            result.add(param.split("=")[1]);
        }
        return result.toArray(new String[0]);
    }
}
