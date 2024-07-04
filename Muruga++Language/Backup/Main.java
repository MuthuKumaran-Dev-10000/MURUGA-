package withrules;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Default code snippet
        String defaultCode = "vetrivel sum(n1, n2) {\n" +
                             "    muruga result: n1 + n2;\n" +
                             "    sendruva result;\n" +
                             "}\n" +
                             "\n" +
                             "mk a: 5;\n" +
                             "mk b: 3;\n" +
                             "vel(sum(a, b));";

        System.out.println("Enter 1 to use default code, or 2 to enter your own code, or 3 to run test cases:");
        String choice = scanner.nextLine().trim();

        String code;
        if (choice.equals("1")) {
            System.out.println("Using default code:\n" + defaultCode);
            code = defaultCode;
        } else if (choice.equals("2")) {
            System.out.println("Enter your code (type 'VARATA' on a new line to finish):");
            StringBuilder codeBuilder = new StringBuilder();
            while (true) {
                String line = scanner.nextLine();
                if (line.trim().equals("VARATA")) {
                    break;
                }
                codeBuilder.append(line).append("\n");
            }
            code = codeBuilder.toString();
        } else if (choice.equals("3")) {
            // Run test cases
            runTestCases();
            scanner.close();
            return;
        } else {
            System.out.println("Invalid choice. Exiting.");
            scanner.close();
            return;
        }

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

    private static void runTestCases() {
        // Test case 1: Variable declaration
        System.out.println("\u001B[33mTest Case: Variable Declaration\u001B[0m");
        String code1 = "mk x: 10;\n" +
                       "mk y: 20;\n" +
                       "mk z: x + y;\n" +
                       "vel(z);";
        runTestCase(code1);

        // Test case 2: Printing
        System.out.println("\u001B[33mTest Case: Printing\u001B[0m");
        String code2 = "vel(x);\n" +
                       "vel(\"Hello, world!\");";
        runTestCase(code2);

        // Test case 3: Looping
        // System.out.println("\u001B[33mTest Case: Looping\u001B[0m");
        // String code3 = "mk i: 1;\n" +
        //                "till(i <= 5) {\n" +
        //                "    sa(i);\n" +
        //                "    i = i + 1;\n" +
        //                "}";
        // runTestCase(code3);

        // Test case 4: Functions
        System.out.println("\u001B[33mTest Case: Functions\u001B[0m");
        String code4 = "vetrivel add(n1, n2) {\n" +
                       "    muruga result: n1 + n2;\n" +
                       "    sendruva result;\n" +
                       "}\n" +
                       "\n" +
                       "mk a: 5;\n" +
                       "mk b: 3;\n" +
                       "vel(add(a, b));";
        runTestCase(code4);

        // Test case 5: Escape sequences
        // System.out.println("\u001B[33mTest Case: Escape Sequences\u001B[0m");
        // String code5 = "vel('This is a \\\"quoted\\\" string.');\n" +
        //                "vel('New line:\\nNext line.');";
        // runTestCase(code5);
    }

    private static void runTestCase(String code) {
        try {
            System.out.println("\u001B[32mRunning test case:\u001B[0m");
            System.out.println(code);

            code = processComments(code);

            Lexer lexer = new Lexer(code);
            List<Token> tokens = lexer.tokenize();
            System.out.println("Tokens: " + tokens);

            Parser parser = new Parser(tokens);
            Node ast = parser.parse();
            System.out.println("AST: " + ast);

            Interpreter interpreter = new Interpreter(ast);
            interpreter.interpret();

            System.out.println("\u001B[32mTest case passed!\u001B[0m");
        } catch (Exception e) {
            System.out.println("\u001B[31mTest case failed!\u001B[0m");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println();
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
