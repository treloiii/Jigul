package com.trelloiii.simplereapitinglib.web.server;

import com.trelloiii.simplereapitinglib.Configuration;
import com.trelloiii.simplereapitinglib.web.Get;
import com.trelloiii.simplereapitinglib.web.Post;
import com.trelloiii.simplereapitinglib.web.httpcodes.HttpCode;
import com.trelloiii.simplereapitinglib.web.pool.ControllersPool;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ConnectionListener {
    private ServerSocket serverSocket;
    private ControllersPool pool;
    public ConnectionListener(int port, Configuration configuration) {
        pool=new ControllersPool(configuration);
        try {
            this.serverSocket = new ServerSocket(port);
            while (true){
                listen();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() throws IOException {
        Socket socket = serverSocket.accept();
        new Thread(()->{
            try {
                Request request = new Request(socket);
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
                    String reqType=entry.getKey().substring(0,1);
                    System.out.println(path+" ---PATH");
                    System.out.println(reqType+" ---REQ TYPE");
                    if(reqType.equals("G"))
                        returned=pool.invokeMethod(path, Get.class, entry.getValue());
                    else
                        returned=pool.invokeMethod(path, Post.class,entry.getValue());//TODO make invoke post and get
                }

                //TODO upgrade response
                Response response=new Response(socket.getOutputStream());
//                if(!(returned instanceof Ok))
//                    code= (HttpCode) returned;
//                else
//                    code=new Ok(returned);
                response.sendResponse(returned);

                socket.close();
                System.out.println("Client disconnected!");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }
}
