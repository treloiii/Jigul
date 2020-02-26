package com.trelloiii.simplereapitinglib.web.server;

import com.google.gson.Gson;
import com.trelloiii.simplereapitinglib.Configuration;
import com.trelloiii.simplereapitinglib.web.httpcodes.HttpCode;
import com.trelloiii.simplereapitinglib.web.httpcodes.NotFound;
import com.trelloiii.simplereapitinglib.web.httpcodes.Ok;
import com.trelloiii.simplereapitinglib.web.pool.ControllersPool;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
                Map<String,String[]> method=new HashMap<>();
                try {
                    method=request.processPath();
                }
                catch (Exception e){
                    e.printStackTrace();
                }

                Object returned = null;
                HttpCode code;
                for(Map.Entry<String,String[]> entry:method.entrySet()) {
                    returned=pool.invokeMethod(entry.getKey(), entry.getValue());
                }

                //TODO upgrade response
                Response response=new Response(socket.getOutputStream());
                if(returned instanceof NotFound)
                    code= (HttpCode) returned;
                else
                    code=new Ok(returned);
                response.sendResponse(code);

                socket.close();
                System.out.println("Client disconnected!");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }
}
