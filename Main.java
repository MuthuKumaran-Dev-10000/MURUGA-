import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your Muruga++ code:");
        String code = scanner.nextLine();

        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.tokenize();
        System.out.println("Tokens: " + tokens);

        Parser parser = new Parser(tokens);
        Node ast = parser.parse();
        System.out.println("AST: " + ast);

        Interpreter interpreter = new Interpreter(ast);
        int result = interpreter.interpret();
        System.out.println("Result: " + result);
    }
}
