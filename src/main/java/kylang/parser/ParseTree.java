// Kyran Day, 12/07/2025.
package kylang.parser;
import kylang.memory.Memory;
import kylang.tree_nodes.StatementList;

/**
 * ParseTree represents the parse tree for a program.
 * Contains the start symbol (program) and provides execution.
 */
public final class ParseTree {

    private final StatementList program; // start symbol: Program ::= Stmt_List

    public ParseTree(StatementList program) {
        this.program = program;
    }

    public void execute(Memory memory) {
        program.execute(memory);
    } // pass memory reference along the tree
}
