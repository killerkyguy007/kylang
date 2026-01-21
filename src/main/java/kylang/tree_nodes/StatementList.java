// Kyran Day, 12/07/2025.
package kylang.tree_nodes;
import kylang.memory.Memory;
import kylang.statements.Statement;
import java.util.ArrayList;

/**
 * Represents a list of statements (the program start symbol).
 * Grammar: Program ::= Stmt_List
 */
public class StatementList {
    private final ArrayList<Statement> statements;

    public StatementList() {
        this.statements = new ArrayList<>();
    }

    public void addStatement(Statement stmt) {
        statements.add(stmt);
    }

    public void execute(Memory memory) {
        for (Statement stmt : statements) {
            stmt.execute(memory);
        }
    }
}
