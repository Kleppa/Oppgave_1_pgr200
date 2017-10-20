package innlevering;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Client {

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        connectToServerAndDoWork();
    }

    public void connectToServerAndDoWork() {
        //We set up the scanner to receive user input
        Scanner scanner = new Scanner(System.in);
        try {
            Socket socket = new Socket("localhost", 4444);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            //This will wait for the server to send the string to the client saying a connection
            //has been made.


            //Again, here is the code that will run the client, this will continue looking for
            //input from the user then it will send that info to the server.

            System.out.println(input.readLine());
            while (true) {

                //Here we look for input from the user
                String userInput = scanner.nextLine();
                //Now we write it to the server
                output.println(userInput);
                System.out.println("Waiting");
                String msg = input.readLine();



                if (userInput.equals("1")) {
                    sendAndRetrieve(2, output, input);

                } else if (userInput.equals("2")) {
                    sendAndRetrieve(4, output, input);

                } else if (userInput.equals("3")) {

                } else {

                }
                // System.out.println(input.readLine());
            }
        } catch (IOException exception) {
            System.out.println("Error: " + exception);
        }
    }

    public void sendAndRetrieve(int amountOfQuestions, PrintWriter output, BufferedReader input) {

        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < amountOfQuestions; i++) {
            System.out.println("prepping answers : " + (i + 1) + " out of " + amountOfQuestions + " before complete");

            output.println(scanner.nextLine());
            output.flush();

            try {
                System.out.println(input.readLine());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        int sbSize = 0;
        try {
            sbSize = Integer.parseInt(input.readLine());

            for (int i = 0; i < sbSize; i++) {
                System.out.println(input.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}