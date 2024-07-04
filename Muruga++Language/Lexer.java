import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Lexer {
    private String input;
    private List<Token> tokens;
    private static final String TOKEN_PATTERNS =
            "\\s*(?:(\\d+)|([a-zA-Z][a-zA-Z0-9]*)|([+\\-*/=;])|(\\S))\\s*";

    public Lexer(String input) {
        this.input = input;
        this.tokens = new ArrayList<>();
    }

    public List<Token> tokenize() {
        Pattern pattern = Pattern.compile(TOKEN_PATTERNS);
        Matcher matcher = pattern.matcher(input);

        while (matcher.find()) {
            if (matcher.group(1) != null) {
                tokens.add(new Token(TokenType.NUMBER, matcher.group(1)));
            } else if (matcher.group(2) != null) {
                tokens.add(new Token(TokenType.IDENTIFIER, matcher.group(2)));
            } else if (matcher.group(3) != null) {
                tokens.add(new Token(TokenType.OPERATOR, matcher.group(3)));
            } else if (matcher.group(4) != null) {
                tokens.add(new Token(TokenType.UNKNOWN, matcher.group(4)));
            }
        }
        return tokens;
    }
}

enum TokenType {
    NUMBER, IDENTIFIER, OPERATOR, UNKNOWN
}

class Token {
    private TokenType type;
    private String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
