package com.jsxnh.test;

import com.jsxnh.util.ByteUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {


    private ServerSocket server;

    public Server(){
        try {
            server = new ServerSocket(8001);
            while (true){
                Socket s = server.accept();
                new Thread(new SingleServer(s)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args){
        new Server();
    }

}

class SingleServer implements Runnable{


    private Socket socket;

    public SingleServer(Socket s){

        socket = s;
    }


    @Override
    public void run() {
        try {


                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                while (reader.ready()){
                    stringBuilder.append(reader.readLine()+"\n");
                }
                System.out.println(stringBuilder.toString());
                PrintStream ps = new PrintStream(socket.getOutputStream());
                ps.println("HTTP/1.1 200 OK");
                ps.println("Content-Type:text/html;Charset:utf-8");
                ps.println();
                ps.println("<html><body>hello world</body></html>");
                ps.flush();

                ps.close();
                //in.close();
                socket.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
