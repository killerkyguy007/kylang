// Kyran Day, 12/07/2025
package kylang.lexer;
import kylang.tree_nodes.Token;
import java.util.ArrayList;
import java.util.List;

/**
 * LexicalAnalyzer
 * ------------------------------------------------------------
 * Splits source lines into lexemes and assigns each one a token type.
 * Handles keywords, identifiers, integer literals, operators, and
 * special symbols like parentheses and the assignment operator. */
public class LexicalAnalyzer {

    private int index = 0; // The last token queried

    private Token[] TOKENS;

    /**
     * Constructs a LexicalAnalyzer for a given source line.
     * @param source the source line to analyze
     * @param lineNumber the line number or position in the program */
    public void analyze(String source, int lineNumber) {
        index = 0;

        List<String> lexemes = lexemeSplitter(source.trim()); // Split it into potential lexemes
        TOKENS = makeTokens(lexemes, lineNumber);          // Create an array of Tokens
    }

    // Public accessors:

    /** Retrieves the next {@link Token} in sequence and advances the internal index.
     * <p>
     * Each call to this method returns the next token. When all tokens have
     * been consumed, this method returns {@code null} instead of throwing an
     * exception.
     * </p>
     *
     * @return the next token in the sequence, or {@code null} if no tokens remain */
    public Token getToken() {
        if (index >= TOKENS.length)
            return TOKENS[TOKENS.length - 1]; // return EOL token at end of line
        return TOKENS[index++];
    }

    // ==== Private helper methods: ====

    /**
     * Splits the given source string into lexemes based on grammar rules.
     *
     * @param source the raw source text
     * @return a list of string lexemes extracted from the source */
    private static List<String> lexemeSplitter(String source) {
        List<String> lexemes = new ArrayList<>();
        int i = 0;
        final int n = source.length();

        while (i < n) {
            char ch = source.charAt(i);

            if (Character.isWhitespace(ch)) { // skip whitespace
                i++;
                continue;
            }
            // Handle two-character operators first before single-char ones
            if (ch == ':' && i + 1 < n && source.charAt(i + 1) == '=') { // two char operator: := (assignment)
                lexemes.add(":=");
                i += 2;
                continue;
            }
            if (ch == '<' && i + 1 < n && source.charAt(i + 1) == '=') { // <=
                lexemes.add("<=");
                i += 2;
                continue;
            }
            if (ch == '>' && i + 1 < n && source.charAt(i + 1) == '=') { // >=
                lexemes.add(">=");
                i += 2;
                continue;
            }
            if (ch == '=' && i + 1 < n && source.charAt(i + 1) == '=') { // ==
                lexemes.add("==");
                i += 2;
                continue;
            }
            if (ch == '/' && i + 1 < n && source.charAt(i + 1) == '=') { // /=
                lexemes.add("/=");
                i += 2;
                continue;
            }
            if (ch == '.' && i + 1 < n && source.charAt(i + 1) == '.') { // ..
                lexemes.add("..");
                i += 2;
                continue;
            }
            if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '(' || ch == ')' || ch == ';' || ch == ':'
                    || ch == '<' || ch == '>' || ch == '=') {
                lexemes.add(String.valueOf(ch)); // single-char operators / punctuation
                i++;
                continue;
            }
            if (Character.isDigit(ch)) { // int lit: one or more digits
                int start = i;
                i++; // consume first digit
                while (i < n && Character.isDigit(source.charAt(i))) i++;
                lexemes.add(source.substring(start, i));
                continue;
            }
            if (Character.isLetter(ch)) { // identifier / keyword, letter, then letters/digits (case-insensitive)
                int start = i;
                i++; // consume first letter
                while (i < n && (Character.isLetterOrDigit(source.charAt(i)) || source.charAt(i) == '_') ) i++;
                String word = source.substring(start, i);

                // Normalize keywords to their exact lexeme spelling
                String lower = word.toLowerCase();
                if (lower.equals("let") || lower.equals("display") || lower.equals("input")
                    || lower.equals("if") || lower.equals("elif") || lower.equals("else")
                    || lower.equals("while") || lower.equals("for") || lower.equals("in")) {
                    lexemes.add(lower);
                } else {
                    lexemes.add(word);  // Identifiers keep original spelling, typing is case-insensitive later
                }
                continue;
            }
            lexemes.add(String.valueOf(ch)); // here, its an unknown character
            i++;                             // for robustness, add it, caller can flag as UNDEF
        }
        lexemes.add(";");
        return lexemes;
    }

    /** Converts a list of lexeme strings into an array of {@link Token} objects.
     *
     * @param lexemes the lexeme strings
     * @return an array of {@code TreeNodes.Token} objects */
    private static Token[] makeTokens(List<String> lexemes, int lineNumber) {
        Token[] tokens = new Token[lexemes.size()];
        for (int i = 0; i < lexemes.size(); i++) {
            if (isValidLexeme(lexemes.get(i))) tokens[i] = new Token(lineNumber, i, lexemes.get(i));
            else throw new IllegalArgumentException("Invalid lexeme \""+lexemes.get(i)+"\" at line "+lineNumber+", column "+i);
        }
        return tokens;
    }

    /** Validates whether a given lexeme string is recognized
     * as a legal token in this language.
     * Valid lexemes include operators, parentheses, semicolon,
     * or an integer literal (digits only), identifiers,
     * assignment operators, input keyword, let keyword, display keyword, and new control flow keywords.
     *
     * @param lex the string to validate
     * @return {@code true} if the lexeme is valid, otherwise {@code false} */
    private static boolean isValidLexeme(String lex) {
        return (lex.contentEquals("-") || lex.contentEquals("+") || lex.contentEquals("*") || lex.contentEquals("/")
            || lex.contentEquals("(") || lex.contentEquals(")") || lex.contentEquals(";") || lex.contentEquals(":")
            || lex.contentEquals("display") || lex.contentEquals("let") || lex.contentEquals(":=")
            || lex.contentEquals("input") || lex.contentEquals("if") || lex.contentEquals("elif")
            || lex.contentEquals("else") || lex.contentEquals("while") || lex.contentEquals("for")
            || lex.contentEquals("in") || lex.contentEquals("<") || lex.contentEquals("<=") || lex.contentEquals(">")
            || lex.contentEquals(">=") || lex.contentEquals("=") || lex.contentEquals("/=") || lex.contentEquals("..")
            || lex.matches("(?i)[a-z][a-z0-9_]*") || lex.matches("\\d+")
        );
    }
}
