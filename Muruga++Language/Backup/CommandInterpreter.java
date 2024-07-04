package withrules;

import java.util.Scanner;

public class CommandInterpreter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Muruga++ Command Interpreter");
        System.out.println("Enter a command (type 'exit' to quit):");

        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine().trim();

            if (command.equalsIgnoreCase("exit")) {
                break;
            }

            interpretCommand(command);
        }

        System.out.println("Exiting Muruga++ Command Interpreter");
        scanner.close();
    }

    private static void interpretCommand(String command) {
        // Split the command into tokens
        String[] tokens = command.split(" ");

        if (tokens.length == 0) {
            System.out.println("Invalid command");
            return;
        }

        String keyword = tokens[0];
        switch (keyword) {
            case "vel":
                if (tokens.length < 2) {
                    System.out.println("Usage: vel(\"message\")");
                    return;
                }
                String message = tokens[1];
                System.out.println("Printing: " + message);
                break;
            case "muruga":
                // Handle muruga command
                break;
            default:
                System.out.println("Unknown command: " + keyword);
        }
    }
}
