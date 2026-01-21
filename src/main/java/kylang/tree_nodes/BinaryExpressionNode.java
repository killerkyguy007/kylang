package kylang.tree_nodes;
import kylang.memory.Memory;
// Kyran Day, 12/07/2025

/**
 * Represents a binary expression: Expression ::= Expression (+|-) Term
 */
public final class BinaryExpressionNode extends ExpressionNode {
    final ExpressionNode left; // child non-terminal
    final Token op;            // terminal: ADD or SUBTRACT
    final TermNode right;      // child non-terminal

    public BinaryExpressionNode(ExpressionNode left, Token op, TermNode right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public int evaluate(Memory memory) {
        // post-order: evaluate children, then combine
        int L = left.evaluate(memory);
        int R = right.evaluate(memory);
        return switch (op.getType()) {
            case "ADD" -> L + R;
            case "SUBTRACT" -> L - R;
            default -> throw new IllegalStateException("Bad +/− token: " + op.getType());
        };
    }
}
