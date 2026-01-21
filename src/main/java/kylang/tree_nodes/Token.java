// Kyran Day, 12/07/2025.
package kylang.tree_nodes;
/** Represents a token in a lexical analysis process.
 * A token stores its position (row, column), the lexeme string,
 * and a token type (determined from the lexeme).
 * The {@code TreeNodes.Token} class encapsulates its enum {@code type} for
 * better modularity, restricting external code from directly
 * manipulating token categories. */
public class Token {

    private final int ROW, COLUMN;
    private final String LEXEME;

    /** Enum representing all possible token types.
     *  Fully encapsulated within this class. */
    private enum type {
        ADD, SUBTRACT, MULTI, DIVIDE, LEFT_PAREN, RIGHT_PAREN, INT_LIT, IDENTIFIER, UNDEF, DISPLAY, LET, ASSIGN,
        INPUT, IF, ELIF, ELSE, WHILE, FOR, IN, COLON, LT, LE, GT, GE, EQ, NE, INDENT, DEDENT, RANGE, EOL
    }
    private final type TYPE;

    /** Constructs a {@code TreeNodes.Token} with the given position and lexeme.
     * Performs error checking to ensure the lexeme is valid and
     * can be mapped to a known token type.
     *
     * @param row     the row number where the token was found (must be non-negative)
     * @param column  the column number where the token was found (must be non-negative)
     * @param lexeme  the raw string representation of the token
     * @throws IllegalArgumentException if the lexeme is blank, invalid,
     *                                  or cannot be cast into a valid type,
     *                                  or if row or column are negative */
    public Token(int row, int column, String lexeme) {
        ROW = row;
        COLUMN = column;

        if (lexeme.isBlank()) // Error checking
            throw new IllegalArgumentException("Lexeme cannot be blank");
        if (row < 0 || column < 0)
            throw new IllegalArgumentException("Negative row or col in TreeNodes.Token constructor");
        if (stringToType(lexeme) == type.UNDEF)
            throw new IllegalArgumentException("LEXEME \"" + lexeme + "\" cannot be mapped to a valid TYPE at row "
                    + row + ", col " + column);
        LEXEME = lexeme;
        TYPE = stringToType(lexeme);
    }

    // Public accessors:
    public String getType() { return TYPE.toString(); }
    public int getRow() { return ROW; }
    public int getCOLUMN() { return COLUMN; }
    public String getLEXEME() { return LEXEME; }

    /** Prints the info about this {@code TreeNodes.Token} object, neatly.
     *  Overrides {@code Object} class toString() method. */
    @Override
    public String toString() {
        return "Lexeme: " + LEXEME + ", Type: " + TYPE + ", at Row: " + ROW + ", and Column: " + COLUMN;
    }

    // Private helper methods:
    /** Converts a given lexeme string into its corresponding {@code type}.
     * Recognizes operators, parentheses, semicolon, integer literals, identifiers,
     * assignment operators, input keyword, let keyword, display keyword, and new control flow keywords.
     *
     * @param lexeme the string to convert
     * @return the corresponding {@code type}, or {@code UNDEF} if no match */
    private static type stringToType(String lexeme) {
        return switch (lexeme) {
            case "+" -> type.ADD;
            case "-" -> type.SUBTRACT;
            case "*" -> type.MULTI;
            case "/" -> type.DIVIDE;
            case "(" -> type.LEFT_PAREN;
            case ")" -> type.RIGHT_PAREN;
            case ";" -> type.EOL;
            case ":" -> type.COLON;
            case "display" -> type.DISPLAY;
            case "let" -> type.LET;
            case ":=" -> type.ASSIGN;
            case "input" -> type.INPUT;
            case "if" -> type.IF;
            case "elif" -> type.ELIF;
            case "else" -> type.ELSE;
            case "while" -> type.WHILE;
            case "for" -> type.FOR;
            case "in" -> type.IN;
            case "<" -> type.LT;
            case "<=" -> type.LE;
            case ">" -> type.GT;
            case ">=" -> type.GE;
            case "=" -> type.EQ;
            case "/=" -> type.NE;
            case ".." -> type.RANGE;
            default -> { // Check if lexeme is an integer literal, identifier, or undefined
                if (lexeme.matches("\\d+")) yield type.INT_LIT;
                else if (lexeme.matches("(?i)[a-z][a-z0-9_]*")) yield type.IDENTIFIER;
                else yield type.UNDEF; // Otherwise, undefined
            }
        };
    }
}
