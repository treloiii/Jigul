package com.trelloiii.simplereapitinglib.web.server;

import com.trelloiii.simplereapitinglib.Configuration;
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
                PrintWriter output = new PrintWriter(socket.getOutputStream());
                Request request = new Request(socket);
                Map<String,String[]> method=new HashMap<>();
                try {
                    method=request.processPath();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            //    pool.invokeMethod("/doNothing","SUKA","returned");
                //TODO request controller
                for(Map.Entry<String,String[]> entry:method.entrySet()) {
                    System.out.println(entry.getKey()+"$");
                    pool.invokeMethod(entry.getKey(), entry.getValue());
                }
                Random r=new Random();

                Thread.sleep(r.nextInt(500));


                //TODO return normal response
                output.println("HTTP/1.1 200 OK");
                output.println("Content-Type: text/html; charset=utf-8");
                output.println();
                output.println("<p>Привет всем!</p>");
                output.flush();


                socket.close();
                System.out.println("Client disconnected!");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }
}
