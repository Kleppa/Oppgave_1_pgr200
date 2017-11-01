package innlevering;

import java.net.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private static ExecutorService executor = Executors.newFixedThreadPool(50);
    int count = 0;
    public static void main(String[] args) {
        server();
    }

    public static void server() {

        //We need a try-catch because lots of errors can be thrown
        //Wait for a client to connect

        int count = 1;
        try (ServerSocket sSocket = new ServerSocket(4444)) {
            //Loop that runs server functions
            while (true) {

                Socket socket = sSocket.accept();

                executor.submit(new SocketClientThread(socket, count));
                System.out.println("Server started at: " + new Date());
                count++;


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
