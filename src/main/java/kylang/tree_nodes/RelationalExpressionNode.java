// Kyran Day, 12/07/2025.
package kylang.tree_nodes;

import kylang.memory.Memory;

/**
 * Represents a relational expression: BooleanExpression ::= ArithmeticExpression relop ArithmeticExpression
 * Supports: <, <=, >, >=, =, /=, ==
 * */
public final class RelationalExpressionNode extends BooleanExpressionNode {
    final ExpressionNode left;  // left arithmetic expression
    final Token relop;          // relational operator token
    final ExpressionNode right; // right arithmetic expression

    public RelationalExpressionNode(ExpressionNode left, Token relop, ExpressionNode right) {
        this.left = left;
        this.relop = relop;
        this.right = right;
    }

    @Override
    public boolean evaluate(Memory memory) {
        int leftVal = left.evaluate(memory);
        int rightVal = right.evaluate(memory);
        
        return switch (relop.getType()) {
            case "LT" -> leftVal < rightVal;
            case "LE" -> leftVal <= rightVal;
            case "GT" -> leftVal > rightVal;
            case "GE" -> leftVal >= rightVal;
            case "EQ" -> leftVal == rightVal;
            case "NE" -> leftVal != rightVal;
           // case "EQUAL" -> leftVal == rightVal;
            default -> throw new IllegalStateException("Bad relational operator: " + relop.getType());
        };
    }
}
