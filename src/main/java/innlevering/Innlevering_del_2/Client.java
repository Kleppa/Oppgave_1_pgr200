package innlevering.Innlevering_del_2;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Client {

    public static void main(String[] args) {
        new Client();
    }

    public Client() {
        setupStreamConnections();
    }

    public void setupStreamConnections() {
        //We set up the scanner to receive user input
        Scanner scanner = new Scanner(System.in);
        try {
            Socket socket = new Socket("localhost", 4444);
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {

               functionalMenu(input,scanner,output);

            }
        } catch (IOException exception) {
            System.out.println("Error: " + exception);
        }
    }

	/**
	 *
	 * @param input bufferedreader to take info from server
	 * @param scanner scanner to read from terminal
	 * @param output sends info to server.
	 * @throws IOException
	 */
	private void functionalMenu(BufferedReader input, Scanner scanner, PrintWriter output) throws IOException {


		int menuLength= Integer.parseInt(input.readLine());
		for (int i = 0; i <menuLength ; i++) {
			System.out.println(input.readLine());
		}
		System.out.println("before userinput");
		String userInput = scanner.nextLine();
		//Now we write it to the server

		System.out.println("Before Menu print");

		output.println(userInput);
		System.out.println("Waiting");

		if (userInput.equals("1")) {
			sendAndRetrieve(2, output, input);
		} else if (userInput.equals("2")) {
			sendAndRetrieve(4, output, input);

		}else if (userInput.equals("3")) {
			sendAndRetrieve(3, output, input);

		} else if (userInput.equals("0")) {
			System.exit(0);

		} else {

		}
	}

	/**
	 *
	 * @param amountOfQuestions forloop number of iteration decider.
	 * @param output sends info to server.
	 * @param input returns info from server.
	 */
	public void sendAndRetrieve(int amountOfQuestions, PrintWriter output, BufferedReader input) throws IOException {

        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < amountOfQuestions; i++) {
            try {
                System.out.println(input.readLine());
                System.out.println("prepping answers : " + (i + 1) + " out of " + amountOfQuestions + " before complete");
                output.println(scanner.nextLine());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

		try {
            String inputString = null;

            while((inputString=input.readLine()).contains(" ")){
                if (inputString.equalsIgnoreCase("done"))
                    break;
                System.out.println(inputString);

            }

        } catch (IOException e) {
			System.out.println("The program experienced an IOexception.");
		}
    }
}