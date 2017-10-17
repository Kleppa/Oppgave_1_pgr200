package innlevering;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Kleppa on 01/10/2017.
 */
public class SocketClientThread implements Runnable {
    //This constructor will be passed the socket
    private boolean activiateUser=false;
    private DBHandler dbhand = new DBHandler();
    private Socket socket;
    private int id;
    private boolean serverRespons = true;
    private Scanner sc = null;
    boolean userCanRequest = false;
    BufferedReader input = null;

    public SocketClientThread(Socket socket, int id) {
        this.socket = socket;
        this.id = id;
        String port = "4444";

    }

    // TODO: 16/10/2017 Bruke ORM TIL AA vise forskjeller og ULEMPER
    @Override
    public void run() {
        //All this should look familiar
        System.out.println("RUN METHOD");
        try {
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));


            while (true) {
                //This will wait until a line of text has been sent

                if (!userCanRequest) {
                    output.println(menu());
                    String msg = input.readLine();

                    System.out.println("Message from client :  " + msg);
                    String answer = sendToUser(handleRequest(msg, input, output));
                    output.println(answer);
                    userCanRequest = false;

                    serverRespons = false;
                }
            }
        } catch (IOException exception) {
            System.out.println("it breaks");
            System.out.println("Error: " + exception);
            exception.printStackTrace();
        }
    }

    private String handleRequest(String msg, BufferedReader input, PrintWriter output) {

        Scanner sc = new Scanner(System.in);

        String table = "";
        String sql = "";
        String col = "";
        String identifier = "";
        boolean whileLock = true;

        while (whileLock) {


            switch (msg) {

                case "1":
                    output.println("Which table do you want to get info from ? ");
                    try {

                        System.out.println("adding answer to query");
                        table += input.readLine().toString();
                        System.out.println(table);

                        output.println("What coloumn are you interested in?");

                        sql += input.readLine() + " ";
                        dbhand.get(table, sql);
                        output.println(table + " " + sql);
                        table = "";
                        sql = "";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case "2":
                    try {
                        output.println("Which table do you want to get info from ? ");

                        table += input.readLine();

                        output.println("What are you interested in?");
                        sql += input.readLine();
                        output.println("Where X =? What is your x");
                        col += input.readLine();
                        output.println("What do you want X to be equal to?");
                        identifier += input.readLine();
                        //// TODO: 14/10/2017 use config to set database name
                        dbhand.get(table, sql, col, identifier);
                        table = "";
                        sql = "";
                        col = "";
                        identifier = "";
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "3":
                    break;
                case "4":
                    dbhand.dropFromDatabase();
                    break;
                case "5":
                    dbhand.dropTable();
                    break;

                case "0":
                    whileLock = false;
                    break;

                default:
                    break;

            }
        }


        return sendToUser(dbhand.getSb().toString());
    }

    private String sendToUser(String s) {

        return s;
    }

    private boolean inputStreamIsAvailable() {
        try {

            if (input.ready())
                return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String menu() {
        String stringMenu = "----- MENU ----- //1 - Get a coloumn from table// 2 - get a specific row from table// 3 - exit";

        return stringMenu;
    }

    public boolean isActiviateUser() {
        return activiateUser;
    }

    public void setActiviateUser(boolean activiateUser) {
        this.activiateUser = activiateUser;
    }
}

