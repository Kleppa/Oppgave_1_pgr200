package innlevering;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Client
{

    public static void main(String[] args)
    {
        new Client();
    }

    public Client()
    {
        //We set up the scanner to receive user input
        Scanner scanner = new Scanner(System.in);
        try {
            Socket socket = new Socket("localhost",4444);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //This will wait for the server to send the string to the client saying a connection
            //has been made.



            //Again, here is the code that will run the client, this will continue looking for 
            //input from the user then it will send that info to the server.

            System.out.println(input.readLine());
            while(true) {

                //Here we look for input from the user
                String userInput = scanner.nextLine();
                //Now we write it to the server
                output.println(userInput);
                System.out.println("Waiting");
                String msg= input.readLine();
                System.out.println(msg);
                System.out.println("Before if check");
                if (msg.length()>0)
                System.out.println(input.readLine());
                if (msg.equals("1")){
                    for (int i =0;i<2;i++){
                        System.out.println("prepping answers");
                        output.println(scanner.nextLine());
                    }

                }
                else if (msg.equals("2")){

                }else if (msg.equals("3")){

                }
               // System.out.println(input.readLine());
            }
        } catch (IOException exception) {
            System.out.println("Error: " + exception);
        }
    }
}