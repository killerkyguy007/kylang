// Kyran Day, 12/07/2025.
package kylang.tree_nodes;

import kylang.memory.Memory;

/** Factor for a parenthesized expression.
 * Grammar: "(" Expression ")" */
public final class ParenFactorNode extends FactorNode {
    final Token lparen;          // terminal LEFT_PAREN
    final ExpressionNode expr;   // child non terminal
    final Token rparen;          // terminal RIGHT_PAREN

    public ParenFactorNode(Token lparen, ExpressionNode expr, Token rparen) {
        this.lparen = lparen;
        this.expr = expr;
        this.rparen = rparen;
    }

    @Override
    public int evaluate(Memory memory) { return expr.evaluate(memory); }
}
