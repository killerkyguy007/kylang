// Kyran Day, 12/07/2025
package kylang.statements;
import kylang.tree_nodes.BooleanExpressionNode;
import kylang.memory.Memory;
import kylang.tree_nodes.StatementList;

/**
 * Represents a while loop statement.
 * Grammar: while <boolean_expression> : <EOL> <statement_block>  */
public class WhileStatement extends Statement {
    
    private BooleanExpressionNode condition;
    private StatementList body;
    
    public WhileStatement(BooleanExpressionNode condition, StatementList body) {
        this.condition = condition;
        this.body = body;
    }
    
    @Override
    public void execute(Memory memory) {
        while (condition.evaluate(memory)) body.execute(memory);
    }
}
