package com.trelloiii.jigul.web.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
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
            //this.input=new BufferedInputStream(socket.getInputStream());

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    public Map<String,Map<String,String>> processRequest(){//return map String - controller method name, String[] - params for method
        try{

            while (!input.ready()) ;
            String content="";
            List<String> headersList=new ArrayList<>();
            while (true) {
                String header=input.readLine();
                if(header.isEmpty())
                    break;
                headersList.add(header);
                System.out.println(header);
                if(header.contains("Content-Length")) {
                    content = header;
                }
            }
            if(content.isEmpty()) {
                return processGet(headersList);
            }
            else {
                if(headerValueSearch(headersList,"Content-Type").contains("multipart/form-data"))
                    return processFormPost(headersList);
                else
                    return processRawPost(headersList);
            }




        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private Map<String,Map<String,String>> processGet(List<String> headersList){

        String[] splitted = processPath(headersList.get(0));
        String path=splitted[0];
        Map<String, Map<String,String>> result = new HashMap<>();
        try {// Request with params
            String[] params = splitted[1].split("&");
//            System.out.println(Arrays.toString(processParams(params)));
//            System.out.println(path);
            result.put("G "+path, processParams(params));//G- mean that it GET request
            return result;
        }
        catch (ArrayIndexOutOfBoundsException e){//request without params
            System.out.println(path);
            result.put("G "+path,null);
            return result;
        }
    }
    private Map<String,Map<String,String>> processFormPost(List<String> headersList){
        try {
            Map<String,Map<String,String>> result=new HashMap<>();
            String path=processPath(headersList.get(0))[0];
            String body=parseBody(headersList);//post body
            String[] splitted=body.split("\\r?\\n");//split by new line
            List<String> buf=new LinkedList<>(Arrays.asList(splitted));//migrate to list for removing
            buf.removeIf(buf1 -> buf1.contains("Content-Length"));//remove content length string
            splitted=buf.toArray(new String[]{});//back to array
            Map<String,String> vals=new HashMap<>();//values of request body params
            System.out.println(Arrays.toString(splitted));
            for(int i=3;i<splitted.length-1;i+=4){
                String bufName=splitted[i-2];
                String name=bufName.substring("Content-Disposition: form-data; name=\"".length(),bufName.length()-1);
                vals.put(name,splitted[i]);//add only values of request body params
            }
            result.put("P "+path,vals);//P - mean that it POST request
            return result;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;// i don't know what need to go in this catch
        }
    }
    private Map<String,Map<String,String>> processRawPost(List<String> headersList){
        try{
            Map<String,Map<String,String>> result=new HashMap<>();
            String path=processPath(headersList.get(0))[0];
            String body=parseBody(headersList);
            Map<String,String> vals=new HashMap<>();
            vals.put("BODY",body);
            result.put("P "+path,vals);//P - mean that it POST request
            return result;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;// i don't know what need to go in this catch
        }
    }

    private String[] processPath(String firstHeader){
        String pathParams = firstHeader.split(" ")[1];
        String[] splitted = pathParams.split("[?]");
        return splitted;//[0]-path,[1]-query params
    }

    private String headerValueSearch(List<String> headersList,String headerName){
        String result="";
        for(String header:headersList){
            if(header.contains(headerName)){
                result=header.substring(headerName.length()+2);
            }
        }
        return result;
    }
    private String parseBody(List<String> headersList) throws IOException {
        int byteLen = Integer.parseInt(headerValueSearch(headersList,"Content-Length"));
        char[] buf = new char[byteLen];
        input.read(buf);//Read body
        return String.valueOf(buf);
    }
    private Map<String,String> processParams(String[] params){//first-param name, second param value
        Map<String,String> result=new HashMap<>();
        for(String param:params){
            System.out.println(param+" &&");
            String[] bufArr=param.split("=");
            result.put(bufArr[0],bufArr[1]);
        }
        return result;
    }
}
