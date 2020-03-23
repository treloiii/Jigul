package com.trelloiii.jigul.web.server;

import com.trelloiii.jigul.Configuration;
import com.trelloiii.jigul.web.Get;
import com.trelloiii.jigul.web.Post;
import com.trelloiii.jigul.web.httpcodes.HttpCode;
import com.trelloiii.jigul.web.mvc.MvcControllersPool;
import com.trelloiii.jigul.web.rest.ControllersPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ConnectionListener {
    private ServerSocket serverSocket;
    private ControllersPool pool;
    private MvcControllersPool mvcPool;
    public ConnectionListener(int port, Configuration configuration,ConnectionType type) {
        if(type.equals(ConnectionType.REST))
            pool=new ControllersPool(configuration);
        else if(type.equals(ConnectionType.MVC))
            mvcPool=new MvcControllersPool(configuration);
        else
            throw new IllegalArgumentException("Wrong connection type: "+type.toString());
        try {
            this.serverSocket = new ServerSocket(port);
            if(type.equals(ConnectionType.REST)) {
                while (true) {
                    listen();
                }
            }
            else {
                while (true){
                    listenMvc();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void listenMvc() throws IOException {
        Socket socket = serverSocket.accept();
        new Thread(()->{
            try {
                Request request = new Request(socket);//TODO переделать Request чтоб хранить в нем обработтаный запрос
                Map<String,Map<String,String>> method=new HashMap<>();
                try {
                    method=request.processRequest();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                HttpCode returned = null;
                //HttpCode code;

                for(Map.Entry<String,Map<String,String>> entry:method.entrySet()) {
                    String path=entry.getKey().substring(2);
                    System.err.println(path);
                    String reqType=entry.getKey().substring(0,1);
                    returned=mvcPool.invokeMethod(path,entry.getValue());
                }
                //TODO вызвать нужный мвс контроллер и передать в него request
                Response response=new Response(socket.getOutputStream());
                response.sendResponse(returned,null,ConnectionType.MVC);

                socket.close();
                System.out.println("Client disconnected!");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }
    private void listen() throws IOException {
        Socket socket = serverSocket.accept();
        new Thread(()->{
            try {
                Request request = new Request(socket);//TODO переделать Request чтоб хранить в нем обработтаный запрос
                Map<String,Map<String,String>> method=new HashMap<>();
                try {
                    method=request.processRequest();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                HttpCode returned = null;
                //HttpCode code;
                String corsPolicy=null;
                for(Map.Entry<String,Map<String,String>> entry:method.entrySet()) {
                    String path=entry.getKey().substring(2);
                    String reqType=entry.getKey().substring(0,1);
                    if(reqType.equals("G"))
                        returned=pool.invokeMethod(path, Get.class, entry.getValue());
                    else
                        returned=pool.invokeMethod(path, Post.class,entry.getValue());
                    corsPolicy=pool.getCorsPolicy(path);
                }
                //TODO вызвать нужный мвс контроллер и передать в него request
                Response response=new Response(socket.getOutputStream());
                response.sendResponse(returned,corsPolicy,ConnectionType.REST);

                socket.close();
                System.out.println("Client disconnected!");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }
}
