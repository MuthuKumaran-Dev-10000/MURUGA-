package withrules;

public enum TokenType {
    IDENTIFIER,
    NUMBER,
    STRING,
    OPERATOR,
    COMMENT,
    FOR_LOOP_START,
    FOR_LOOP_END,
    LESS_THAN,       // <
    GREATER_THAN,    // >
    LESS_THAN_EQUAL, // <=
    GREATER_THAN_EQUAL, // >=
    EQUAL_EQUAL,     // ==
    NOT_EQUAL,       // !=
    LOGICAL_NOT,     // !
    LOGICAL_AND,     // &&
    LOGICAL_OR,      // ||
}
