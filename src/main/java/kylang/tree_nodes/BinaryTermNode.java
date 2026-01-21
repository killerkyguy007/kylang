// Kyran Day, 12/07/2025
package kylang.tree_nodes;

import kylang.memory.Memory;

/**
 * Represents a binary term: Term ::= Term (*|/) Factor
 */
public final class BinaryTermNode extends TermNode {
    final TermNode left;   // child non-terminal
    final Token op;        // terminal: MULTI or DIVIDE
    final FactorNode right;// child non-terminal

    public BinaryTermNode(TermNode left, Token op, FactorNode right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }

    @Override
    public int evaluate(Memory memory) {
        int L = left.evaluate(memory);
        int R = right.evaluate(memory);
        return switch (op.getType()) {
            case "MULTI" -> L * R;
            case "DIVIDE" -> {
                if (R == 0) throw new ArithmeticException("divide by zero");
                yield L / R;
            }
            default -> throw new IllegalStateException("Bad */ token: " + op.getType());
        };
    }
}
