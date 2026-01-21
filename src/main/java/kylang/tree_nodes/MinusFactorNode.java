// Kyran Day, 12/07/2025.
package kylang.tree_nodes;

import kylang.memory.Memory;

/** Factor representing a unary minus.
 * Grammar: "-" Expression */
public final class MinusFactorNode extends FactorNode {
    final Token minus;           // terminal: SUBTRACT
    final ExpressionNode expr;   // child non-terminal

    public MinusFactorNode(Token minus, ExpressionNode expr) {
        this.minus = minus;
        this.expr = expr;
    }

    @Override
    public int evaluate(Memory memory) { return -expr.evaluate(memory); }
}

