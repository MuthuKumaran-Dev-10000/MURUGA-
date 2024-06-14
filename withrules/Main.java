package withrules;

import java.util.*;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder codeBuilder = new StringBuilder();

        System.out.println("Enter your code (type 'VARATA' on a new line to finish):");
        while (true) {
            String line = scanner.nextLine();
            if (line.trim().equals("VARATA")) {
                break;
            }
            codeBuilder.append(line).append("\n");
        }

        String code = codeBuilder.toString();

        try {
            // Process comments
            code = processComments(code);

            Lexer lexer = new Lexer(code);
            List<Token> tokens = lexer.tokenize();
            System.out.println("Tokens: " + tokens);

            Parser parser = new Parser(tokens);
            Node ast = parser.parse();
            System.out.println("AST: " + ast);

            Interpreter interpreter = new Interpreter(ast);
            interpreter.interpret();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static String processComments(String code) {
        String ANSI_RESET = "\u001B[0m";
        String ANSI_RED = "\u001B[31m";
        String ANSI_GREEN = "\u001B[32m";
        StringBuilder processedCode = new StringBuilder();
        String[] lines = code.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) {
                continue; // Skip empty lines
            }
    
            int index = line.indexOf("??");
            if (index != -1) {
                // Extract the comment
                String comment = line.substring(index + 2).trim();
                if (comment.endsWith("*")) {
                    System.out.println(ANSI_RED + comment.substring(0, comment.length() - 1) + ANSI_RESET);
                } else if (comment.endsWith("|")) {
                    System.out.println(ANSI_GREEN + comment.substring(0, comment.length() - 1) + ANSI_RESET);
                }
               // Omit the commented part from the code
                line = line.substring(0, index);
            }
    
            processedCode.append(line).append("\n");
        }
        return processedCode.toString();
    }
    
    
}
