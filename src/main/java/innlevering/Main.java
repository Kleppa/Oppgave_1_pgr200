package innlevering;

import java.sql.SQLException;
import java.util.Scanner;

/**
 * Created by Kleppa on 04/09/2017.
 */
public class Main {
    private static DBHandler dbHandler;

    public static void main(String[] args) throws SQLException {
        dbHandler = new DBHandler();
        menuChoices();
        //Server server=new Server();
        //Client client= new Client();

    }

    public static void menu() {
        System.out.println("----- MENU -----");
        System.out.println("1 -Get an entire coloumn");
        System.out.println("2 - Get a row from a table");
        System.out.println("3 - Create a table");
        System.out.println("4 - Drop a specific element from a table");
        System.out.println("5 - Drop a table from the schema");
        System.out.println("0 - exit");
        System.out.println();
        System.out.println();

    }

    public static void menuChoices() {
        Scanner sc = new Scanner(System.in);

        String table = "";
        String sql = "";
        String col = "";
        String identifier = "";
        boolean whileLock = true;

        while (whileLock) {

            menu();
            switch (sc.nextLine()) {

                case "1":
                    System.out.println("\t\t\t\t Tables: \n");
                    dbHandler.getTableNames().forEach(t->System.out.print("| "+t+" | "));

                    System.out.println("\n\nWhich table do you want to get info from ? \n");

                    table += sc.nextLine();

                    dbHandler.getColoumns(table).forEach(c->System.out.print("| "+c+"| "));
                    System.out.println("\nWhat coloumn are you interested in?");

                    sql += sc.nextLine() + " ";
                    dbHandler.get(table, sql);
                    table = "";
                    sql = "";
                    break;

                case "2":
                    System.out.println("\t\t\t\t Tables: \n");
                    dbHandler.getTableNames().forEach(t->System.out.print("| "+t+" | "));

                    System.out.println("\nWhich table do you want to get info from ? ");
                    table += sc.nextLine();
                    System.out.println("\n\t\t\t\t Coloumns :");
                    dbHandler.getColoumns(table).forEach(c->System.out.print("| "+c+"| "));
                    System.out.println("(You can use * for everything)");
                    System.out.println("\nWhat coloumn are you interested in?");
                    sql += sc.nextLine();
                    System.out.println("Where X =? What is your x");
                    col += sc.nextLine();
                    System.out.println("What do you want X to be equal to?");
                    identifier += sc.nextLine();

                    dbHandler.get(table, sql, col, identifier);
                    table = "";
                    sql = "";
                    col = "";
                    identifier = "";
                    break;
                case "3":
                    dbHandler.createTable("Test");
                    break;
                case "4":
                    dbHandler.dropFromDatabaseAssignement_2();
                    break;
                case "5":
                    dbHandler.dropTable();
                    break;

                case "0":
                    whileLock = false;
                    break;

                default:
                    break;

            }
        }

    }
}
