// Kyran Day, 11/12/2025.
package kylang.parser;
import kylang.tree_nodes.*;
import kylang.statements.*;
import kylang.lexer.LexicalAnalyzer;
import java.util.ArrayList;
/**
 * Parser
 * ------------------------------------------------------------
 * Implements a recursive-descent parser for a simplified language
 * supporting assignment, input, display statements with integer arithmetic
 * expressions, boolean expressions, and control flow (if/elif/else, while, for).
 * Builds parse trees (does not execute). */
public final class Parser {

    private final LexicalAnalyzer lexer = new LexicalAnalyzer();
    private ArrayList<String> programLines;
    private int currentLineIndex;
    private Token current;  // Single token param used across the code

    /**
     * Parses a program (list of lines) and builds a parse tree.
     * Grammar: Program ::= Stmt_List
     *
     * @param programLines the source lines to parse
     * @return StatementList representing the program
     */
    public StatementList parse(ArrayList<String> programLines) {

        this.programLines = programLines;
        this.currentLineIndex = 0;
        StatementList stmtList = new StatementList();
        
        while (currentLineIndex < programLines.size()) {

            String line = programLines.get(currentLineIndex);
    
            if (line.trim().isEmpty()) { // Skip empty lines
                currentLineIndex++;
                continue;
            }
            
            int indentLevel = countLeadingTabs(line); // Check for indentation (tabs at start)
            
            // Process statements at base level (indent level 0)
            if (indentLevel == 0) {
                int lineBeforeStatement = currentLineIndex;
                lexer.analyze(line.trim(), currentLineIndex);
                this.current = lexer.getToken(); // seed token
                Statement stmt = statement(0); // build statement node
                stmtList.addStatement(stmt);
                
                // Check if this was a control statement that already advanced the line index
                // Control statements (if/while/for) consume the colon and then advance to the block
                // If currentLineIndex hasn't advanced, it's a simple statement that needs EOL
                if (currentLineIndex == lineBeforeStatement) {
                    match("EOL"); // after a statement, expect EOL for the line
                    currentLineIndex++;
                }
                // Otherwise, the control statement already advanced currentLineIndex past the block
            } else {
                // This shouldn't happen at top level - indented statements belong in blocks
                throw new RuntimeException("Unexpected indentation at line " + (currentLineIndex + 1));
            }
        }
        
        return stmtList;
    }

    // ====== RD parser methods ======
    /**
     * Parses a single statement and returns a Statement node.
     * Grammar: Statement ::= Assn_Stmt | Display_Stmt | Input_Stmt | If_Stmt | While_Stmt | For_Stmt */
    private Statement statement(int currentIndentLevel) {
        if (current == null) throw error("Expected a statement, found <null>");
        return switch (current.getType()) {
            case "LET" -> assnStmt();
            case "DISPLAY" -> displayStmt();
            case "INPUT" -> inputStmt();
            case "IF" -> ifStmt();
            case "WHILE" -> whileStmt(++currentIndentLevel);
            case "FOR" -> forStmt(++currentIndentLevel);
            default -> throw error("Expected statement, found: "+current.getType()+" at line "+current.getRow());
        };
    }

    /**
     * Parses an assignment statement and returns an AssignmentStatement node.
     * Grammar: Assn_Stmt ::= let Id ":=" Arithmetic_Expression */
    private AssignmentStatement assnStmt() {
        match("LET");
        String id = match("IDENTIFIER").getLEXEME();
        match("ASSIGN"); // found ":="
        ExpressionNode expr = arithmeticExpression();
        return new AssignmentStatement(id, expr);
    }

    /**
     * Parses a display statement and returns a DisplayStatement node.
     * Grammar: Display_Stmt ::= "display" Id */
    private DisplayStatement displayStmt() {
        match("DISPLAY");
        String id = match("IDENTIFIER").getLEXEME();
        return new DisplayStatement(id);
    }

    /**
     * Parses an input statement and returns an InputStatement node.
     * Grammar: Input_Stmt ::= "input" Id */
    private InputStatement inputStmt() {
        match("INPUT");
        String id = match("IDENTIFIER").getLEXEME();
        return new InputStatement(id);
    }

    /**
     * Expression ::= Term ExpressionPrime
     *
     * Builds an Expression subtree by first parsing a Term,
     * then any following plus or minus Term sequences. */
    private ExpressionNode arithmeticExpression() { // <Expression> ::= <Term> <Expression'>
        TermNode t = term();
        ExpressionNode acc = new UnaryExpressionNode(t); // term is an arithExpr, start left accumulator as a unary arithExpr
        return expressionPrime(acc);
    }

    /**
     * ExpressionPrime ::= ("+" Term ExpressionPrime)
     *                    | ("-" Term ExpressionPrime)
     *                    | epsilon
     *
     * Performs left-associative folding for addition and subtraction.
     * Continues while the lookahead token is "+" or "-". */
    private ExpressionNode expressionPrime(ExpressionNode acc) { // <Expression'> ::= ("+" <Term> <Expression'>) | ("-" <Term> <Expression'>)
        boolean done = false;
        while (current != null && !done) {
            String type = current.getType();
            if ("ADD".contentEquals(type) || "SUBTRACT".contentEquals(type)) {
                Token op = current;                     // terminal token stays as field
                match(type);                            // consume + or -

                TermNode rhs = term();
                acc = new BinaryExpressionNode(acc, op, rhs); // loop to keep folding left-associatively
            } else {
                done = true;
            }
        }
        return acc;
    }

    /**
     * Term ::= Factor TermPrime
     *
     * Builds a Term subtree starting with one Factor,
     * followed by any chain of multiply or divide operations. */
    private TermNode term() { // <Term> ::= <Factor> <Term'>
        FactorNode f = factor();
        TermNode acc = new UnaryTermNode(f);
        return termPrime(acc);
    }

    /**
     * TermPrime ::= ("*" Factor TermPrime)
     *              | ("/" Factor TermPrime)
     *              | epsilon
     *
     * Performs left-associative folding for multiplication and division. */
    private TermNode termPrime(TermNode acc) {// <Term'> ::= ("*" <Factor> <Term'>) | ("/" <Factor> <Term'>)
        boolean done = false;
        while (current != null && !done ) {
            String type = current.getType();
            if ("MULTI".contentEquals(type) || "DIVIDE".contentEquals(type)) {
                Token op = current;
                match(type);             // consume * or /

                FactorNode rhs = factor();
                acc = new BinaryTermNode(acc, op, rhs);
            } else done = true;
        } // loop to keep folding
        return acc;
    }

    /**
     * Factor ::= "(" Expression ")"
     *           | "-" Expression
     *           | Number
     *
     * Recognizes factors and constructs the corresponding
     * ParenFactorNode, MinusFactorNode, or NumberFactorNode. */
    private FactorNode factor() {  // <Factor> ::= "(" Expression ")" | "-" Expression | <Number> | <Id>
        if (current == null) throw error("Expected factor, found <null>");

        switch (current.getType()) {
            case "LEFT_PAREN": {
                Token lp = current; match("LEFT_PAREN");
                ExpressionNode inner = arithmeticExpression();
                Token rp = match("RIGHT_PAREN");
                return new ParenFactorNode(lp, inner, rp);
            }
            case "SUBTRACT": {
                Token minus = current; match("SUBTRACT");
                ExpressionNode inner = arithmeticExpression();
                return new MinusFactorNode(minus, inner);
            }
            case "INT_LIT": {
                Token lit = current; number();
                return new NumberFactorNode(lit);
            }
            case "IDENTIFIER": {
                Token idToken = current;
                match("IDENTIFIER");
                return new IdentifierFactorNode(idToken);
            }
            default:
                throw error("Expected factor, found: "+current.getType()+" at line "+current.getRow());
        }
    }

    /**
     * Number ::= INT_LIT
     *
     * Consumes a numeric literal token.
     * Delegates to match for token validation. */
    private void number() { match("INT_LIT"); } // <Number> ::= INT_LIT

    /**
     * Parses a boolean expression.
     * Grammar: <boolean_expr> ::= <arithmetic_expression> relop <arithmetic_expression>   */
    private BooleanExpressionNode booleanExpression() {
        ExpressionNode left = arithmeticExpression();
        Token relop = current;
        
        // Check if it's a relational operator
        String relopType = relop.getType();
        if (!relopType.equals("LT") && !relopType.equals("LE") && !relopType.equals("GT")
            && !relopType.equals("GE") && !relopType.equals("EQ") && !relopType.equals("NE")) {
            throw error("Expected relational operator, found: " + relopType);
        }
        
        match(relopType);
        ExpressionNode right = arithmeticExpression();
        return new RelationalExpressionNode(left, relop, right);
    }
    
    /**
     * Parses an if statement with optional elif and else clauses.
     * Grammar: if <boolean_expression> : <EOL> <statement_block> <remaining_if> */
    private IfStatement ifStmt() {
        match("IF");
        BooleanExpressionNode condition = booleanExpression();
        match("COLON");
        // EOL is implicit (next line)
        currentLineIndex++;
        StatementList thenBlock = statementBlock(1); // expect indent level 1
        
        // Parse remaining_if: else_clause | elif_clause remaining_if | null
        IfStatement elifChain = null;
        StatementList elseBlock = null;
        boolean inBlock = true;
        
        while (currentLineIndex < programLines.size() && inBlock) { // Check for elif or else (should be at indent level 0)
            String line = programLines.get(currentLineIndex);
            if (line.trim().isEmpty()) { // Skip empty lines
                currentLineIndex++;
                continue;
            }
            
            int indentLevel = countLeadingTabs(line);
            String trimmed = line.trim();
            
            if (indentLevel == 0 && trimmed.startsWith("elif ")) {
                lexer.analyze(trimmed, currentLineIndex);
                current = lexer.getToken();
                match("ELIF");
                BooleanExpressionNode elifCondition = booleanExpression();
                match("COLON");
                currentLineIndex++;
                StatementList elifBlock = statementBlock(1);
                elifChain = new IfStatement(elifCondition, elifBlock, elifChain, null);
            } else if (indentLevel == 0 && trimmed.startsWith("else")) {
                lexer.analyze(trimmed, currentLineIndex);
                current = lexer.getToken();
                match("ELSE");
                match("COLON");
                currentLineIndex++;
                elseBlock = statementBlock(1);
                inBlock = false;
            } else {
                inBlock = false; // No more elif/else - could be another statement or end of file
            }
        }
        
        return new IfStatement(condition, thenBlock, elifChain, elseBlock);
    }
    
    /**
     * Parses a while statement.
     * Grammar: while <boolean_expression> : <EOL> <statement_block> */
    private WhileStatement whileStmt(int currentIndentLevel) {
        match("WHILE");
        BooleanExpressionNode condition = booleanExpression();
        match("COLON");
        currentLineIndex++;
        StatementList body = statementBlock(currentIndentLevel);
        return new WhileStatement(condition, body);
    }
    
    /**
     * Parses a for statement.
     * Grammar: for <id> in <arithmetic_expression> .. <arithmetic_expression> : <EOL> <statement_block> */
    private ForStatement forStmt(int expectedIndentLevel) {
        match("FOR");
        String loopVar = match("IDENTIFIER").getLEXEME();
        match("IN");
        ExpressionNode startExpr = arithmeticExpression();
        match("RANGE"); // ".."
        ExpressionNode endExpr = arithmeticExpression();
        match("COLON");
        currentLineIndex++;
        StatementList body = statementBlock(expectedIndentLevel); // expect indent level 1
        return new ForStatement(loopVar, startExpr, endExpr, body);
    }
    
    /**
     * Parses a statement block.
     * Grammar: <statement_block> → <indent> <statement_list> <dedent>
     * @param currentIndentLevel the expected indentation level (number of tabs) */
    private StatementList statementBlock(int currentIndentLevel) {
        StatementList block = new StatementList();
        int startLineIndex = currentLineIndex;
        boolean inBlock = true;
        
        while (currentLineIndex < programLines.size() && inBlock) {
            String line = programLines.get(currentLineIndex);
            
            if (line.trim().isEmpty()) { // Skip empty lines
                currentLineIndex++;
                continue;
            }
            int indentLevel = countLeadingTabs(line);

            if (indentLevel < currentIndentLevel) inBlock = false; // Dedent - end of block
            else if (indentLevel > currentIndentLevel) { // More indented - error (shouldn't skip levels)
                throw new RuntimeException("Unexpected indentation level at line " + (currentLineIndex + 1) 
                    + ". Expected " + currentIndentLevel + " tabs, found " + indentLevel);
            } else { // Correct indentation level - parse statement
                lexer.analyze(line.trim(), currentLineIndex);
                current = lexer.getToken();
                Statement stmt = statement(currentIndentLevel);
                // For the future, here current is null when attempting to move on to a LOWER indent level (breaking out of an inner nested block)
                block.addStatement(stmt);
                match("EOL");
                currentLineIndex++;
            }
        }
        return block;
    }

    // ==== Helpers ====

    /**
     * Counts the number of leading tab characters in a line.
     * Also counts spaces (4 spaces = 1 indent level for compatibility). */
    private int countLeadingTabs(String line) {
        int count = 0;
        int spaceCount = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '\t') count++;
            else if (line.charAt(i) == ' ') {
                spaceCount++; // treat 4 spaces as equivalent to 1 tab (kept getting a bug from spaces, .txt file treated tab as 4 spaces)
                if (spaceCount >= 4) {
                    count++;
                    spaceCount = 0;
                }
            } else i = line.length(); // stop counting
        }
        return count;
    }
    
    /**
     * Matches the current token type with the expected type.
     * If they match, consumes the token and advances to the next one.
     * Otherwise, throws a parse error.
     *
     * @param expectedType the token type expected at this point
     * @return the consumed Token (useful for node construction) */
    private Token match(String expectedType) {
        if (current == null) throw error("Unexpected end of input; expected "+expectedType);
        if (!current.getType().contentEquals(expectedType))
            throw error("Expected "+expectedType+" but found "+current.getType()+" at line "+current.getRow()+", column "+current.getCOLUMN()+".");

        Token parsed = current;
        current = lexer.getToken(); // advance
        return parsed;
    }

    /**
     * Creates and returns a formatted runtime parse error.
     * Includes the message and optionally token position info.
     *
     * @param msg error description
     * @return RuntimeException to be thrown */
    private RuntimeException error(String msg) {
        if (current != null) {
            return new RuntimeException("Parse error at row "+current.getRow()+", column "+current.getCOLUMN() +" : "+msg);
        } else {
            return new RuntimeException("Parse error: "+msg);
        }
    }
}
