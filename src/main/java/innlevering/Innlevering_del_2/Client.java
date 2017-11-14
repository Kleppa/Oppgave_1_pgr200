package innlevering.Innlevering_del_2;

import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Client {

	public static void main(String[] args) {
		new Client();
	}

	public Client() {
		startCommunicationWithServer();
	}

	/**
	 * sets up streams with server and presents menu.
	 */
	public void startCommunicationWithServer() {
		//We set up the scanner to receive user input
		Scanner scanner = new Scanner(System.in);
		try {
			Socket socket = new Socket("localhost", 4444);
			PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while (true) {

				functionalMenu(input, scanner, output);

			}
		} catch (IOException exception) {
			System.out.println("Could not set up streams with server");
		}
	}

	/**
	 * Presents user the menu and take in user choice afterwards.
	 *
	 * @param input   bufferedreader to take info from server
	 * @param scanner scanner to read from terminal
	 * @param output  sends info to server.
	 * @throws IOException
	 */
	private void functionalMenu(BufferedReader input, Scanner scanner, PrintWriter output) throws IOException {


		int menuLength = Integer.parseInt(input.readLine());
		for (int i = 0; i < menuLength; i++) {
			System.out.println(input.readLine());
		}

		String userInput = scanner.nextLine();
		//Now we write it to the server


		output.println(userInput);


		if (userInput.equals("1")) {
			sendAndRetrieve(2, output, input);
		} else if (userInput.equals("2")) {
			sendAndRetrieve(4, output, input);

		} else if (userInput.equals("3")) {
			sendAndRetrieve(3, output, input);

		} else if (userInput.equals("0")) {
			System.exit(0);

		} else {

		}
	}

	/**
	 * Preps output for db, reads input from db.
	 *
	 * @param amountOfQuestions forloop number of iteration decider.
	 * @param output            sends info to server.
	 * @param input             returns info from server.
	 */
	public void sendAndRetrieve(int amountOfQuestions, PrintWriter output, BufferedReader input) throws IOException {

		Scanner scanner = new Scanner(System.in);
		printTables(input);
		System.out.println();
		for (int i = 0; i < amountOfQuestions; i++) {
			try {
				if (i == 1) {

					printColoumns(input);
				}

				System.out.println(input.readLine());

				System.out.println("prepping answers : " + (i + 1) + " out of " + amountOfQuestions + " before complete");
				output.println(scanner.nextLine());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		try {
			String inputString = null;

			while ((inputString = input.readLine()).contains(" ")) {
				if (inputString.equalsIgnoreCase("done"))
					break;
				System.out.println(inputString);

			}

		} catch (IOException e) {
			System.out.println("The program experienced an IOexception.");
		}
	}

	private boolean printColoumns(BufferedReader input) {
		System.out.println("\t\t\t----Coloumns----");
		System.out.print("|| ");
		try {

			int amountOfcols=Integer.parseInt(input.readLine());

			for (int j = 0; j < amountOfcols; j++) {

					System.out.print(input.readLine() + " ||");
				}

		} catch (IOException e) {
			System.out.println("Connenction to server has been lost");
			return false;
		}

		System.out.println();
		return true;
	}

	private boolean printTables(BufferedReader input) {
		System.out.println("\t\t\t----Tables----");
		System.out.print("|| ");

		for (int j = 0; j < 4; j++) {
			try {
				System.out.print(input.readLine() + " ||");
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		System.out.println();
		return true;
	}
}