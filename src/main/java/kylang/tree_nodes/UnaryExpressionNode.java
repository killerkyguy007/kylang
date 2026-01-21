// Kyran Day, 12/07/2025.
package kylang.tree_nodes;

import kylang.memory.Memory;

/**
 * Represents an expression containing only a single term.
 * Grammar: Expression ::= Term
 */
public final class UnaryExpressionNode extends ExpressionNode {
    final TermNode term; // child non terminal

    public UnaryExpressionNode(TermNode term) {
        this.term = term;
    }

    @Override
    public int evaluate(Memory memory) {
        // post-order, child first
        return term.evaluate(memory);
    }
}
