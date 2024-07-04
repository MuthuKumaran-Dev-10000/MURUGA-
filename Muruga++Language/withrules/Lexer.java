package withrules;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String input;
    private int position;

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (position < input.length()) {
            char currentChar = input.charAt(position);
            if (Character.isWhitespace(currentChar)) {
                position++;
            } else if (Character.isDigit(currentChar)) {
                tokens.add(tokenizeNumber());
            } else if (Character.isLetter(currentChar)) {
                tokens.add(tokenizeIdentifier());
            } else if (currentChar == '\"') {
                tokens.add(tokenizeString());
            } else if (currentChar == '.') {
                if (position + 1 < input.length() && input.charAt(position + 1) == '.') {
                    tokens.add(new Token(TokenType.OPERATOR, ".."));
                    position += 2;
                } else {
                    throw new RuntimeException("Unexpected character: " + currentChar);
                }
            } else if (currentChar == '?') {
                if (position + 1 < input.length() && input.charAt(position + 1) == '?') {
                    // Skip the rest of the line
                    while (position < input.length() && input.charAt(position) != '\n') {
                        position++;
                    }
                } else {
                    tokens.add(tokenizeOperator());
                }
            } else {
                tokens.add(tokenizeOperator());
            }
        }
        return tokens;
    }
    
    

    private Token tokenizeNumber() {
        StringBuilder buffer = new StringBuilder();
        while (position < input.length() && Character.isDigit(input.charAt(position))) {
            buffer.append(input.charAt(position));
            position++;
        }
        return new Token(TokenType.NUMBER, buffer.toString());
    }

    private Token tokenizeIdentifier() {
        StringBuilder buffer = new StringBuilder();
        while (position < input.length() && Character.isLetter(input.charAt(position))) {
            buffer.append(input.charAt(position));
            position++;
        }
        return new Token(TokenType.IDENTIFIER, buffer.toString());
    }

    private Token tokenizeString() {
        StringBuilder buffer = new StringBuilder();
        position++; // Skip the opening quote
        while (position < input.length() && input.charAt(position) != '\"') {
            buffer.append(input.charAt(position));
            position++;
        }
        position++; // Skip the closing quote
        return new Token(TokenType.STRING, buffer.toString());
    }

    private Token tokenizeOperator() {
        char currentChar = input.charAt(position);
        position++;
        return new Token(TokenType.OPERATOR, String.valueOf(currentChar));
    }
}
